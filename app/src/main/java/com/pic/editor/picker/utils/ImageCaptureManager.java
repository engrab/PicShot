package com.pic.editor.picker.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageCaptureManager {
    private static final String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    private Context mContext;
    private String mCurrentPhotoPath;

    public ImageCaptureManager(Context context) {
        this.mContext = context;
    }

    private File createImageFile() throws IOException {
        String str = "JPEG_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()) + ".jpg";
        File externalStoragePublicDirectory = this.mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalStoragePublicDirectory.exists() || externalStoragePublicDirectory.mkdir()) {
            File file = new File(externalStoragePublicDirectory, str);
            this.mCurrentPhotoPath = file.getAbsolutePath();
            return file;
        }
        Log.e("TAG", "Throwing Errors....");
        throw new IOException();
    }

    public Intent dispatchTakePictureIntent() throws IOException {
        Uri uri;
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (intent.resolveActivity(this.mContext.getPackageManager()) != null) {
            File createImageFile = createImageFile();
            if (Build.VERSION.SDK_INT >= 24) {
                Context applicationContext = this.mContext.getApplicationContext();
                uri = FileProvider.getUriForFile(applicationContext, this.mContext.getApplicationInfo().packageName + ".provider", createImageFile);
            } else {
                uri = Uri.fromFile(createImageFile);
            }
            if (uri != null) {
                intent.putExtra("output", uri);
            }
        }
        return intent;
    }

    public void galleryAddPic() {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        if (!TextUtils.isEmpty(mCurrentPhotoPath)) {
            intent.setData(Uri.fromFile(new File(this.mCurrentPhotoPath)));
            this.mContext.sendBroadcast(intent);
        }
    }

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void onSaveInstanceState(Bundle bundle) {
        String str;
        if (bundle != null && (str = this.mCurrentPhotoPath) != null) {
            bundle.putString(CAPTURED_PHOTO_PATH_KEY, str);
        }
    }

    public void onRestoreInstanceState(Bundle bundle) {
        if (bundle != null && bundle.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
            this.mCurrentPhotoPath = bundle.getString(CAPTURED_PHOTO_PATH_KEY);
        }
    }
}
