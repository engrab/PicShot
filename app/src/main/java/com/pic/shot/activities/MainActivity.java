package com.pic.shot.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pic.shot.R;
import com.pic.shot.ads.AdmobAds;
import com.pic.shot.ads.AdsUtils;
import com.pic.shot.constants.Constants;
import com.pic.shot.dialog.RateDialog;
import com.pic.shot.dialog.SettingDialog;
import com.pic.shot.picker.PhotoPicker;
import com.pic.shot.picker.utils.ImageCaptureManager;
import com.pic.shot.utils.SharePreferenceUtil;
import java.io.IOException;
import java.util.List;

public class MainActivity extends BaseActivity {
    private ImageCaptureManager imageCaptureManager;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        public final void onClick(View view) {
            MainActivity.this.lambda$new$2$MainActivity(view);
        }
    };

    public void onCreate(Bundle bundle) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(bundle);
        makeFullScreen();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#424160"));
        }
        setContentView((int) R.layout.activity_main);
        findViewById(R.id.relative_layout_edit_photo).setOnClickListener(this.onClickListener);
        findViewById(R.id.relatve_layout_edit).setOnClickListener(this.onClickListener);
        findViewById(R.id.relative_layout_take_photo).setOnClickListener(this.onClickListener);
        findViewById(R.id.relatve_layout_camera).setOnClickListener(this.onClickListener);
        this.imageCaptureManager = new ImageCaptureManager(this);
        if (SharePreferenceUtil.isPurchased(getApplicationContext())) {
            findViewById(R.id.image_view_remove_ads).setVisibility(View.GONE);
        }
        findViewById(R.id.image_view_about).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
               popupMenu(view);
            }
        });
        if (Constants.SHOW_ADS) {
            AdmobAds.loadNativeAds(this, (View) null);
        } else {
            findViewById(R.id.adsContainer).setVisibility(View.GONE);
        }
    }

    public /* synthetic */ void lambda$new$2$MainActivity(View view) {
        switch (view.getId()) {
            case R.id.relative_layout_edit_photo:
            case R.id.relatve_layout_edit:
                Dexter.withContext(this).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            PhotoPicker.builder().setPhotoCount(1).setPreviewEnabled(false).setShowCamera(false).start(MainActivity.this);
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            SettingDialog.showSettingDialog(MainActivity.this);
                        }
                    }

                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
                    public final void onError(DexterError dexterError) {
                        MainActivity.this.lambda$null$1$MainActivity(dexterError);
                    }
                }).onSameThread().check();
                return;
            case R.id.relative_layout_take_photo:
            case R.id.relatve_layout_camera:
                Dexter.withContext(this).withPermissions("android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            MainActivity.this.takePhotoFromCamera();
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            SettingDialog.showSettingDialog(MainActivity.this);
                        }
                    }

                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
                    public final void onError(DexterError dexterError) {
                        MainActivity.this.lambda$null$0$MainActivity(dexterError);
                    }
                }).onSameThread().check();
                return;
            default:
                return;
        }
    }

    public /* synthetic */ void lambda$null$0$MainActivity(DexterError dexterError) {
        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
    }

    public /* synthetic */ void lambda$null$1$MainActivity(DexterError dexterError) {
        Toast.makeText(this, "Error occurred! ", Toast.LENGTH_SHORT).show();
    }

    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            super.onActivityResult(i, i2, intent);
        } else if (i == 1) {
            if (this.imageCaptureManager == null) {
                this.imageCaptureManager = new ImageCaptureManager(this);
            }
            new Handler().post(new Runnable() {
                public final void run() {
                    MainActivity.this.lambda$onActivityResult$3$MainActivity();
                }
            });
            Intent intent2 = new Intent(getApplicationContext(), EditImageActivity.class);
            intent2.putExtra(PhotoPicker.KEY_SELECTED_PHOTOS, this.imageCaptureManager.getCurrentPhotoPath());
            startActivity(intent2);
        }
    }

    public /* synthetic */ void lambda$onActivityResult$3$MainActivity() {
        this.imageCaptureManager.galleryAddPic();
    }

    public void onResume() {
        super.onResume();
        if (AdsUtils.isNetworkAvailabel(getApplicationContext())) {
            if (!SharePreferenceUtil.isRated(getApplicationContext()) && SharePreferenceUtil.getCounter(getApplicationContext()) % 6 == 0) {
                new RateDialog(this, false).show();
            }
            SharePreferenceUtil.increateCounter(getApplicationContext());
        }
    }

    public void takePhotoFromCamera() {
        try {
            startActivityForResult(this.imageCaptureManager.dispatchTakePictureIntent(), 1);
        } catch (ActivityNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void popupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public final boolean onMenuItemClick(MenuItem menuItem) {
                return MainActivity.this.lambda$popupMenu$4$MainActivity(menuItem);
            }
        });
        popupMenu.show();
    }

    public /* synthetic */ boolean lambda$popupMenu$4$MainActivity(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_feedback) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.EMAIL", new String[]{getResources().getString(R.string.email_feedback)});
            intent.putExtra("android.intent.extra.SUBJECT", "PicShot Feedback: ");
            intent.putExtra("android.intent.extra.TEXT", "");
            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, getString(R.string.choose_email) + " :"));
        } else if (itemId == R.id.action_privacy_policy) {
            try {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://sites.google.com/view/nttteam/home")));
            } catch (Exception e) {
            }
        } else if (itemId == R.id.action_rate_us) {
            new RateDialog(this, false).show();
        } else if (itemId == R.id.action_more_app) {
            Log.d("qq", "moreApp");
            try {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://search?q=pub:" + getString(R.string.developerId))));
            } catch (ActivityNotFoundException e2) {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/developer?id=" + getString(R.string.developerId))));
            }
        } else if (itemId == R.id.action_share_friend) {
            Intent intent2 = new Intent("android.intent.action.SEND");
            intent2.setType("text/plain");
            intent2.putExtra("android.intent.extra.SUBJECT", getString(R.string.app_name));
            intent2.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=com.pic.shot");
            startActivity(Intent.createChooser(intent2, "Choose"));
        }
        return false;
    }

    public void onBackPressed() {
        finish();
    }
}
