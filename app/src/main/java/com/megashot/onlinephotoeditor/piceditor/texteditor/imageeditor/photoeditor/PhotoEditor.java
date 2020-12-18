package com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.photoeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.UiThread;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.wysaid.view.ImageGLSurfaceView;

public class PhotoEditor implements BrushViewChangeListener {
    private static final String TAG = "PhotoEditor";
    private final List<View> addedViews;
    private final BrushDrawingView brushDrawingView;
    private final Context context;
    private final View deleteView;
    private final ImageGLSurfaceView glSurfaceView;
    private final boolean isTextPinchZoomable;
    private final Typeface mDefaultEmojiTypeface;
    private final Typeface mDefaultTextTypeface;
    private final LayoutInflater mLayoutInflater;
    private OnPhotoEditorListener mOnPhotoEditorListener;
    public PhotoEditorView parentView;
    private final List<View> redoViews;

    public interface OnSaveListener {
        void onFailure(Exception exc);

        void onSuccess(String str);
    }

    private PhotoEditor(Builder builder) {
        this.context = builder.context;
        this.parentView = builder.parentView;
        this.deleteView = builder.deleteView;
        this.brushDrawingView = builder.brushDrawingView;
        this.glSurfaceView = builder.glSurfaceView;
        this.isTextPinchZoomable = builder.isTextPinchZoomable;
        this.mDefaultTextTypeface = builder.textTypeface;
        this.mDefaultEmojiTypeface = builder.emojiTypeface;
        this.mLayoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.brushDrawingView.setBrushViewChangeListener(this);
        this.addedViews = new ArrayList();
        this.redoViews = new ArrayList();
    }

    public BrushDrawingView getBrushDrawingView() {
        return this.brushDrawingView;
    }

