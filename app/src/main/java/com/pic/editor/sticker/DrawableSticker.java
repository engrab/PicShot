package com.pic.editor.sticker;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.util.Objects;

public class DrawableSticker extends Sticker {
    private Drawable drawable;

    private final Rect realBounds = new Rect(0, 0, 75, 75);

    public DrawableSticker(Drawable drawable) {
        this.drawable = drawable;
    }

    @NonNull
    public Drawable getDrawable() {
        return this.drawable;
    }

    public DrawableSticker setDrawable(@NonNull Drawable drawable) {
        this.drawable = drawable;
        return this;
    }

    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.concat(getMatrix());
        this.drawable.setBounds(this.realBounds);
        this.drawable.draw(canvas);
        canvas.restore();
    }

    @NonNull
    public DrawableSticker setAlpha(@IntRange(from = 0, to = 255) int i) {
        this.drawable.setAlpha(i);
        return this;
    }

    public int getAlpha() {
        return this.drawable.getAlpha();
    }

    public int getWidth() {
        return this.drawable.getIntrinsicWidth();
    }

    public int getHeight() {
        return this.drawable.getIntrinsicHeight();
    }

    public void release() {
        super.release();
        if (this.drawable != null) {
            this.drawable = null;
        }
    }
}
