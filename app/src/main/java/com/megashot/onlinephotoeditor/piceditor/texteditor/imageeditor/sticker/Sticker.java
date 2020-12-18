package com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.sticker;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public abstract class Sticker {
    private final float[] boundPoints = new float[8];
    private boolean isFlippedHorizontally;
    private boolean isFlippedVertically;
    private boolean isShow = true;
    private final float[] mappedBounds = new float[8];
    private final Matrix matrix = new Matrix();
    private final float[] matrixValues = new float[9];
    private final RectF trappedRect = new RectF();
    private final float[] unrotatedPoint = new float[2];
    private final float[] unrotatedWrapperCorner = new float[8];

    public abstract void draw(Canvas canvas);

    public abstract int getAlpha();

    public abstract Drawable getDrawable();

    public abstract int getHeight();

    public abstract int getWidth();

    public abstract Sticker setAlpha(int i);

    public abstract Sticker setDrawable(Drawable drawable);

    public boolean contains(float paramFloat1, float paramFloat2) {
        return contains(new float[]{paramFloat1, paramFloat2});
    }

    public boolean contains(float[] paramArrayOffloat) {
        Matrix matrix2 = new Matrix();
        matrix2.setRotate(-getCurrentAngle());
        getBoundPoints(this.boundPoints);
        getMappedPoints(this.mappedBounds, this.boundPoints);
        matrix2.mapPoints(this.unrotatedWrapperCorner, this.mappedBounds);
        matrix2.mapPoints(this.unrotatedPoint, paramArrayOffloat);
        StickerUtils.trapToRect(this.trappedRect, this.unrotatedWrapperCorner);
        RectF rectF = this.trappedRect;
        float[] fArr = this.unrotatedPoint;
        return rectF.contains(fArr[0], fArr[1]);
    }

    public RectF getBound() {
        RectF rectF = new RectF();
        getBound(rectF);
        return rectF;
    }

    public void getBound(RectF paramRectF) {
        paramRectF.set(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
    }

    public void getBoundPoints(float[] paramArrayOffloat) {
        if (!this.isFlippedHorizontally) {
            if (!this.isFlippedVertically) {
                paramArrayOffloat[0] = 0.0f;
                paramArrayOffloat[1] = 0.0f;
                paramArrayOffloat[2] = (float) getWidth();
                paramArrayOffloat[3] = 0.0f;
                paramArrayOffloat[4] = 0.0f;
                paramArrayOffloat[5] = (float) getHeight();
                paramArrayOffloat[6] = (float) getWidth();
                paramArrayOffloat[7] = (float) getHeight();
                return;
            }
            paramArrayOffloat[0] = 0.0f;
            paramArrayOffloat[1] = (float) getHeight();
            paramArrayOffloat[2] = (float) getWidth();
            paramArrayOffloat[3] = (float) getHeight();
            paramArrayOffloat[4] = 0.0f;
            paramArrayOffloat[5] = 0.0f;
            paramArrayOffloat[6] = (float) getWidth();
            paramArrayOffloat[7] = 0.0f;
        } else if (!this.isFlippedVertically) {
            paramArrayOffloat[0] = (float) getWidth();
            paramArrayOffloat[1] = 0.0f;
            paramArrayOffloat[2] = 0.0f;
            paramArrayOffloat[3] = 0.0f;
            paramArrayOffloat[4] = (float) getWidth();
            paramArrayOffloat[5] = (float) getHeight();
            paramArrayOffloat[6] = 0.0f;
            paramArrayOffloat[7] = (float) getHeight();
        } else {
            paramArrayOffloat[0] = (float) getWidth();
            paramArrayOffloat[1] = (float) getHeight();
            paramArrayOffloat[2] = 0.0f;
            paramArrayOffloat[3] = (float) getHeight();
            paramArrayOffloat[4] = (float) getWidth();
            paramArrayOffloat[5] = 0.0f;
            paramArrayOffloat[6] = 0.0f;
            paramArrayOffloat[7] = 0.0f;
        }
    }

    public float[] getBoundPoints() {
        float[] arrayOfFloat = new float[8];
        getBoundPoints(arrayOfFloat);
        return arrayOfFloat;
    }

    public PointF getCenterPoint() {
        PointF pointF = new PointF();
        getCenterPoint(pointF);
        return pointF;
    }

    public void getCenterPoint(PointF paramPointF) {
        paramPointF.set((((float) getWidth()) * 1.0f) / 2.0f, (((float) getHeight()) * 1.0f) / 2.0f);
    }

    public float getCurrentAngle() {
        return getMatrixAngle(this.matrix);
    }

    public RectF getMappedBound() {
        RectF rectF = new RectF();
        getMappedBound(rectF, getBound());
        return rectF;
    }

    public void getMappedBound(RectF paramRectF1, RectF paramRectF2) {
        this.matrix.mapRect(paramRectF1, paramRectF2);
    }

    public PointF getMappedCenterPoint() {
        PointF pointF = getCenterPoint();
        getMappedCenterPoint(pointF, new float[2], new float[2]);
        return pointF;
    }

    public void getMappedCenterPoint(PointF paramPointF, float[] paramArrayOffloat1, float[] paramArrayOffloat2) {
        getCenterPoint(paramPointF);
        paramArrayOffloat2[0] = paramPointF.x;
        paramArrayOffloat2[1] = paramPointF.y;
        getMappedPoints(paramArrayOffloat1, paramArrayOffloat2);
        paramPointF.set(paramArrayOffloat1[0], paramArrayOffloat1[1]);
    }

    public void getMappedPoints(float[] paramArrayOffloat1, float[] paramArrayOffloat2) {
        this.matrix.mapPoints(paramArrayOffloat1, paramArrayOffloat2);
    }

    public Matrix getMatrix() {
        return this.matrix;
    }

    public float getMatrixAngle(Matrix paramMatrix) {
        return (float) Math.toDegrees(-Math.atan2((double) getMatrixValue(paramMatrix, 1), (double) getMatrixValue(paramMatrix, 0)));
    }

    public float getMatrixValue(Matrix paramMatrix, int paramInt) {
        paramMatrix.getValues(this.matrixValues);
        return this.matrixValues[paramInt];
    }

    public boolean isFlippedHorizontally() {
        return this.isFlippedHorizontally;
    }

    public boolean isFlippedVertically() {
        return this.isFlippedVertically;
    }

    public boolean isShow() {
        return this.isShow;
    }

    public void release() {
    }

    public Sticker setFlippedHorizontally(boolean paramBoolean) {
        this.isFlippedHorizontally = paramBoolean;
        return this;
    }

    public Sticker setFlippedVertically(boolean paramBoolean) {
        this.isFlippedVertically = paramBoolean;
        return this;
    }

    public Sticker setMatrix(Matrix paramMatrix) {
        this.matrix.set(paramMatrix);
        return this;
    }

    public void setShow(boolean paramBoolean) {
        this.isShow = paramBoolean;
    }
}
