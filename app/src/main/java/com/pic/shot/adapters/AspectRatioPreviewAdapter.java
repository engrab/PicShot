package com.pic.shot.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.pic.shot.R;
import com.pic.shot.custom.AspectRatioCustom;
import com.steelkiwi.cropiwa.AspectRatio;
import java.util.Arrays;
import java.util.List;

public class AspectRatioPreviewAdapter extends RecyclerView.Adapter<AspectRatioPreviewAdapter.ViewHolder> {
    public int lastSelectedView;
    public OnNewSelectedListener listener;
    public List<AspectRatioCustom> ratios;
    public AspectRatioCustom selectedRatio;

    public interface OnNewSelectedListener {
        void onNewAspectRatioSelected(AspectRatio aspectRatio);
    }

    public AspectRatioPreviewAdapter() {
        List<AspectRatioCustom> asList = Arrays.asList(new AspectRatioCustom[]{new AspectRatioCustom(10, 10, R.drawable.crop_free, R.drawable.crop_free_click), new AspectRatioCustom(1, 1, R.drawable.ratio_1_1, R.drawable.ratio_1_1_click), new AspectRatioCustom(4, 3, R.drawable.ratio_4_3, R.drawable.ratio_4_3_click), new AspectRatioCustom(3, 4, R.drawable.ratio_3_4, R.drawable.ratio_3_4_click), new AspectRatioCustom(5, 4, R.drawable.ratio_5_4, R.drawable.ratio_5_4_click), new AspectRatioCustom(4, 5, R.drawable.ratio_4_5, R.drawable.ratio_4_5_click), new AspectRatioCustom(3, 2, R.drawable.ratio_3_2, R.drawable.ratio_3_2_click), new AspectRatioCustom(2, 3, R.drawable.ratio_2_3, R.drawable.ratio_2_3_click), new AspectRatioCustom(9, 16, R.drawable.ratio_9_16, R.drawable.ratio_9_16_click), new AspectRatioCustom(16, 9, R.drawable.ratio_16_9, R.drawable.ratio_16_9_click)});
        this.ratios = asList;
        this.selectedRatio = asList.get(0);
    }

    public AspectRatioPreviewAdapter(boolean z) {
        List<AspectRatioCustom> asList = Arrays.asList(new AspectRatioCustom[]{new AspectRatioCustom(1, 1, R.drawable.ratio_1_1, R.drawable.ratio_1_1_click), new AspectRatioCustom(4, 3, R.drawable.ratio_4_3, R.drawable.ratio_4_3_click), new AspectRatioCustom(3, 4, R.drawable.ratio_3_4, R.drawable.ratio_3_4_click), new AspectRatioCustom(5, 4, R.drawable.ratio_5_4, R.drawable.ratio_5_4_click), new AspectRatioCustom(4, 5, R.drawable.ratio_4_5, R.drawable.ratio_4_5_click), new AspectRatioCustom(3, 2, R.drawable.ratio_3_2, R.drawable.ratio_3_2_click), new AspectRatioCustom(2, 3, R.drawable.ratio_2_3, R.drawable.ratio_2_3_click), new AspectRatioCustom(9, 16, R.drawable.ratio_9_16, R.drawable.ratio_9_16_click), new AspectRatioCustom(16, 9, R.drawable.ratio_16_9, R.drawable.ratio_16_9_click)});
        this.ratios = asList;
        this.selectedRatio = asList.get(0);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_aspect_ratio, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        AspectRatioCustom aspectRatioCustom = this.ratios.get(i);
        if (i == this.lastSelectedView) {
            viewHolder.ratioView.setImageResource(aspectRatioCustom.getSelectedIem());
        } else {
            viewHolder.ratioView.setImageResource(aspectRatioCustom.getUnselectItem());
        }
    }

    public void setLastSelectedView(int i) {
        this.lastSelectedView = i;
    }

    public int getItemCount() {
        return this.ratios.size();
    }

    public void setListener(OnNewSelectedListener onNewSelectedListener) {
        this.listener = onNewSelectedListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ratioView;

        public ViewHolder(View view) {
            super(view);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_view_aspect_ratio);
            this.ratioView = imageView;
            imageView.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (AspectRatioPreviewAdapter.this.lastSelectedView != getAdapterPosition()) {
                AspectRatioPreviewAdapter aspectRatioPreviewAdapter = AspectRatioPreviewAdapter.this;
                aspectRatioPreviewAdapter.selectedRatio = aspectRatioPreviewAdapter.ratios.get(getAdapterPosition());
                AspectRatioPreviewAdapter.this.lastSelectedView = getAdapterPosition();
                if (AspectRatioPreviewAdapter.this.listener != null) {
                    AspectRatioPreviewAdapter.this.listener.onNewAspectRatioSelected(AspectRatioPreviewAdapter.this.selectedRatio);
                }
                AspectRatioPreviewAdapter.this.notifyDataSetChanged();
            }
        }
    }
}
