package com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.photoeditor;

interface BrushViewChangeListener {
    void onStartDrawing();

    void onStopDrawing();

    void onViewAdd(BrushDrawingView brushDrawingView);

    void onViewRemoved(BrushDrawingView brushDrawingView);
}
