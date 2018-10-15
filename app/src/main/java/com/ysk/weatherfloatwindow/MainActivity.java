package com.ysk.weatherfloatwindow;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private int weathercode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
            startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())));
        }
        final Button sunbtn=(Button)findViewById(R.id.sun);
        Button rainbtn=(Button)findViewById(R.id.rain);
        Button cloudbtn=(Button)findViewById(R.id.cloud);

        sunbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weathercode=0;
                Intent intent=new Intent(MainActivity.this,weatherFloatService.class);
                intent.putExtra("weathercode",String.valueOf(weathercode));
                Log.e(TAG, "weathercode: "+weathercode );
                startService(intent);
            }
        });
        cloudbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weathercode=1;
                Intent intent=new Intent(MainActivity.this,weatherFloatService.class);
                intent.putExtra("weathercode",String.valueOf(weathercode));
                startService(intent);
            }
        });
        rainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weathercode=2;
                Intent intent=new Intent(MainActivity.this,weatherFloatService.class);
                intent.putExtra("weathercode",String.valueOf(weathercode));
                startService(intent);
            }
        });

    }


    @Override
    protected void onDestroy() {
        stopService(new Intent(this,weatherFloatService.class));
        super.onDestroy();
    }
}
