package com.pic.shot.picker.event;

import com.pic.shot.picker.entity.Photo;

public interface OnItemCheckListener {
    boolean onItemCheck(int i, Photo photo, int i2);
}