    public void setBrushDrawingMode(boolean z) {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.setBrushDrawingMode(z);
        }
    }

    public void setAdjustFilter(String str) {
        this.glSurfaceView.setFilterWithConfig(str);
    }

    public void setFilterIntensityForIndex(float f, int i, boolean z) {
        this.glSurfaceView.setFilterIntensityForIndex(f, i, z);
    }

    public void setBrushMode(int i) {
        this.brushDrawingView.setDrawMode(i);
    }

    public void setBrushMagic(DrawBitmapModel drawBitmapModel) {
        this.brushDrawingView.setCurrentMagicBrush(drawBitmapModel);
    }

    public void setBrushSize(float f) {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.setBrushSize(f);
        }
    }

    public void setBrushColor(int i) {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.setBrushColor(i);
        }
    }

    public void setBrushEraserSize(float f) {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.setBrushEraserSize(f);
        }
    }

    public void brushEraser() {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.brushEraser();
        }
    }

    public boolean undo() {
        if (this.addedViews.size() > 0) {
            List<View> list = this.addedViews;
            View view = list.get(list.size() - 1);
            if (!(view instanceof BrushDrawingView)) {
                List<View> list2 = this.addedViews;
                list2.remove(list2.size() - 1);
                this.parentView.removeView(view);
                this.redoViews.add(view);
                OnPhotoEditorListener onPhotoEditorListener = this.mOnPhotoEditorListener;
                if (onPhotoEditorListener != null) {
                    onPhotoEditorListener.onRemoveViewListener(this.addedViews.size());
                    Object tag = view.getTag();
                    if (tag != null && (tag instanceof ViewType)) {
                        this.mOnPhotoEditorListener.onRemoveViewListener((ViewType) tag, this.addedViews.size());
                    }
                }
            } else {
                BrushDrawingView brushDrawingView2 = this.brushDrawingView;
                return brushDrawingView2 != null && brushDrawingView2.undo();
            }
        }
        return this.addedViews.size() != 0;
    }

    public boolean redo() {
        if (this.redoViews.size() > 0) {
            List<View> list = this.redoViews;
            View view = list.get(list.size() - 1);
            if (!(view instanceof BrushDrawingView)) {
                List<View> list2 = this.redoViews;
                list2.remove(list2.size() - 1);
                this.parentView.addView(view);
                this.addedViews.add(view);
                Object tag = view.getTag();
                OnPhotoEditorListener onPhotoEditorListener = this.mOnPhotoEditorListener;
                if (!(onPhotoEditorListener == null || tag == null || !(tag instanceof ViewType))) {
                    onPhotoEditorListener.onAddViewListener((ViewType) tag, this.addedViews.size());
                }
            } else {
                BrushDrawingView brushDrawingView2 = this.brushDrawingView;
                return brushDrawingView2 != null && brushDrawingView2.redo();
            }
        }
        return this.redoViews.size() != 0;
    }

    public void redoBrush() {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.redo();
        }
    }

    public void undoBrush() {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.undo();
        }
    }

    public void clearBrushAllViews() {
        BrushDrawingView brushDrawingView2 = this.brushDrawingView;
        if (brushDrawingView2 != null) {
            brushDrawingView2.clearAll();
        }
    }

    public void clearAllViews() {
        for (int i = 0; i < this.addedViews.size(); i++) {
            this.parentView.removeView(this.addedViews.get(i));
        }
        if (this.addedViews.contains(this.brushDrawingView)) {
            this.parentView.addView(this.brushDrawingView);
        }
        this.addedViews.clear();
        this.redoViews.clear();
        clearBrushAllViews();
    }

    @UiThread
    public void clearHelperBox() {
        for (int i = 0; i < this.parentView.getChildCount(); i++) {
            View childAt = this.parentView.getChildAt(i);
//            FrameLayout frameLayout = childAt.findViewById(R.id.frmBorder);
//            if (frameLayout != null) {
//                frameLayout.setBackgroundResource(0);
//            }
//            ImageView imageView = childAt.findViewById(R.id.imgPhotoEditorClose);
//            if (imageView != null) {
//                imageView.setVisibility(View.GONE);
//            }
        }
    }

    public void setFilterEffect(String str) {
        this.parentView.setFilterEffect(str);
    }

    @Deprecated
    public void saveImage(String str, OnSaveListener onSaveListener) {
        saveAsFile(str, onSaveListener);
    }

    public void saveAsFile(String str, OnSaveListener onSaveListener) {
        saveAsFile(str, new SaveSettings.Builder().build(), onSaveListener);
    }

    public void saveAsFile(final String str, final SaveSettings saveSettings, final OnSaveListener onSaveListener) {

        this.parentView.saveGLSurfaceViewAsBitmap(new OnSaveBitmap() {

            public void onBitmapReady(Bitmap bitmap) {
                new AsyncTask<String, String, Exception>() {
                    public void onPreExecute() {
                        super.onPreExecute();
                        PhotoEditor.this.clearHelperBox();
                    }

                    public Exception doInBackground(String... strArr) {
                        Bitmap bitmap;
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(new File(str), false);
                            if (PhotoEditor.this.parentView != null) {
                                if (saveSettings.isTransparencyEnabled()) {
                                    bitmap = BitmapUtil.removeTransparency(PhotoEditor.this.getBitmapFromView(PhotoEditor.this.parentView));
                                } else {
                                    bitmap = PhotoEditor.this.getBitmapFromView(PhotoEditor.this.parentView);
                                }
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                            }
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            Log.d(PhotoEditor.TAG, "Filed Saved Successfully");
                            return null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(PhotoEditor.TAG, "Failed to save File");
                            return e;
                        }
                    }

                    public void onPostExecute(Exception exc) {
                        super.onPostExecute(exc);
                        if (exc == null) {
                            if (saveSettings.isClearViewsEnabled()) {
                                PhotoEditor.this.clearAllViews();
                            }
                            onSaveListener.onSuccess(str);
                            return;
                        }
                        onSaveListener.onFailure(exc);
                    }
                }.execute();
            }

            public void onFailure(Exception exc) {
                onSaveListener.onFailure(exc);
            }
        });
    }

    public void saveStickerAsBitmap(OnSaveBitmap onSaveBitmap) {
        saveStickerAsBitmap(new SaveSettings.Builder().build(), onSaveBitmap);
    }

    public void saveStickerAsBitmap(SaveSettings saveSettings, OnSaveBitmap onSaveBitmap) {
        Bitmap bitmap;
        if (saveSettings.isTransparencyEnabled()) {
            bitmap = BitmapUtil.removeTransparency(getBitmapFromView(this.parentView));
        } else {
            bitmap = getBitmapFromView(this.parentView);
        }
        onSaveBitmap.onBitmapReady(bitmap);
    }

    private static String convertEmoji(String str) {
        try {
            return new String(Character.toChars(Integer.parseInt(str.substring(2), 16)));
        } catch (NumberFormatException e) {
            return "";
        }
    }

    public void setOnPhotoEditorListener(OnPhotoEditorListener onPhotoEditorListener) {
        this.mOnPhotoEditorListener = onPhotoEditorListener;
    }

    public boolean isCacheEmpty() {
        return this.addedViews.size() == 0 && this.redoViews.size() == 0;
    }

    public void onViewAdd(BrushDrawingView brushDrawingView2) {
        if (this.redoViews.size() > 0) {
            List<View> list = this.redoViews;
            list.remove(list.size() - 1);
        }
        this.addedViews.add(brushDrawingView2);
        OnPhotoEditorListener onPhotoEditorListener = this.mOnPhotoEditorListener;
        if (onPhotoEditorListener != null) {
            onPhotoEditorListener.onAddViewListener(ViewType.BRUSH_DRAWING, this.addedViews.size());
        }
    }

    public void onViewRemoved(BrushDrawingView brushDrawingView2) {
        if (this.addedViews.size() > 0) {
            List<View> list = this.addedViews;
            View remove = list.remove(list.size() - 1);
            if (!(remove instanceof BrushDrawingView)) {
                this.parentView.removeView(remove);
            }
            this.redoViews.add(remove);
        }
        OnPhotoEditorListener onPhotoEditorListener = this.mOnPhotoEditorListener;
        if (onPhotoEditorListener != null) {
            onPhotoEditorListener.onRemoveViewListener(this.addedViews.size());
            this.mOnPhotoEditorListener.onRemoveViewListener(ViewType.BRUSH_DRAWING, this.addedViews.size());
        }
    }

    public void onStartDrawing() {
        OnPhotoEditorListener onPhotoEditorListener = this.mOnPhotoEditorListener;
        if (onPhotoEditorListener != null) {
            onPhotoEditorListener.onStartViewChangeListener(ViewType.BRUSH_DRAWING);
        }
    }

    public void onStopDrawing() {
        OnPhotoEditorListener onPhotoEditorListener = this.mOnPhotoEditorListener;
        if (onPhotoEditorListener != null) {
            onPhotoEditorListener.onStopViewChangeListener(ViewType.BRUSH_DRAWING);
        }
    }

    public static class Builder {
        public BrushDrawingView brushDrawingView;
        public Context context;
        public View deleteView;
        public Typeface emojiTypeface;
        public ImageGLSurfaceView glSurfaceView;
        public boolean isTextPinchZoomable = true;
        public PhotoEditorView parentView;
        public Typeface textTypeface;

        public Builder(Context context2, PhotoEditorView photoEditorView) {
            this.context = context2;
            this.parentView = photoEditorView;
            this.brushDrawingView = photoEditorView.getBrushDrawingView();
            this.glSurfaceView = photoEditorView.getGLSurfaceView();
        }

        public Builder setDeleteView(View view) {
            this.deleteView = view;
            return this;
        }

        public Builder setDefaultTextTypeface(Typeface typeface) {
            this.textTypeface = typeface;
            return this;
        }

        public Builder setDefaultEmojiTypeface(Typeface typeface) {
            this.emojiTypeface = typeface;
            return this;
        }

        public Builder setPinchTextScalable(boolean z) {
            this.isTextPinchZoomable = z;
            return this;
        }

        public PhotoEditor build() {
            return new PhotoEditor(this);
        }
    }

    public Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }
}
