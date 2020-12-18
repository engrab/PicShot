package com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.sticker.event;

import android.view.MotionEvent;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.sticker.StickerView;

public class DeleteIconEvent implements StickerIconEvent {
    public void onActionDown(StickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionMove(StickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionUp(StickerView paramStickerView, MotionEvent paramMotionEvent) {
        paramStickerView.removeCurrentSticker();
    }
}
