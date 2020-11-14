package com.pic.shot.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import com.bumptech.glide.Glide;
import com.pic.shot.R;
import com.pic.shot.ads.AdmobAds;
import com.pic.shot.constants.Constants;
import com.pic.shot.dialog.RateDialog;
import com.pic.shot.utils.SharePreferenceUtil;
import java.io.File;

public class ShareActivity extends BaseActivity implements View.OnClickListener {
    static final boolean $assertionsDisabled = false;
    private File file;

    private void loadAd() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        makeFullScreen();
        setContentView((int) R.layout.activity_share);
        this.file = new File(getIntent().getExtras().getString("path"));
        Glide.with(getApplicationContext()).load(this.file).into((ImageView) findViewById(R.id.image_view_preview));
        findViewById(R.id.image_view_preview).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ShareActivity.this.onClick(view);
            }
        });
        findViewById(R.id.image_view_back).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ShareActivity.this.onClick(view);
            }
        });
        findViewById(R.id.image_view_home).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ShareActivity.this.onClick(view);
            }
        });
        findViewById(R.id.image_view_Wallpaper).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ShareActivity.this.onClick(view);
            }
        });
        findViewById(R.id.image_view_share_more).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ShareActivity.this.onClick(view);
            }
        });
        findViewById(R.id.image_view_insta).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ShareActivity.this.onClick(view);
            }
        });
        findViewById(R.id.image_view_facebook).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ShareActivity.this.onClick(view);
            }
        });
        findViewById(R.id.image_view_whatsapp).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ShareActivity.this.onClick(view);
            }
        });
        findViewById(R.id.image_view_twitter).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ShareActivity.this.onClick(view);
            }
        });
        findViewById(R.id.image_view_gmail).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ShareActivity.this.onClick(view);
            }
        });
        findViewById(R.id.image_view_messenger).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ShareActivity.this.onClick(view);
            }
        });
        if (!SharePreferenceUtil.isPurchased(getApplicationContext())) {
            loadAd();
        }
        findViewById(R.id.linear_layout_shares).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ShareActivity.lamb(ShareActivity.this, view);
            }
        });
        if (!SharePreferenceUtil.isRated(this)) {
            new RateDialog(this, false).show();
        }
        if (Constants.SHOW_ADS) {
            AdmobAds.showFullAds((AdmobAds.OnAdsCloseListener) null);
            View view = null;
            AdmobAds.loadNativeAds(this, view, view);
            return;
        }
        findViewById(R.id.adsContainer).setVisibility(View.GONE);
    }

    public static void lamb(ShareActivity saveAndShareActivity, View view) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(saveAndShareActivity.getApplicationContext(), "com.devchie.photoeditor.provider", saveAndShareActivity.file));
        intent.addFlags(3);
        saveAndShareActivity.startActivity(Intent.createChooser(intent, "Share"));
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        finish();
        return true;
    }

    public void onResume() {
        super.onResume();
    }

    public void onClick(View view) {
        if (view != null) {
            int id = view.getId();
            if (id == R.id.image_view_back) {
                super.onBackPressed();
            } else if (id != R.id.image_view_preview) {
                switch (id) {
                    case R.id.image_view_facebook:
                        sharePhoto(Constants.FACE);
                        return;
                    case R.id.image_view_gmail:
                        sharePhoto(Constants.GMAIL);
                        return;
                    case R.id.image_view_insta:
                        sharePhoto(Constants.INSTA);
                        return;
                    case R.id.image_view_messenger:
                        sharePhoto(Constants.MESSEGER);
                        return;
                    case R.id.image_view_share_more:
                        Uri createCacheFile = createCacheFile();
                        if (createCacheFile != null) {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.SEND");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.setDataAndType(createCacheFile, getContentResolver().getType(createCacheFile));
                            intent.putExtra("android.intent.extra.STREAM", createCacheFile);
                            startActivity(Intent.createChooser(intent, "Choose an app"));
                            return;
                        }
                        Toast.makeText(this, "Fail to sharing", Toast.LENGTH_SHORT).show();
                        return;
                    default:
                        switch (id) {
                            case R.id.image_view_Wallpaper:
                                Uri createCacheFile2 = createCacheFile();
                                if (createCacheFile2 != null) {
                                    Intent intent2 = new Intent("android.intent.action.ATTACH_DATA");
                                    intent2.setDataAndType(createCacheFile2, getContentResolver().getType(createCacheFile2));
                                    intent2.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    startActivity(Intent.createChooser(intent2, "Use as"));
                                    return;
                                }
                                Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
                                return;
                            case R.id.image_view_home:
                                Intent intent3 = new Intent(this, MainActivity.class);
                                intent3.setFlags(67108864);
                                startActivity(intent3);
                                AdmobAds.showFullAds((AdmobAds.OnAdsCloseListener) null);
                                return;
                            case R.id.image_view_twitter:
                                sharePhoto(Constants.TWITTER);
                                return;
                            case R.id.image_view_whatsapp:
                                sharePhoto(Constants.WHATSAPP);
                                return;
                            default:
                                return;
                        }
                }
            } else {
                Intent intent4 = new Intent();
                intent4.setAction("android.intent.action.VIEW");
                intent4.setDataAndType(FileProvider.getUriForFile(getApplicationContext(), "com.devchie.photoeditor.provider", this.file), "image/*");
                intent4.addFlags(3);
                startActivity(intent4);
            }
        }
    }

    public void sharePhoto(String str) {
        if (isPackageInstalled(this, str)) {
            Uri createCacheFile = createCacheFile();
            if (createCacheFile != null) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(createCacheFile, getContentResolver().getType(createCacheFile));
                intent.putExtra("android.intent.extra.STREAM", createCacheFile);
                intent.setPackage(str);
                startActivity(intent);
                return;
            }
            Toast.makeText(this, "Fail to sharing", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Can't find this App, please download and try it again", Toast.LENGTH_SHORT).show();
        Intent intent2 = new Intent("android.intent.action.VIEW");
        intent2.setData(Uri.parse("market://details?id=" + str));
        startActivity(intent2);
    }

    public static boolean isPackageInstalled(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, 128);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private Uri createCacheFile() {
        return FileProvider.getUriForFile(getApplicationContext(), "com.devchie.photoeditor.provider", this.file);
    }
}
