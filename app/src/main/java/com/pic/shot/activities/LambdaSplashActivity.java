package com.pic.shot.activities;

import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public final  class LambdaSplashActivity implements OnInitializationCompleteListener {
    public static final  LambdaSplashActivity INSTANCE = new LambdaSplashActivity();

    private LambdaSplashActivity() {
    }

    public final void onInitializationComplete(InitializationStatus initializationStatus) {
        SplashActivity.lambda$loadAds$1(initializationStatus);
    }
}
