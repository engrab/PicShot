package com.pic.editor.sticker;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.Objects;

public class DrawableSticker extends Sticker {
    private Drawable drawable;
    private Rect realBounds = new Rect(0, 0, 500, 500);

    public DrawableSticker(Drawable paramDrawable) {
        drawable = paramDrawable;
    }

    public void draw(Canvas paramCanvas) {
        paramCanvas.save();
        paramCanvas.concat(getMatrix());
        this.drawable.setBounds(this.realBounds);
        this.drawable.draw(paramCanvas);
        paramCanvas.restore();
    }

    public int getAlpha() {
        return this.drawable.getAlpha();
    }

    public Drawable getDrawable() {
        return this.drawable;
    }

    public int getHeight() {
        return drawable.getIntrinsicHeight();
    }

    public int getWidth() {
        return drawable.getIntrinsicWidth();
    }

    public void release() {
        super.release();
        if (this.drawable != null) {
            this.drawable = null;
        }
    }

    public DrawableSticker setAlpha(int paramInt) {
        this.drawable.setAlpha(paramInt);
        return this;
    }

    public DrawableSticker setDrawable(Drawable paramDrawable) {
        this.drawable = paramDrawable;
        return this;
    }
}
