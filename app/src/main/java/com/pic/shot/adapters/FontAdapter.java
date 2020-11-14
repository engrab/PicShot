package com.pic.shot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.pic.shot.R;
import com.pic.shot.utils.FontUtils;
import java.util.List;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.ViewHolder> {
    private Context context;
    private List<String> lstFonts;
    public ItemClickListener mClickListener;
    private LayoutInflater mInflater;
    public int selectedItem = 0;

    public interface ItemClickListener {
        void onItemClick(View view, int i);
    }

    public int getItemViewType(int i) {
        return i;
    }

    public FontAdapter(Context context2, List<String> list) {
        this.mInflater = LayoutInflater.from(context2);
        this.context = context2;
        this.lstFonts = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.mInflater.inflate(R.layout.item_font, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        int i2;
        FontUtils.setFontByName(this.context, viewHolder.font, this.lstFonts.get(i));
        ConstraintLayout constraintLayout = viewHolder.wrapperFontItems;
        if (this.selectedItem != i) {
            i2 = R.drawable.border_black_view;
        } else {
            i2 = R.drawable.border_view;
        }
        constraintLayout.setBackground(ContextCompat.getDrawable(this.context, i2));
    }

    public int getItemCount() {
        return this.lstFonts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView font;
        ConstraintLayout wrapperFontItems;

        ViewHolder(View view) {
            super(view);
            this.font = (TextView) view.findViewById(R.id.text_view_font_item);
            this.wrapperFontItems = (ConstraintLayout) view.findViewById(R.id.constraint_layout_wrapper_font_item);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            FontAdapter.this.selectedItem = getAdapterPosition();
            if (FontAdapter.this.mClickListener != null) {
                FontAdapter.this.mClickListener.onItemClick(view, FontAdapter.this.selectedItem);
            }
            FontAdapter.this.notifyDataSetChanged();
        }
    }

    public void setSelectedItem(int i) {
        this.selectedItem = i;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}
