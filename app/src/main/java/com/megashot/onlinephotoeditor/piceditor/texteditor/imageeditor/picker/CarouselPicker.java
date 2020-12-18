package com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.picker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.R;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.utils.SystemUtil;
import java.util.List;

public class CarouselPicker extends ViewPager {
    public static final int[] CarouselPicker = {R.attr.item_width};
    private Context context;
    private int itemWidth;

    public interface PickerItem {
        Bitmap getBitmap();

        String getColor();

        Drawable getDrawable();

        boolean hasDrawable();
    }

    public CarouselPicker(Context context2) {
        this(context2, (AttributeSet) null);
        this.context = context2;
    }

    public CarouselPicker(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        this.context = context2;
        init(attributeSet);
    }

    private void init(AttributeSet attributeSet) {
        setClipChildren(false);
        setFadingEdgeLength(0);
        TypedArray obtainStyledAttributes = this.context.obtainStyledAttributes(attributeSet, CarouselPicker, 0, 0);
        this.itemWidth = obtainStyledAttributes.getInt(0, 15);
        obtainStyledAttributes.recycle();
    }

    public void onMeasure(int i, int i2) {
        int i3 = 0;
        for (int i4 = 0; i4 < getChildCount(); i4++) {
            View childAt = getChildAt(i4);
            childAt.measure(i, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int measuredHeight = childAt.getMeasuredHeight();
            if (measuredHeight > i3) {
                i3 = measuredHeight;
            }
        }
        super.onMeasure(i, MeasureSpec.makeMeasureSpec(i3, BasicMeasure.EXACTLY));
        setPageMargin((-getMeasuredWidth()) + SystemUtil.dpToPx(this.context, this.itemWidth));
    }

    public void setAdapter(PagerAdapter pagerAdapter) {
        super.setAdapter(pagerAdapter);
        setOffscreenPageLimit(pagerAdapter.getCount());
    }

    public static class CarouselViewAdapter extends PagerAdapter {
        Context context;
        int drawable;
        List<PickerItem> items;
        int textColor = 0;

        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        public CarouselViewAdapter(Context context2, List<PickerItem> list, int i) {
            this.context = context2;
            this.drawable = i;
            this.items = list;
            if (i == 0) {
                this.drawable = R.layout.item_carousel_picker;
            }
        }

        public int getCount() {
            return this.items.size();
        }

        public Object instantiateItem(ViewGroup viewGroup, int i) {
            View inflate = LayoutInflater.from(this.context).inflate(this.drawable, (ViewGroup) null);
            ImageView imageView = (ImageView) inflate.findViewById(R.id.icon);
            View color = inflate.findViewById(R.id.color);
            PickerItem pickerItem = this.items.get(i);
            imageView.setVisibility(View.VISIBLE);
            if (pickerItem.hasDrawable()) {
                imageView.setVisibility(View.VISIBLE);
                color.setVisibility(View.GONE);
                imageView.setImageDrawable(pickerItem.getDrawable());
            } else if (pickerItem.getColor() != null) {
                imageView.setVisibility(View.GONE);
                color.setVisibility(View.VISIBLE);
                color.setBackgroundColor(Color.parseColor(pickerItem.getColor()));
            }
            inflate.setTag(Integer.valueOf(i));
            viewGroup.addView(inflate);
            return inflate;
        }

        public int getTextColor() {
            return this.textColor;
        }

        public void setTextColor(int i) {
            this.textColor = i;
        }

        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((View) obj);
        }
    }

    public static class ColorItem implements PickerItem {
        private String color;

        public Bitmap getBitmap() {
            return null;
        }

        public Drawable getDrawable() {
            return null;
        }

        public boolean hasDrawable() {
            return false;
        }

        public ColorItem(String str) {
            this.color = str;
        }

        public String getColor() {
            return this.color;
        }
    }

    public static class DrawableItem implements PickerItem {
        private Bitmap bitmap;
        private Drawable drawable;

        public String getColor() {
            return null;
        }

        public boolean hasDrawable() {
            return true;
        }

        public DrawableItem(Drawable drawable2) {
            this.drawable = drawable2;
            this.bitmap = ((BitmapDrawable) drawable2).getBitmap();
        }

        public Drawable getDrawable() {
            return this.drawable;
        }

        public Bitmap getBitmap() {
            return this.bitmap;
        }
    }
}
