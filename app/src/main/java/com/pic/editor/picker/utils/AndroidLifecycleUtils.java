package com.pic.editor.picker.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import androidx.fragment.app.Fragment;

public class AndroidLifecycleUtils {
    public static boolean canLoadImage(Fragment fragment) {
        if (fragment == null) {
            return true;
        }
        return canLoadImage((Activity) fragment.getActivity());
    }

    public static boolean canLoadImage(Context context) {
        if (context == null || !(context instanceof Activity)) {
            return true;
        }
        return canLoadImage((Activity) context);
    }

    public static boolean canLoadImage(Activity activity) {
        if (activity == null) {
            return true;
        }
        if ((Build.VERSION.SDK_INT < 17 || !activity.isDestroyed()) && !activity.isFinishing()) {
            return true;
        }
        return false;
    }
}
