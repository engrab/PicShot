package com.pic.editor.ads;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.pic.editor.R;

public class AdmobAds {
    private static InterstitialAd interstitialAd;
    public static OnAdsCloseListener mOnAdsCloseListener;

    public interface OnAdsCloseListener {
        void onAdsClose();
    }

    public static void initFullAds(Context context) {
        if (interstitialAd == null) {
            InterstitialAd interstitialAd2 = new InterstitialAd(context);
            interstitialAd = interstitialAd2;
            interstitialAd2.setAdUnitId(context.getString(R.string.admob_inter_id));
            interstitialAd.setAdListener(new AdListener() {
                public void onAdClosed() {
                    super.onAdClosed();
                    AdmobAds.loadFullAds();
                    if (AdmobAds.mOnAdsCloseListener != null) {
                        AdmobAds.mOnAdsCloseListener.onAdsClose();
                    }
                }

                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                }
            });
        }
        loadFullAds();
    }

    public static void loadFullAds() {
        if (interstitialAd != null) {
            interstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }

    public static boolean showFullAds(OnAdsCloseListener onAdsCloseListener) {
        mOnAdsCloseListener = onAdsCloseListener;
        if (interstitialAd == null || !interstitialAd.isLoaded()) {
            return false;
        }
        interstitialAd.show();
        return true;
    }

    public static void loadBanner(Activity activity) {
        AdView adView = new AdView(activity);
        adView.setAdUnitId(activity.getString(R.string.admob_banner_id));
        adView.setAdSize(AdSize.BANNER);
        ((FrameLayout) activity.findViewById(R.id.admob_banner)).addView(adView);
        adView.loadAd(new AdRequest.Builder().build());
    }

    public static void loadBanner(Activity activity, View view) {
        AdView adView = new AdView(activity);
        adView.setAdUnitId(activity.getString(R.string.admob_banner_id));
        adView.setAdSize(getAdSize(activity));
        ((FrameLayout) view.findViewById(R.id.admob_banner)).addView(adView);
        adView.loadAd(new AdRequest.Builder().build());
    }

    private static AdSize getAdSize(Activity activity) {
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, (int) (((float) displayMetrics.widthPixels) / displayMetrics.density));
    }

    public static void loadNativeAds(Activity activity, final View view) {
        final ViewGroup viewGroup = (ViewGroup) activity.findViewById(R.id.admob_native_container);
        final UnifiedNativeAdView unifiedNativeAdView = (UnifiedNativeAdView) activity.findViewById(R.id.native_ad_view);
        unifiedNativeAdView.setMediaView((MediaView) unifiedNativeAdView.findViewById(R.id.media_view));
        unifiedNativeAdView.setHeadlineView(unifiedNativeAdView.findViewById(R.id.primary));
        unifiedNativeAdView.setBodyView(unifiedNativeAdView.findViewById(R.id.secondary));
        unifiedNativeAdView.setCallToActionView(unifiedNativeAdView.findViewById(R.id.cta));
        unifiedNativeAdView.setIconView(unifiedNativeAdView.findViewById(R.id.icon));
        unifiedNativeAdView.setAdvertiserView(unifiedNativeAdView.findViewById(R.id.tertiary));
        new AdLoader.Builder((Context) activity, activity.getString(R.string.admob_native_id)).forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                AdmobAds.populateNativeAdView(unifiedNativeAd, unifiedNativeAdView);
                viewGroup.setVisibility(View.VISIBLE);
                ((View) viewGroup.getParent().getParent()).setVisibility(View.VISIBLE);
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }).withAdListener(new AdListener() {
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        }).build().loadAd(new AdRequest.Builder().build());
    }

    public static void loadNativeAds(Activity activity, View view, final View view2) {
        final ViewGroup viewGroup;
        final UnifiedNativeAdView unifiedNativeAdView;
        if (view != null) {
            viewGroup = (ViewGroup) view.findViewById(R.id.admob_native_container);
            unifiedNativeAdView = (UnifiedNativeAdView) view.findViewById(R.id.native_ad_view);
        } else {
            viewGroup = (ViewGroup) activity.findViewById(R.id.admob_native_container);
            unifiedNativeAdView = (UnifiedNativeAdView) activity.findViewById(R.id.native_ad_view);
        }
        MediaView mediaView = (MediaView) unifiedNativeAdView.findViewById(R.id.media_view);
        if (mediaView != null) {
            unifiedNativeAdView.setMediaView(mediaView);
        }
        unifiedNativeAdView.setHeadlineView(unifiedNativeAdView.findViewById(R.id.primary));
        unifiedNativeAdView.setBodyView(unifiedNativeAdView.findViewById(R.id.secondary));
        unifiedNativeAdView.setCallToActionView(unifiedNativeAdView.findViewById(R.id.cta));
        unifiedNativeAdView.setIconView(unifiedNativeAdView.findViewById(R.id.icon));
        unifiedNativeAdView.setAdvertiserView(unifiedNativeAdView.findViewById(R.id.tertiary));
        new AdLoader.Builder((Context) activity, activity.getString(R.string.admob_native_id)).forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                AdmobAds.populateNativeAdView(unifiedNativeAd, unifiedNativeAdView);
                viewGroup.setVisibility(View.VISIBLE);
                ((View) viewGroup.getParent().getParent()).setVisibility(View.VISIBLE);
                View view = view2;
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }).withAdListener(new AdListener() {
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        }).build().loadAd(new AdRequest.Builder().build());
    }

    public static void populateNativeAdView(UnifiedNativeAd unifiedNativeAd, UnifiedNativeAdView unifiedNativeAdView) {
        ((TextView) unifiedNativeAdView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
        ((TextView) unifiedNativeAdView.getBodyView()).setText(unifiedNativeAd.getBody());
        ((TextView) unifiedNativeAdView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
        NativeAd.Image icon = unifiedNativeAd.getIcon();
        if (icon == null) {
            unifiedNativeAdView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) unifiedNativeAdView.getIconView()).setImageDrawable(icon.getDrawable());
            unifiedNativeAdView.getIconView().setVisibility(View.VISIBLE);
        }
        if (unifiedNativeAd.getAdvertiser() == null) {
            unifiedNativeAdView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) unifiedNativeAdView.getAdvertiserView()).setText(unifiedNativeAd.getAdvertiser());
            unifiedNativeAdView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        unifiedNativeAdView.setNativeAd(unifiedNativeAd);
    }

    public static void hideNativeAds(Activity activity) {
        ((View) ((ViewGroup) activity.findViewById(R.id.admob_native_container)).getParent()).setVisibility(View.GONE);
    }
}
