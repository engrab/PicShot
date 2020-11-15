package com.pic.shot.sticker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import com.pic.shot.R;
import com.pic.shot.sticker.event.DeleteIconEvent;
import com.pic.shot.sticker.event.FlipHorizontallyEvent;
import com.pic.shot.sticker.event.ZoomIconEvent;
import com.pic.shot.utils.SystemUtil;
import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StickerView extends RelativeLayout {
    private static final int DEFAULT_MIN_CLICK_DELAY_TIME = 200;
    public static final int FLIP_HORIZONTALLY = 1;
    public static final int FLIP_VERTICALLY = 2;
    private static final String TAG = "StickerView";
    private final float[] bitmapPoints;
    private final Paint borderPaint;
    private final Paint borderPaintRed;
    private final float[] bounds;
    private boolean bringToFrontCurrentSticker;
    private int circleRadius;
    private boolean constrained;
    private final PointF currentCenterPoint;
    private BitmapStickerIcon currentIcon;
    private int currentMode;
    private float currentMoveingX;
    private float currentMoveingY;
    private final Matrix downMatrix;
    private float downX;
    private float downY;
    private boolean drawCirclePoint;
    private Sticker handlingSticker;
    private final List<BitmapStickerIcon> icons;
    private long lastClickTime;
    private Sticker lastHandlingSticker;
    private final Paint linePaint;
    private boolean locked;
    private PointF midPoint;
    private int minClickDelayTime;
    private final Matrix moveMatrix;
    private float oldDistance;
    private float oldRotation;
    private boolean onMoving;
    private OnStickerOperationListener onStickerOperationListener;
    private Paint paintCircle;
    private final float[] point;
    private boolean showBorder;
    private boolean showIcons;
    private final Matrix sizeMatrix;
    private final RectF stickerRect;
    private final List<Sticker> stickers;
    private final float[] tmp;
    private int touchSlop;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ActionMode {
        public static final int CLICK = 4;
        public static final int DRAG = 1;
        public static final int ICON = 3;
        public static final int NONE = 0;
        public static final int ZOOM_WITH_TWO_FINGER = 2;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Flip {
    }

    public interface OnStickerOperationListener {
        void onStickerAdded(Sticker sticker);

        void onStickerClicked(Sticker sticker);

        void onStickerDeleted(Sticker sticker);

        void onStickerDoubleTapped(Sticker sticker);

        void onStickerDragFinished(Sticker sticker);

        void onStickerFlipped(Sticker sticker);

        void onStickerTouchOutside();

        void onStickerTouchedDown(Sticker sticker);

        void onStickerZoomFinished(Sticker sticker);

        void onTouchDownForBeauty(float f, float f2);

        void onTouchDragForBeauty(float f, float f2);

        void onTouchUpForBeauty(float f, float f2);
    }

    public StickerView(Context context) {
        this(context, (AttributeSet) null);
    }

    public StickerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public StickerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray typedArray;
        this.stickers = new ArrayList();
        this.icons = new ArrayList(4);
        this.borderPaint = new Paint();
        this.borderPaintRed = new Paint();
        this.linePaint = new Paint();
        this.stickerRect = new RectF();
        this.sizeMatrix = new Matrix();
        this.downMatrix = new Matrix();
        this.moveMatrix = new Matrix();
        this.bitmapPoints = new float[8];
        this.bounds = new float[8];
        this.point = new float[2];
        this.currentCenterPoint = new PointF();
        this.tmp = new float[2];
        this.midPoint = new PointF();
        this.drawCirclePoint = false;
        this.onMoving = false;
        this.oldDistance = 0.0f;
        this.oldRotation = 0.0f;
        this.currentMode = 0;
        this.lastClickTime = 0;
        this.minClickDelayTime = 200;
        Paint paint = new Paint();
        this.paintCircle = paint;
        paint.setAntiAlias(true);
        this.paintCircle.setDither(true);
        this.paintCircle.setColor(getContext().getResources().getColor(R.color.colorAccent));
        this.paintCircle.setStrokeWidth((float) SystemUtil.dpToPx(getContext(), 2));
        this.paintCircle.setStyle(Paint.Style.STROKE);
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        try {
            typedArray = context.obtainStyledAttributes(attributeSet, new int[]{R.attr.borderAlpha, R.attr.borderColor, R.attr.bringToFrontCurrentSticker, R.attr.showBorder, R.attr.showIcons});
            this.showIcons = typedArray.getBoolean(ALIGN_BASELINE, false);
            this.showBorder = typedArray.getBoolean(TEXT_ALIGNMENT_TEXT_END, false);
            this.bringToFrontCurrentSticker = typedArray.getBoolean(TEXT_ALIGNMENT_TEXT_START, false);
            this.borderPaint.setAntiAlias(true);
            this.borderPaint.setColor(typedArray.getColor(FLIP_HORIZONTALLY, Color.parseColor("#FFFFFF")));
            this.borderPaint.setAlpha(typedArray.getInteger(0, 255));
            this.borderPaintRed.setAntiAlias(true);
            this.borderPaintRed.setColor(typedArray.getColor(FLIP_HORIZONTALLY, Color.parseColor("#E13e3e")));
            this.borderPaintRed.setAlpha(typedArray.getInteger(0, 255));
            configDefaultIcons();
            if (typedArray != null) {
                typedArray.recycle();
            }
        } catch (Throwable th) {
        }
    }

    public StickerView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.stickers = new ArrayList();
        this.icons = new ArrayList(4);
        this.borderPaint = new Paint();
        this.borderPaintRed = new Paint();
        this.linePaint = new Paint();
        this.stickerRect = new RectF();
        this.sizeMatrix = new Matrix();
        this.downMatrix = new Matrix();
        this.moveMatrix = new Matrix();
        this.bitmapPoints = new float[8];
        this.bounds = new float[8];
        this.point = new float[2];
        this.currentCenterPoint = new PointF();
        this.tmp = new float[2];
        this.midPoint = new PointF();
        this.drawCirclePoint = false;
        this.onMoving = false;
        this.oldDistance = 0.0f;
        this.oldRotation = 0.0f;
        this.currentMode = 0;
        this.lastClickTime = 0;
        this.minClickDelayTime = 200;
    }

    public Matrix getSizeMatrix() {
        return this.sizeMatrix;
    }

    public Matrix getDownMatrix() {
        return this.downMatrix;
    }

    public Matrix getMoveMatrix() {
        return this.moveMatrix;
    }

    public List<Sticker> getStickers() {
        return this.stickers;
    }

    public void configDefaultIcons() {
        BitmapStickerIcon bitmapStickerIcon = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.sticker_ic_close_white_18dp), 0, BitmapStickerIcon.REMOVE);
        bitmapStickerIcon.setIconEvent(new DeleteIconEvent());
        BitmapStickerIcon bitmapStickerIcon2 = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_sticker_ic_scale_black_18dp), 3, BitmapStickerIcon.ZOOM);
        bitmapStickerIcon2.setIconEvent(new ZoomIconEvent());
        BitmapStickerIcon bitmapStickerIcon3 = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_sticker_ic_flip_black_18dp), 1, BitmapStickerIcon.FLIP);
        bitmapStickerIcon3.setIconEvent(new FlipHorizontallyEvent());
        BitmapStickerIcon bitmapStickerIcon4 = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_edit_black_18dp), 2, BitmapStickerIcon.EDIT);
        bitmapStickerIcon4.setIconEvent(new FlipHorizontallyEvent());
        this.icons.clear();
        this.icons.add(bitmapStickerIcon);
        this.icons.add(bitmapStickerIcon2);
        this.icons.add(bitmapStickerIcon3);
        this.icons.add(bitmapStickerIcon4);
    }

    public void swapLayers(int i, int i2) {
        if (this.stickers.size() >= i && this.stickers.size() >= i2) {
            Collections.swap(this.stickers, i, i2);
            invalidate();
        }
    }

    public void setHandlingSticker(Sticker sticker) {
        this.lastHandlingSticker = this.handlingSticker;
        this.handlingSticker = sticker;
        invalidate();
    }

    public void showLastHandlingSticker() {
        Sticker sticker = this.lastHandlingSticker;
        if (sticker != null && !sticker.isShow()) {
            this.lastHandlingSticker.setShow(true);
            invalidate();
        }
    }

    public void sendToLayer(int i, int i2) {
        if (this.stickers.size() >= i && this.stickers.size() >= i2) {
            this.stickers.remove(i);
            List<Sticker> list = this.stickers;
            list.add(i2, list.get(i));
            invalidate();
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            this.stickerRect.left = (float) i;
            this.stickerRect.top = (float) i2;
            this.stickerRect.right = (float) i3;
            this.stickerRect.bottom = (float) i4;
        }
    }

    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.drawCirclePoint && this.onMoving) {
            canvas.drawCircle(this.downX, this.downY, (float) this.circleRadius, this.paintCircle);
            canvas.drawLine(this.downX, this.downY, this.currentMoveingX, this.currentMoveingY, this.paintCircle);
        }
        drawStickers(canvas);
    }

    public void setCircleRadius(int i) {
        this.circleRadius = i;
    }

    public void drawStickers(Canvas canvas) {
        boolean i;
        float f8;
        float f7;
        float f;
        float f2;
        float f13;
        float f4;
        float f22;
        float f82;
        Canvas canvas2 = canvas;
        for (int i2 = 0; i2 < this.stickers.size(); i2++) {
            Sticker sticker = this.stickers.get(i2);
            if (sticker != null && sticker.isShow()) {
                sticker.draw(canvas2);
            }
        }
        if (this.handlingSticker == null || this.locked) {
            i = false;
        } else if (this.showBorder || this.showIcons) {
            getStickerPoints(this.handlingSticker, this.bitmapPoints);
            float[] fArr = this.bitmapPoints;
            float f5 = fArr[0];
            float f6 = fArr[1];
            float f72 = fArr[2];
            float f83 = fArr[3];
            float f9 = fArr[4];
            float f10 = fArr[5];
            float f11 = fArr[6];
            float f12 = fArr[7];
            if (this.showBorder) {
                Canvas canvas3 = canvas;
                float f132 = f5;
                float f42 = f12;
                float f14 = f6;
                float f3 = f11;
                float f23 = f10;
                float f15 = f9;
                f8 = f83;
                canvas3.drawLine(f132, f14, f72, f83, this.borderPaint);
                canvas3.drawLine(f132, f14, f15, f23, this.borderPaint);
                f7 = f72;
                canvas3.drawLine(f7, f8, f3, f42, this.borderPaint);
                canvas3.drawLine(f3, f42, f15, f23, this.borderPaint);
                f13 = f3;
                f4 = f42;
                f = f15;
                f2 = f23;
            } else {
                f8 = f83;
                f7 = f72;
                f4 = f12;
                f13 = f11;
                f2 = f10;
                f = f9;
            }
            if (this.showIcons) {
                float f152 = f4;
                float f16 = f13;
                float f17 = f2;
                float f18 = f;
                float calculateRotation = calculateRotation(f16, f152, f18, f17);
                float f19 = f12;
                int i3 = 1;
                int i4 = 0;
                while (true) {
                    float f43 = f4;
                    if (i4 >= this.icons.size()) {
                        break;
                    }
                    BitmapStickerIcon bitmapStickerIcon = this.icons.get(i4);
                    float f32 = f13;
                    int position = bitmapStickerIcon.getPosition();
                    if (position != 0) {
                        f22 = f2;
                        if (position == 1) {
                            if ((!(this.handlingSticker instanceof TextSticker) || !bitmapStickerIcon.getTag().equals(BitmapStickerIcon.EDIT)) && (!(this.handlingSticker instanceof DrawableSticker) || !bitmapStickerIcon.getTag().equals(BitmapStickerIcon.FLIP))) {
                                f82 = f8;
                            } else {
                                f82 = f8;
                                configIconMatrix(bitmapStickerIcon, f7, f82, calculateRotation);
                                bitmapStickerIcon.draw(canvas2, this.borderPaint);
                            }
                        } else if (position == 2) {
                            f82 = f8;
                        } else if (position != 3) {
                            int i5 = i3;
                            f82 = f8;
                        } else if ((!(this.handlingSticker instanceof TextSticker) || !bitmapStickerIcon.getTag().equals(BitmapStickerIcon.ROTATE)) && (!(this.handlingSticker instanceof DrawableSticker) || !bitmapStickerIcon.getTag().equals(BitmapStickerIcon.ZOOM))) {
                            Sticker sticker2 = this.handlingSticker;
                            if (sticker2 instanceof BeautySticker) {
                                BeautySticker beautySticker = (BeautySticker) sticker2;
                                if (beautySticker.getType() != i3) {
                                    int i6 = i3;
                                    if (beautySticker.getType() != 2 && beautySticker.getType() != 8) {
                                        if (beautySticker.getType() != 4) {
                                            f82 = f8;
                                        }
                                    }
                                    configIconMatrix(bitmapStickerIcon, f16, f152, calculateRotation);
                                    bitmapStickerIcon.draw(canvas2, this.borderPaint);
                                    f82 = f8;
                                } else {
                                    configIconMatrix(bitmapStickerIcon, f16, f152, calculateRotation);
                                    bitmapStickerIcon.draw(canvas2, this.borderPaint);
                                    f82 = f8;
                                }
                            } else {
                                f82 = f8;
                            }
                        } else {
                            configIconMatrix(bitmapStickerIcon, f16, f152, calculateRotation);
                            bitmapStickerIcon.draw(canvas2, this.borderPaint);
                            int i7 = i3;
                            f82 = f8;
                        }
                        Sticker sticker3 = this.handlingSticker;
                        if (!(sticker3 instanceof BeautySticker)) {
                            configIconMatrix(bitmapStickerIcon, f18, f17, calculateRotation);
                            bitmapStickerIcon.draw(canvas2, this.borderPaint);
                        } else if (((BeautySticker) sticker3).getType() == 0) {
                            configIconMatrix(bitmapStickerIcon, f18, f17, calculateRotation);
                            bitmapStickerIcon.draw(canvas2, this.borderPaint);
                        }
                    } else {
                        f22 = f2;
                        f82 = f8;
                        configIconMatrix(bitmapStickerIcon, f5, f6, calculateRotation);
                        bitmapStickerIcon.draw(canvas2, this.borderPaintRed);
                    }
                    i4++;
                    f8 = f82;
                    i3 = 1;
                    f4 = f43;
                    f13 = f32;
                    f2 = f22;
                }
                int i8 = i3;
                float f20 = f13;
                float f21 = f2;
                float f24 = f8;
                float calculateRotation2 = i4;
                invalidate();
            }
            i = false;
            float f25 = f12;
            float f26 = f4;
            float f27 = f13;
            float f28 = f2;
            float f122 = f8;
        } else {
            i = false;
        }
        invalidate();
    }

    public void configIconMatrix(BitmapStickerIcon bitmapStickerIcon, float f, float f2, float f3) {
        bitmapStickerIcon.setX(f);
        bitmapStickerIcon.setY(f2);
        bitmapStickerIcon.getMatrix().reset();
        bitmapStickerIcon.getMatrix().postRotate(f3, (float) (bitmapStickerIcon.getWidth() / 2), (float) (bitmapStickerIcon.getHeight() / 2));
        bitmapStickerIcon.getMatrix().postTranslate(f - ((float) (bitmapStickerIcon.getWidth() / 2)), f2 - ((float) (bitmapStickerIcon.getHeight() / 2)));
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.locked) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        if (motionEvent.getAction() != 0) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        this.downX = motionEvent.getX();
        this.downY = motionEvent.getY();
        return (findCurrentIconTouched() == null && findHandlingSticker() == null) ? false : true;
    }

    public void setDrawCirclePoint(boolean z) {
        this.drawCirclePoint = z;
        this.onMoving = false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001a, code lost:
        if (r0 != 6) goto L_0x007d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(MotionEvent r7) {
        /*
            r6 = this;
            boolean r0 = r6.locked
            if (r0 == 0) goto L_0x0009
            boolean r0 = super.onTouchEvent(r7)
            return r0
        L_0x0009:
            int r0 = androidx.core.view.MotionEventCompat.getActionMasked(r7)
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x0067
            if (r0 == r2) goto L_0x0063
            r3 = 2
            if (r0 == r3) goto L_0x005c
            r4 = 5
            if (r0 == r4) goto L_0x001d
            r4 = 6
            if (r0 == r4) goto L_0x004a
            goto L_0x007d
        L_0x001d:
            float r0 = r6.calculateDistance(r7)
            r6.oldDistance = r0
            float r0 = r6.calculateRotation(r7)
            r6.oldRotation = r0
            android.graphics.PointF r0 = r6.calculateMidPoint(r7)
            r6.midPoint = r0
            com.pic.shot.sticker.Sticker r0 = r6.handlingSticker
            if (r0 == 0) goto L_0x004a
            float r4 = r7.getX(r2)
            float r5 = r7.getY(r2)
            boolean r0 = r6.isInStickerArea(r0, r4, r5)
            if (r0 == 0) goto L_0x004a
            com.pic.shot.sticker.BitmapStickerIcon r0 = r6.findCurrentIconTouched()
            if (r0 != 0) goto L_0x004a
            r6.currentMode = r3
            goto L_0x007d
        L_0x004a:
            int r0 = r6.currentMode
            if (r0 != r3) goto L_0x0059
            com.pic.shot.sticker.Sticker r0 = r6.handlingSticker
            if (r0 == 0) goto L_0x0059
            com.pic.shot.sticker.StickerView$OnStickerOperationListener r3 = r6.onStickerOperationListener
            if (r3 == 0) goto L_0x0059
            r3.onStickerZoomFinished(r0)
        L_0x0059:
            r6.currentMode = r1
            goto L_0x007d
        L_0x005c:
            r6.handleCurrentMode(r7)
            r6.invalidate()
            goto L_0x007d
        L_0x0063:
            r6.onTouchUp(r7)
            goto L_0x007d
        L_0x0067:
            boolean r0 = r6.onTouchDown(r7)
            if (r0 != 0) goto L_0x007d
            com.pic.shot.sticker.StickerView$OnStickerOperationListener r0 = r6.onStickerOperationListener
            if (r0 != 0) goto L_0x0072
            return r1
        L_0x0072:
            r0.onStickerTouchOutside()
            r6.invalidate()
            boolean r0 = r6.drawCirclePoint
            if (r0 != 0) goto L_0x007d
            return r1
        L_0x007d:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.pic.shot.sticker.StickerView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public boolean onTouchDown(MotionEvent motionEvent) {
        this.currentMode = 1;
        this.downX = motionEvent.getX();
        this.downY = motionEvent.getY();
        this.onMoving = true;
        this.currentMoveingX = motionEvent.getX();
        this.currentMoveingY = motionEvent.getY();
        PointF calculateMidPoint = calculateMidPoint();
        this.midPoint = calculateMidPoint;
        this.oldDistance = calculateDistance(calculateMidPoint.x, this.midPoint.y, this.downX, this.downY);
        this.oldRotation = calculateRotation(this.midPoint.x, this.midPoint.y, this.downX, this.downY);
        BitmapStickerIcon findCurrentIconTouched = findCurrentIconTouched();
        this.currentIcon = findCurrentIconTouched;
        if (findCurrentIconTouched != null) {
            this.currentMode = 3;
            findCurrentIconTouched.onActionDown(this, motionEvent);
        } else {
            this.handlingSticker = findHandlingSticker();
        }
        Sticker sticker = this.handlingSticker;
        if (sticker != null) {
            this.downMatrix.set(sticker.getMatrix());
            if (this.bringToFrontCurrentSticker) {
                this.stickers.remove(this.handlingSticker);
                this.stickers.add(this.handlingSticker);
            }
            OnStickerOperationListener onStickerOperationListener2 = this.onStickerOperationListener;
            if (onStickerOperationListener2 != null) {
                onStickerOperationListener2.onStickerTouchedDown(this.handlingSticker);
            }
        }
        if (this.drawCirclePoint) {
            this.onStickerOperationListener.onTouchDownForBeauty(this.currentMoveingX, this.currentMoveingY);
            invalidate();
            return true;
        } else if (this.currentIcon == null && this.handlingSticker == null) {
            return false;
        } else {
            invalidate();
            return true;
        }
    }

    public void onTouchUp(MotionEvent motionEvent) {
        Sticker sticker;
        OnStickerOperationListener onStickerOperationListener2;
        Sticker sticker2;
        OnStickerOperationListener onStickerOperationListener3;
        BitmapStickerIcon bitmapStickerIcon;
        long uptimeMillis = SystemClock.uptimeMillis();
        this.onMoving = false;
        if (this.drawCirclePoint) {
            this.onStickerOperationListener.onTouchUpForBeauty(motionEvent.getX(), motionEvent.getY());
        }
        if (!(this.currentMode != 3 || (bitmapStickerIcon = this.currentIcon) == null || this.handlingSticker == null)) {
            bitmapStickerIcon.onActionUp(this, motionEvent);
        }
        if (this.currentMode == 1 && Math.abs(motionEvent.getX() - this.downX) < ((float) this.touchSlop) && Math.abs(motionEvent.getY() - this.downY) < ((float) this.touchSlop) && (sticker2 = this.handlingSticker) != null) {
            this.currentMode = 4;
            OnStickerOperationListener onStickerOperationListener4 = this.onStickerOperationListener;
            if (onStickerOperationListener4 != null) {
                onStickerOperationListener4.onStickerClicked(sticker2);
            }
            if (uptimeMillis - this.lastClickTime < ((long) this.minClickDelayTime) && (onStickerOperationListener3 = this.onStickerOperationListener) != null) {
                onStickerOperationListener3.onStickerDoubleTapped(this.handlingSticker);
            }
        }
        if (!(this.currentMode != 1 || (sticker = this.handlingSticker) == null || (onStickerOperationListener2 = this.onStickerOperationListener) == null)) {
            onStickerOperationListener2.onStickerDragFinished(sticker);
        }
        this.currentMode = 0;
        this.lastClickTime = uptimeMillis;
    }

    public void handleCurrentMode(MotionEvent motionEvent) {
        BitmapStickerIcon bitmapStickerIcon;
        int i = this.currentMode;
        if (i == 1) {
            this.currentMoveingX = motionEvent.getX();
            float y = motionEvent.getY();
            this.currentMoveingY = y;
            if (this.drawCirclePoint) {
                this.onStickerOperationListener.onTouchDragForBeauty(this.currentMoveingX, y);
            }
            if (this.handlingSticker != null) {
                this.moveMatrix.set(this.downMatrix);
                Sticker sticker = this.handlingSticker;
                if (sticker instanceof BeautySticker) {
                    BeautySticker beautySticker = (BeautySticker) sticker;
                    if (beautySticker.getType() == 10 || beautySticker.getType() == 11) {
                        this.moveMatrix.postTranslate(0.0f, motionEvent.getY() - this.downY);
                    } else {
                        this.moveMatrix.postTranslate(motionEvent.getX() - this.downX, motionEvent.getY() - this.downY);
                    }
                } else {
                    this.moveMatrix.postTranslate(motionEvent.getX() - this.downX, motionEvent.getY() - this.downY);
                }
                this.handlingSticker.setMatrix(this.moveMatrix);
                if (this.constrained) {
                    constrainSticker(this.handlingSticker);
                }
            }
        } else if (i != 2) {
            if (i == 3 && this.handlingSticker != null && (bitmapStickerIcon = this.currentIcon) != null) {
                bitmapStickerIcon.onActionMove(this, motionEvent);
            }
        } else if (this.handlingSticker != null) {
            float calculateDistance = calculateDistance(motionEvent);
            float calculateRotation = calculateRotation(motionEvent);
            this.moveMatrix.set(this.downMatrix);
            Matrix matrix = this.moveMatrix;
            float f = this.oldDistance;
            matrix.postScale(calculateDistance / f, calculateDistance / f, this.midPoint.x, this.midPoint.y);
            this.moveMatrix.postRotate(calculateRotation - this.oldRotation, this.midPoint.x, this.midPoint.y);
            this.handlingSticker.setMatrix(this.moveMatrix);
        }
    }

    public void zoomAndRotateCurrentSticker(MotionEvent motionEvent) {
        zoomAndRotateSticker(this.handlingSticker, motionEvent);
    }

    public void alignHorizontally() {
        this.moveMatrix.set(this.downMatrix);
        this.moveMatrix.postRotate(-getCurrentSticker().getCurrentAngle(), this.midPoint.x, this.midPoint.y);
        this.handlingSticker.setMatrix(this.moveMatrix);
    }

    public void zoomAndRotateSticker(Sticker sticker, MotionEvent motionEvent) {
        float f;
        if (sticker != null) {
            boolean z = sticker instanceof BeautySticker;
            if (z) {
                BeautySticker beautySticker = (BeautySticker) sticker;
                if (beautySticker.getType() == 10 || beautySticker.getType() == 11) {
                    return;
                }
            }
            if (sticker instanceof TextSticker) {
                f = this.oldDistance;
            } else {
                f = calculateDistance(this.midPoint.x, this.midPoint.y, motionEvent.getX(), motionEvent.getY());
            }
            float calculateRotation = calculateRotation(this.midPoint.x, this.midPoint.y, motionEvent.getX(), motionEvent.getY());
            this.moveMatrix.set(this.downMatrix);
            Matrix matrix = this.moveMatrix;
            float f2 = this.oldDistance;
            matrix.postScale(f / f2, f / f2, this.midPoint.x, this.midPoint.y);
            if (!z) {
                this.moveMatrix.postRotate(calculateRotation - this.oldRotation, this.midPoint.x, this.midPoint.y);
            }
            this.handlingSticker.setMatrix(this.moveMatrix);
        }
    }

    public void constrainSticker(Sticker sticker) {
        int width = getWidth();
        int height = getHeight();
        sticker.getMappedCenterPoint(this.currentCenterPoint, this.point, this.tmp);
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
        sticker.getMatrix().postTranslate(f2, f);
    }

    public BitmapStickerIcon findCurrentIconTouched() {
        for (BitmapStickerIcon next : this.icons) {
            float x = next.getX() - this.downX;
            float y = next.getY() - this.downY;
            if (((double) ((x * x) + (y * y))) <= Math.pow((double) (next.getIconRadius() + next.getIconRadius()), 2.0d)) {
                return next;
            }
        }
        return null;
    }

    public Sticker findHandlingSticker() {
        for (int size = this.stickers.size() - 1; size >= 0; size--) {
            if (isInStickerArea(this.stickers.get(size), this.downX, this.downY)) {
                return this.stickers.get(size);
            }
        }
        return null;
    }

    public boolean isInStickerArea(Sticker sticker, float f, float f2) {
        float[] fArr = this.tmp;
        fArr[0] = f;
        fArr[1] = f2;
        return sticker.contains(fArr);
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
        Sticker sticker = this.handlingSticker;
        if (sticker == null) {
            this.midPoint.set(0.0f, 0.0f);
            return this.midPoint;
        }
        sticker.getMappedCenterPoint(this.midPoint, this.point, this.tmp);
        return this.midPoint;
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

    public float calculateDistance(MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            return 0.0f;
        }
        return calculateDistance(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1));
    }

    public float calculateDistance(float f, float f2, float f3, float f4) {
        double d = (double) (f - f3);
        double d2 = (double) (f2 - f4);
        return (float) Math.sqrt((d * d) + (d2 * d2));
    }

    public void transformSticker(Sticker sticker) {
        if (sticker == null) {
            Log.e(TAG, "transformSticker: the bitmapSticker is null or the bitmapSticker bitmap is null");
            return;
        }
        this.sizeMatrix.reset();
        float width = (float) getWidth();
        float height = (float) getHeight();
        float width2 = (float) sticker.getWidth();
        float height2 = (float) sticker.getHeight();
        this.sizeMatrix.postTranslate((width - width2) / 2.0f, (height - height2) / 2.0f);
        float f = (width < height ? width / width2 : height / height2) / 2.0f;
        this.sizeMatrix.postScale(f, f, width / 2.0f, height / 2.0f);
        sticker.getMatrix().reset();
        sticker.setMatrix(this.sizeMatrix);
        invalidate();
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        for (int i5 = 0; i5 < this.stickers.size(); i5++) {
            Sticker sticker = this.stickers.get(i5);
            if (sticker != null) {
                transformSticker(sticker);
            }
        }
    }

    public void flipCurrentSticker(int i) {
        flip(this.handlingSticker, i);
    }

    public void flip(Sticker sticker, int i) {
        if (sticker != null) {
            sticker.getCenterPoint(this.midPoint);
            if ((i & 1) > 0) {
                sticker.getMatrix().preScale(-1.0f, 1.0f, this.midPoint.x, this.midPoint.y);
                sticker.setFlippedHorizontally(!sticker.isFlippedHorizontally());
            }
            if ((i & 2) > 0) {
                sticker.getMatrix().preScale(1.0f, -1.0f, this.midPoint.x, this.midPoint.y);
                sticker.setFlippedVertically(!sticker.isFlippedVertically());
            }
            OnStickerOperationListener onStickerOperationListener2 = this.onStickerOperationListener;
            if (onStickerOperationListener2 != null) {
                onStickerOperationListener2.onStickerFlipped(sticker);
            }
            invalidate();
        }
    }

    public boolean replace(Sticker sticker) {
        return replace(sticker, true);
    }

    public Sticker getLastHandlingSticker() {
        return this.lastHandlingSticker;
    }

    public boolean replace(Sticker sticker, boolean z) {
        float f;
        if (this.handlingSticker == null) {
            this.handlingSticker = this.lastHandlingSticker;
        }
        if (this.handlingSticker == null || sticker == null) {
            return false;
        }
        float width = (float) getWidth();
        float height = (float) getHeight();
        if (z) {
            sticker.setMatrix(this.handlingSticker.getMatrix());
            sticker.setFlippedVertically(this.handlingSticker.isFlippedVertically());
            sticker.setFlippedHorizontally(this.handlingSticker.isFlippedHorizontally());
        } else {
            this.handlingSticker.getMatrix().reset();
            sticker.getMatrix().postTranslate((width - ((float) this.handlingSticker.getWidth())) / 2.0f, (height - ((float) this.handlingSticker.getHeight())) / 2.0f);
            if (width < height) {
                Sticker sticker2 = this.handlingSticker;
                if (sticker2 instanceof TextSticker) {
                    f = width / ((float) sticker2.getWidth());
                } else {
                    f = width / ((float) sticker2.getDrawable().getIntrinsicWidth());
                }
            } else {
                Sticker sticker3 = this.handlingSticker;
                if (sticker3 instanceof TextSticker) {
                    f = height / ((float) sticker3.getHeight());
                } else {
                    f = height / ((float) sticker3.getDrawable().getIntrinsicHeight());
                }
            }
            float f2 = f / 2.0f;
            sticker.getMatrix().postScale(f2, f2, width / 2.0f, height / 2.0f);
        }
        List<Sticker> list = this.stickers;
        list.set(list.indexOf(this.handlingSticker), sticker);
        this.handlingSticker = sticker;
        invalidate();
        return true;
    }

    public boolean remove(Sticker sticker) {
        if (this.stickers.contains(sticker)) {
            this.stickers.remove(sticker);
            OnStickerOperationListener onStickerOperationListener2 = this.onStickerOperationListener;
            if (onStickerOperationListener2 != null) {
                onStickerOperationListener2.onStickerDeleted(sticker);
            }
            if (this.handlingSticker == sticker) {
                this.handlingSticker = null;
            }
            invalidate();
            return true;
        }
        Log.d(TAG, "remove: the sticker is not in this StickerView");
        return false;
    }

    public boolean removeCurrentSticker() {
        return remove(this.handlingSticker);
    }

    public void removeAllStickers() {
        this.stickers.clear();
        Sticker sticker = this.handlingSticker;
        if (sticker != null) {
            sticker.release();
            this.handlingSticker = null;
        }
        invalidate();
    }

    public StickerView addSticker(Sticker sticker) {
        return addSticker(sticker, 1);
    }

    public StickerView addSticker(final Sticker sticker, final int i) {
        if (ViewCompat.isLaidOut(this)) {
            addStickerImmediately(sticker, i);
        } else {
            post(new Runnable() {
                public void run() {
                    StickerView.this.addStickerImmediately(sticker, i);
                }
            });
        }
        return this;
    }

    public void addStickerImmediately(Sticker sticker, int i) {
        setStickerPosition(sticker, i);
        sticker.getMatrix().postScale(1.0f, 1.0f, (float) getWidth(), (float) getHeight());
        this.handlingSticker = sticker;
        this.stickers.add(sticker);
        OnStickerOperationListener onStickerOperationListener2 = this.onStickerOperationListener;
        if (onStickerOperationListener2 != null) {
            onStickerOperationListener2.onStickerAdded(sticker);
        }
        invalidate();
    }

    public void setStickerPosition(Sticker sticker, int i) {
        float f;
        float width = ((float) getWidth()) - ((float) sticker.getWidth());
        float height = ((float) getHeight()) - ((float) sticker.getHeight());
        if (sticker instanceof BeautySticker) {
            BeautySticker beautySticker = (BeautySticker) sticker;
            f = height / 2.0f;
            if (beautySticker.getType() == 0) {
                width /= 3.0f;
            } else if (beautySticker.getType() == 1) {
                width = (2.0f * width) / 3.0f;
            } else if (beautySticker.getType() == 2) {
                width /= 2.0f;
            } else if (beautySticker.getType() == 4) {
                width /= 2.0f;
            } else if (beautySticker.getType() == 10) {
                width /= 2.0f;
                f = (2.0f * f) / 3.0f;
            } else if (beautySticker.getType() == 11) {
                width /= 2.0f;
                f = (3.0f * f) / 2.0f;
            }
        } else {
            float f2 = (i & 2) > 0 ? height / 4.0f : (i & 16) > 0 ? height * 0.75f : height / 2.0f;
            width = (i & 4) > 0 ? width / 4.0f : (i & 8) > 0 ? width * 0.75f : width / 2.0f;
            f = f2;
        }
        sticker.getMatrix().postTranslate(width, f);
    }

    public void editTextSticker() {
        this.onStickerOperationListener.onStickerDoubleTapped(this.handlingSticker);
    }

    public float[] getStickerPoints(Sticker sticker) {
        float[] fArr = new float[8];
        getStickerPoints(sticker, fArr);
        return fArr;
    }

    public void getStickerPoints(Sticker sticker, float[] fArr) {
        if (sticker == null) {
            Arrays.fill(fArr, 0.0f);
            return;
        }
        sticker.getBoundPoints(this.bounds);
        sticker.getMappedPoints(fArr, this.bounds);
    }

    public void save(File file) {
        try {
            StickerUtils.saveImageToGallery(file, createBitmap());
            StickerUtils.notifySystemGallery(getContext(), file);
        } catch (IllegalArgumentException | IllegalStateException e) {
        }
    }

    public Bitmap createBitmap() throws OutOfMemoryError {
        this.handlingSticker = null;
        Bitmap createBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        draw(new Canvas(createBitmap));
        return createBitmap;
    }

    public int getStickerCount() {
        return this.stickers.size();
    }

    public boolean isNoneSticker() {
        return getStickerCount() == 0;
    }

    public StickerView setLocked(boolean z) {
        this.locked = z;
        invalidate();
        return this;
    }

    public StickerView setMinClickDelayTime(int i) {
        this.minClickDelayTime = i;
        return this;
    }

    public int getMinClickDelayTime() {
        return this.minClickDelayTime;
    }

    public boolean isConstrained() {
        return this.constrained;
    }

    public StickerView setConstrained(boolean z) {
        this.constrained = z;
        postInvalidate();
        return this;
    }

    public StickerView setOnStickerOperationListener(OnStickerOperationListener onStickerOperationListener2) {
        this.onStickerOperationListener = onStickerOperationListener2;
        return this;
    }

    public OnStickerOperationListener getOnStickerOperationListener() {
        return this.onStickerOperationListener;
    }

    public Sticker getCurrentSticker() {
        return this.handlingSticker;
    }

    public List<BitmapStickerIcon> getIcons() {
        return this.icons;
    }

    public void setIcons(List<BitmapStickerIcon> list) {
        this.icons.clear();
        this.icons.addAll(list);
        invalidate();
    }
}
