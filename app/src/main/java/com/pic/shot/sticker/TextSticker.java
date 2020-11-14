package com.pic.shot.sticker;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import com.pic.shot.addtext.AddTextProperties;
import com.pic.shot.utils.SystemUtil;

public class TextSticker extends Sticker {
    private AddTextProperties addTextProperties;
    private int backgroundAlpha;
    private int backgroundBorder;
    private int backgroundColor;
    private BitmapDrawable backgroundDrawable;
    private final Context context;
    private Drawable drawable;
    private boolean isShowBackground;
    private float lineSpacingExtra = 0.0f;
    private float lineSpacingMultiplier = 1.0f;
    private float maxTextSizePixels;
    private float minTextSizePixels;
    private int paddingHeight;
    private int paddingWidth;
    private StaticLayout staticLayout;
    private String text;
    private Layout.Alignment textAlign;
    private int textAlpha;
    private int textColor;
    private int textHeight;
    private final TextPaint textPaint;
    private AddTextProperties.TextShadow textShadow;
    private int textWidth;

    public TextSticker(Context paramContext, AddTextProperties paramAddTextProperties) {
        this.context = paramContext;
        this.addTextProperties = paramAddTextProperties;
        this.textPaint = new TextPaint(1);
        TextSticker textSticker = setTextSize(paramAddTextProperties.getTextSize()).setTextWidth(paramAddTextProperties.getTextWidth()).setTextHeight(paramAddTextProperties.getTextHeight()).setText(paramAddTextProperties.getText()).setPaddingWidth(SystemUtil.dpToPx(paramContext, paramAddTextProperties.getPaddingWidth())).setBackgroundBorder(SystemUtil.dpToPx(paramContext, paramAddTextProperties.getBackgroundBorder())).setTextShadow(paramAddTextProperties.getTextShadow()).setTextColor(paramAddTextProperties.getTextColor()).setTextAlpha(paramAddTextProperties.getTextAlpha()).setBackgroundColor(paramAddTextProperties.getBackgroundColor()).setBackgroundAlpha(paramAddTextProperties.getBackgroundAlpha()).setShowBackground(paramAddTextProperties.isShowBackground()).setTextColor(paramAddTextProperties.getTextColor());
        AssetManager assetManager = paramContext.getAssets();
        textSticker.setTypeface(Typeface.createFromAsset(assetManager, "fonts/" + paramAddTextProperties.getFontName())).setTextAlign(paramAddTextProperties.getTextAlign()).setTextShare(paramAddTextProperties.getTextShader()).resizeText();
    }

    private float convertSpToPx(float paramFloat) {
        return this.context.getResources().getDisplayMetrics().scaledDensity * paramFloat;
    }

    public void draw(Canvas paramCanvas) {
        Matrix matrix = getMatrix();
        paramCanvas.save();
        paramCanvas.concat(matrix);
        if (this.isShowBackground) {
            Paint paint = new Paint();
            if (this.backgroundDrawable != null) {
                paint.setShader(new BitmapShader(this.backgroundDrawable.getBitmap(), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR));
                paint.setAlpha(this.backgroundAlpha);
            } else {
                paint.setARGB(this.backgroundAlpha, Color.red(this.backgroundColor), Color.green(this.backgroundColor), Color.blue(this.backgroundColor));
            }
            float f = (float) this.textWidth;
            float f2 = (float) this.textHeight;
            int i = this.backgroundBorder;
            paramCanvas.drawRoundRect(0.0f, 0.0f, f, f2, (float) i, (float) i, paint);
            paramCanvas.restore();
            paramCanvas.save();
            paramCanvas.concat(matrix);
        }
        paramCanvas.restore();
        paramCanvas.save();
        paramCanvas.concat(matrix);
        paramCanvas.translate((float) this.paddingWidth, (float) ((this.textHeight / 2) - (this.staticLayout.getHeight() / 2)));
        this.staticLayout.draw(paramCanvas);
        paramCanvas.restore();
        paramCanvas.save();
        paramCanvas.concat(matrix);
        paramCanvas.restore();
    }

