package com.pic.editor.splash;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.core.view.ViewCompat;

import com.pic.editor.R;
import com.pic.editor.photoeditor.BrushDrawingView;
import com.pic.editor.sticker.Sticker;
import com.pic.editor.utils.SystemUtil;

import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

public class SplashView extends androidx.appcompat.widget.AppCompatImageView {
    public static final int DRAW = 1;
    public static final int SHAPE = 0;
    private Bitmap bitmap;
    private int brushBitmapSize = 100;
    private final PointF currentCenterPoint = new PointF();
    private int currentMode = 0;
    public int currentSplashMode = 0;
    private float currentX;
    private float currentY;
    private final Matrix downMatrix = new Matrix();
    private Stack<BrushDrawingView.LinePath> lstPoints = new Stack<>();
    private Paint mDrawPaint;
    private Path mPath;
    private Stack<BrushDrawingView.LinePath> mPoints = new Stack<>();
    private Stack<BrushDrawingView.LinePath> mRedoPaths = new Stack<>();
    private float mTouchX;
    private float mTouchY;
    private PointF midPoint = new PointF();
    private final Matrix moveMatrix = new Matrix();
    private float oldDistance = 0.0f;
    private float oldRotation = 0.0f;
    private Paint paintCircle;
    private final float[] point = new float[2];
    private boolean showTouchIcon = false;
    private Sticker sticker;
    private final float[] tmp = new float[2];

    public void setCurrentSplashMode(int i) {
        this.currentSplashMode = i;
    }

    public SplashView(Context context) {
        super(context);
        init();
    }

