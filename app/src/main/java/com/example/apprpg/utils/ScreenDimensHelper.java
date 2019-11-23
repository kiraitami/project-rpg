package com.example.apprpg.utils;

import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class ScreenDimensHelper {

    public static long getScreenWidth(WindowManager windowManager){
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return  size.x;
    }

    public static long getScreenHeight(WindowManager windowManager){
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return  size.y;
    }

}
