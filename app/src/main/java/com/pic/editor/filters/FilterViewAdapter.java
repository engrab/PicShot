package com.pic.editor.filters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.pic.editor.R;
import com.pic.editor.constants.Constants;
import com.pic.editor.utils.SystemUtil;
import java.util.List;

public class FilterViewAdapter extends RecyclerView.Adapter<FilterViewAdapter.ViewHolder> {
    private List<Bitmap> bitmaps;
    private int borderWidth;
    private Context context;
    public List<FilterUtils.FilterBean> filterBeanList;
    public FilterListener filterListener;
    public int selectedFilterIndex = 0;

    public FilterViewAdapter(List<Bitmap> bitmapList, FilterListener filterListener2, Context mContext, List<FilterUtils.FilterBean> mfilterBeanList) {
        this.filterListener = filterListener2;
        this.bitmaps = bitmapList;
        this.context = mContext;
        this.filterBeanList = mfilterBeanList;
        this.borderWidth = SystemUtil.dpToPx(mContext, Constants.BORDER_WIDTH_DP);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_filter, viewGroup, false));
    }

    public void reset() {
        this.selectedFilterIndex = 0;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.text_view_filter_name.setText(this.filterBeanList.get(i).getName());
        viewHolder.text_view_filter_name.setTextColor(ContextCompat.getColor(this.context, R.color.mainColor));
        viewHolder.round_image_view_filter_item.setImageBitmap(this.bitmaps.get(i));
        viewHolder.round_image_view_filter_item.setBorderColor(ContextCompat.getColor(this.context, R.color.mainColor));
        if (this.selectedFilterIndex == i) {
            viewHolder.round_image_view_filter_item.setBorderColor(ContextCompat.getColor(this.context, R.color.mainColor));
            viewHolder.text_view_filter_name.setTextColor(ContextCompat.getColor(this.context, R.color.mainColor));
            viewHolder.round_image_view_filter_item.setBorderWidth(this.borderWidth);
            return;
        }
        viewHolder.round_image_view_filter_item.setBorderColor(0);
        viewHolder.text_view_filter_name.setTextColor(ContextCompat.getColor(this.context, R.color.itemColor));
        viewHolder.round_image_view_filter_item.setBorderWidth(this.borderWidth);
    }

    public int getItemCount() {
        return this.bitmaps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relative_layout_wrapper_filter_item;
        RoundedImageView round_image_view_filter_item;
        TextView text_view_filter_name;

        ViewHolder(View view) {
            super(view);
            this.round_image_view_filter_item = (RoundedImageView) view.findViewById(R.id.round_image_view_filter_item);
            this.text_view_filter_name = (TextView) view.findViewById(R.id.text_view_filter_name);
            this.relative_layout_wrapper_filter_item = (RelativeLayout) view.findViewById(R.id.relative_layout_wrapper_filter_item);
            view.setOnClickListener(view1 -> {
                FilterViewAdapter.this.selectedFilterIndex = ViewHolder.this.getLayoutPosition();
                FilterViewAdapter.this.filterListener.onFilterSelected(FilterViewAdapter.this.filterBeanList.get(FilterViewAdapter.this.selectedFilterIndex).getConfig());
                FilterViewAdapter.this.notifyDataSetChanged();
            });
        }
    }
}
