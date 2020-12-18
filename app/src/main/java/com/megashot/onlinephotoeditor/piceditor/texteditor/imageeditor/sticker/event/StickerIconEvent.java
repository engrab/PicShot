package com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.sticker.event;

import android.view.MotionEvent;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.sticker.StickerView;

public interface StickerIconEvent {
    void onActionDown(StickerView stickerView, MotionEvent motionEvent);

    void onActionMove(StickerView stickerView, MotionEvent motionEvent);

    void onActionUp(StickerView stickerView, MotionEvent motionEvent);
}
