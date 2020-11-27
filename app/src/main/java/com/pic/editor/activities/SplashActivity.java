package com.pic.editor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.pic.editor.R;
import com.pic.editor.ads.AdmobAds;
import com.pic.editor.constants.Constants;


public class SplashActivity extends AppCompatActivity {

    ImageView go;
    LinearLayout mHalfBlur;
    LinearLayout mFullBlur;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView((int) R.layout.activity_splash);

        init();

        halfBlurBackground();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                go.setVisibility(View.VISIBLE);
                fadeInGoButton();
            }
        }, 3000);


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fadeoutGoButton();
                go.setVisibility(View.GONE);
                mFullBlur.setVisibility(View.VISIBLE);
                fullBlurBackground();
                mHalfBlur.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    public final void run() {

                        startToMainActivity();
                        if (Constants.SHOW_ADS) {
                            AdmobAds.showFullAds((AdmobAds.OnAdsCloseListener) null);
                        }
                    }
                }, 2000);

            }
        });


        loadAds();

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

    private void halfBlurBackground() {
        Animation leftIn = AnimationUtils.loadAnimation(this, R.anim.left_in);
        mHalfBlur.setAnimation(leftIn);
    }

    private void fullBlurBackground() {
        Animation leftToRight = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.left_to_right);
        mFullBlur.setAnimation(leftToRight);
    }

    private void fadeoutGoButton() {
        Animation fadeOut = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fade_out);
        go.setAnimation(fadeOut);
    }

    private void fadeInGoButton() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        go.setAnimation(fadeIn);
    }

    private void init() {
        go = findViewById(R.id.iv_go);
        mHalfBlur = findViewById(R.id.ll_half_blur);
        mFullBlur = findViewById(R.id.ll_full_blur);
    }
}
