package com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.picker.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.OverScroller;
import android.widget.Scroller;

public class TouchImageView extends androidx.appcompat.widget.AppCompatImageView {
    private static final String DEBUG = "DEBUG";
    private static final float SUPER_MAX_MULTIPLIER = 1.25f;
    private static final float SUPER_MIN_MULTIPLIER = 0.75f;
    public Context context;
    private ZoomVariables delayedZoomVariables;
    public GestureDetector.OnDoubleTapListener doubleTapListener = null;
    public Fling fling;
    private boolean imageRenderedAtLeastOnce;

    /* renamed from: m */
    public float[] f324m;
    public GestureDetector mGestureDetector;
    public ScaleGestureDetector mScaleDetector;
    private ScaleType mScaleType;
    private float matchViewHeight;
    private float matchViewWidth;
    public Matrix matrix;
    public float maxScale;
    public float minScale;
    public float normalizedScale;
    private boolean onDrawReady;
    private float prevMatchViewHeight;
    private float prevMatchViewWidth;
    private Matrix prevMatrix;
    private int prevViewHeight;
    private int prevViewWidth;
    public State state;
    private float superMaxScale;
    private float superMinScale;
    public OnTouchImageViewListener touchImageViewListener = null;
    public OnTouchListener userTouchListener = null;
    public int viewHeight;
    public int viewWidth;

    public interface OnTouchImageViewListener {
        void onMove();
    }

    private enum State {
        NONE,
        DRAG,
        ZOOM,
        FLING,
        ANIMATE_ZOOM
    }

    public float getFixDragTrans(float f, float f2, float f3) {
        if (f3 <= f2) {
            return 0.0f;
        }
        return f;
    }

    private float getFixTrans(float f, float f2, float f3) {
        float f5;
        float f4;
        if (f3 <= f2) {
            f4 = f2 - f3;
            f5 = 0.0f;
        } else {
            f5 = f2 - f3;
            f4 = 0.0f;
        }
        if (f < f5) {
            return (-f) + f5;
        }
        if (f > f4) {
            return (-f) + f4;
        }
        return 0.0f;
    }

    public TouchImageView(Context context2) {
        super(context2);
        sharedConstructing(context2);
    }

    public TouchImageView(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        sharedConstructing(context2);
    }

    public TouchImageView(Context context2, AttributeSet attributeSet, int i) {
        super(context2, attributeSet, i);
        sharedConstructing(context2);
    }

    private void sharedConstructing(Context context2) {
        super.setClickable(true);
        this.context = context2;
        ScaleTypeArr scaleTypeArr = null;
        this.mScaleDetector = new ScaleGestureDetector(context2, new ScaleListener(this, this, scaleTypeArr));
        this.mGestureDetector = new GestureDetector(context2, new GestureListener(this, this, scaleTypeArr));
        this.matrix = new Matrix();
        this.prevMatrix = new Matrix();
        this.f324m = new float[9];
        this.normalizedScale = 1.0f;
        if (this.mScaleType == null) {
            this.mScaleType = ScaleType.FIT_CENTER;
        }
        this.minScale = 1.0f;
        this.maxScale = 3.0f;
        this.superMinScale = 1.0f * SUPER_MIN_MULTIPLIER;
        this.superMaxScale = 3.0f * SUPER_MAX_MULTIPLIER;
        setImageMatrix(this.matrix);
        setScaleType(ScaleType.MATRIX);
        setState(State.NONE);
        this.onDrawReady = false;
        super.setOnTouchListener(new PrivateOnTouchListener(this, this, scaleTypeArr));
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.userTouchListener = onTouchListener;
    }

