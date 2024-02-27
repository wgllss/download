package com.atar.download;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.atar.downloadapp.R;

public class DeviceActivity extends AppCompatActivity {

    private TextView txt_screen_info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        txt_screen_info = findViewById(R.id.txt_screen_info);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels; // 屏幕宽度(像素)
        int height = metric.heightPixels; // 屏幕高度(像素)
        float density = metric.density; // 屏幕密度(0.75 / 1.0 / 1.5)
        int densityDpi = metric.densityDpi; // 屏幕密度DPI(120 / 160 / 240)
        txt_screen_info.setText("屏幕宽度(像素):  " + width + "\n"
                + "屏幕高度(像素):  " + height + "\n"
                + "屏幕高度 density:  " + density + "\n"
                + "屏幕densityDpi:  " + densityDpi + "\n"
                + "Build.BOARD:  " + Build.BOARD + "\n"
                + "Build.MODEL:  " + Build.MODEL + "\n"
                + "Build.CPU_ABI:  " + Build.CPU_ABI + "\n"
        );
    }
}
