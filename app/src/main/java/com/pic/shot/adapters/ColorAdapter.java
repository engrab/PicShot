package com.pic.shot.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.github.pavlospt.CircleView;
import com.pic.shot.R;
import com.pic.shot.clicklistener.BrushColorListener;
import com.pic.shot.utils.ColorUtils;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {
    public BrushColorListener brushColorListener;
    public List<String> colors;
    private Context context;
    public int selectedColorIndex;

    public ColorAdapter(Context context2, BrushColorListener brushColorListener2) {
        this.colors = ColorUtils.lstColorForBrush();
        this.context = context2;
        this.brushColorListener = brushColorListener2;
    }

    public ColorAdapter(Context context2, BrushColorListener brushColorListener2, boolean z) {
        List<String> lstColorForBrush = ColorUtils.lstColorForBrush();
        this.colors = lstColorForBrush;
        this.context = context2;
        this.brushColorListener = brushColorListener2;
        lstColorForBrush.add(0, "#00000000");
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_color, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.circle_view_color.setFillColor(Color.parseColor(this.colors.get(i)));
        viewHolder.circle_view_color.setStrokeColor(Color.parseColor(this.colors.get(i)));
        if (this.selectedColorIndex == i) {
            if (this.colors.get(i).equals("#00000000")) {
                viewHolder.circle_view_color.setBackgroundColor(Color.parseColor("#00000000"));
                viewHolder.circle_view_color.setStrokeColor(Color.parseColor("#FF4081"));
                return;
            }
            viewHolder.circle_view_color.setBackgroundColor(-1);
        } else if (this.colors.get(i).equals("#00000000")) {
            viewHolder.circle_view_color.setBackground(this.context.getDrawable(R.drawable.none));
            viewHolder.circle_view_color.setBackgroundColor(Color.parseColor("#00000000"));
        } else {
            viewHolder.circle_view_color.setBackgroundColor(Color.parseColor(this.colors.get(i)));
        }
    }

    public int getItemCount() {
        return this.colors.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleView circle_view_color;

        ViewHolder(View view) {
            super(view);
            CircleView circleView = (CircleView) view.findViewById(R.id.circle_view_color);
            this.circle_view_color = circleView;
            circleView.setOnClickListener(view1 -> {
                ColorAdapter.this.selectedColorIndex = ViewHolder.this.getLayoutPosition();
                ColorAdapter.this.brushColorListener.onColorChanged(ColorAdapter.this.colors.get(ColorAdapter.this.selectedColorIndex));
                ColorAdapter.this.notifyDataSetChanged();
            });
        }
    }

    public void setSelectedColorIndex(int i) {
        this.selectedColorIndex = i;
    }
}
