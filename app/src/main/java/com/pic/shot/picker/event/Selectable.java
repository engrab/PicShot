package com.pic.shot.picker.event;

import com.pic.shot.picker.entity.Photo;

public interface Selectable {
    int getSelectedItemCount();

    boolean isSelected(Photo photo);
}
