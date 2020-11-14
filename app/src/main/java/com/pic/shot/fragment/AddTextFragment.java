package com.pic.shot.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.pic.shot.R;
import com.pic.shot.adapters.FontAdapter;
import com.pic.shot.adapters.ShadowAdapter;
import com.pic.shot.addtext.AddTextProperties;
import com.pic.shot.custom.CustomEditText;
import com.pic.shot.picker.CarouselPicker;
import com.pic.shot.utils.FontUtils;
import com.pic.shot.utils.SharePreferenceUtil;
import com.pic.shot.utils.SystemUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddTextFragment extends DialogFragment implements View.OnClickListener, FontAdapter.ItemClickListener, ShadowAdapter.ShadowItemClickListener {
    public static final String EXTRA_COLOR_CODE = "extra_color_code";
    public static final String EXTRA_INPUT_TEXT = "extra_input_text";
    public static final String TAG = "AddTextFragment";
    public AddTextProperties addTextProperties;
    CustomEditText add_text_edit_text;
    CarouselPicker background_carousel_picker;
    CheckBox checkbox_background;
    public List<CarouselPicker.PickerItem> colorItems;
    CarouselPicker color_carousel_picker;
    private FontAdapter fontAdapter;
    ImageView image_view_align;
    ImageView image_view_background;
    ImageView image_view_color;
    ImageView image_view_color_down;
    ImageView image_view_fonts;
    ImageView image_view_keyboard;
    ImageView image_view_save_change;
    ImageView image_view_text_texture;
    private InputMethodManager inputMethodManager;
    LinearLayout linear_layout_edit_text_tools;
    LinearLayout linear_layout_preview;
    RecyclerView recycler_view_fonts;
    RecyclerView recycler_view_shadows;
    ScrollView scroll_view_change_color_layout;
    ScrollView scroll_view_change_font_layout;
    SeekBar seekbar_background_opacity;
    SeekBar seekbar_height;
    SeekBar seekbar_radius;
    TextView seekbar_text_opacity;
    SeekBar seekbar_text_size;
    SeekBar seekbar_width;
    private ShadowAdapter shadowAdapter;
    private TextEditor textEditor;
    private List<ImageView> textFunctions;
    public List<CarouselPicker.PickerItem> textTextureItems;
    SeekBar textTransparent;
    CarouselPicker texture_carousel_picker;
    View view_highlight_background_color;
    View view_highlight_text_color;
    View view_highlight_texture;

    public interface TextEditor {
        void onBackButton();

        void onDone(AddTextProperties addTextProperties);
    }

    public void initView(View view) {
        this.add_text_edit_text = (CustomEditText) view.findViewById(R.id.add_text_edit_text);
        this.image_view_keyboard = (ImageView) view.findViewById(R.id.image_view_keyboard);
        this.image_view_fonts = (ImageView) view.findViewById(R.id.image_view_fonts);
        this.image_view_color = (ImageView) view.findViewById(R.id.image_view_color);
        this.image_view_align = (ImageView) view.findViewById(R.id.image_view_align);
        this.image_view_save_change = (ImageView) view.findViewById(R.id.image_view_save_change);
        this.scroll_view_change_font_layout = (ScrollView) view.findViewById(R.id.scroll_view_change_font_layout);
        this.linear_layout_edit_text_tools = (LinearLayout) view.findViewById(R.id.linear_layout_edit_text_tools);
        this.recycler_view_fonts = (RecyclerView) view.findViewById(R.id.recycler_view_fonts);
        this.recycler_view_shadows = (RecyclerView) view.findViewById(R.id.recycler_view_shadows);
        this.scroll_view_change_color_layout = (ScrollView) view.findViewById(R.id.scroll_view_change_color_layout);
        this.color_carousel_picker = (CarouselPicker) view.findViewById(R.id.color_carousel_picker);
        this.texture_carousel_picker = (CarouselPicker) view.findViewById(R.id.texture_carousel_picker);
        this.image_view_text_texture = (ImageView) view.findViewById(R.id.image_view_text_texture);
        this.image_view_color_down = (ImageView) view.findViewById(R.id.image_view_color_down);
        this.view_highlight_text_color = view.findViewById(R.id.view_highlight_text_color);
        this.view_highlight_texture = view.findViewById(R.id.view_highlight_texture);
        this.textTransparent = (SeekBar) view.findViewById(R.id.seekbar_text_opacity);
        this.seekbar_text_opacity = (TextView) view.findViewById(R.id.text_view_preview_effect);
        this.linear_layout_preview = (LinearLayout) view.findViewById(R.id.linear_layout_preview);
        this.checkbox_background = (CheckBox) view.findViewById(R.id.checkbox_background);
        this.image_view_background = (ImageView) view.findViewById(R.id.image_view_background);
        this.view_highlight_background_color = view.findViewById(R.id.view_highlight_background_color);
        this.background_carousel_picker = (CarouselPicker) view.findViewById(R.id.background_carousel_picker);
        this.seekbar_width = (SeekBar) view.findViewById(R.id.seekbar_width);
        this.seekbar_height = (SeekBar) view.findViewById(R.id.seekbar_height);
        this.seekbar_background_opacity = (SeekBar) view.findViewById(R.id.seekbar_background_opacity);
        this.seekbar_text_size = (SeekBar) view.findViewById(R.id.seekbar_text_size);
        this.seekbar_radius = (SeekBar) view.findViewById(R.id.seekbar_radius);
    }

    public void onItemClick(View view, int i) {
        FontUtils.setFontByName((Context) Objects.requireNonNull(getContext()), this.seekbar_text_opacity, FontUtils.getListFonts().get(i));
        this.addTextProperties.setFontName(FontUtils.getListFonts().get(i));
        this.addTextProperties.setFontIndex(i);
    }

    public void onShadowItemClick(View view, int i) {
        AddTextProperties.TextShadow textShadow = AddTextProperties.getLstTextShadow().get(i);
        this.seekbar_text_opacity.setShadowLayer((float) textShadow.getRadius(), (float) textShadow.getDx(), (float) textShadow.getDy(), textShadow.getColorShadow());
        this.seekbar_text_opacity.invalidate();
        this.addTextProperties.setTextShadow(textShadow);
        this.addTextProperties.setTextShadowIndex(i);
    }

    public static AddTextFragment show(AppCompatActivity appCompatActivity, String str, int i) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_INPUT_TEXT, str);
        bundle.putInt(EXTRA_COLOR_CODE, i);
        AddTextFragment addTextFragment = new AddTextFragment();
        addTextFragment.setArguments(bundle);
        addTextFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return addTextFragment;
    }

    public static AddTextFragment show(AppCompatActivity appCompatActivity, AddTextProperties addTextProperties2) {
        AddTextFragment addTextFragment = new AddTextFragment();
        addTextFragment.setAddTextProperties(addTextProperties2);
        addTextFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return addTextFragment;
    }

    public static AddTextFragment show(AppCompatActivity appCompatActivity) {
        return show(appCompatActivity, "Test", ContextCompat.getColor(appCompatActivity, R.color.white));
    }

    public void setAddTextProperties(AddTextProperties addTextProperties2) {
        this.addTextProperties = addTextProperties2;
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setFlags(1024, 1024);
        return layoutInflater.inflate(R.layout.fragment_add_texte, viewGroup, false);
    }

    public void dismissAndShowSticker() {
        TextEditor textEditor2 = this.textEditor;
        if (textEditor2 != null) {
            textEditor2.onBackButton();
        }
        dismiss();
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        initView(view);
        if (this.addTextProperties == null) {
            this.addTextProperties = AddTextProperties.getDefaultProperties();
        }
        this.add_text_edit_text.setDialogFragment(this);
        initAddTextLayout();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
        this.inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        setDefaultStyleForEdittext();
        this.inputMethodManager.toggleSoftInput(2, 0);
        highlightFunction(this.image_view_keyboard);
        this.recycler_view_fonts.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        FontAdapter fontAdapter2 = new FontAdapter(getContext(), FontUtils.getListFonts());
        this.fontAdapter = fontAdapter2;
        fontAdapter2.setClickListener(this);
        this.recycler_view_fonts.setAdapter(this.fontAdapter);
        this.recycler_view_shadows.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        ShadowAdapter shadowAdapter2 = new ShadowAdapter(getContext(), AddTextProperties.getLstTextShadow());
        this.shadowAdapter = shadowAdapter2;
        shadowAdapter2.setClickListener(this);
        this.recycler_view_shadows.setAdapter(this.shadowAdapter);
        this.color_carousel_picker.setAdapter(new CarouselPicker.CarouselViewAdapter(getContext(), this.colorItems, 0));
        this.color_carousel_picker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageSelected(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
                if (f > 0.0f) {
                    if (AddTextFragment.this.image_view_color_down.getVisibility() == View.INVISIBLE) {
                        AddTextFragment.this.image_view_color_down.setVisibility(View.VISIBLE);
                        AddTextFragment.this.view_highlight_text_color.setVisibility(View.VISIBLE);
                        AddTextFragment.this.image_view_text_texture.setVisibility(View.INVISIBLE);
                        AddTextFragment.this.view_highlight_texture.setVisibility(View.GONE);
                    }
                    AddTextFragment.this.seekbar_text_opacity.getPaint().setShader((Shader) null);
                    int i3 = -1;
                    float f2 = ((float) i) + f;
                    if (Math.round(f2) < AddTextFragment.this.colorItems.size()) {
                        i3 = Color.parseColor(AddTextFragment.this.colorItems.get(Math.round(f2)).getColor());
                    }
                    AddTextFragment.this.seekbar_text_opacity.setTextColor(i3);
                    AddTextFragment.this.addTextProperties.setTextColorIndex(Math.round(f2));
                    AddTextFragment.this.addTextProperties.setTextColor(i3);
                    AddTextFragment.this.addTextProperties.setTextShader((Shader) null);
                }
            }
        });
        this.texture_carousel_picker.setAdapter(new CarouselPicker.CarouselViewAdapter(getContext(), this.textTextureItems, 0));
        this.texture_carousel_picker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageSelected(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
                if (f > 0.0f) {
                    if (AddTextFragment.this.image_view_text_texture.getVisibility() == View.INVISIBLE) {
                        AddTextFragment.this.image_view_text_texture.setVisibility(View.VISIBLE);
                        AddTextFragment.this.view_highlight_texture.setVisibility(View.VISIBLE);
                        AddTextFragment.this.image_view_color_down.setVisibility(View.INVISIBLE);
                        AddTextFragment.this.view_highlight_text_color.setVisibility(View.GONE);
                    }
                    float f2 = ((float) i) + f;
                    BitmapShader bitmapShader = new BitmapShader(AddTextFragment.this.textTextureItems.get(Math.round(f2)).getBitmap(), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                    AddTextFragment.this.seekbar_text_opacity.setLayerType(View.LAYER_TYPE_SOFTWARE, (Paint) null);
                    AddTextFragment.this.seekbar_text_opacity.getPaint().setShader(bitmapShader);
                    AddTextFragment.this.addTextProperties.setTextShader(bitmapShader);
                    AddTextFragment.this.addTextProperties.setTextShaderIndex(Math.round(f2));
                }
            }
        });
        this.textTransparent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int i2 = 255 - i;
                AddTextFragment.this.addTextProperties.setTextAlpha(i2);
                AddTextFragment.this.seekbar_text_opacity.setTextColor(Color.argb(i2, Color.red(AddTextFragment.this.addTextProperties.getTextColor()), Color.green(AddTextFragment.this.addTextProperties.getTextColor()), Color.blue(AddTextFragment.this.addTextProperties.getTextColor())));
            }
        });
        this.add_text_edit_text.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                AddTextFragment.this.seekbar_text_opacity.setText(charSequence.toString());
                AddTextFragment.this.addTextProperties.setText(charSequence.toString());
            }
        });
        this.checkbox_background.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (!z) {
                    AddTextFragment.this.addTextProperties.setShowBackground(false);
                    AddTextFragment.this.seekbar_text_opacity.setBackgroundResource(0);
                    AddTextFragment.this.seekbar_text_opacity.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                } else if (AddTextFragment.this.checkbox_background.isPressed() || AddTextFragment.this.addTextProperties.isShowBackground()) {
                    AddTextFragment.this.addTextProperties.setShowBackground(true);
                    AddTextFragment.this.initPreviewText();
                } else {
                    AddTextFragment.this.checkbox_background.setChecked(false);
                    AddTextFragment.this.addTextProperties.setShowBackground(false);
                    AddTextFragment.this.initPreviewText();
                }
            }
        });
        this.background_carousel_picker.setAdapter(new CarouselPicker.CarouselViewAdapter(getContext(), this.colorItems, 0));
        this.background_carousel_picker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageSelected(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
                if (f > 0.0f) {
                    int i3 = 0;
                    if (AddTextFragment.this.image_view_background.getVisibility() == View.INVISIBLE) {
                        AddTextFragment.this.image_view_background.setVisibility(View.VISIBLE);
                        AddTextFragment.this.view_highlight_background_color.setVisibility(View.VISIBLE);
                    }
                    AddTextFragment.this.addTextProperties.setShowBackground(true);
                    if (!AddTextFragment.this.checkbox_background.isChecked()) {
                        AddTextFragment.this.checkbox_background.setChecked(true);
                    }
                    float f2 = ((float) i) + f;
                    int round = Math.round(f2);
                    if (round >= AddTextFragment.this.colorItems.size()) {
                        i3 = AddTextFragment.this.colorItems.size() - 1;
                    } else if (round >= 0) {
                        i3 = round;
                    }
                    int parseColor = Color.parseColor(AddTextFragment.this.colorItems.get(i3).getColor());
                    int red = Color.red(parseColor);
                    int green = Color.green(parseColor);
                    int blue = Color.blue(parseColor);
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setColor(Color.argb(AddTextFragment.this.addTextProperties.getBackgroundAlpha(), red, green, blue));
                    gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx((Context) Objects.requireNonNull(AddTextFragment.this.getContext()), AddTextFragment.this.addTextProperties.getBackgroundBorder()));
                    AddTextFragment.this.seekbar_text_opacity.setBackground(gradientDrawable);
                    AddTextFragment.this.addTextProperties.setBackgroundColor(parseColor);
                    AddTextFragment.this.addTextProperties.setBackgroundColorIndex(Math.round(f2));
                    AddTextFragment.this.seekbar_radius.setEnabled(true);
                }
            }
        });
        this.seekbar_width.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                AddTextFragment.this.seekbar_text_opacity.setPadding(SystemUtil.dpToPx((Context) Objects.requireNonNull(AddTextFragment.this.getContext()), i), AddTextFragment.this.seekbar_text_opacity.getPaddingTop(), SystemUtil.dpToPx(AddTextFragment.this.getContext(), i), AddTextFragment.this.seekbar_text_opacity.getPaddingBottom());
                AddTextFragment.this.addTextProperties.setPaddingWidth(i);
            }
        });
        this.seekbar_height.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                AddTextFragment.this.seekbar_text_opacity.setPadding(AddTextFragment.this.seekbar_text_opacity.getPaddingLeft(), SystemUtil.dpToPx((Context) Objects.requireNonNull(AddTextFragment.this.getContext()), i), AddTextFragment.this.seekbar_text_opacity.getPaddingRight(), SystemUtil.dpToPx(AddTextFragment.this.getContext(), i));
                AddTextFragment.this.addTextProperties.setPaddingHeight(i);
            }
        });
        this.seekbar_background_opacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                AddTextFragment.this.addTextProperties.setBackgroundAlpha(255 - i);
                if (AddTextFragment.this.addTextProperties.isShowBackground()) {
                    int red = Color.red(AddTextFragment.this.addTextProperties.getBackgroundColor());
                    int green = Color.green(AddTextFragment.this.addTextProperties.getBackgroundColor());
                    int blue = Color.blue(AddTextFragment.this.addTextProperties.getBackgroundColor());
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setColor(Color.argb(AddTextFragment.this.addTextProperties.getBackgroundAlpha(), red, green, blue));
                    gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx((Context) Objects.requireNonNull(AddTextFragment.this.getContext()), AddTextFragment.this.addTextProperties.getBackgroundBorder()));
                    AddTextFragment.this.seekbar_text_opacity.setBackground(gradientDrawable);
                }
            }
        });
        this.seekbar_text_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int i2 = 15;
                if (i >= 15) {
                    i2 = i;
                }
                AddTextFragment.this.seekbar_text_opacity.setTextSize((float) i2);
                AddTextFragment.this.addTextProperties.setTextSize(i2);
            }
        });
        this.seekbar_radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                AddTextFragment.this.addTextProperties.setBackgroundBorder(i);
                if (AddTextFragment.this.addTextProperties.isShowBackground()) {
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx((Context) Objects.requireNonNull(AddTextFragment.this.getContext()), i));
                    gradientDrawable.setColor(Color.argb(AddTextFragment.this.addTextProperties.getBackgroundAlpha(), Color.red(AddTextFragment.this.addTextProperties.getBackgroundColor()), Color.green(AddTextFragment.this.addTextProperties.getBackgroundColor()), Color.blue(AddTextFragment.this.addTextProperties.getBackgroundColor())));
                    AddTextFragment.this.seekbar_text_opacity.setBackground(gradientDrawable);
                }
            }
        });
        if (SharePreferenceUtil.getHeightOfKeyboard((Context) Objects.requireNonNull(getContext())) > 0) {
            updateAddTextBottomToolbarHeight(SharePreferenceUtil.getHeightOfKeyboard(getContext()));
        }
        initPreviewText();
    }

    public void initPreviewText() {
        if (this.addTextProperties.isFullScreen()) {
            this.seekbar_text_opacity.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        }
        if (this.addTextProperties.isShowBackground()) {
            if (this.addTextProperties.getBackgroundColor() != 0) {
                this.seekbar_text_opacity.setBackgroundColor(this.addTextProperties.getBackgroundColor());
            }
            if (this.addTextProperties.getBackgroundAlpha() < 255) {
                this.seekbar_text_opacity.setBackgroundColor(Color.argb(this.addTextProperties.getBackgroundAlpha(), Color.red(this.addTextProperties.getBackgroundColor()), Color.green(this.addTextProperties.getBackgroundColor()), Color.blue(this.addTextProperties.getBackgroundColor())));
            }
            if (this.addTextProperties.getBackgroundBorder() > 0) {
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx((Context) Objects.requireNonNull(getContext()), this.addTextProperties.getBackgroundBorder()));
                gradientDrawable.setColor(Color.argb(this.addTextProperties.getBackgroundAlpha(), Color.red(this.addTextProperties.getBackgroundColor()), Color.green(this.addTextProperties.getBackgroundColor()), Color.blue(this.addTextProperties.getBackgroundColor())));
                this.seekbar_text_opacity.setBackground(gradientDrawable);
            }
        }
        if (this.addTextProperties.getPaddingHeight() > 0) {
            TextView textView = this.seekbar_text_opacity;
            textView.setPadding(textView.getPaddingLeft(), this.addTextProperties.getPaddingHeight(), this.seekbar_text_opacity.getPaddingRight(), this.addTextProperties.getPaddingHeight());
            this.seekbar_height.setProgress(this.addTextProperties.getPaddingHeight());
        }
        if (this.addTextProperties.getPaddingWidth() > 0) {
            this.seekbar_text_opacity.setPadding(this.addTextProperties.getPaddingWidth(), this.seekbar_text_opacity.getPaddingTop(), this.addTextProperties.getPaddingWidth(), this.seekbar_text_opacity.getPaddingBottom());
            this.seekbar_width.setProgress(this.addTextProperties.getPaddingWidth());
        }
        if (this.addTextProperties.getText() != null) {
            this.seekbar_text_opacity.setText(this.addTextProperties.getText());
            this.add_text_edit_text.setText(this.addTextProperties.getText());
        }
        if (this.addTextProperties.getTextShader() != null) {
            this.seekbar_text_opacity.setLayerType(View.LAYER_TYPE_SOFTWARE, (Paint) null);
            this.seekbar_text_opacity.getPaint().setShader(this.addTextProperties.getTextShader());
        }
        if (this.addTextProperties.getTextAlign() == 4) {
            this.image_view_align.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_center));
        } else if (this.addTextProperties.getTextAlign() == 3) {
            this.image_view_align.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_right));
        } else if (this.addTextProperties.getTextAlign() == 2) {
            this.image_view_align.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_left));
        }
        this.seekbar_text_opacity.setPadding(SystemUtil.dpToPx(getContext(), this.addTextProperties.getPaddingWidth()), this.seekbar_text_opacity.getPaddingTop(), SystemUtil.dpToPx(getContext(), this.addTextProperties.getPaddingWidth()), this.seekbar_text_opacity.getPaddingBottom());
        this.seekbar_text_opacity.setTextColor(this.addTextProperties.getTextColor());
        this.seekbar_text_opacity.setTextAlignment(this.addTextProperties.getTextAlign());
        this.seekbar_text_opacity.setTextSize((float) this.addTextProperties.getTextSize());
        FontUtils.setFontByName(getContext(), this.seekbar_text_opacity, this.addTextProperties.getFontName());
        if (this.addTextProperties.getTextShadow() != null) {
            AddTextProperties.TextShadow textShadow = this.addTextProperties.getTextShadow();
            this.seekbar_text_opacity.setShadowLayer((float) textShadow.getRadius(), (float) textShadow.getDx(), (float) textShadow.getDy(), textShadow.getColorShadow());
        }
        this.seekbar_text_opacity.invalidate();
    }

    private void setDefaultStyleForEdittext() {
        this.add_text_edit_text.requestFocus();
        this.add_text_edit_text.setTextSize(20.0f);
        this.add_text_edit_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.add_text_edit_text.setTextColor(Color.parseColor("#FFFFFF"));
    }

    private void initAddTextLayout() {
        this.textFunctions = getTextFunctions();
        this.image_view_keyboard.setOnClickListener(this);
        this.image_view_fonts.setOnClickListener(this);
        this.image_view_color.setOnClickListener(this);
        this.image_view_align.setOnClickListener(this);
        this.image_view_save_change.setOnClickListener(this);
        this.scroll_view_change_font_layout.setVisibility(View.GONE);
        this.scroll_view_change_color_layout.setVisibility(View.GONE);
        this.image_view_text_texture.setVisibility(View.INVISIBLE);
        this.view_highlight_texture.setVisibility(View.GONE);
        this.seekbar_width.setProgress(this.addTextProperties.getPaddingWidth());
        this.colorItems = getColorItems();
        this.textTextureItems = getTextTextures();
    }

    public void onResume() {
        super.onResume();
        ViewCompat.setOnApplyWindowInsetsListener(getDialog().getWindow().getDecorView(), new OnApplyWindowInsetsListener() {
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                return ViewCompat.onApplyWindowInsets(AddTextFragment.this.getDialog().getWindow().getDecorView(), windowInsetsCompat.inset(windowInsetsCompat.getSystemWindowInsetLeft(), 0, windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom()));
            }
        });
    }

    public void updateAddTextBottomToolbarHeight(final int i) {
        new Handler().post(new Runnable() {
            public void run() {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) AddTextFragment.this.linear_layout_edit_text_tools.getLayoutParams();
                layoutParams.bottomMargin = i;
                AddTextFragment.this.linear_layout_edit_text_tools.setLayoutParams(layoutParams);
                AddTextFragment.this.linear_layout_edit_text_tools.invalidate();
                ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) AddTextFragment.this.scroll_view_change_font_layout.getLayoutParams();
                layoutParams2.height = i;
                AddTextFragment.this.scroll_view_change_font_layout.setLayoutParams(layoutParams2);
                AddTextFragment.this.scroll_view_change_font_layout.invalidate();
                ConstraintLayout.LayoutParams layoutParams3 = (ConstraintLayout.LayoutParams) AddTextFragment.this.scroll_view_change_color_layout.getLayoutParams();
                layoutParams3.height = i;
                AddTextFragment.this.scroll_view_change_color_layout.setLayoutParams(layoutParams3);
                AddTextFragment.this.scroll_view_change_color_layout.invalidate();
                Log.i("HIHIH", i + "");
            }
        });
    }

    public void setOnTextEditorListener(TextEditor textEditor2) {
        this.textEditor = textEditor2;
    }

    private void highlightFunction(ImageView imageView) {
        for (ImageView next : this.textFunctions) {
            if (next == imageView) {
                imageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.highlight));
            } else {
                next.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.fake_highlight));
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_align:
                if (this.addTextProperties.getTextAlign() == 4) {
                    this.addTextProperties.setTextAlign(3);
                    this.image_view_align.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_right));
                } else if (this.addTextProperties.getTextAlign() == 3) {
                    this.addTextProperties.setTextAlign(2);
                    this.image_view_align.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_left));
                } else if (this.addTextProperties.getTextAlign() == 2) {
                    this.addTextProperties.setTextAlign(4);
                    this.image_view_align.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_center));
                }
                this.seekbar_text_opacity.setTextAlignment(this.addTextProperties.getTextAlign());
                TextView textView = this.seekbar_text_opacity;
                textView.setText(this.seekbar_text_opacity.getText().toString().trim() + " ");
                TextView textView2 = this.seekbar_text_opacity;
                textView2.setText(textView2.getText().toString().trim());
                return;
            case R.id.image_view_color:
                this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                this.scroll_view_change_color_layout.setVisibility(View.VISIBLE);
                toggleTextEditEditable(false);
                highlightFunction(this.image_view_color);
                this.scroll_view_change_font_layout.setVisibility(View.GONE);
                this.add_text_edit_text.setVisibility(View.GONE);
                this.color_carousel_picker.setCurrentItem(this.addTextProperties.getTextColorIndex());
                this.texture_carousel_picker.setCurrentItem(this.addTextProperties.getTextShaderIndex());
                this.textTransparent.setProgress(255 - this.addTextProperties.getTextAlpha());
                this.checkbox_background.setChecked(this.addTextProperties.isShowBackground());
                this.background_carousel_picker.setCurrentItem(this.addTextProperties.getBackgroundColorIndex());
                this.seekbar_background_opacity.setProgress(255 - this.addTextProperties.getBackgroundAlpha());
                this.seekbar_radius.setProgress(this.addTextProperties.getBackgroundBorder());
                this.seekbar_width.setProgress(this.addTextProperties.getPaddingWidth());
                this.seekbar_height.setProgress(this.addTextProperties.getPaddingHeight());
                this.checkbox_background.setChecked(this.addTextProperties.isShowBackground());
                if (this.addTextProperties.getTextShader() != null && this.image_view_text_texture.getVisibility() == View.INVISIBLE) {
                    this.image_view_text_texture.setVisibility(View.VISIBLE);
                    this.view_highlight_texture.setVisibility(View.VISIBLE);
                    this.image_view_color_down.setVisibility(View.INVISIBLE);
                    this.view_highlight_text_color.setVisibility(View.GONE);
                    return;
                }
                return;
            case R.id.image_view_fonts:
                this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                this.scroll_view_change_font_layout.setVisibility(View.VISIBLE);
                this.scroll_view_change_color_layout.setVisibility(View.GONE);
                this.add_text_edit_text.setVisibility(View.GONE);
                toggleTextEditEditable(false);
                highlightFunction(this.image_view_fonts);
                this.seekbar_text_size.setProgress(this.addTextProperties.getTextSize());
                this.fontAdapter.setSelectedItem(this.addTextProperties.getFontIndex());
                this.shadowAdapter.setSelectedItem(this.addTextProperties.getTextShadowIndex());
                return;
            case R.id.image_view_keyboard:
                toggleTextEditEditable(true);
                this.add_text_edit_text.setVisibility(View.VISIBLE);
                this.add_text_edit_text.requestFocus();
                highlightFunction(this.image_view_keyboard);
                this.scroll_view_change_font_layout.setVisibility(View.GONE);
                this.scroll_view_change_color_layout.setVisibility(View.GONE);
                this.linear_layout_edit_text_tools.invalidate();
                this.inputMethodManager.toggleSoftInput(2, 0);
                return;
            case R.id.image_view_save_change:
                if (this.addTextProperties.getText() == null || this.addTextProperties.getText().length() == 0) {
                    this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    this.textEditor.onBackButton();
                    dismiss();
                    return;
                }
                this.addTextProperties.setTextWidth(this.seekbar_text_opacity.getMeasuredWidth());
                this.addTextProperties.setTextHeight(this.seekbar_text_opacity.getMeasuredHeight());
                this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                this.textEditor.onDone(this.addTextProperties);
                dismiss();
                return;
            default:
                return;
        }
    }

    private void toggleTextEditEditable(boolean z) {
        this.add_text_edit_text.setFocusable(z);
        this.add_text_edit_text.setFocusableInTouchMode(z);
        this.add_text_edit_text.setClickable(z);
    }

    private List<ImageView> getTextFunctions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.image_view_keyboard);
        arrayList.add(this.image_view_fonts);
        arrayList.add(this.image_view_color);
        arrayList.add(this.image_view_align);
        arrayList.add(this.image_view_save_change);
        return arrayList;
    }

    public List<CarouselPicker.PickerItem> getTextTextures() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < 42; i++) {
            try {
                AssetManager assets = getContext().getAssets();
                arrayList.add(new CarouselPicker.DrawableItem(Drawable.createFromStream(assets.open("text_texture/" + (i + 1) + ".jpg"), (String) null)));
            } catch (Exception e) {
            }
        }
        return arrayList;
    }

    public List<CarouselPicker.PickerItem> getColorItems() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new CarouselPicker.ColorItem("#f1948a"));
        arrayList.add(new CarouselPicker.ColorItem("#e74c3c"));
        arrayList.add(new CarouselPicker.ColorItem("#DC143C"));
        arrayList.add(new CarouselPicker.ColorItem("#FF0000"));
        arrayList.add(new CarouselPicker.ColorItem("#bb8fce"));
        arrayList.add(new CarouselPicker.ColorItem("#8e44ad"));
        arrayList.add(new CarouselPicker.ColorItem("#6c3483"));
        arrayList.add(new CarouselPicker.ColorItem("#FF00FF"));
        arrayList.add(new CarouselPicker.ColorItem("#3498db"));
        arrayList.add(new CarouselPicker.ColorItem("#2874a6"));
        arrayList.add(new CarouselPicker.ColorItem("#1b4f72"));
        arrayList.add(new CarouselPicker.ColorItem("#0000FF"));
        arrayList.add(new CarouselPicker.ColorItem("#73c6b6"));
        arrayList.add(new CarouselPicker.ColorItem("#16a085"));
        arrayList.add(new CarouselPicker.ColorItem("#117a65"));
        arrayList.add(new CarouselPicker.ColorItem("#0b5345"));
        arrayList.add(new CarouselPicker.ColorItem("#ffffff"));
        arrayList.add(new CarouselPicker.ColorItem("#d7dbdd"));
        arrayList.add(new CarouselPicker.ColorItem("#bdc3c7"));
        arrayList.add(new CarouselPicker.ColorItem("#909497"));
        arrayList.add(new CarouselPicker.ColorItem("#626567"));
        arrayList.add(new CarouselPicker.ColorItem("#000000"));
        arrayList.add(new CarouselPicker.ColorItem("#239b56"));
        arrayList.add(new CarouselPicker.ColorItem("#186a3b"));
        arrayList.add(new CarouselPicker.ColorItem("#f8c471"));
        arrayList.add(new CarouselPicker.ColorItem("#f39c12"));
        arrayList.add(new CarouselPicker.ColorItem("#FFA500"));
        arrayList.add(new CarouselPicker.ColorItem("#FFFF00"));
        arrayList.add(new CarouselPicker.ColorItem("#7e5109"));
        arrayList.add(new CarouselPicker.ColorItem("#e59866"));
        arrayList.add(new CarouselPicker.ColorItem("#d35400"));
        arrayList.add(new CarouselPicker.ColorItem("#a04000"));
        arrayList.add(new CarouselPicker.ColorItem("#6e2c00"));
        arrayList.add(new CarouselPicker.ColorItem("#808b96"));
        arrayList.add(new CarouselPicker.ColorItem("#2c3e50"));
        arrayList.add(new CarouselPicker.ColorItem("#212f3d"));
        arrayList.add(new CarouselPicker.ColorItem("#17202a"));
        return arrayList;
    }
}
