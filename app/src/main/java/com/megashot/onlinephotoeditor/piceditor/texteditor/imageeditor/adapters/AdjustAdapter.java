package com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.R;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.clicklistener.AdjustListener;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.photoeditor.PhotoEditor;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class AdjustAdapter extends RecyclerView.Adapter<AdjustAdapter.ViewHolder> {
    public String FILTER_CONFIG_TEMPLATE = "@adjust brightness 0 @adjust contrast 1 @adjust saturation 1 @adjust sharpen 0";
    public AdjustListener adjustListener;
    public List<AdjustModel> adjustModelList;
    private Context context;
    public int selectedFilterIndex = 0;

    public class AdjustModel {
        String code;
        Drawable icon;
        public int index;
        public float intensity;
        public float maxValue;
        public float minValue;
        public String name;
        public float originValue;
        public float seekbarIntensity = 0.5f;
        Drawable selectedIcon;

        AdjustModel(int index2, String name2, String code2, Drawable icon2, Drawable selectedIcon2, float minValue2, float originValue2, float maxValue2) {
            this.index = index2;
            this.name = name2;
            this.code = code2;
            this.icon = icon2;
            this.minValue = minValue2;
            this.originValue = originValue2;
            this.maxValue = maxValue2;
            this.selectedIcon = selectedIcon2;
        }

        public void setSeekBarIntensity(PhotoEditor photoEditor, float mFloat, boolean mBoolean) {
            if (photoEditor != null) {
                this.seekbarIntensity = mFloat;
                float calcIntensity = calcIntensity(mFloat);
                this.intensity = calcIntensity;
                photoEditor.setFilterIntensityForIndex(calcIntensity, this.index, mBoolean);
            }
        }

        public float calcIntensity(float f) {
            if (f <= 0.0f) {
                return this.minValue;
            }
            if (f >= 1.0f) {
                return this.maxValue;
            }
            if (f <= 0.5f) {
                float f2 = this.minValue;
                return f2 + ((this.originValue - f2) * f * 2.0f);
            }
            float f3 = this.maxValue;
            return f3 + ((this.originValue - f3) * (1.0f - f) * 2.0f);
        }
    }

    public AdjustAdapter(Context mContext, AdjustListener mAdjustListener) {
        this.context = mContext;
        this.adjustListener = mAdjustListener;
        init();
    }

    public void setSelectedAdjust(int i) {
        this.adjustListener.onAdjustSelected(this.adjustModelList.get(i));
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_adjust, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.text_view_adjust_name.setText(this.adjustModelList.get(i).name);
        viewHolder.image_view_adjust_icon.setImageDrawable(this.selectedFilterIndex != i ? this.adjustModelList.get(i).icon : this.adjustModelList.get(i).selectedIcon);
        if (this.selectedFilterIndex == i) {
            viewHolder.text_view_adjust_name.setTextColor(ContextCompat.getColor(this.context, R.color.mainColor));
            viewHolder.image_view_adjust_icon.setColorFilter(ContextCompat.getColor(this.context, R.color.mainColor));
            return;
        }
        viewHolder.text_view_adjust_name.setTextColor(ContextCompat.getColor(this.context, R.color.itemColor));
        viewHolder.image_view_adjust_icon.setColorFilter(ContextCompat.getColor(this.context, R.color.itemColor));
    }

    public int getItemCount() {
        return this.adjustModelList.size();
    }

    public String getFilterConfig() {
        String str = this.FILTER_CONFIG_TEMPLATE;
        return MessageFormat.format(str, new Object[]{this.adjustModelList.get(0).originValue + "", this.adjustModelList.get(1).originValue + "", this.adjustModelList.get(2).originValue + "", Float.valueOf(this.adjustModelList.get(3).originValue)});
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_view_adjust_icon;
        TextView text_view_adjust_name;

        ViewHolder(View view) {
            super(view);
            this.image_view_adjust_icon = (ImageView) view.findViewById(R.id.image_view_adjust_icon);
            this.text_view_adjust_name = (TextView) view.findViewById(R.id.text_view_adjust_name);
            view.setOnClickListener(view1 -> {
                AdjustAdapter.this.selectedFilterIndex = ViewHolder.this.getLayoutPosition();
                AdjustAdapter.this.adjustListener.onAdjustSelected(AdjustAdapter.this.adjustModelList.get(AdjustAdapter.this.selectedFilterIndex));
                AdjustAdapter.this.notifyDataSetChanged();
            });
        }
    }

    public AdjustModel getCurrentAdjustModel() {
        return this.adjustModelList.get(this.selectedFilterIndex);
    }

    private void init() {
        ArrayList arrayList = new ArrayList();
        this.adjustModelList = arrayList;
        arrayList.add(new AdjustModel(0, this.context.getString(R.string.brightness), "brightness", this.context.getDrawable(R.drawable.brightness), this.context.getDrawable(R.drawable.brightness_selected), -1.0f, 0.0f, 1.0f));
        this.adjustModelList.add(new AdjustModel(1, this.context.getString(R.string.contrast), "contrast", this.context.getDrawable(R.drawable.contrast), this.context.getDrawable(R.drawable.contrast_selected), 0.1f, 1.0f, 3.0f));
        this.adjustModelList.add(new AdjustModel(2, this.context.getString(R.string.saturation), "saturation", this.context.getDrawable(R.drawable.saturation), this.context.getDrawable(R.drawable.saturation_selected), 0.0f, 1.0f, 3.0f));
        this.adjustModelList.add(new AdjustModel(3, this.context.getString(R.string.sharpen), "sharpen", this.context.getDrawable(R.drawable.sharpen), this.context.getDrawable(R.drawable.sharpen_selected), -1.0f, 0.0f, 10.0f));
    }
}
