package com.pic.shot.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import androidx.multidex.BuildConfig;

public class SettingDialog {
    public static void showSettingDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GO SETTINGS", new DialogInterface.OnClickListener(activity) {
            public final /* synthetic */ Activity f$0;

            {
                this.f$0 = r1;
            }

            public final void onClick(DialogInterface dialogInterface, int i) {
                SettingDialog.lambda$showSettingDialog$0(this.f$0, dialogInterface, i);
            }
        });
        builder.setNegativeButton("Cancel", $$Lambda$SettingDialog$g_c90akZAWnuUOI5HH12CBF5MlY.INSTANCE);
        builder.show();
    }

    static /* synthetic */ void lambda$showSettingDialog$0(Activity activity, DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
        openSettings(activity);
    }

    public static void openSettings(Activity activity) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", BuildConfig.APPLICATION_ID, (String) null));
        activity.startActivityForResult(intent, 101);
    }
}