    public void setImageResource(int i) {
        super.setImageResource(i);
        savePreviousImageValues();
        fitImageToView();
    }

    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        savePreviousImageValues();
        fitImageToView();
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        savePreviousImageValues();
        fitImageToView();
    }

    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        savePreviousImageValues();
        fitImageToView();
    }

    public void setScaleType(ScaleType scaleType) {
        if (scaleType == ScaleType.FIT_START || scaleType == ScaleType.FIT_END) {
            throw new UnsupportedOperationException("TouchImageView does not support FIT_START or FIT_END");
        } else if (scaleType == ScaleType.MATRIX) {
            super.setScaleType(ScaleType.MATRIX);
        } else {
            this.mScaleType = scaleType;
            if (this.onDrawReady) {
                setZoom(this);
            }
        }
    }

    public ScaleType getScaleType() {
        return this.mScaleType;
    }

    private void savePreviousImageValues() {
        Matrix matrix2 = this.matrix;
        if (matrix2 != null && this.viewHeight != 0 && this.viewWidth != 0) {
            matrix2.getValues(this.f324m);
            this.prevMatrix.setValues(this.f324m);
            this.prevMatchViewHeight = this.matchViewHeight;
            this.prevMatchViewWidth = this.matchViewWidth;
            this.prevViewHeight = this.viewHeight;
            this.prevViewWidth = this.viewWidth;
        }
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putFloat("saveScale", this.normalizedScale);
        bundle.putFloat("matchViewHeight", this.matchViewHeight);
        bundle.putFloat("matchViewWidth", this.matchViewWidth);
        bundle.putInt("viewWidth", this.viewWidth);
        bundle.putInt("viewHeight", this.viewHeight);
        this.matrix.getValues(this.f324m);
        bundle.putFloatArray("matrix", this.f324m);
        bundle.putBoolean("imageRendered", this.imageRenderedAtLeastOnce);
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.normalizedScale = bundle.getFloat("saveScale");
            float[] floatArray = bundle.getFloatArray("matrix");
            this.f324m = floatArray;
            this.prevMatrix.setValues(floatArray);
            this.prevMatchViewHeight = bundle.getFloat("matchViewHeight");
            this.prevMatchViewWidth = bundle.getFloat("matchViewWidth");
            this.prevViewHeight = bundle.getInt("viewHeight");
            this.prevViewWidth = bundle.getInt("viewWidth");
            this.imageRenderedAtLeastOnce = bundle.getBoolean("imageRendered");
            super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    public void onDraw(Canvas canvas) {
        this.onDrawReady = true;
        this.imageRenderedAtLeastOnce = true;
        ZoomVariables zoomVariables = this.delayedZoomVariables;
        if (zoomVariables != null) {
            setZoom(zoomVariables.scale, this.delayedZoomVariables.focusX, this.delayedZoomVariables.focusY, this.delayedZoomVariables.scaleType);
            this.delayedZoomVariables = null;
        }
        super.onDraw(canvas);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        savePreviousImageValues();
    }

    public float getCurrentZoom() {
        return this.normalizedScale;
    }

    public void resetZoom() {
        this.normalizedScale = 1.0f;
        fitImageToView();
    }

    public void setZoom(float f, float f2, float f3) {
        setZoom(f, f2, f3, this.mScaleType);
    }

    public void setZoom(float f, float f2, float f3, ScaleType scaleType) {
        if (!this.onDrawReady) {
            this.delayedZoomVariables = new ZoomVariables(f, f2, f3, scaleType);
            return;
        }
        if (scaleType != this.mScaleType) {
            setScaleType(scaleType);
        }
        resetZoom();
        scaleImage((double) f, (float) (this.viewWidth / 2), (float) (this.viewHeight / 2), true);
        this.matrix.getValues(this.f324m);
        this.f324m[2] = -((getImageWidth() * f2) - (((float) this.viewWidth) * 0.5f));
        this.f324m[5] = -((getImageHeight() * f3) - (((float) this.viewHeight) * 0.5f));
        this.matrix.setValues(this.f324m);
        fixTrans();
        setImageMatrix(this.matrix);
    }

    public void setZoom(TouchImageView touchImageView) {
        PointF scrollPosition = touchImageView.getScrollPosition();
        setZoom(touchImageView.getCurrentZoom(), scrollPosition.x, scrollPosition.y, touchImageView.getScaleType());
    }

    public PointF getScrollPosition() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return null;
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        PointF transformCoordTouchToBitmap = transformCoordTouchToBitmap((float) (this.viewWidth / 2), (float) (this.viewHeight / 2), true);
        transformCoordTouchToBitmap.x /= (float) intrinsicWidth;
        transformCoordTouchToBitmap.y /= (float) intrinsicHeight;
        return transformCoordTouchToBitmap;
    }

    public void fixTrans() {
        this.matrix.getValues(this.f324m);
        float[] fArr = this.f324m;
        float f = fArr[2];
        float f2 = fArr[5];
        float fixTrans = getFixTrans(f, (float) this.viewWidth, getImageWidth());
        float fixTrans2 = getFixTrans(f2, (float) this.viewHeight, getImageHeight());
        if (fixTrans != 0.0f || fixTrans2 != 0.0f) {
            this.matrix.postTranslate(fixTrans, fixTrans2);
        }
    }

    public void fixScaleTrans() {
        fixTrans();
        this.matrix.getValues(this.f324m);
        float imageWidth = getImageWidth();
        int i = this.viewWidth;
        if (imageWidth < ((float) i)) {
            this.f324m[2] = (((float) i) - getImageWidth()) / 2.0f;
        }
        float imageHeight = getImageHeight();
        int i2 = this.viewHeight;
        if (imageHeight < ((float) i2)) {
            this.f324m[5] = (((float) i2) - getImageHeight()) / 2.0f;
        }
        this.matrix.setValues(this.f324m);
    }

    public float getImageWidth() {
        return this.matchViewWidth * this.normalizedScale;
    }

    public float getImageHeight() {
        return this.matchViewHeight * this.normalizedScale;
    }

    public void onMeasure(int i, int i2) {
        Drawable drawable = getDrawable();
        if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0) {
            setMeasuredDimension(0, 0);
            return;
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        int size = MeasureSpec.getSize(i);
        int mode = MeasureSpec.getMode(i);
        int size2 = MeasureSpec.getSize(i2);
        int mode2 = MeasureSpec.getMode(i2);
        this.viewWidth = setViewSize(mode, size, intrinsicWidth);
        int viewSize = setViewSize(mode2, size2, intrinsicHeight);
        this.viewHeight = viewSize;
        setMeasuredDimension(this.viewWidth, viewSize);
        fitImageToView();
    }

    private void fitImageToView() {
        Drawable drawable = getDrawable();
        if (drawable != null && drawable.getIntrinsicWidth() != 0 && drawable.getIntrinsicHeight() != 0 && this.matrix != null && this.prevMatrix != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            float f2 = ((float) this.viewWidth) / ((float) intrinsicWidth);
            float f4 = ((float) this.viewHeight) / ((float) intrinsicHeight);
            int i = ScaleTypeArr.arr[this.mScaleType.ordinal()];
            if (i == 1) {
                return;
            }
            if (i == 2) {
                float f22 = Math.max(f2, f4);
            } else if (i == 3) {
                float f42 = Math.min(1.0f, Math.min(f2, f4));
            } else if (i != 4 && i != 5) {
                throw new UnsupportedOperationException("TouchImageView does not support FIT_START or FIT_END");
            }
        }
    }

    static class ScaleTypeArr {
        static final int[] arr;

        ScaleTypeArr() {
        }

        static {
            int[] iArr = new int[ScaleType.values().length];
            arr = iArr;
            iArr[ScaleType.CENTER.ordinal()] = 1;
            arr[ScaleType.CENTER_CROP.ordinal()] = 2;
            arr[ScaleType.CENTER_INSIDE.ordinal()] = 3;
            arr[ScaleType.FIT_CENTER.ordinal()] = 4;
            arr[ScaleType.FIT_XY.ordinal()] = 5;
        }
    }

    private int setViewSize(int i, int i2, int i3) {
        if (i != Integer.MIN_VALUE) {
            return i != 0 ? i2 : i3;
        }
        return Math.min(i3, i2);
    }

    public void setState(State state2) {
        this.state = state2;
    }

    public boolean canScrollHorizontally(int i) {
        this.matrix.getValues(this.f324m);
        float f = this.f324m[2];
        if (getImageWidth() < ((float) this.viewWidth)) {
            return false;
        }
        if (f >= -1.0f && i < 0) {
            return false;
        }
        if (Math.abs(f) + ((float) this.viewWidth) + 1.0f < getImageWidth() || i <= 0) {
            return true;
        }
        return false;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private GestureListener() {
        }

        GestureListener(TouchImageView touchImageView, TouchImageView touchImageView2, ScaleTypeArr r2) {
            this();
        }

        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            if (TouchImageView.this.doubleTapListener != null) {
                return TouchImageView.this.doubleTapListener.onSingleTapConfirmed(motionEvent);
            }
            return TouchImageView.this.performClick();
        }

        public void onLongPress(MotionEvent motionEvent) {
            TouchImageView.this.performLongClick();
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (TouchImageView.this.fling != null) {
                TouchImageView.this.fling.cancelFling();
            }
            TouchImageView touchImageView = TouchImageView.this;
            touchImageView.fling = new Fling(touchImageView, (int) f, (int) f2);
            TouchImageView touchImageView2 = TouchImageView.this;
            touchImageView2.compatPostOnAnimation(touchImageView2.fling);
            return super.onFling(motionEvent, motionEvent2, f, f2);
        }

        public boolean onDoubleTap(MotionEvent motionEvent) {
            boolean onDoubleTap = TouchImageView.this.doubleTapListener != null ? TouchImageView.this.doubleTapListener.onDoubleTap(motionEvent) : false;
            if (TouchImageView.this.state != State.NONE) {
                return onDoubleTap;
            }
            TouchImageView touchImageView = TouchImageView.this;
            touchImageView.compatPostOnAnimation(new DoubleTapZoom(touchImageView.normalizedScale == TouchImageView.this.minScale ? TouchImageView.this.maxScale : TouchImageView.this.minScale, motionEvent.getX(), motionEvent.getY(), false));
            return true;
        }

        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            if (TouchImageView.this.doubleTapListener != null) {
                return TouchImageView.this.doubleTapListener.onDoubleTapEvent(motionEvent);
            }
            return false;
        }
    }

    private class PrivateOnTouchListener implements OnTouchListener {
        private PointF last;

        private PrivateOnTouchListener() {
            this.last = new PointF();
        }

        PrivateOnTouchListener(TouchImageView touchImageView, TouchImageView touchImageView2, ScaleTypeArr r2) {
            this();
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            TouchImageView.this.mScaleDetector.onTouchEvent(motionEvent);
            TouchImageView.this.mGestureDetector.onTouchEvent(motionEvent);
            PointF pointF = new PointF(motionEvent.getX(), motionEvent.getY());
            if (TouchImageView.this.state == State.NONE || TouchImageView.this.state == State.DRAG || TouchImageView.this.state == State.FLING) {
                int action = motionEvent.getAction();
                if (action != 6) {
                    if (action == 0) {
                        this.last.set(pointF);
                        if (TouchImageView.this.fling != null) {
                            TouchImageView.this.fling.cancelFling();
                        }
                        TouchImageView.this.setState(State.DRAG);
                    } else if (action == 2 && TouchImageView.this.state == State.DRAG) {
                        float f = pointF.x - this.last.x;
                        float f2 = pointF.y - this.last.y;
                        Matrix matrix = TouchImageView.this.matrix;
                        TouchImageView touchImageView = TouchImageView.this;
                        float fixDragTrans = touchImageView.getFixDragTrans(f, (float) touchImageView.viewWidth, TouchImageView.this.getImageWidth());
                        TouchImageView touchImageView2 = TouchImageView.this;
                        matrix.postTranslate(fixDragTrans, touchImageView2.getFixDragTrans(f2, (float) touchImageView2.viewHeight, TouchImageView.this.getImageHeight()));
                        TouchImageView.this.fixTrans();
                        this.last.set(pointF.x, pointF.y);
                    }
                }
                TouchImageView.this.setState(State.NONE);
            }
            TouchImageView touchImageView3 = TouchImageView.this;
            touchImageView3.setImageMatrix(touchImageView3.matrix);
            if (TouchImageView.this.userTouchListener != null) {
                TouchImageView.this.userTouchListener.onTouch(view, motionEvent);
            }
            if (TouchImageView.this.touchImageViewListener == null) {
                return true;
            }
            TouchImageView.this.touchImageViewListener.onMove();
            return true;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        ScaleListener(TouchImageView touchImageView, TouchImageView touchImageView2, ScaleTypeArr r2) {
            this();
        }

        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            TouchImageView.this.setState(State.ZOOM);
            return true;
        }

        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            TouchImageView.this.scaleImage((double) scaleGestureDetector.getScaleFactor(), scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY(), true);
            if (TouchImageView.this.touchImageViewListener == null) {
                return true;
            }
            TouchImageView.this.touchImageViewListener.onMove();
            return true;
        }

        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            super.onScaleEnd(scaleGestureDetector);
            TouchImageView.this.setState(State.NONE);
            float access$700 = TouchImageView.this.normalizedScale;
            boolean z = true;
            if (TouchImageView.this.normalizedScale > TouchImageView.this.maxScale) {
                access$700 = TouchImageView.this.maxScale;
            } else if (TouchImageView.this.normalizedScale < TouchImageView.this.minScale) {
                access$700 = TouchImageView.this.minScale;
            } else {
                z = false;
            }
            float f = access$700;
            if (z) {
                TouchImageView touchImageView = TouchImageView.this;
                touchImageView.compatPostOnAnimation(new DoubleTapZoom(f, (float) (touchImageView.viewWidth / 2), (float) (TouchImageView.this.viewHeight / 2), true));
            }
        }
    }

    public void scaleImage(double d, float f, float f2, boolean z) {
        float f4;
        float f3;
        if (z) {
            f3 = this.superMinScale;
            f4 = this.superMaxScale;
        } else {
            f3 = this.minScale;
            f4 = this.maxScale;
        }
        float f5 = this.normalizedScale;
        float f6 = (float) (((double) this.normalizedScale) * d);
        this.normalizedScale = f6;
        if (f6 > f4) {
            this.normalizedScale = f4;
            d = (double) (f4 / f5);
        } else if (f6 < f3) {
            this.normalizedScale = f3;
            d = (double) (f3 / f5);
        }
        float f62 = (float) d;
        this.matrix.postScale(f62, f62, f, f2);
        fixScaleTrans();
    }

    private class DoubleTapZoom implements Runnable {
        private static final float ZOOM_TIME = 500.0f;
        private float bitmapX;
        private float bitmapY;
        private PointF endTouch;
        private AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        private long startTime;
        private PointF startTouch;
        private float startZoom;
        private boolean stretchImageToSuper;
        private float targetZoom;

        DoubleTapZoom(float f, float f2, float f3, boolean z) {
            TouchImageView.this.setState(State.ANIMATE_ZOOM);
            this.startTime = System.currentTimeMillis();
            this.startZoom = TouchImageView.this.normalizedScale;
            this.targetZoom = f;
            this.stretchImageToSuper = z;
            PointF pointF = TouchImageView.this.transformCoordTouchToBitmap(f2, f3, false);
            this.bitmapX = pointF.x;
            float f4 = pointF.y;
            this.bitmapY = f4;
            this.startTouch = TouchImageView.this.transformCoordBitmapToTouch(this.bitmapX, f4);
            this.endTouch = new PointF((float) (TouchImageView.this.viewWidth / 2), (float) (TouchImageView.this.viewHeight / 2));
        }

        public void run() {
            float interpolate = interpolate();
            TouchImageView.this.scaleImage(calculateDeltaScale(interpolate), this.bitmapX, this.bitmapY, this.stretchImageToSuper);
            translateImageToCenterTouchPosition(interpolate);
            TouchImageView.this.fixScaleTrans();
            TouchImageView touchImageView = TouchImageView.this;
            touchImageView.setImageMatrix(touchImageView.matrix);
            if (TouchImageView.this.touchImageViewListener != null) {
                TouchImageView.this.touchImageViewListener.onMove();
            }
            if (interpolate < 1.0f) {
                TouchImageView.this.compatPostOnAnimation(this);
            } else {
                TouchImageView.this.setState(State.NONE);
            }
        }

        private void translateImageToCenterTouchPosition(float f) {
            float f2 = this.startTouch.x + ((this.endTouch.x - this.startTouch.x) * f);
            float f3 = this.startTouch.y + ((this.endTouch.y - this.startTouch.y) * f);
            PointF access$2400 = TouchImageView.this.transformCoordBitmapToTouch(this.bitmapX, this.bitmapY);
            TouchImageView.this.matrix.postTranslate(f2 - access$2400.x, f3 - access$2400.y);
        }

        private float interpolate() {
            return this.interpolator.getInterpolation(Math.min(1.0f, ((float) (System.currentTimeMillis() - this.startTime)) / ZOOM_TIME));
        }

        private double calculateDeltaScale(float f) {
            float f2 = this.startZoom;
            return ((double) (f2 + ((this.targetZoom - f2) * f))) / ((double) TouchImageView.this.normalizedScale);
        }
    }

    public PointF transformCoordTouchToBitmap(float f, float f2, boolean z) {
        this.matrix.getValues(this.f324m);
        float intrinsicWidth = (float) getDrawable().getIntrinsicWidth();
        float intrinsicHeight = (float) getDrawable().getIntrinsicHeight();
        float[] fArr = this.f324m;
        float f3 = fArr[2];
        float f4 = fArr[5];
        float imageWidth = ((f - f3) * intrinsicWidth) / getImageWidth();
        float imageHeight = ((f2 - f4) * intrinsicHeight) / getImageHeight();
        if (z) {
            imageWidth = Math.min(Math.max(imageWidth, 0.0f), intrinsicWidth);
            imageHeight = Math.min(Math.max(imageHeight, 0.0f), intrinsicHeight);
        }
        return new PointF(imageWidth, imageHeight);
    }

    public PointF transformCoordBitmapToTouch(float f, float f2) {
        this.matrix.getValues(this.f324m);
        return new PointF(this.f324m[2] + (getImageWidth() * (f / ((float) getDrawable().getIntrinsicWidth()))), this.f324m[5] + (getImageHeight() * (f2 / ((float) getDrawable().getIntrinsicHeight()))));
    }

    private class Fling implements Runnable {
        int currX;
        int currY;
        CompatScroller scroller;
        final /* synthetic */ TouchImageView this$0;

        Fling(TouchImageView touchImageView, int i, int i2) {
            int i3;
            int i4;
            int i6;
            int i5;
            TouchImageView touchImageView2 = touchImageView;
            this.this$0 = touchImageView2;
            touchImageView2.setState(State.FLING);
            this.scroller = new CompatScroller(touchImageView2.context);
            touchImageView2.matrix.getValues(touchImageView2.f324m);
            int i7 = (int) touchImageView2.f324m[2];
            int i8 = (int) touchImageView2.f324m[5];
            if (touchImageView.getImageWidth() > ((float) touchImageView2.viewWidth)) {
                i4 = touchImageView2.viewWidth - ((int) touchImageView.getImageWidth());
                i3 = 0;
            } else {
                int i42 = i7;
                i4 = i42;
                i3 = i42;
            }
            if (touchImageView.getImageHeight() > ((float) touchImageView2.viewHeight)) {
                int i62 = touchImageView2.viewHeight - ((int) touchImageView.getImageHeight());
                i5 = 0;
                i6 = i62;
            } else {
                int i63 = i8;
                i5 = i63;
                i6 = i63;
            }
            this.scroller.fling(i7, i8, i, i2, i4, i3, i6, i5);
            this.currX = i7;
            this.currY = i8;
        }

        public void cancelFling() {
            if (this.scroller != null) {
                this.this$0.setState(State.NONE);
                this.scroller.forceFinished(true);
            }
        }

        public void run() {
            if (this.this$0.touchImageViewListener != null) {
                this.this$0.touchImageViewListener.onMove();
            }
            if (this.scroller.isFinished()) {
                this.scroller = null;
            } else if (this.scroller.computeScrollOffset()) {
                int currX2 = this.scroller.getCurrX();
                int currY2 = this.scroller.getCurrY();
                this.currX = currX2;
                this.currY = currY2;
                this.this$0.matrix.postTranslate((float) (currX2 - this.currX), (float) (currY2 - this.currY));
                this.this$0.fixTrans();
                TouchImageView touchImageView = this.this$0;
                touchImageView.setImageMatrix(touchImageView.matrix);
                this.this$0.compatPostOnAnimation(this);
            }
        }
    }

    private class CompatScroller {
        boolean isPreGingerbread;
        OverScroller overScroller;
        Scroller scroller;

        public CompatScroller(Context context) {
            if (Build.VERSION.SDK_INT < 9) {
                this.isPreGingerbread = true;
                this.scroller = new Scroller(context);
                return;
            }
            this.isPreGingerbread = false;
            this.overScroller = new OverScroller(context);
        }

        public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            if (this.isPreGingerbread) {
                this.scroller.fling(i, i2, i3, i4, i5, i6, i7, i8);
            } else {
                this.overScroller.fling(i, i2, i3, i4, i5, i6, i7, i8);
            }
        }

        public void forceFinished(boolean z) {
            if (this.isPreGingerbread) {
                this.scroller.forceFinished(z);
            } else {
                this.overScroller.forceFinished(z);
            }
        }

        public boolean isFinished() {
            if (this.isPreGingerbread) {
                return this.scroller.isFinished();
            }
            return this.overScroller.isFinished();
        }

        public boolean computeScrollOffset() {
            if (this.isPreGingerbread) {
                return this.scroller.computeScrollOffset();
            }
            this.overScroller.computeScrollOffset();
            return this.overScroller.computeScrollOffset();
        }

        public int getCurrX() {
            if (this.isPreGingerbread) {
                return this.scroller.getCurrX();
            }
            return this.overScroller.getCurrX();
        }

        public int getCurrY() {
            if (this.isPreGingerbread) {
                return this.scroller.getCurrY();
            }
            return this.overScroller.getCurrY();
        }
    }

    public void compatPostOnAnimation(Runnable runnable) {
        if (Build.VERSION.SDK_INT >= 16) {
            postOnAnimation(runnable);
        } else {
            postDelayed(runnable, 16);
        }
    }

    private class ZoomVariables {
        public float focusX;
        public float focusY;
        public float scale;
        public ScaleType scaleType;

        public ZoomVariables(float f, float f2, float f3, ScaleType scaleType2) {
            this.scale = f;
            this.focusX = f2;
            this.focusY = f3;
            this.scaleType = scaleType2;
        }
    }
}
