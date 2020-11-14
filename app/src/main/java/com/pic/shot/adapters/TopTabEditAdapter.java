package com.pic.shot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.pic.shot.R;
import com.pic.shot.adapters.RecyclerTabLayout;

public class TopTabEditAdapter extends RecyclerTabLayout.Adapter<TopTabEditAdapter.ViewHolder> {
    private Context context;
    private PagerAdapter mAdapater = this.mViewPager.getAdapter();

    public TopTabEditAdapter(ViewPager viewPager, Context context2) {
        super(viewPager);
        this.context = context2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.top_tab_view, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        switch (i) {
            case 0:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.emoij));
                break;
            case 1:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.giddy));
                break;
            case 2:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.text));
                break;
            case 3:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.other));
                break;
            case 4:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.diadem));
                break;
            case 5:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.tie));
                break;
            case 6:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.heard));
                break;
            case 7:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.cat));
                break;
            case 8:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.eye));
                break;
            case 9:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.glasses));
                break;
            case 10:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.tatoo));
                break;
        }
        viewHolder.imageView.setSelected(i == getCurrentIndicatorPosition());
    }

    public int getItemCount() {
        return this.mAdapater.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.image);
            view.setOnClickListener(view1 -> TopTabEditAdapter.this.getViewPager().setCurrentItem(ViewHolder.this.getAdapterPosition()));
        }
    }
}