    public SplashView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public SplashView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public void setImageBitmap(Bitmap bitmap2) {
        super.setImageBitmap(bitmap2);
        setBitmap(bitmap2);
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    private void init() {
        Paint paint = new Paint();
        this.mDrawPaint = paint;
        paint.setAntiAlias(true);
        this.mDrawPaint.setDither(true);
        this.mDrawPaint.setStyle(Paint.Style.FILL);
        this.mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mDrawPaint.setStrokeWidth((float) this.brushBitmapSize);
        this.mDrawPaint.setMaskFilter(new BlurMaskFilter(20.0f, BlurMaskFilter.Blur.NORMAL));
        this.mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        this.mDrawPaint.setStyle(Paint.Style.STROKE);
        Paint paint2 = new Paint();
        this.paintCircle = paint2;
        paint2.setAntiAlias(true);
        this.paintCircle.setDither(true);
        this.paintCircle.setColor(getContext().getResources().getColor(R.color.colorAccent));
        this.paintCircle.setStrokeWidth((float) SystemUtil.dpToPx(getContext(), 2));
        this.paintCircle.setStyle(Paint.Style.STROKE);
        this.mPath = new Path();
    }

    public void updateBrush() {
        this.mPath = new Path();
        this.mDrawPaint.setAntiAlias(true);
        this.mDrawPaint.setDither(true);
        this.mDrawPaint.setStyle(Paint.Style.FILL);
        this.mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mDrawPaint.setStrokeWidth((float) this.brushBitmapSize);
        this.mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        this.mDrawPaint.setStyle(Paint.Style.STROKE);
        this.showTouchIcon = false;
        invalidate();
    }

    public void addSticker(Sticker sticker2) {
        addSticker(sticker2, 1);
    }

    public void addSticker(final Sticker sticker2, final int i) {
        if (ViewCompat.isLaidOut(this)) {
            addStickerImmediately(sticker2, i);
        } else {
            post(new Runnable() {
                public void run() {
                    SplashView.this.addStickerImmediately(sticker2, i);
                }
            });
        }
    }

    public void addStickerImmediately(Sticker sticker2, int i) {
        this.sticker = sticker2;
        setStickerPosition(sticker2, i);
        invalidate();
    }

    public void setStickerPosition(Sticker sticker2, int i) {
        float f;
        float width = (float) getWidth();
        float height = (float) getHeight();
        if (width > height) {
            f = ((height * 4.0f) / 5.0f) / ((float) sticker2.getHeight());
        } else {
            f = ((width * 4.0f) / 5.0f) / ((float) sticker2.getWidth());
        }
        this.midPoint.set(0.0f, 0.0f);
        this.downMatrix.reset();
        this.moveMatrix.set(this.downMatrix);
        this.moveMatrix.postScale(f, f);
        this.moveMatrix.postRotate((float) (new Random().nextInt(20) - 10), this.midPoint.x, this.midPoint.y);
        float width2 = width - ((float) ((int) (((float) sticker2.getWidth()) * f)));
        float height2 = height - ((float) ((int) (((float) sticker2.getHeight()) * f)));
        this.moveMatrix.postTranslate((i & 4) > 0 ? width2 / 4.0f : (i & 8) > 0 ? width2 * 0.75f : width2 / 2.0f, (i & 2) > 0 ? height2 / 4.0f : (i & 16) > 0 ? height2 * 0.75f : height2 / 2.0f);
        sticker2.setMatrix(this.moveMatrix);
    }

    public void onDraw(Canvas canvas) {
        Bitmap bitmap2 = this.bitmap;
        if (bitmap2 != null && !bitmap2.isRecycled()) {
            super.onDraw(canvas);
            if (this.currentSplashMode == 0) {
                drawStickers(canvas);
                return;
            }
            Iterator it = this.mPoints.iterator();
            while (it.hasNext()) {
                BrushDrawingView.LinePath linePath = (BrushDrawingView.LinePath) it.next();
                canvas.drawPath(linePath.getDrawPath(), linePath.getDrawPaint());
            }
            canvas.drawPath(this.mPath, this.mDrawPaint);
            if (this.showTouchIcon) {
                canvas.drawCircle(this.currentX, this.currentY, (float) (this.brushBitmapSize / 2), this.paintCircle);
            }
        }
    }

    public void drawStickers(Canvas canvas) {
        Sticker sticker2 = this.sticker;
        if (sticker2 != null && sticker2.isShow()) {
            this.sticker.draw(canvas);
        }
        invalidate();
    }

    public float calculateDistance(float f, float f2, float f3, float f4) {
        double d = (double) (f - f3);
        double d2 = (double) (f2 - f4);
        return (float) Math.sqrt((d * d) + (d2 * d2));
    }

    public float calculateDistance(MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            return 0.0f;
        }
        return calculateDistance(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1));
    }

    public float calculateRotation(MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            return 0.0f;
        }
        return calculateRotation(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1));
    }

    public float calculateRotation(float f, float f2, float f3, float f4) {
        return (float) Math.toDegrees(Math.atan2((double) (f2 - f4), (double) (f - f3)));
    }

    public PointF calculateMidPoint(MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            this.midPoint.set(0.0f, 0.0f);
            return this.midPoint;
        }
        this.midPoint.set((motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f, (motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f);
        return this.midPoint;
    }

    public PointF calculateMidPoint() {
        Sticker sticker2 = this.sticker;
        if (sticker2 != null) {
            sticker2.getMappedCenterPoint(this.midPoint, this.point, this.tmp);
        }
        return this.midPoint;
    }

    public boolean isInStickerArea(Sticker sticker2, float f, float f2) {
        if (sticker2 == null) {
            return false;
        }
        float[] fArr = this.tmp;
        fArr[0] = f;
        fArr[1] = f2;
        return sticker2.contains(fArr);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001d, code lost:
        if (r0 != 6) goto L_0x005f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(MotionEvent r10) {
        /*
            r9 = this;
            int r0 = androidx.core.view.MotionEventCompat.getActionMasked(r10)
            float r1 = r10.getX()
            float r2 = r10.getY()
            r9.currentX = r1
            r9.currentY = r2
            r3 = 0
            r4 = 1
            if (r0 == 0) goto L_0x0055
            if (r0 == r4) goto L_0x0051
            r5 = 2
            if (r0 == r5) goto L_0x004a
            r6 = 5
            if (r0 == r6) goto L_0x0020
            r5 = 6
            if (r0 == r5) goto L_0x0047
            goto L_0x005f
        L_0x0020:
            float r6 = r9.calculateDistance(r10)
            r9.oldDistance = r6
            float r6 = r9.calculateRotation(r10)
            r9.oldRotation = r6
            android.graphics.PointF r6 = r9.calculateMidPoint(r10)
            r9.midPoint = r6
            com.pic.shot.sticker.Sticker r6 = r9.sticker
            if (r6 == 0) goto L_0x0047
            float r7 = r10.getX(r4)
            float r8 = r10.getY(r4)
            boolean r6 = r9.isInStickerArea(r6, r7, r8)
            if (r6 == 0) goto L_0x0047
            r9.currentMode = r5
            goto L_0x005f
        L_0x0047:
            r9.currentMode = r3
            goto L_0x005f
        L_0x004a:
            r9.handleCurrentMode(r1, r2, r10)
            r9.invalidate()
            goto L_0x005f
        L_0x0051:
            r9.onTouchUp(r10)
            goto L_0x005f
        L_0x0055:
            boolean r5 = r9.onTouchDown(r1, r2)
            if (r5 != 0) goto L_0x005f
            r9.invalidate()
            return r3
        L_0x005f:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.pic.shot.splash.SplashView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public void constrainSticker(Sticker sticker2) {
        int width = getWidth();
        int height = getHeight();
        sticker2.getMappedCenterPoint(this.currentCenterPoint, this.point, this.tmp);
        float f = 0.0f;
        float f2 = this.currentCenterPoint.x < 0.0f ? -this.currentCenterPoint.x : 0.0f;
        float f3 = (float) width;
        if (this.currentCenterPoint.x > f3) {
            f2 = f3 - this.currentCenterPoint.x;
        }
        if (this.currentCenterPoint.y < 0.0f) {
            f = -this.currentCenterPoint.y;
        }
        float f4 = (float) height;
        if (this.currentCenterPoint.y > f4) {
            f = f4 - this.currentCenterPoint.y;
        }
        sticker2.getMatrix().postTranslate(f2, f);
    }

    public synchronized void handleCurrentMode(float f, float f2, MotionEvent motionEvent) {
        if (this.currentSplashMode == 0) {
            int i = this.currentMode;
            if (i != 4) {
                if (i != 1) {
                    if (i == 2) {
                        if (this.sticker != null) {
                            float calculateDistance = calculateDistance(motionEvent);
                            float calculateRotation = calculateRotation(motionEvent);
                            this.moveMatrix.set(this.downMatrix);
                            this.moveMatrix.postScale(calculateDistance / this.oldDistance, calculateDistance / this.oldDistance, this.midPoint.x, this.midPoint.y);
                            this.moveMatrix.postRotate(calculateRotation - this.oldRotation, this.midPoint.x, this.midPoint.y);
                            this.sticker.setMatrix(this.moveMatrix);
                        }
                    }
                } else if (this.sticker != null) {
                    this.moveMatrix.set(this.downMatrix);
                    this.moveMatrix.postTranslate(motionEvent.getX() - this.mTouchX, motionEvent.getY() - this.mTouchY);
                    this.sticker.setMatrix(this.moveMatrix);
                }
            }
        } else {
            this.mPath.quadTo(this.mTouchX, this.mTouchY, (this.mTouchX + f) / 2.0f, (this.mTouchY + f2) / 2.0f);
            this.mTouchX = f;
            this.mTouchY = f2;
        }
    }

    public void setBrushBitmapSize(int i) {
        this.brushBitmapSize = i;
        this.mDrawPaint.setStrokeWidth((float) i);
        this.showTouchIcon = true;
        this.currentX = (float) (getWidth() / 2);
        this.currentY = (float) (getHeight() / 2);
        invalidate();
    }

    public Sticker findHandlingSticker() {
        if (isInStickerArea(this.sticker, this.mTouchX, this.mTouchY)) {
            return this.sticker;
        }
        return null;
    }

    public boolean onTouchDown(float f, float f2) {
        this.currentMode = 1;
        this.mTouchX = f;
        this.mTouchY = f2;
        this.currentX = f;
        this.currentY = f2;
        if (this.currentSplashMode == 0) {
            PointF calculateMidPoint = calculateMidPoint();
            this.midPoint = calculateMidPoint;
            this.oldDistance = calculateDistance(calculateMidPoint.x, this.midPoint.y, this.mTouchX, this.mTouchY);
            this.oldRotation = calculateRotation(this.midPoint.x, this.midPoint.y, this.mTouchX, this.mTouchY);
            Sticker findHandlingSticker = findHandlingSticker();
            if (findHandlingSticker != null) {
                this.downMatrix.set(this.sticker.getMatrix());
            }
            if (findHandlingSticker == null) {
                return false;
            }
        } else {
            this.showTouchIcon = true;
            this.mRedoPaths.clear();
            this.mPath.reset();
            this.mPath.moveTo(f, f2);
        }
        invalidate();
        return true;
    }

    public void onTouchUp(MotionEvent motionEvent) {
        this.showTouchIcon = false;
        if (this.currentSplashMode == 0) {
            this.currentMode = 0;
        } else {
            BrushDrawingView.LinePath linePath = new BrushDrawingView.LinePath(this.mPath, this.mDrawPaint);
            this.mPoints.push(linePath);
            this.lstPoints.push(linePath);
            this.mPath = new Path();
        }
        invalidate();
    }

    public boolean undo() {
        if (!this.lstPoints.empty()) {
            BrushDrawingView.LinePath pop = this.lstPoints.pop();
            this.mRedoPaths.push(pop);
            this.mPoints.remove(pop);
            invalidate();
        }
        return !this.lstPoints.empty();
    }

    public boolean redo() {
        if (!this.mRedoPaths.empty()) {
            BrushDrawingView.LinePath pop = this.mRedoPaths.pop();
            this.mPoints.push(pop);
            this.lstPoints.push(pop);
            invalidate();
        }
        return !this.mRedoPaths.empty();
    }

    public Sticker getSticker() {
        return this.sticker;
    }

    public Bitmap getBitmap(Bitmap bitmap2) {
        int width = getWidth();
        int height = getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Rect rect = null;
        Paint paint = null;
        canvas.drawBitmap(this.bitmap, rect, new RectF(0.0f, 0.0f, (float) width, (float) height), paint);
        if (this.currentSplashMode == 0) {
            drawStickers(canvas);
        } else {
            Iterator it = this.mPoints.iterator();
            while (it.hasNext()) {
                BrushDrawingView.LinePath linePath = (BrushDrawingView.LinePath) it.next();
                canvas.drawPath(linePath.getDrawPath(), linePath.getDrawPaint());
            }
        }
        Bitmap createBitmap2 = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(createBitmap2);
        canvas2.drawBitmap(bitmap2, rect, new RectF(0.0f, 0.0f, (float) bitmap2.getWidth(), (float) bitmap2.getHeight()), paint);
        canvas2.drawBitmap(createBitmap, rect, new RectF(0.0f, 0.0f, (float) bitmap2.getWidth(), (float) bitmap2.getHeight()), paint);
        return createBitmap2;
    }
}
