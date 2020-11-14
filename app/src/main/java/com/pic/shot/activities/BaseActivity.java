package com.pic.shot.activities;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    public void isPermissionGranted(boolean z, String str) {
    }

    public void makeFullScreen() {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 52) {
            isPermissionGranted(iArr[0] == 0, strArr[0]);
        }
    }
}
