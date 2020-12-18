package com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class MosaicUtil {
    public static Bitmap getMosaic(Bitmap bitmap) {
        int i;
        Bitmap bitmap2;
        int i2 = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(i2, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        int i22 = 50;
        int ceil = (int) Math.ceil((double) (((float) i2) / 50.0f));
        int ceil2 = (int) Math.ceil((double) (((float) height) / 50.0f));
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        int i3 = 0;
        while (i3 < ceil) {
            int i4 = 0;
            while (i4 < ceil2) {
                int i5 = i22 * i3;
                int i6 = i22 * i4;
                int i7 = i5 + 50;
                if (i7 > i2) {
                    i7 = i2;
                }
                int i8 = i6 + 50;
                if (i8 > height) {
                    bitmap2 = bitmap;
                    i = height;
                } else {
                    i = i8;
                    bitmap2 = bitmap;
                }
                int width = i;
                int height2 = height;
                int pixel = bitmap2.getPixel(i5, i6);
                int i9 = i22;
                Rect rect = new Rect(i5, i6, i7, width);
                paint.setColor(pixel);
                canvas.drawRect(rect, paint);
                i4++;
                i22 = 50;
                i2 = i2;
                height = height2;
            }
            int width2 = i2;
            int i10 = height;
            int i11 = i22;
            i3++;
            i22 = 50;
        }
        canvas.save();
        return createBitmap;
    }
}
