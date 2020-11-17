package com.pic.editor.sticker.event;

import android.view.MotionEvent;
import com.pic.editor.sticker.StickerView;

public abstract class AbstractFlipEvent implements StickerIconEvent {
    /* access modifiers changed from: protected */
    public abstract int getFlipDirection();

    public void onActionDown(StickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionMove(StickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionUp(StickerView paramStickerView, MotionEvent paramMotionEvent) {
        paramStickerView.flipCurrentSticker(getFlipDirection());
    }
}
