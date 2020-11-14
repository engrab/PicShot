package com.pic.shot.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.pic.shot.R;
import com.pic.shot.adapters.ColorAdapter;
import com.pic.shot.clicklistener.BrushColorListener;

public class ColorSplashFragment extends DialogFragment implements BrushColorListener {
    private Bitmap bitmap;
    private ImageView image_view_preview;
    private RecyclerView recycler_view_color_brush;

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public static ColorSplashFragment show(AppCompatActivity appCompatActivity, Bitmap bitmap2) {
        ColorSplashFragment colorSplashDialog = new ColorSplashFragment();
        colorSplashDialog.setBitmap(bitmap2);
        colorSplashDialog.show(appCompatActivity.getSupportFragmentManager(), "ColorSplashDialog");
        return colorSplashDialog;
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.layout_splash, viewGroup, false);
        this.image_view_preview = (ImageView) inflate.findViewById(R.id.image_view_preview);
        this.recycler_view_color_brush = (RecyclerView) inflate.findViewById(R.id.recycler_view_color_brush);
        this.image_view_preview.setImageBitmap(this.bitmap);
        this.recycler_view_color_brush.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.recycler_view_color_brush.setHasFixedSize(true);
        this.recycler_view_color_brush.setAdapter(new ColorAdapter(getContext(), this));
        return inflate;
    }

    public void onColorChanged(String str) {
        Bitmap createBitmap = Bitmap.createBitmap(this.bitmap.getWidth(), this.bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(this.bitmap, 0.0f, 0.0f, paint);
        this.image_view_preview.setImageBitmap(createBitmap);
    }
}
