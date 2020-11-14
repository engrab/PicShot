package com.pic.shot.sticker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

class StickerUtils {
    StickerUtils() {
    }

    public static void notifySystemGallery(Context paramContext, File paramFile) {
        if (paramFile == null || !paramFile.exists()) {
            throw new IllegalArgumentException("bmp should not be null");
        }
        try {
            MediaStore.Images.Media.insertImage(paramContext.getContentResolver(), paramFile.getAbsolutePath(), paramFile.getName(), (String) null);
            paramContext.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(paramFile)));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("File couldn't be found");
        }
    }

    public static File saveImageToGallery(File paramFile, Bitmap paramBitmap) {
        if (paramBitmap != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(paramFile);
                paramBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }
            Log.e("StickerView", "saveImageToGallery: the path of bmp is " + paramFile.getAbsolutePath());
            return paramFile;
        }
        throw new IllegalArgumentException("bmp should not be null");
    }

    public static void trapToRect(RectF paramRectF, float[] paramArrayOffloat) {
        float f3;
        float f32;
        paramRectF.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        for (int i = 1; i < paramArrayOffloat.length; i += 2) {
            float f2 = ((float) Math.round(paramArrayOffloat[i - 1] * 10.0f)) / 10.0f;
            float f1 = ((float) Math.round(paramArrayOffloat[i] * 10.0f)) / 10.0f;
            if (f2 < paramRectF.left) {
                f3 = f2;
            } else {
                f3 = paramRectF.left;
            }
            paramRectF.left = f3;
            if (f1 < paramRectF.top) {
                f32 = f1;
            } else {
                f32 = paramRectF.top;
            }
            paramRectF.top = f32;
            if (f2 <= paramRectF.right) {
                f2 = paramRectF.right;
            }
            paramRectF.right = f2;
            if (f1 <= paramRectF.bottom) {
                f1 = paramRectF.bottom;
            }
            paramRectF.bottom = f1;
        }
        paramRectF.sort();
    }
}
