package com.pic.shot.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtils {
    public static File saveBitmapAsFile(Bitmap bitmap) {
        String file = Environment.getExternalStorageDirectory().toString();
        File file2 = new File(file + "/PicShot");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        try {
            File file3 = new File(file + "/PicShot/" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()) + ".jpg");
            file3.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file3);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return file3;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }
}
