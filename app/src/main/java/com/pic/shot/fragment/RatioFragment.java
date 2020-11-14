package com.pic.shot.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.pic.shot.R;
import com.pic.shot.adapters.AspectRatioPreviewAdapter;
import com.pic.shot.adapters.ColorAdapter;
import com.pic.shot.adapters.RatioAdapter;
import com.pic.shot.clicklistener.BrushColorListener;
import com.pic.shot.filters.FilterUtils;
import com.pic.shot.utils.SystemUtil;
import com.steelkiwi.cropiwa.AspectRatio;

public class RatioFragment extends DialogFragment implements AspectRatioPreviewAdapter.OnNewSelectedListener, RatioAdapter.BackgroundInstaListener, BrushColorListener {
    private static final String TAG = "RatioFragment";
    private AspectRatio aspectRatio;
    private Bitmap bitmap;
    private Bitmap blurBitmap;
    public ConstraintLayout constraint_layout_padding;
    private ConstraintLayout constraint_layout_ratio;
    public FrameLayout frame_layout_wrapper;
    private ImageView image_view_blur;
    public ImageView image_view_ratio;
    public RatioSaveListener ratioSaveListener;
    public RecyclerView recycler_view_background;
    public RecyclerView recycler_view_ratio;
    public TextView text_view_background;
    public TextView text_view_border;
    public TextView text_view_ratio;

    public interface RatioSaveListener {
        void ratioSavedBitmap(Bitmap bitmap);
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static RatioFragment show(AppCompatActivity appCompatActivity, RatioSaveListener ratioSaveListener2, Bitmap mBitmap, Bitmap iBitmap) {
        RatioFragment ratioFragment = new RatioFragment();
        ratioFragment.setBitmap(mBitmap);
        ratioFragment.setBlurBitmap(iBitmap);
        ratioFragment.setRatioSaveListener(ratioSaveListener2);
        ratioFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return ratioFragment;
    }

