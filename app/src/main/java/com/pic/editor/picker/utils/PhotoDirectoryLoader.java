package com.pic.editor.picker.utils;

import android.content.Context;
import android.provider.MediaStore;
import androidx.loader.content.CursorLoader;

public class PhotoDirectoryLoader extends CursorLoader {
    final String[] IMAGE_PROJECTION;

    public PhotoDirectoryLoader(Context context, boolean z) {
        super(context);
        String[] strArr;
        String[] strArr2 = {"_id", "_data", "bucket_id", "bucket_display_name", "date_added", "_size"};
        this.IMAGE_PROJECTION = strArr2;
        setProjection(strArr2);
        setUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        setSortOrder("date_added DESC");
        StringBuilder sb = new StringBuilder();
        sb.append("mime_type=? or mime_type=? or mime_type=? ");
        sb.append(z ? "or mime_type=?" : "");
        setSelection(sb.toString());
        if (z) {
            strArr = new String[]{"image/jpeg", "image/png", "image/jpg", "image/gif"};
        } else {
            strArr = new String[]{"image/jpeg", "image/png", "image/jpg"};
        }
        setSelectionArgs(strArr);
    }
}
