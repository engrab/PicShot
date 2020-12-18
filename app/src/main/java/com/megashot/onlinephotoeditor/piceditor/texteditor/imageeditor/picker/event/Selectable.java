package com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.picker.event;

import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.picker.entity.Photo;

public interface Selectable {
    int getSelectedItemCount();

    boolean isSelected(Photo photo);
}
