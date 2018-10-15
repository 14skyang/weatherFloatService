package com.ysk.weatherfloatwindow;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class weatherFloatService extends Service {
    private static final String TAG = "weatherFloatService";
    public static boolean isStarted = false;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private View weatherView;
    private int weatherCode;

    private int[] images;

    @Override
    public void onCreate() {//创建服务
        super.onCreate();
        windowManager=(WindowManager)getSystemService(WINDOW_SERVICE);
        layoutParams=new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = 500;
        layoutParams.height = 500;
        layoutParams.x = 300;
        layoutParams.y = 300;
        images=new int[]{
                R.drawable.sun,//晴天
                R.drawable.cloud,//多云
                R.drawable.rain,//雨天
        };


    }

    public weatherFloatService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String tmp=intent.getStringExtra("weathercode");
        weatherCode=Integer.valueOf(tmp);
        Log.e(TAG, "weathercode: "+weatherCode);
        showFloatingWindow();//启动服务
        return super.onStartCommand(intent, flags, startId);
    }
    private void showFloatingWindow() {
        if (Settings.canDrawOverlays(this)) { //如果允许了悬浮窗
            if (isStarted){
                windowManager.removeViewImmediate(weatherView);//移除原来存在的悬浮窗，注意逻辑顺序：AddView()方法添加的控件是否有依存体(初始化的weatherView)
                isStarted=false;
            }
            LayoutInflater layoutInflater = LayoutInflater.from(this);//获取LayoutInflater对象
            weatherView = layoutInflater.inflate(R.layout.weather_image, null);
            weatherView.setOnTouchListener(new FloatingOnTouchListener());
            ImageView imageView = weatherView.findViewById(R.id.weather_image);//初始化

            isStarted=true;
            switch (weatherCode){
                case 0:
                    imageView.setImageResource(images[0]);//晴天
                    break;
                case 1:
                    imageView.setImageResource(images[1]);//多云
                    break;
                case 2:
                    imageView.setImageResource(images[2]);//雨天
                    break;
                    default:
                        break;
            }
            windowManager.addView(weatherView, layoutParams);

        }
    }

        private class FloatingOnTouchListener implements View.OnTouchListener {
            private int x;
            private int y;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getRawX();
                        y = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int nowX = (int) event.getRawX();
                        int nowY = (int) event.getRawY();
                        int movedX = nowX - x;
                        int movedY = nowY - y;
                        x = nowX;
                        y = nowY;
                        layoutParams.x = layoutParams.x + movedX;
                        layoutParams.y = layoutParams.y + movedY;
                        windowManager.updateViewLayout(view, layoutParams);
                        break;
                    default:
                        break;
                }
                return false;
            }
        }

    }
