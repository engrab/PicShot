package com.pic.editor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.pic.editor.R;
import com.pic.editor.ads.AdmobAds;
import com.pic.editor.constants.Constants;

public class SplashActivity extends AppCompatActivity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView((int) R.layout.activity_splash);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom_delay);
        findViewById(R.id.linear_layout_logo).startAnimation(animation);
        findViewById(R.id.linear_layout_title).startAnimation(animation1);
        loadAds();
        new Handler().postDelayed(new Runnable() {
            public final void run() {

                startToMainActivity();
                if (Constants.SHOW_ADS) {
                    AdmobAds.showFullAds((AdmobAds.OnAdsCloseListener) null);
                }
            }
        }, 2000);
    }


    private void loadAds() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdmobAds.initFullAds(this);
    }


    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

    public void startToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