    public void setBlurBitmap(Bitmap bitmap2) {
        this.blurBitmap = bitmap2;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public void setRatioSaveListener(RatioSaveListener iRatioSaveListener) {
        this.ratioSaveListener = iRatioSaveListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.fragment_ratio, viewGroup, false);
        AspectRatioPreviewAdapter aspectRatioPreviewAdapter = new AspectRatioPreviewAdapter(true);
        aspectRatioPreviewAdapter.setListener(this);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recycler_view_ratio);
        this.recycler_view_ratio = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.recycler_view_ratio.setAdapter(aspectRatioPreviewAdapter);
        this.aspectRatio = new AspectRatio(1, 1);
        RecyclerView recyclerView2 = (RecyclerView) inflate.findViewById(R.id.recycler_view_background);
        this.recycler_view_background = recyclerView2;
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.recycler_view_background.setAdapter(new RatioAdapter(getContext(), this));
        this.recycler_view_background.setVisibility(View.GONE);
        TextView textView = (TextView) inflate.findViewById(R.id.text_view_ratio);
        this.text_view_ratio = textView;
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RatioFragment.this.recycler_view_ratio.setVisibility(View.VISIBLE);
                RatioFragment.this.recycler_view_background.setVisibility(View.GONE);
                RatioFragment.this.constraint_layout_padding.setVisibility(View.GONE);
                RatioFragment.this.text_view_ratio.setTextColor(ContextCompat.getColor(RatioFragment.this.getContext(), R.color.mainColor));
                RatioFragment.this.text_view_background.setTextColor(ContextCompat.getColor(RatioFragment.this.getContext(), R.color.itemColor));
                RatioFragment.this.text_view_border.setTextColor(ContextCompat.getColor(RatioFragment.this.getContext(), R.color.itemColor));
                RatioFragment.this.text_view_background.setBackgroundResource(0);
                RatioFragment.this.text_view_border.setBackgroundResource(0);
            }
        });
        TextView textView2 = (TextView) inflate.findViewById(R.id.text_view_background);
        this.text_view_background = textView2;
        textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RatioFragment.this.recycler_view_ratio.setVisibility(View.GONE);
                RatioFragment.this.recycler_view_background.setVisibility(View.VISIBLE);
                RatioFragment.this.constraint_layout_padding.setVisibility(View.GONE);
                RatioFragment.this.text_view_background.setTextColor(ContextCompat.getColor(RatioFragment.this.getContext(), R.color.mainColor));
                RatioFragment.this.text_view_ratio.setTextColor(ContextCompat.getColor(RatioFragment.this.getContext(), R.color.itemColor));
                RatioFragment.this.text_view_border.setTextColor(ContextCompat.getColor(RatioFragment.this.getContext(), R.color.itemColor));
                RatioFragment.this.text_view_ratio.setBackgroundResource(0);
                RatioFragment.this.text_view_border.setBackgroundResource(0);
            }
        });
        ConstraintLayout constraintLayout = (ConstraintLayout) inflate.findViewById(R.id.constraint_layout_padding);
        this.constraint_layout_padding = constraintLayout;
        constraintLayout.setVisibility(View.GONE);
        TextView textView3 = (TextView) inflate.findViewById(R.id.text_view_border);
        this.text_view_border = textView3;
        textView3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RatioFragment.this.constraint_layout_padding.setVisibility(View.VISIBLE);
                RatioFragment.this.recycler_view_ratio.setVisibility(View.GONE);
                RatioFragment.this.recycler_view_background.setVisibility(View.GONE);
                RatioFragment.this.text_view_border.setTextColor(ContextCompat.getColor(RatioFragment.this.getContext(), R.color.mainColor));
                RatioFragment.this.text_view_background.setTextColor(ContextCompat.getColor(RatioFragment.this.getContext(), R.color.itemColor));
                RatioFragment.this.text_view_ratio.setTextColor(ContextCompat.getColor(RatioFragment.this.getContext(), R.color.itemColor));
                RatioFragment.this.text_view_background.setBackgroundResource(0);
                RatioFragment.this.text_view_ratio.setBackgroundResource(0);
            }
        });
        RecyclerView recyclerView3 = (RecyclerView) inflate.findViewById(R.id.recycler_vew_border);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setAdapter(new ColorAdapter(getContext(), this, true));
        ((SeekBar) inflate.findViewById(R.id.seekbar_padding)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int dpToPx = SystemUtil.dpToPx(RatioFragment.this.getContext(), i);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) RatioFragment.this.image_view_ratio.getLayoutParams();
                layoutParams.setMargins(dpToPx, dpToPx, dpToPx, dpToPx);
                RatioFragment.this.image_view_ratio.setLayoutParams(layoutParams);
            }
        });
        ImageView imageView = (ImageView) inflate.findViewById(R.id.image_view_ratio);
        this.image_view_ratio = imageView;
        imageView.setImageBitmap(this.bitmap);
        this.image_view_ratio.setAdjustViewBounds(true);
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        this.constraint_layout_ratio = (ConstraintLayout) inflate.findViewById(R.id.constraint_layout_ratio);
        ImageView imageView2 = (ImageView) inflate.findViewById(R.id.image_view_blur);
        this.image_view_blur = imageView2;
        imageView2.setImageBitmap(this.blurBitmap);
        FrameLayout frameLayout = (FrameLayout) inflate.findViewById(R.id.frame_layout_wrapper);
        this.frame_layout_wrapper = frameLayout;
        frameLayout.setLayoutParams(new ConstraintLayout.LayoutParams(point.x, point.x));
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraint_layout_ratio);
        ConstraintSet constraintSet2 = constraintSet;
        constraintSet2.connect(this.frame_layout_wrapper.getId(), 3, this.constraint_layout_ratio.getId(), 3, 0);
        ConstraintSet constraintSet3 = constraintSet2;
        constraintSet3.connect(this.frame_layout_wrapper.getId(), 1, this.constraint_layout_ratio.getId(), 1, 0);
        constraintSet3.connect(this.frame_layout_wrapper.getId(), 4, this.constraint_layout_ratio.getId(), 4, 0);
        constraintSet3.connect(this.frame_layout_wrapper.getId(), 2, this.constraint_layout_ratio.getId(), 2, 0);
        constraintSet.applyTo(this.constraint_layout_ratio);
        inflate.findViewById(R.id.image_view_close).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RatioFragment.this.dismiss();
            }
        });
        inflate.findViewById(R.id.image_view_save).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SaveInstaView saveInstaView = new SaveInstaView();
                RatioFragment ratioFragment = RatioFragment.this;
                saveInstaView.execute(new Bitmap[]{ratioFragment.getBitmapFromView(ratioFragment.frame_layout_wrapper)});
            }
        });
        return inflate;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
        }
    }

    public void onStop() {
        super.onStop();
    }

    private int[] calculateWidthAndHeight(AspectRatio aspectRatio2, Point point) {
        int height = this.constraint_layout_ratio.getHeight();
        if (aspectRatio2.getHeight() > aspectRatio2.getWidth()) {
            int mRatio = (int) (aspectRatio2.getRatio() * ((float) height));
            if (mRatio < point.x) {
                return new int[]{mRatio, height};
            }
            return new int[]{point.x, (int) (((float) point.x) / aspectRatio2.getRatio())};
        }
        int iRatio = (int) (((float) point.x) / aspectRatio2.getRatio());
        if (iRatio > height) {
            return new int[]{(int) (((float) height) * aspectRatio2.getRatio()), height};
        }
        return new int[]{point.x, iRatio};
    }

    private int[] calculateWidthAndHeightReal(AspectRatio aspectRatio2, Point point) {
        int height = this.bitmap.getHeight();
        if (aspectRatio2.getHeight() > aspectRatio2.getWidth()) {
            int ratio2 = (int) (aspectRatio2.getRatio() * ((float) height));
            if (ratio2 < point.x) {
                return new int[]{ratio2, height};
            }
            return new int[]{point.x, (int) (((float) point.x) / aspectRatio2.getRatio())};
        }
        int ratio3 = (int) (((float) point.x) / aspectRatio2.getRatio());
        if (ratio3 > height) {
            return new int[]{(int) (((float) height) * aspectRatio2.getRatio()), height};
        }
        return new int[]{point.x, ratio3};
    }

    public void onNewAspectRatioSelected(AspectRatio aspectRatio2) {
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        this.aspectRatio = aspectRatio2;
        int[] calculateWidthAndHeight = calculateWidthAndHeight(aspectRatio2, point);
        this.frame_layout_wrapper.setLayoutParams(new ConstraintLayout.LayoutParams(calculateWidthAndHeight[0], calculateWidthAndHeight[1]));
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraint_layout_ratio);
        ConstraintSet constraintSet2 = constraintSet;
        constraintSet2.connect(this.frame_layout_wrapper.getId(), 3, this.constraint_layout_ratio.getId(), 3, 0);
        ConstraintSet constraintSet3 = constraintSet2;
        constraintSet3.connect(this.frame_layout_wrapper.getId(), 1, this.constraint_layout_ratio.getId(), 1, 0);
        constraintSet3.connect(this.frame_layout_wrapper.getId(), 4, this.constraint_layout_ratio.getId(), 4, 0);
        constraintSet3.connect(this.frame_layout_wrapper.getId(), 2, this.constraint_layout_ratio.getId(), 2, 0);
        constraintSet.applyTo(this.constraint_layout_ratio);
    }

    class SaveInstaView extends AsyncTask<Bitmap, Bitmap, Bitmap> {
        SaveInstaView() {
        }

        public Bitmap doInBackground(Bitmap... bitmapArr) {
            Bitmap cloneBitmap = FilterUtils.cloneBitmap(bitmapArr[0]);
            bitmapArr[0].recycle();
            bitmapArr[0] = null;
            return cloneBitmap;
        }

        public void onPostExecute(Bitmap bitmap) {
            RatioFragment.this.ratioSaveListener.ratioSavedBitmap(bitmap);
            RatioFragment.this.dismiss();
        }
    }

    public void onBackgroundSelected(RatioAdapter.SquareView squareView) {
        if (squareView.isColor) {
            this.frame_layout_wrapper.setBackgroundColor(squareView.drawableId);
        } else if (squareView.text.equals("Blur")) {
            this.image_view_blur.setVisibility(View.VISIBLE);
        } else {
            this.frame_layout_wrapper.setBackgroundResource(squareView.drawableId);
            this.image_view_blur.setVisibility(View.GONE);
        }
        this.frame_layout_wrapper.invalidate();
    }

    public void onDestroyView() {
        super.onDestroyView();
        Bitmap bitmap2 = this.blurBitmap;
        if (bitmap2 != null) {
            bitmap2.recycle();
            this.blurBitmap = null;
        }
        this.bitmap = null;
    }

    public void onColorChanged(String str) {
        this.image_view_ratio.setBackgroundColor(Color.parseColor(str));
        if (!str.equals("#00000000")) {
            int dpToPx = SystemUtil.dpToPx(getContext(), 3);
            this.image_view_ratio.setPadding(dpToPx, dpToPx, dpToPx, dpToPx);
            return;
        }
        this.image_view_ratio.setPadding(0, 0, 0, 0);
    }

    public Bitmap getBitmapFromView(View view) {
        Bitmap bitmap2 = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap2));
        return bitmap2;
    }
}
