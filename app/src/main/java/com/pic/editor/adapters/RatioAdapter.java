package com.pic.editor.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.pic.editor.R;
import com.pic.editor.utils.ColorUtils;
import java.util.ArrayList;
import java.util.List;

public class RatioAdapter extends RecyclerView.Adapter<RatioAdapter.ViewHolder> {
    public BackgroundInstaListener backgroundInstaListener;
    private Context context;
    public int selectedSquareIndex;
    public List<SquareView> squareViews;

    public interface BackgroundInstaListener {
        void onBackgroundSelected(SquareView squareView);
    }

    public RatioAdapter(Context context2, BackgroundInstaListener backgroundInstaListener2) {
        ArrayList arrayList = new ArrayList();
        this.squareViews = arrayList;
        this.context = context2;
        this.backgroundInstaListener = backgroundInstaListener2;
        arrayList.add(new SquareView(R.drawable.background_blur, "Blur"));
        this.squareViews.add(new SquareView(R.color.white, "White"));
        this.squareViews.add(new SquareView(R.color.black, "Black"));
        this.squareViews.add(new SquareView(R.drawable.gradient_1, "G1"));
        this.squareViews.add(new SquareView(R.drawable.gradient_2, "G2"));
        this.squareViews.add(new SquareView(R.drawable.gradient_3, "G3"));
        this.squareViews.add(new SquareView(R.drawable.gradient_4, "G4"));
        this.squareViews.add(new SquareView(R.drawable.gradient_5, "G5"));
        this.squareViews.add(new SquareView(R.drawable.gradient_11, "G11"));
        this.squareViews.add(new SquareView(R.drawable.gradient_10, "G10"));
        this.squareViews.add(new SquareView(R.drawable.gradient_6, "G6"));
        this.squareViews.add(new SquareView(R.drawable.gradient_7, "G7"));
        this.squareViews.add(new SquareView(R.drawable.gradient_13, "G13"));
        this.squareViews.add(new SquareView(R.drawable.gradient_14, "G14"));
        this.squareViews.add(new SquareView(R.drawable.gradient_16, "G16"));
        this.squareViews.add(new SquareView(R.drawable.gradient_17, "G17"));
        this.squareViews.add(new SquareView(R.drawable.gradient_18, "G18"));
        List<String> lstColorForBrush = ColorUtils.lstColorForBrush();
        for (int i = 0; i < lstColorForBrush.size() - 2; i++) {
            this.squareViews.add(new SquareView(Color.parseColor(lstColorForBrush.get(i)), "", true));
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_square_view, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        SquareView squareView = this.squareViews.get(i);
        if (squareView.isColor) {
            viewHolder.squareView.setBackgroundColor(squareView.drawableId);
        } else {
            viewHolder.squareView.setBackgroundResource(squareView.drawableId);
        }
        if (this.selectedSquareIndex == i) {
            viewHolder.wrapSquareView.setBackground(this.context.getDrawable(R.drawable.border_view));
        } else {
            viewHolder.wrapSquareView.setBackground(this.context.getDrawable(R.drawable.border_transparent_view));
        }
    }

    public int getItemCount() {
        return this.squareViews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View squareView;
        public ConstraintLayout wrapSquareView;

        public ViewHolder(View view) {
            super(view);
            this.squareView = view.findViewById(R.id.square_view);
            this.wrapSquareView = (ConstraintLayout) view.findViewById(R.id.constraint_layout_wrapper_square_view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            RatioAdapter.this.selectedSquareIndex = getAdapterPosition();
            RatioAdapter.this.backgroundInstaListener.onBackgroundSelected(RatioAdapter.this.squareViews.get(RatioAdapter.this.selectedSquareIndex));
            RatioAdapter.this.notifyDataSetChanged();
        }
    }

    public class SquareView {
        public int drawableId;
        public boolean isColor;
        public String text;

        SquareView(int i, String str) {
            this.drawableId = i;
            this.text = str;
        }

        SquareView(int i, String str, boolean z) {
            this.drawableId = i;
            this.text = str;
            this.isColor = z;
        }
    }
}
