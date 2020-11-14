package com.pic.shot.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.pic.shot.R;
import com.pic.shot.adapters.SplashAdapter;
import com.pic.shot.filters.FilterUtils;
import com.pic.shot.splash.SplashView;
import com.pic.shot.sticker.SplashSticker;
import com.pic.shot.utils.AssetUtils;
import com.pic.shot.utils.SharePreferenceUtil;

public class SplashFragment extends DialogFragment implements SplashAdapter.SplashChangeListener {
    private static final String TAG = "SplashFragment";
    public Bitmap bitmap;
    private Bitmap blackAndWhiteBitmap;
    private Bitmap blurBitmap;
    private SplashSticker blurSticker;
    private ElegantNumberButton blur_number_button;
    private FrameLayout frame_layout;
    private ImageView image_view_background;
    public TextView image_view_draw;
    private ImageView image_view_redo;
    public TextView image_view_shape;
    private ImageView image_view_undo;
    public boolean isSplashView;
    public LinearLayout linear_layout_draw;
    public RecyclerView recycler_view_splash;
    private SeekBar seekbar_brush;
    public SplashDialogListener splashDialogListener;
    private SplashSticker splashSticker;
    public SplashView splashView;
    private ViewGroup viewGroup;

    public interface SplashDialogListener {
        void onSaveSplash(Bitmap bitmap);
    }

