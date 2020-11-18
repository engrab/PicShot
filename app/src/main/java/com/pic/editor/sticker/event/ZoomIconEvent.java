package com.pic.editor.sticker.event;

import android.view.MotionEvent;
import com.pic.editor.sticker.StickerView;

public class ZoomIconEvent implements StickerIconEvent {
    public void onActionDown(StickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionMove(StickerView paramStickerView, MotionEvent paramMotionEvent) {
        paramStickerView.zoomAndRotateCurrentSticker(paramMotionEvent);
    }

    public void onActionUp(StickerView paramStickerView, MotionEvent paramMotionEvent) {
        if (paramStickerView.getOnStickerOperationListener() != null) {
            if (paramStickerView.getCurrentSticker() != null) {
                paramStickerView.getOnStickerOperationListener().onStickerZoomFinished(paramStickerView.getCurrentSticker());
            }
        }
    }
}
