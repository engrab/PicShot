package com.pic.shot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.pic.shot.R;
import com.pic.shot.constants.Constants;
import com.pic.shot.sticker.SplashSticker;
import com.pic.shot.utils.AssetUtils;
import com.pic.shot.utils.SystemUtil;
import java.util.ArrayList;
import java.util.List;

public class SplashAdapter extends RecyclerView.Adapter<SplashAdapter.ViewHolder> {
    private int borderWidth;
    private Context context;
    public int selectedSquareIndex;
    public SplashChangeListener splashChangeListener;
    public List<SplashItem> splashList = new ArrayList();

    public interface SplashChangeListener {
        void onSelected(SplashSticker splashSticker);
    }

    public SplashAdapter(Context context2, SplashChangeListener splashChangeListener2, boolean z) {
        this.context = context2;
        this.splashChangeListener = splashChangeListener2;
        this.borderWidth = SystemUtil.dpToPx(context2, Constants.BORDER_WIDTH_DP);
        if (z) {
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/1_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/1_frame.png")), R.drawable.splash_1));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/2_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/2_frame.png")), R.drawable.splash_2));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/3_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/3_frame.png")), R.drawable.splash_3));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/4_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/4_frame.png")), R.drawable.splash_4));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/5_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/5_frame.png")), R.drawable.splash_5));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/6_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/6_frame.png")), R.drawable.splash_6));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/7_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/7_frame.png")), R.drawable.splash_7));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/8_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/8_frame.png")), R.drawable.splash_8));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/9_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/9_frame.png")), R.drawable.splash_9));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/10_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/10_frame.png")), R.drawable.splash_10));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/11_mask.webp"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/11_frame.webp")), R.drawable.splash_11));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/12_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/12_frame.png")), R.drawable.splash_12));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/13_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/13_frame.png")), R.drawable.splash_13));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/14_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/14_frame.png")), R.drawable.splash_14));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/15_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/15_frame.png")), R.drawable.splash_15));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/16_mask.webp"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/16_frame.webp")), R.drawable.splash_16));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/17_mask.webp"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/17_frame.webp")), R.drawable.splash_17));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/18_mask.webp"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/18_frame.webp")), R.drawable.splash_18));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/19_mask.webp"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/19_frame.webp")), R.drawable.splash_19));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/20_mask.webp"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/20_frame.webp")), R.drawable.splash_20));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/21_mask.webp"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/21_frame.webp")), R.drawable.splash_21));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/22_mask.webp"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/22_frame.webp")), R.drawable.splash_22));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/23_mask.webp"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/23_frame.webp")), R.drawable.splash_23));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/24_mask.webp"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/24_frame.webp")), R.drawable.splash_24));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/25_mask.webp"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/25_frame.webp")), R.drawable.splash_25));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/26_mask.webp"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/26_frame.webp")), R.drawable.splash_26));
            this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "splash/icons/27_mask.webp"), AssetUtils.loadBitmapFromAssets(context2, "splash/icons/27_frame.webp")), R.drawable.splash_27));
            return;
        }
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/icons/1_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "blur/icons/1_shadow.png")), R.drawable.blur_1));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/icons/2_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "blur/icons/2_shadow.png")), R.drawable.blur_2));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/icons/3_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "blur/icons/3_shadow.png")), R.drawable.blur_3));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/icons/4_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "blur/icons/4_shadow.png")), R.drawable.blur_4));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/icons/5_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "blur/icons/5_shadow.png")), R.drawable.blur_5));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/icons/6_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "blur/icons/6_shadow.png")), R.drawable.blur_6));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/icons/7_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "blur/icons/7_shadow.png")), R.drawable.blur_7));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/icons/8_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "blur/icons/8_shadow.png")), R.drawable.blur_8));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/icons/9_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "blur/icons/9_shadow.png")), R.drawable.blur_9));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/icons/10_mask.png"), AssetUtils.loadBitmapFromAssets(context2, "blur/icons/10_shadow.png")), R.drawable.blur_10));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/icons/11_mask.webp"), AssetUtils.loadBitmapFromAssets(context2, "blur/icons/11_shadow.webp")), R.drawable.blur_11));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/icons/12_mask.webp"), AssetUtils.loadBitmapFromAssets(context2, "blur/icons/12_shadow.webp")), R.drawable.blur_12));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/icons/13_mask.webp"), AssetUtils.loadBitmapFromAssets(context2, "blur/icons/13_shadow.webp")), R.drawable.blur_13));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/icons/14_mask.webp"), AssetUtils.loadBitmapFromAssets(context2, "blur/icons/14_shadow.webp")), R.drawable.blur_14));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/icons/15_mask.webp"), AssetUtils.loadBitmapFromAssets(context2, "blur/icons/15_shadow.webp")), R.drawable.blur_15));
        this.splashList.add(new SplashItem(new SplashSticker(AssetUtils.loadBitmapFromAssets(context2, "blur/icons/16_mask.webp"), AssetUtils.loadBitmapFromAssets(context2, "blur/icons/16_shadow.webp")), R.drawable.blur_16));
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_splash_view, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.splash.setImageResource(this.splashList.get(i).drawableId);
        if (this.selectedSquareIndex == i) {
            viewHolder.splash.setBorderColor(ContextCompat.getColor(this.context, R.color.colorAccent));
            viewHolder.splash.setBorderWidth(this.borderWidth);
            return;
        }
        viewHolder.splash.setBorderColor(0);
        viewHolder.splash.setBorderWidth(this.borderWidth);
    }

    public int getItemCount() {
        return this.splashList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RoundedImageView splash;

        public ViewHolder(View view) {
            super(view);
            this.splash = (RoundedImageView) view.findViewById(R.id.round_image_view_splash_item);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            SplashAdapter.this.selectedSquareIndex = getAdapterPosition();
            if (SplashAdapter.this.selectedSquareIndex < 0) {
                SplashAdapter.this.selectedSquareIndex = 0;
            }
            if (SplashAdapter.this.selectedSquareIndex >= SplashAdapter.this.splashList.size()) {
                SplashAdapter splashAdapter = SplashAdapter.this;
                splashAdapter.selectedSquareIndex = splashAdapter.splashList.size() - 1;
            }
            SplashAdapter.this.splashChangeListener.onSelected(SplashAdapter.this.splashList.get(SplashAdapter.this.selectedSquareIndex).splashSticker);
            SplashAdapter.this.notifyDataSetChanged();
        }
    }

    class SplashItem {
        int drawableId;
        SplashSticker splashSticker;

        SplashItem(SplashSticker splashSticker2, int i) {
            this.splashSticker = splashSticker2;
            this.drawableId = i;
        }
    }
}
