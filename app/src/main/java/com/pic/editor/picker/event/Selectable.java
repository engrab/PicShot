package com.pic.editor.picker.event;

import com.pic.editor.picker.entity.Photo;

public interface Selectable {
    int getSelectedItemCount();

    boolean isSelected(Photo photo);
}
