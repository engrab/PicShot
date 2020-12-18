package com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.sticker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.utils.SystemUtil;

public class BeautySticker extends Sticker {
    private Drawable drawable;
    private int drawableSizeBoobs;
    private int drawableSizeFace_Height;
    private int drawableSizeFace_Width;
    private int drawableSizeHip1_Height;
    private int drawableSizeHip1_Width;
    private int height_Width;
    private PointF mappedCenterPoint;
    private int radius;
    private Rect realBounds = new Rect(0, 0, getWidth(), getHeight());
    private int type;

    public BeautySticker(Context paramContext, int paramInt, Drawable paramDrawable) {
        this.drawableSizeBoobs = SystemUtil.dpToPx(paramContext, 50);
        this.drawableSizeHip1_Width = SystemUtil.dpToPx(paramContext, 150);
        this.drawableSizeHip1_Height = SystemUtil.dpToPx(paramContext, 75);
        this.drawableSizeFace_Height = SystemUtil.dpToPx(paramContext, 50);
        this.drawableSizeFace_Width = SystemUtil.dpToPx(paramContext, 80);
        this.type = paramInt;
        this.drawable = paramDrawable;
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
        return null;
    }

    public int getHeight() {
        int i = this.type;
        if (i == 1 || i == 0) {
            return this.drawableSizeBoobs;
        }
        if (i == 2) {
            return this.drawableSizeHip1_Height;
        }
        if (i == 4) {
            return this.drawableSizeFace_Height;
        }
        if (i == 10 || i == 11) {
            return this.drawable.getIntrinsicHeight();
        }
        return 0;
    }

    public PointF getMappedCenterPoint2() {
        return this.mappedCenterPoint;
    }

    public int getRadius() {
        return this.radius;
    }

    public int getType() {
        return this.type;
    }

    public int getWidth() {
        int i = this.type;
        if (i == 1 || i == 0) {
            return this.drawableSizeBoobs;
        }
        if (i == 2) {
            return this.drawableSizeHip1_Width;
        }
        if (i == 4) {
            return this.drawableSizeFace_Width;
        }
        if (i == 10 || i == 11) {
            return this.height_Width;
        }
        return 0;
    }

    public void release() {
        super.release();
        if (this.drawable != null) {
            this.drawable = null;
        }
    }

    public BeautySticker setAlpha(int paramInt) {
        this.drawable.setAlpha(paramInt);
        return this;
    }

    public BeautySticker setDrawable(Drawable paramDrawable) {
        return this;
    }

    public void setRadius(int paramInt) {
        this.radius = paramInt;
    }

    public void updateRadius() {
        RectF rectF = getBound();
        int i = this.type;
        if (i == 0 || i == 1 || i == 4) {
            this.radius = (int) (rectF.left + rectF.right);
        } else if (i == 2) {
            this.radius = (int) (rectF.top + rectF.bottom);
        }
        this.mappedCenterPoint = getMappedCenterPoint();
    }
}
