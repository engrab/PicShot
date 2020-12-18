package com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.R;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.type.ToolType;
import java.util.ArrayList;
import java.util.List;

public class ToolsAdapter extends RecyclerView.Adapter<ToolsAdapter.ViewHolder> {
    public OnItemSelected onItemSelected;
    public List<ToolModel> toolLists;

    public interface OnItemSelected {
        void onToolSelected(ToolType toolType);
    }

    public ToolsAdapter(OnItemSelected onItemSelected2) {
        ArrayList arrayList = new ArrayList();
        this.toolLists = arrayList;
        this.onItemSelected = onItemSelected2;
        arrayList.add(new ToolModel("Filter", R.drawable.ic_filter, ToolType.FILTER));
        this.toolLists.add(new ToolModel("Adjust", R.drawable.ic_adjust, ToolType.ADJUST));
        this.toolLists.add(new ToolModel("Crop", R.drawable.ic_crop, ToolType.CROP));
        this.toolLists.add(new ToolModel("Sticker", R.drawable.ic_sticker, ToolType.STICKER));
        this.toolLists.add(new ToolModel("Overlay", R.drawable.ic_overlay, ToolType.OVERLAY));
        this.toolLists.add(new ToolModel("Text", R.drawable.ic_text, ToolType.TEXT));
        this.toolLists.add(new ToolModel("Blur", R.drawable.ic_blur, ToolType.BLUR));
        this.toolLists.add(new ToolModel("Paint", R.drawable.ic_paint, ToolType.BRUSH));
        this.toolLists.add(new ToolModel("Ratio", R.drawable.ic_ratio, ToolType.INSTA));
        this.toolLists.add(new ToolModel("Splash", R.drawable.ic_splash, ToolType.SPLASH));
        this.toolLists.add(new ToolModel("Mosaic", R.drawable.ic_mosaic, ToolType.MOSAIC));
        this.toolLists.add(new ToolModel("Beauty", R.drawable.ic_beauty, ToolType.BEAUTY));
    }

    class ToolModel {
        public int toolIcon;
        public String toolName;
        public ToolType toolType;

        ToolModel(String str, int i, ToolType toolType) {
            this.toolName = str;
            this.toolIcon = i;
            this.toolType = toolType;
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_editing_tools, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ToolModel toolModel = this.toolLists.get(i);
        viewHolder.text_view_tool_name.setText(toolModel.toolName);
        viewHolder.image_view_tool_icon.setImageResource(toolModel.toolIcon);
    }

    public int getItemCount() {
        return this.toolLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_view_tool_icon;
        RelativeLayout relative_layout_wrapper_tool;
        TextView text_view_tool_name;

        ViewHolder(View view) {
            super(view);
            this.image_view_tool_icon = view.findViewById(R.id.image_view_tool_icon);
            this.text_view_tool_name = view.findViewById(R.id.text_view_tool_name);
            RelativeLayout relativeLayout = view.findViewById(R.id.relative_layout_wrapper_tool);
            this.relative_layout_wrapper_tool = relativeLayout;
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    ToolsAdapter.this.onItemSelected.onToolSelected(ToolsAdapter.this.toolLists.get(getLayoutPosition()).toolType);
                }
            });
        }


    }
}
