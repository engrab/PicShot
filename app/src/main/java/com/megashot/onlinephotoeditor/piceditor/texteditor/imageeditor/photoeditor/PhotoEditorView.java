package com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.photoeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.R;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.sticker.StickerView;
import org.wysaid.view.ImageGLSurfaceView;

public class PhotoEditorView extends StickerView {
    private BrushDrawingView brushDrawingView;
    private Bitmap currentBitmap;
    private FilterImageView filterImageView;
    public ImageGLSurfaceView imageGLSurfaceView;

    public PhotoEditorView(Context context) {
        super(context);
        init((AttributeSet) null);
    }

    public PhotoEditorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public PhotoEditorView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet);
    }

    private void init(AttributeSet attributeSet) {
        FilterImageView filterImageView2 = new FilterImageView(getContext());
        this.filterImageView = filterImageView2;
        filterImageView2.setId(FLIP_HORIZONTALLY);
        this.filterImageView.setAdjustViewBounds(true);
        this.filterImageView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black));
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        layoutParams.addRule(13, -1);
        BrushDrawingView brushDrawingView2 = new BrushDrawingView(getContext());
        this.brushDrawingView = brushDrawingView2;
        brushDrawingView2.setVisibility(View.GONE);
        this.brushDrawingView.setId(FLIP_VERTICALLY);
        LayoutParams layoutParams2 = new LayoutParams(-1, -2);
        layoutParams2.addRule(13, -1);
        layoutParams2.addRule(6, 1);
        layoutParams2.addRule(8, 1);
        ImageGLSurfaceView imageGLSurfaceView2 = new ImageGLSurfaceView(getContext(), attributeSet);
        this.imageGLSurfaceView = imageGLSurfaceView2;
        imageGLSurfaceView2.setId(AUTOFILL_TYPE_LIST);
        this.imageGLSurfaceView.setVisibility(View.VISIBLE);
        this.imageGLSurfaceView.setAlpha(1.0f);
        this.imageGLSurfaceView.setDisplayMode(ImageGLSurfaceView.DisplayMode.DISPLAY_ASPECT_FIT);
        LayoutParams layoutParams3 = new LayoutParams(-1, -2);
        layoutParams3.addRule(13, -1);
        layoutParams3.addRule(6, 1);
        layoutParams3.addRule(8, 1);
        addView(this.filterImageView, layoutParams);
        addView(this.imageGLSurfaceView, layoutParams3);
        addView(this.brushDrawingView, layoutParams2);
    }

    public void setImageSource(final Bitmap bitmap) {
        this.filterImageView.setImageBitmap(bitmap);
        if (this.imageGLSurfaceView.getImageHandler() != null) {
            this.imageGLSurfaceView.setImageBitmap(bitmap);
        } else {
            this.imageGLSurfaceView.setSurfaceCreatedCallback(new ImageGLSurfaceView.OnSurfaceCreatedCallback() {
                public void surfaceCreated() {
                    PhotoEditorView.this.imageGLSurfaceView.setImageBitmap(bitmap);
                }
            });
        }
        this.currentBitmap = bitmap;
    }

    public void setImageSource(Bitmap bitmap, ImageGLSurfaceView.OnSurfaceCreatedCallback onSurfaceCreatedCallback) {
        this.filterImageView.setImageBitmap(bitmap);
        if (this.imageGLSurfaceView.getImageHandler() != null) {
            this.imageGLSurfaceView.setImageBitmap(bitmap);
        } else {
            this.imageGLSurfaceView.setSurfaceCreatedCallback(onSurfaceCreatedCallback);
        }
        this.currentBitmap = bitmap;
    }

    public Bitmap getCurrentBitmap() {
        return this.currentBitmap;
    }

    public BrushDrawingView getBrushDrawingView() {
        return this.brushDrawingView;
    }

    public ImageGLSurfaceView getGLSurfaceView() {
        return this.imageGLSurfaceView;
    }

    public void saveGLSurfaceViewAsBitmap(final OnSaveBitmap onSaveBitmap) {
        if (this.imageGLSurfaceView.getVisibility() == VISIBLE) {
            this.imageGLSurfaceView.getResultBitmap(new ImageGLSurfaceView.QueryResultBitmapCallback() {
                public void get(Bitmap bitmap) {
                    onSaveBitmap.onBitmapReady(bitmap);
                }
            });
        }
    }

    public void setFilterEffect(String str) {
        this.imageGLSurfaceView.setFilterWithConfig(str);
    }

    public void setFilterIntensity(float f) {
        this.imageGLSurfaceView.setFilterIntensity(f);
    }
}
