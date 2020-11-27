package com.pic.editor.activities;

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

import androidx.annotation.Nullable;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pic.editor.R;
import com.pic.editor.ads.AdmobAds;
import com.pic.editor.ads.AdsUtils;
import com.pic.editor.constants.Constants;
import com.pic.editor.dialog.RateDialog;
import com.pic.editor.dialog.SettingDialog;
import com.pic.editor.picker.PhotoPicker;
import com.pic.editor.picker.utils.ImageCaptureManager;
import com.pic.editor.picker.utils.PermissionsUtils;
import com.pic.editor.utils.SharePreferenceUtil;

import java.io.IOException;
import java.util.List;

public class MainActivity extends BaseActivity {
    public static final int REQUEST_IMAGE_CAPTURE = 100;
    private ImageCaptureManager imageCaptureManager;
    boolean doubleBackToExitPressedOnce = false;

    View.OnClickListener onClickListener = new View.OnClickListener() {

        public final void onClick(View view) {
            switch (view.getId()) {

                case R.id.iv_edit:
                    Dexter.withContext(MainActivity.this).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
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
                            Toast.makeText(MainActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show();
                        }
                    }).onSameThread().check();
                    return;

                case R.id.iv_camera:
                    Dexter.withContext(MainActivity.this).withPermissions("android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
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
                            Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                        }
                    }).onSameThread().check();
                    return;

                case R.id.iv_mywork:
                    startActivity(new Intent(MainActivity.this,  MyCreationActivity.class));
                    return;
                default:
                    return;
            }
        }
    };

    public void onCreate(Bundle bundle) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(bundle);
        makeFullScreen();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#424160"));
        }
        setContentView(R.layout.activity_main);
        findViewById(R.id.iv_camera).setOnClickListener(this.onClickListener);
        findViewById(R.id.iv_edit).setOnClickListener(this.onClickListener);
        findViewById(R.id.iv_mywork).setOnClickListener(this.onClickListener);

        imageCaptureManager = new ImageCaptureManager(this);
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


    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (this.imageCaptureManager == null) {
                this.imageCaptureManager = new ImageCaptureManager(this);
            }
            new Handler().post(new Runnable() {
                public final void run() {
                    imageCaptureManager.galleryAddPic();
                }
            });
            Intent intent = new Intent(getApplicationContext(), EditImageActivity.class);
            intent.putExtra(PhotoPicker.KEY_SELECTED_PHOTOS, imageCaptureManager.getCurrentPhotoPath());
            startActivity(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
            startActivityForResult(this.imageCaptureManager.dispatchTakePictureIntent(), REQUEST_IMAGE_CAPTURE);
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
                int itemId = menuItem.getItemId();
                if (itemId == R.id.action_feedback) {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.putExtra("android.intent.extra.EMAIL", new String[]{getResources().getString(R.string.email_feedback)});
                    intent.putExtra("android.intent.extra.SUBJECT", "MegaShot Feedback: ");
                    intent.putExtra("android.intent.extra.TEXT", "");
                    intent.setType("message/rfc822");
                    startActivity(Intent.createChooser(intent, getString(R.string.choose_email) + " :"));
                } else if (itemId == R.id.action_privacy_policy) {
                    try {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://sites.google.com")));
                    } catch (Exception e) {
                    }
                } else if (itemId == R.id.action_rate_us) {
                    new RateDialog(MainActivity.this, false).show();
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
                    intent2.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=" + getPackageName());
                    startActivity(Intent.createChooser(intent2, "Choose"));
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
