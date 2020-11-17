package com.pic.editor.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.pic.editor.R;
import com.pic.editor.utils.AssetUtils;
import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {
    public Context context;
    public int screenWidth;
    public OnClickStickerListener stickerListener;
    public List<String> stickers;

    public interface OnClickStickerListener {
        void addSticker(Bitmap bitmap);
    }

    public StickerAdapter(Context context2, List<String> list, int i, OnClickStickerListener onClickStickerListener) {
        this.context = context2;
        this.stickers = list;
        this.screenWidth = i;
        this.stickerListener = onClickStickerListener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_sticker, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Glide.with(this.context).load(AssetUtils.loadBitmapFromAssets(this.context, this.stickers.get(i))).into(viewHolder.sticker);
    }

    public int getItemCount() {
        return this.stickers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView sticker;

        public ViewHolder(View view) {
            super(view);
            this.sticker = (ImageView) view.findViewById(R.id.image_view_item_sticker);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            StickerAdapter.this.stickerListener.addSticker(AssetUtils.loadBitmapFromAssets(StickerAdapter.this.context, StickerAdapter.this.stickers.get(getAdapterPosition())));
        }
    }
}