    public void setSplashView(boolean z) {
        this.isSplashView = z;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static SplashFragment show(AppCompatActivity appCompatActivity, Bitmap bitmap2, Bitmap bitmap3, Bitmap bitmap4, SplashDialogListener splashDialogListener2, boolean z) {
        SplashFragment splashDialog = new SplashFragment();
        splashDialog.setBlurBitmap(bitmap3);
        splashDialog.setBitmap(bitmap2);
        splashDialog.setBlackAndWhiteBitmap(bitmap4);
        splashDialog.setSplashDialogListener(splashDialogListener2);
        splashDialog.setSplashView(z);
        splashDialog.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return splashDialog;
    }

    public void setBlackAndWhiteBitmap(Bitmap bitmap2) {
        this.blackAndWhiteBitmap = bitmap2;
    }

    public void setBlurBitmap(Bitmap bitmap2) {
        this.blurBitmap = bitmap2;
    }

    public void setSplashDialogListener(SplashDialogListener splashDialogListener2) {
        this.splashDialogListener = splashDialogListener2;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup2, Bundle bundle) {
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.fragment_splash, viewGroup2, false);
        this.viewGroup = viewGroup2;
        this.image_view_background = (ImageView) inflate.findViewById(R.id.image_view_background);
        this.splashView = (SplashView) inflate.findViewById(R.id.splash_view);
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.linear_layout_draw);
        this.linear_layout_draw = linearLayout;
        linearLayout.setVisibility(View.GONE);
        this.frame_layout = (FrameLayout) inflate.findViewById(R.id.frame_layout);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.image_view_undo);
        this.image_view_undo = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SplashFragment.this.splashView.undo();
            }
        });
        ImageView imageView2 = (ImageView) inflate.findViewById(R.id.image_view_redo);
        this.image_view_redo = imageView2;
        imageView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SplashFragment.this.splashView.redo();
            }
        });
        SeekBar seekBar = (SeekBar) inflate.findViewById(R.id.seekbar_brush);
        this.seekbar_brush = seekBar;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                SplashFragment.this.splashView.setBrushBitmapSize(i + 25);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                SplashFragment.this.splashView.updateBrush();
            }
        });
        this.blur_number_button = (ElegantNumberButton) inflate.findViewById(R.id.blur_number_button);
        this.image_view_background.setImageBitmap(this.bitmap);
        this.image_view_shape = (TextView) inflate.findViewById(R.id.image_view_shape);
        this.image_view_draw = (TextView) inflate.findViewById(R.id.image_view_draw);
        if (this.isSplashView) {
            this.splashView.setImageBitmap(this.blackAndWhiteBitmap);
            this.blur_number_button.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.image_view_shape.getLayoutParams();
            layoutParams.horizontalBias = 0.3f;
            this.image_view_shape.setLayoutParams(layoutParams);
            ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) this.image_view_draw.getLayoutParams();
            layoutParams2.horizontalBias = 0.7f;
            this.image_view_draw.setLayoutParams(layoutParams2);
        } else {
            this.splashView.setImageBitmap(this.blurBitmap);
            this.blur_number_button.setRange(0, 10);
        }
        this.blur_number_button.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            public void onValueChange(ElegantNumberButton elegantNumberButton, int i, int i2) {
                new LoadBlurBitmap((float) i2).execute(new Void[0]);
            }
        });
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recycler_view_splash);
        this.recycler_view_splash = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        this.recycler_view_splash.setHasFixedSize(true);
        this.recycler_view_splash.setAdapter(new SplashAdapter(getContext(), this, this.isSplashView));
        if (this.isSplashView) {
            SplashSticker splashSticker2 = new SplashSticker(AssetUtils.loadBitmapFromAssets(getContext(), "splash/icons/1_mask.png"), AssetUtils.loadBitmapFromAssets(getContext(), "splash/icons/1_frame.png"));
            this.splashSticker = splashSticker2;
            this.splashView.addSticker(splashSticker2);
        } else {
            SplashSticker splashSticker3 = new SplashSticker(AssetUtils.loadBitmapFromAssets(getContext(), "blur/icons/1_mask.png"), AssetUtils.loadBitmapFromAssets(getContext(), "blur/icons/1_shadow.png"));
            this.blurSticker = splashSticker3;
            this.splashView.addSticker(splashSticker3);
        }
        this.splashView.refreshDrawableState();
        this.splashView.setLayerType(View.LAYER_TYPE_HARDWARE, (Paint) null);
        this.image_view_shape.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SplashFragment.this.image_view_draw.setBackgroundResource(0);
                SplashFragment.this.image_view_draw.setTextColor(ContextCompat.getColor(SplashFragment.this.getContext(), R.color.itemColor));
                SplashFragment.this.image_view_shape.setTextColor(ContextCompat.getColor(SplashFragment.this.getContext(), R.color.mainColor));
                SplashFragment.this.splashView.setCurrentSplashMode(0);
                SplashFragment.this.linear_layout_draw.setVisibility(View.GONE);
                SplashFragment.this.recycler_view_splash.setVisibility(View.VISIBLE);
                SplashFragment.this.splashView.refreshDrawableState();
                SplashFragment.this.splashView.invalidate();
                if (SplashFragment.this.isSplashView) {
                    if (SharePreferenceUtil.isFirstShapeSplash(SplashFragment.this.getContext())) {
                        SplashFragment.this.showShapeTutorial();
                    }
                } else if (SharePreferenceUtil.isFirstShapeBlur(SplashFragment.this.getContext())) {
                    SplashFragment.this.showShapeTutorial();
                }
            }
        });
        this.image_view_draw.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SplashFragment.this.splashView.refreshDrawableState();
                SplashFragment.this.splashView.setLayerType(View.LAYER_TYPE_SOFTWARE, (Paint) null);
                SplashFragment.this.image_view_shape.setBackgroundResource(0);
                SplashFragment.this.image_view_shape.setTextColor(ContextCompat.getColor(SplashFragment.this.getContext(), R.color.itemColor));
                SplashFragment.this.image_view_draw.setTextColor(ContextCompat.getColor(SplashFragment.this.getContext(), R.color.mainColor));
                SplashFragment.this.splashView.setCurrentSplashMode(1);
                SplashFragment.this.linear_layout_draw.setVisibility(View.VISIBLE);
                SplashFragment.this.recycler_view_splash.setVisibility(View.GONE);
                SplashFragment.this.splashView.invalidate();
                if (SplashFragment.this.isSplashView) {
                    if (SharePreferenceUtil.isFirstDrawSplash(SplashFragment.this.getContext())) {
                        SplashFragment.this.showDrawTutorial();
                    }
                } else if (SharePreferenceUtil.isFirstDrawBlur(SplashFragment.this.getContext())) {
                    SplashFragment.this.showDrawTutorial();
                }
            }
        });
        inflate.findViewById(R.id.image_view_save).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SplashFragment.this.splashDialogListener.onSaveSplash(SplashFragment.this.splashView.getBitmap(SplashFragment.this.bitmap));
                SplashFragment.this.dismiss();
            }
        });
        inflate.findViewById(R.id.image_view_close).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SplashFragment.this.dismiss();
            }
        });
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (SplashFragment.this.isSplashView) {
                    if (SharePreferenceUtil.isFirstShapeSplash(SplashFragment.this.getContext())) {
                        SplashFragment.this.showShapeTutorial();
                    }
                } else if (SharePreferenceUtil.isFirstShapeBlur(SplashFragment.this.getContext())) {
                    SplashFragment.this.showShapeTutorial();
                }
            }
        }, 1000);
        return inflate;
    }

    public void showDrawTutorial() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_draw_splash, this.viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(inflate);
        final AlertDialog create = builder.create();
        inflate.findViewById(R.id.text_view_done).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (SplashFragment.this.isSplashView) {
                    SharePreferenceUtil.setFirstDrawSplash(SplashFragment.this.getContext(), false);
                } else {
                    SharePreferenceUtil.setFirstDrawBlur(SplashFragment.this.getContext(), false);
                }
                create.dismiss();
            }
        });
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        create.show();
    }

    public void showShapeTutorial() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_zoom_splash, this.viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(inflate);
        final AlertDialog create = builder.create();
        inflate.findViewById(R.id.text_view_done).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (SplashFragment.this.isSplashView) {
                    SharePreferenceUtil.setFirstShapeSplash(SplashFragment.this.getContext(), false);
                } else {
                    SharePreferenceUtil.setFirstShapeBlur(SplashFragment.this.getContext(), false);
                }
                create.dismiss();
            }
        });
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        create.show();
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.splashView.getSticker().release();
        Bitmap bitmap2 = this.blurBitmap;
        if (bitmap2 != null) {
            bitmap2.recycle();
        }
        this.blurBitmap = null;
        Bitmap bitmap3 = this.blackAndWhiteBitmap;
        if (bitmap3 != null) {
            bitmap3.recycle();
        }
        this.blackAndWhiteBitmap = null;
        this.bitmap = null;
    }

    public void onSelected(SplashSticker splashSticker2) {
        this.splashView.addSticker(splashSticker2);
    }

    class LoadBlurBitmap extends AsyncTask<Void, Bitmap, Bitmap> {
        private float intensity;

        public LoadBlurBitmap(float f) {
            this.intensity = f;
        }

        public Bitmap doInBackground(Void... voidArr) {
            return FilterUtils.getBlurImageFromBitmap(SplashFragment.this.bitmap, this.intensity);
        }

        public void onPostExecute(Bitmap bitmap) {
            SplashFragment.this.splashView.setImageBitmap(bitmap);
        }
    }
}
