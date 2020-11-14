package com.pic.shot.sticker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import com.pic.shot.sticker.event.StickerIconEvent;

public class BitmapStickerIcon extends DrawableSticker implements StickerIconEvent {
    public static final String ALIGN_HORIZONTALLY = "ALIGN_HORIZONTALLY";
    public static final String EDIT = "EDIT";
    public static final String FLIP = "FLIP";
    public static final String REMOVE = "REMOVE";
    public static final String ROTATE = "ROTATE";
    public static final String ZOOM = "ZOOM";
    private StickerIconEvent iconEvent;
    private float iconExtraRadius = 10.0f;
    private float iconRadius = 30.0f;
    private int position = 0;
    private String tag;

    /* renamed from: x */
    private float f325x;

    /* renamed from: y */
    private float f326y;

    public BitmapStickerIcon(Drawable paramDrawable, int paramInt, String paramString) {
        super(paramDrawable);
        this.position = paramInt;
        this.tag = paramString;
    }

    public void draw(Canvas paramCanvas, Paint paramPaint) {
        paramCanvas.drawCircle(this.f325x, this.f326y, this.iconRadius, paramPaint);
        draw(paramCanvas);
    }

    public float getIconRadius() {
        return this.iconRadius;
    }

    public int getPosition() {
        return this.position;
    }

    public String getTag() {
        return this.tag;
    }

    public float getX() {
        return this.f325x;
    }

    public float getY() {
        return this.f326y;
    }

    public void onActionDown(StickerView paramStickerView, MotionEvent paramMotionEvent) {
        StickerIconEvent stickerIconEvent = this.iconEvent;
        if (stickerIconEvent != null) {
            stickerIconEvent.onActionDown(paramStickerView, paramMotionEvent);
        }
    }

    public void onActionMove(StickerView paramStickerView, MotionEvent paramMotionEvent) {
        StickerIconEvent stickerIconEvent = this.iconEvent;
        if (stickerIconEvent != null) {
            stickerIconEvent.onActionMove(paramStickerView, paramMotionEvent);
        }
    }

    public void onActionUp(StickerView paramStickerView, MotionEvent paramMotionEvent) {
        StickerIconEvent stickerIconEvent = this.iconEvent;
        if (stickerIconEvent != null) {
            stickerIconEvent.onActionUp(paramStickerView, paramMotionEvent);
        }
    }

    public void setIconEvent(StickerIconEvent paramStickerIconEvent) {
        this.iconEvent = paramStickerIconEvent;
    }

    public void setTag(String paramString) {
        this.tag = paramString;
    }

    public void setX(float paramFloat) {
        this.f325x = paramFloat;
    }

    public void setY(float paramFloat) {
        this.f326y = paramFloat;
    }
}
