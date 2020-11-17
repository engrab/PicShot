package com.pic.editor.filters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DegreeSeekBar extends View {
    private static final String TAG = "DegreeSeekBar";
    private int mBaseLine;
    private final Rect mCanvasClipBounds;
    private int mCenterTextColor;
    private Paint mCirclePaint;
    private int mCurrentDegrees;
    private float mDragFactor;
    private Paint.FontMetricsInt mFontMetrics;
    private Path mIndicatorPath;
    private float mLastTouchedPosition;
    private int mMaxReachableDegrees;
    private int mMinReachableDegrees;
    private int mPointColor;
    private int mPointCount;
    private float mPointMargin;
    private Paint mPointPaint;
    private boolean mScrollStarted;
    private ScrollingListener mScrollingListener;
    private int mTextColor;
    private Paint mTextPaint;
    private float[] mTextWidths;
    private int mTotalScrollDistance;

    public interface ScrollingListener {
        void onScroll(int i);

        void onScrollEnd();

        void onScrollStart();
    }

    public DegreeSeekBar(Context context) {
        this(context, (AttributeSet) null);
    }

    public DegreeSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DegreeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mCanvasClipBounds = new Rect();
        this.mIndicatorPath = new Path();
        this.mCurrentDegrees = 0;
        this.mPointCount = 51;
        this.mPointColor = -1;
        this.mTextColor = -1;
        this.mCenterTextColor = -1;
        this.mDragFactor = 2.1f;
        this.mMinReachableDegrees = -45;
        this.mMaxReachableDegrees = 45;
        init();
    }

    public DegreeSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mCanvasClipBounds = new Rect();
        this.mIndicatorPath = new Path();
        this.mCurrentDegrees = 0;
        this.mPointCount = 51;
        this.mPointColor = -1;
        this.mTextColor = -1;
        this.mCenterTextColor = -1;
        this.mDragFactor = 2.1f;
        this.mMinReachableDegrees = -45;
        this.mMaxReachableDegrees = 45;
        init();
    }

    private void init() {
        Paint paint = new Paint(1);
        this.mPointPaint = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.mPointPaint.setColor(this.mPointColor);
        this.mPointPaint.setStrokeWidth(2.0f);
        Paint paint2 = new Paint();
        this.mTextPaint = paint2;
        paint2.setColor(this.mTextColor);
        this.mTextPaint.setStyle(Paint.Style.STROKE);
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setTextSize(24.0f);
        this.mTextPaint.setTextAlign(Paint.Align.LEFT);
        this.mTextPaint.setAlpha(100);
        this.mFontMetrics = this.mTextPaint.getFontMetricsInt();
        float[] fArr = new float[1];
        this.mTextWidths = fArr;
        this.mTextPaint.getTextWidths("0", fArr);
        Paint paint3 = new Paint();
        this.mCirclePaint = paint3;
        paint3.setStyle(Paint.Style.FILL);
        this.mCirclePaint.setAlpha(255);
        this.mCirclePaint.setAntiAlias(true);
    }


    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mPointMargin = ((float) w) / ((float) this.mPointCount);
        this.mBaseLine = (((h - this.mFontMetrics.bottom) + this.mFontMetrics.top) / 2) - this.mFontMetrics.top;
        this.mIndicatorPath.moveTo((float) (w / 2), (float) (((h / 2) + (this.mFontMetrics.top / 2)) - 18));
        this.mIndicatorPath.rLineTo(-8.0f, -8.0f);
        this.mIndicatorPath.rLineTo(16.0f, 0.0f);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == 0) {
            this.mLastTouchedPosition = event.getX();
            if (!this.mScrollStarted) {
                this.mScrollStarted = true;
                ScrollingListener scrollingListener = this.mScrollingListener;
                if (scrollingListener != null) {
                    scrollingListener.onScrollStart();
                }
            }
        } else if (action == 1) {
            this.mScrollStarted = false;
            ScrollingListener scrollingListener2 = this.mScrollingListener;
            if (scrollingListener2 != null) {
                scrollingListener2.onScrollEnd();
            }
            invalidate();
        } else if (action == 2) {
            float distance = event.getX() - this.mLastTouchedPosition;
            int i = this.mCurrentDegrees;
            int i2 = this.mMaxReachableDegrees;
            if (i < i2 || distance >= 0.0f) {
                int i3 = this.mCurrentDegrees;
                int i4 = this.mMinReachableDegrees;
                if (i3 <= i4 && distance > 0.0f) {
                    this.mCurrentDegrees = i4;
                    invalidate();
                } else if (distance != 0.0f) {
                    onScrollEvent(event, distance);
                }
            } else {
                this.mCurrentDegrees = i2;
                invalidate();
            }
        }
        return true;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.getClipBounds(this.mCanvasClipBounds);
        int zeroIndex = (this.mPointCount / 2) + ((0 - this.mCurrentDegrees) / 2);
        this.mPointPaint.setColor(this.mPointColor);
        for (int i = 0; i < this.mPointCount; i++) {
            if (i <= zeroIndex - (Math.abs(this.mMinReachableDegrees) / 2) || i >= (Math.abs(this.mMaxReachableDegrees) / 2) + zeroIndex || !this.mScrollStarted) {
                this.mPointPaint.setAlpha(100);
            } else {
                this.mPointPaint.setAlpha(255);
            }
            int i2 = this.mPointCount;
            if (i > (i2 / 2) - 8 && i < (i2 / 2) + 8 && i > zeroIndex - (Math.abs(this.mMinReachableDegrees) / 2) && i < (Math.abs(this.mMaxReachableDegrees) / 2) + zeroIndex) {
                if (this.mScrollStarted) {
                    this.mPointPaint.setAlpha((Math.abs((this.mPointCount / 2) - i) * 255) / 8);
                } else {
                    this.mPointPaint.setAlpha((Math.abs((this.mPointCount / 2) - i) * 100) / 8);
                }
            }
            canvas.drawPoint(((float) this.mCanvasClipBounds.centerX()) + (((float) (i - (this.mPointCount / 2))) * this.mPointMargin), (float) this.mCanvasClipBounds.centerY(), this.mPointPaint);
            if (this.mCurrentDegrees != 0 && i == zeroIndex) {
                if (this.mScrollStarted) {
                    this.mTextPaint.setAlpha(255);
                } else {
                    this.mTextPaint.setAlpha(192);
                }
                this.mPointPaint.setStrokeWidth(4.0f);
                canvas.drawPoint(((float) this.mCanvasClipBounds.centerX()) + (((float) (i - (this.mPointCount / 2))) * this.mPointMargin), (float) this.mCanvasClipBounds.centerY(), this.mPointPaint);
                this.mPointPaint.setStrokeWidth(2.0f);
                this.mTextPaint.setAlpha(100);
            }
        }
        for (int i3 = -180; i3 <= 180; i3 += 15) {
            if (i3 < this.mMinReachableDegrees || i3 > this.mMaxReachableDegrees) {
                drawDegreeText(i3, canvas, false);
            } else {
                drawDegreeText(i3, canvas, true);
            }
        }
        this.mTextPaint.setTextSize(28.0f);
        this.mTextPaint.setAlpha(255);
        this.mTextPaint.setColor(this.mCenterTextColor);
        int i4 = this.mCurrentDegrees;
        if (i4 >= 10) {
            canvas.drawText(this.mCurrentDegrees + "°", ((float) (getWidth() / 2)) - this.mTextWidths[0], (float) this.mBaseLine, this.mTextPaint);
        } else if (i4 <= -10) {
            canvas.drawText(this.mCurrentDegrees + "°", ((float) (getWidth() / 2)) - ((this.mTextWidths[0] / 2.0f) * 3.0f), (float) this.mBaseLine, this.mTextPaint);
        } else if (i4 < 0) {
            canvas.drawText(this.mCurrentDegrees + "°", ((float) (getWidth() / 2)) - this.mTextWidths[0], (float) this.mBaseLine, this.mTextPaint);
        } else {
            canvas.drawText(this.mCurrentDegrees + "°", ((float) (getWidth() / 2)) - (this.mTextWidths[0] / 2.0f), (float) this.mBaseLine, this.mTextPaint);
        }
        this.mTextPaint.setAlpha(100);
        this.mTextPaint.setTextSize(24.0f);
        this.mTextPaint.setColor(this.mTextColor);
        this.mCirclePaint.setColor(this.mCenterTextColor);
        canvas.drawPath(this.mIndicatorPath, this.mCirclePaint);
        this.mCirclePaint.setColor(this.mCenterTextColor);
    }

    private void drawDegreeText(int degrees, Canvas canvas, boolean canReach) {
        if (!canReach) {
            this.mTextPaint.setAlpha(100);
        } else if (this.mScrollStarted) {
            this.mTextPaint.setAlpha(Math.min(255, (Math.abs(degrees - this.mCurrentDegrees) * 255) / 15));
            if (Math.abs(degrees - this.mCurrentDegrees) <= 7) {
                this.mTextPaint.setAlpha(0);
            }
        } else {
            this.mTextPaint.setAlpha(100);
            if (Math.abs(degrees - this.mCurrentDegrees) <= 7) {
                this.mTextPaint.setAlpha(0);
            }
        }
        if (degrees == 0) {
            if (Math.abs(this.mCurrentDegrees) >= 15 && !this.mScrollStarted) {
                this.mTextPaint.setAlpha(180);
            }
            canvas.drawText("0°", (((float) (getWidth() / 2)) - (this.mTextWidths[0] / 2.0f)) - (((float) (this.mCurrentDegrees / 2)) * this.mPointMargin), (float) ((getHeight() / 2) - 10), this.mTextPaint);
            return;
        }
        float f = this.mPointMargin;
        canvas.drawText(degrees + "°", ((((float) (getWidth() / 2)) + ((((float) degrees) * f) / 2.0f)) - ((this.mTextWidths[0] / 2.0f) * 3.0f)) - (((float) (this.mCurrentDegrees / 2)) * f), (float) ((getHeight() / 2) - 10), this.mTextPaint);
    }

    private void onScrollEvent(MotionEvent event, float distance) {
        this.mTotalScrollDistance = (int) (((float) this.mTotalScrollDistance) - distance);
        postInvalidate();
        this.mLastTouchedPosition = event.getX();
        int i = (int) ((((float) this.mTotalScrollDistance) * this.mDragFactor) / this.mPointMargin);
        this.mCurrentDegrees = i;
        ScrollingListener scrollingListener = this.mScrollingListener;
        if (scrollingListener != null) {
            scrollingListener.onScroll(i);
        }
    }

    public void setDegreeRange(int min, int max) {
        if (min > max) {
            Log.e(TAG, "setDegreeRange: error, max must greater than min");
            return;
        }
        this.mMinReachableDegrees = min;
        this.mMaxReachableDegrees = max;
        int i = this.mCurrentDegrees;
        if (i >= max || i <= min) {
            this.mCurrentDegrees = (this.mMinReachableDegrees + this.mMaxReachableDegrees) / 2;
        }
        this.mTotalScrollDistance = 0;
        invalidate();
    }

    public void setCurrentDegrees(int degrees) {
        if (degrees <= this.mMaxReachableDegrees && degrees >= this.mMinReachableDegrees) {
            this.mCurrentDegrees = degrees;
            this.mTotalScrollDistance = 0;
            invalidate();
        }
    }

    public void setScrollingListener(ScrollingListener scrollingListener) {
        this.mScrollingListener = scrollingListener;
    }

    public int getPointColor() {
        return this.mPointColor;
    }

    public void setPointColor(int pointColor) {
        this.mPointColor = pointColor;
        this.mPointPaint.setColor(pointColor);
        postInvalidate();
    }

    public int getTextColor() {
        return this.mTextColor;
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        this.mTextPaint.setColor(textColor);
        postInvalidate();
    }

    public int getCenterTextColor() {
        return this.mCenterTextColor;
    }

    public void setCenterTextColor(int centerTextColor) {
        this.mCenterTextColor = centerTextColor;
        postInvalidate();
    }

    public float getDragFactor() {
        return this.mDragFactor;
    }

    public void setDragFactor(float dragFactor) {
        this.mDragFactor = dragFactor;
    }
}
