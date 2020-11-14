package com.pic.shot.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.pic.shot.R;
import com.pic.shot.ads.AdmobAds;
import com.pic.shot.constants.Constants;

public class SplashActivity extends AppCompatActivity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.backgroundContentColor));
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.activity_splash);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom_delay);
        findViewById(R.id.linear_layout_logo).startAnimation(animation);
        findViewById(R.id.linear_layout_title).startAnimation(animation1);
        loadAds();
        new Handler().postDelayed(new Runnable() {
            public final void run() {
                SplashActivity.this.lambda$onCreate$0$SplashActivity();
            }
        }, 2000);
    }

    public /* synthetic */ void lambda$onCreate$0$SplashActivity() {
        startToMainActivity();
        if (Constants.SHOW_ADS) {
            AdmobAds.showFullAds((AdmobAds.OnAdsCloseListener) null);
        }
    }

    private void loadAds() {
        MobileAds.initialize((Context) this, (OnInitializationCompleteListener) LambdaSplashActivity.INSTANCE);
        AdmobAds.initFullAds(this);
    }

    static /* synthetic */ void lambda$loadAds$1(InitializationStatus initializationStatus) {
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
