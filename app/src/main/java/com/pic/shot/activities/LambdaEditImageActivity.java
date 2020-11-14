package com.pic.shot.activities;

import android.content.DialogInterface;


public final  class LambdaEditImageActivity implements DialogInterface.OnClickListener {
    public static final  LambdaEditImageActivity INSTANCE = new LambdaEditImageActivity();

    private LambdaEditImageActivity() {
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
    }
}