    public AddTextProperties getAddTextProperties() {
        return this.addTextProperties;
    }

    public int getAlpha() {
        return this.textPaint.getAlpha();
    }

    public Drawable getDrawable() {
        return this.drawable;
    }

    public int getHeight() {
        return this.textHeight;
    }

    public String getText() {
        return this.text;
    }

    public int getWidth() {
        return this.textWidth;
    }

    public void release() {
        super.release();
        if (this.drawable != null) {
            this.drawable = null;
        }
    }

    public TextSticker resizeText() {
        String str = getText();
        if (str == null || str.length() <= 0) {
            return this;
        }
        AddTextProperties.TextShadow textShadow2 = this.textShadow;
        if (textShadow2 != null) {
            this.textPaint.setShadowLayer((float) textShadow2.getRadius(), (float) this.textShadow.getDx(), (float) this.textShadow.getDy(), this.textShadow.getColorShadow());
        }
        this.textPaint.setTextAlign(Paint.Align.LEFT);
        this.textPaint.setARGB(this.textAlpha, Color.red(this.textColor), Color.green(this.textColor), Color.blue(this.textColor));
        int i = this.textWidth - (this.paddingWidth * 2);
        if (i <= 0) {
            i = 100;
        }
        String str2 = this.text;
        this.staticLayout = StaticLayout.Builder.obtain(str2, 0, str2.length(), this.textPaint, i).build();
        return this;
    }

    public TextSticker setAlpha(int paramInt) {
        this.textPaint.setAlpha(paramInt);
        return this;
    }

    public TextSticker setBackgroundAlpha(int paramInt) {
        this.backgroundAlpha = paramInt;
        return this;
    }

    public TextSticker setBackgroundBorder(int paramInt) {
        this.backgroundBorder = paramInt;
        return this;
    }

    public TextSticker setBackgroundColor(int paramInt) {
        this.backgroundColor = paramInt;
        return this;
    }

    public TextSticker setDrawable(Drawable paramDrawable) {
        this.drawable = paramDrawable;
        return this;
    }

    public TextSticker setPaddingWidth(int paramInt) {
        this.paddingWidth = paramInt;
        return this;
    }

    public TextSticker setShowBackground(boolean paramBoolean) {
        this.isShowBackground = paramBoolean;
        return this;
    }

    public TextSticker setText(String paramString) {
        this.text = paramString;
        return this;
    }

    public TextSticker setTextAlign(int paramInt) {
        if (paramInt == 2) {
            this.textAlign = Layout.Alignment.ALIGN_NORMAL;
            return this;
        } else if (paramInt == 3) {
            this.textAlign = Layout.Alignment.ALIGN_OPPOSITE;
            return this;
        } else if (paramInt != 4) {
            return this;
        } else {
            this.textAlign = Layout.Alignment.ALIGN_CENTER;
            return this;
        }
    }

    public TextSticker setTextAlpha(int paramInt) {
        this.textAlpha = paramInt;
        return this;
    }

    public TextSticker setTextColor(int paramInt) {
        this.textColor = paramInt;
        return this;
    }

    public TextSticker setTextHeight(int paramInt) {
        this.textHeight = paramInt;
        return this;
    }

    public TextSticker setTextShadow(AddTextProperties.TextShadow paramTextShadow) {
        this.textShadow = paramTextShadow;
        return this;
    }

    public TextSticker setTextShare(Shader paramShader) {
        this.textPaint.setShader(paramShader);
        return this;
    }

    public TextSticker setTextSize(int paramInt) {
        this.textPaint.setTextSize(convertSpToPx((float) paramInt));
        return this;
    }

    public TextSticker setTextWidth(int paramInt) {
        this.textWidth = paramInt;
        return this;
    }

    public TextSticker setTypeface(Typeface paramTypeface) {
        this.textPaint.setTypeface(paramTypeface);
        return this;
    }
}
