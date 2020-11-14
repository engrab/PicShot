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
import com.pic.shot.addtext.AddTextProperties;
import java.util.List;

public class ShadowAdapter extends RecyclerView.Adapter<ShadowAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    public ShadowItemClickListener mClickListener;
    public int selectedItem = 0;
    private List<AddTextProperties.TextShadow> textShadowList;

    public interface ShadowItemClickListener {
        void onShadowItemClick(View view, int i);
    }

    public int getItemViewType(int i) {
        return i;
    }

    public ShadowAdapter(Context context2, List<AddTextProperties.TextShadow> list) {
        this.layoutInflater = LayoutInflater.from(context2);
        this.context = context2;
        this.textShadowList = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.layoutInflater.inflate(R.layout.item_shadow, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        int i2;
        AddTextProperties.TextShadow textShadow = this.textShadowList.get(i);
        viewHolder.textShadow.setShadowLayer((float) textShadow.getRadius(), (float) textShadow.getDx(), (float) textShadow.getDy(), textShadow.getColorShadow());
        ConstraintLayout constraintLayout = viewHolder.wrapperFontItems;
        if (this.selectedItem != i) {
            i2 = R.drawable.border_black_view;
        } else {
            i2 = R.drawable.border_view;
        }
        constraintLayout.setBackground(ContextCompat.getDrawable(this.context, i2));
    }

    public int getItemCount() {
        return this.textShadowList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textShadow;
        ConstraintLayout wrapperFontItems;

        ViewHolder(View view) {
            super(view);
            this.textShadow = (TextView) view.findViewById(R.id.text_view_shadow_item);
            this.wrapperFontItems = (ConstraintLayout) view.findViewById(R.id.constraint_layout_wrapper_shadow_item);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (ShadowAdapter.this.mClickListener != null) {
                ShadowAdapter.this.mClickListener.onShadowItemClick(view, getAdapterPosition());
            }
            ShadowAdapter.this.selectedItem = getAdapterPosition();
            ShadowAdapter.this.notifyDataSetChanged();
        }
    }

    public void setSelectedItem(int i) {
        this.selectedItem = i;
    }

    public void setClickListener(ShadowItemClickListener shadowItemClickListener) {
        this.mClickListener = shadowItemClickListener;
    }
}
