package com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

public class PicShot extends Application {
    private static PicShot picShot;

    public void onCreate() {
        super.onCreate();
        picShot = this;
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke((Object) null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Context getContext() {
        return picShot.getContext();
    }
}
