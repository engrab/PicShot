package com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.sticker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;

public class SplashSticker extends Sticker {
    private Bitmap bitmapOver;
    private Bitmap bitmapXor;
    private Paint over;
    private Paint xor;

    public SplashSticker(Bitmap paramBitmap1, Bitmap paramBitmap2) {
        Paint paint = new Paint();
        this.xor = paint;
        paint.setDither(true);
        this.xor.setAntiAlias(true);
        this.xor.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        Paint paint2 = new Paint();
        this.over = paint2;
        paint2.setDither(true);
        this.over.setAntiAlias(true);
        this.over.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        this.bitmapXor = paramBitmap1;
        this.bitmapOver = paramBitmap2;
    }

    private Bitmap getBitmapOver() {
        return this.bitmapOver;
    }

    private Bitmap getBitmapXor() {
        return this.bitmapXor;
    }

    public void draw(Canvas paramCanvas) {
        paramCanvas.drawBitmap(getBitmapXor(), getMatrix(), this.xor);
        paramCanvas.drawBitmap(getBitmapOver(), getMatrix(), this.over);
    }

    public int getAlpha() {
        return 1;
    }

    public Drawable getDrawable() {
        return null;
    }

    public int getHeight() {
        return this.bitmapOver.getHeight();
    }

    public int getWidth() {
        return this.bitmapXor.getWidth();
    }

    public void release() {
        super.release();
        this.xor = null;
        this.over = null;
        Bitmap bitmap = this.bitmapXor;
        if (bitmap != null) {
            bitmap.recycle();
        }
        this.bitmapXor = null;
        Bitmap bitmap2 = this.bitmapOver;
        if (bitmap2 != null) {
            bitmap2.recycle();
        }
        this.bitmapOver = null;
    }

    public SplashSticker setAlpha(int paramInt) {
        return this;
    }

    public SplashSticker setDrawable(Drawable paramDrawable) {
        return this;
    }
}
