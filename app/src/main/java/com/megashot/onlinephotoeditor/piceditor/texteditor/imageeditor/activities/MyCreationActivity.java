package com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.R;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.adapters.MyCreationAdapter;

import java.io.File;
import java.util.ArrayList;

public class MyCreationActivity extends AppCompatActivity {

    public static ArrayList<String> IMAGEALLARY = new ArrayList();
    public static int pos;
    private ImageView Iv_back_creation;
    private GridView grid_crea;
    MyCreationAdapter myCreationAdapter;
    AdView mAdView;
    private static final String TAG = "MyCreationActivity";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_creation);

            mAdView = findViewById(R.id.adView);
            mAdView.loadAd(new AdRequest.Builder().build());
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mAdView.setVisibility(View.VISIBLE);
                }
            });

        if (MyCreationAdapter.imagegallary.size() == 0) {
            findViewById(R.id.text_noimage).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.text_noimage).setVisibility(View.GONE);
        }
        this.grid_crea = findViewById(R.id.grid_crea);
        this.myCreationAdapter = new MyCreationAdapter(this, IMAGEALLARY);
        IMAGEALLARY.clear();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory().getPath());
        stringBuilder.append("/");
        stringBuilder.append(R.string.app_name);
        listAllImages(new File(stringBuilder.toString()));
        this.grid_crea.setAdapter(this.myCreationAdapter);
        this.Iv_back_creation = findViewById(R.id.back_click_iv);
        this.Iv_back_creation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCreationActivity.this.finish();

            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void listAllImages(File filepath) {
        File[] files = filepath.listFiles();
        if (MyCreationAdapter.imagegallary.size() == 0) {
            findViewById(R.id.text_noimage).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.text_noimage).setVisibility(View.GONE);
        }
        if (files != null) {
            for (int j = files.length - 1; j >= 0; j--) {
                String ss = files[j].toString();
                File check = new File(ss);
                StringBuilder stringBuilder = new StringBuilder();
                String str = "";
                stringBuilder.append(str);
                stringBuilder.append(check.length());
                String stringBuilder2 = stringBuilder.toString();
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(str);
                stringBuilder3.append(check.length());
                Log.d(stringBuilder2, stringBuilder3.toString());
                if (check.toString().contains(".jpg") || check.toString().contains(".png") || check.toString().contains(".jpeg")) {
                    IMAGEALLARY.add(ss);
                }
                else {
                    Log.d(TAG, "listAllImages: Image Extension not supported");
                }
                System.out.println(ss);
            }
            return;
        }
        System.out.println("Empty Folder");
    }

    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        if (MyCreationAdapter.imagegallary.size() == 0) {
            findViewById(R.id.text_noimage).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.text_noimage).setVisibility(View.GONE);
        }
    }


    @Override
    protected void onPause() {

        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}