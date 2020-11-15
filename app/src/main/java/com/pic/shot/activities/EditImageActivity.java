package com.pic.shot.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.hold1.keyboardheightprovider.KeyboardHeightProvider;
import com.pic.shot.R;
import com.pic.shot.adapters.AdjustAdapter;
import com.pic.shot.adapters.ColorAdapter;
import com.pic.shot.adapters.MagicBrushAdapter;
import com.pic.shot.adapters.RecyclerTabLayout;
import com.pic.shot.adapters.StickerAdapter;
import com.pic.shot.adapters.ToolsAdapter;
import com.pic.shot.adapters.TopTabEditAdapter;
import com.pic.shot.addtext.AddTextProperties;
import com.pic.shot.ads.AdmobAds;
import com.pic.shot.clicklistener.AdjustListener;
import com.pic.shot.clicklistener.BrushColorListener;
import com.pic.shot.clicklistener.BrushMagicListener;
import com.pic.shot.constants.Constants;
import com.pic.shot.filters.FilterListener;
import com.pic.shot.filters.FilterUtils;
import com.pic.shot.filters.FilterViewAdapter;
import com.pic.shot.fragment.AddTextFragment;
import com.pic.shot.fragment.BeautyFragment;
import com.pic.shot.fragment.ColorSplashFragment;
import com.pic.shot.fragment.CropFragment;
import com.pic.shot.fragment.MosaicFragment;
import com.pic.shot.fragment.RatioFragment;
import com.pic.shot.fragment.SplashFragment;
import com.pic.shot.photoeditor.DrawBitmapModel;
import com.pic.shot.photoeditor.OnPhotoEditorListener;
import com.pic.shot.photoeditor.OnSaveBitmap;
import com.pic.shot.photoeditor.PhotoEditor;
import com.pic.shot.photoeditor.PhotoEditorView;
import com.pic.shot.photoeditor.ViewType;
import com.pic.shot.picker.PhotoPicker;
import com.pic.shot.picker.utils.PermissionsUtils;
import com.pic.shot.sticker.BitmapStickerIcon;
import com.pic.shot.sticker.DrawableSticker;
import com.pic.shot.sticker.Sticker;
import com.pic.shot.sticker.StickerView;
import com.pic.shot.sticker.TextSticker;
import com.pic.shot.sticker.event.AlignHorizontallyEvent;
import com.pic.shot.sticker.event.DeleteIconEvent;
import com.pic.shot.sticker.event.EditTextIconEvent;
import com.pic.shot.sticker.event.FlipHorizontallyEvent;
import com.pic.shot.sticker.event.ZoomIconEvent;
import com.pic.shot.type.ToolType;
import com.pic.shot.utils.AssetUtils;
import com.pic.shot.utils.FileUtils;
import com.pic.shot.utils.SharePreferenceUtil;
import com.pic.shot.utils.SystemUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.wysaid.myUtils.MsgUtil;
import org.wysaid.nativePort.CGENativeLibrary;

public class EditImageActivity extends BaseActivity implements OnPhotoEditorListener, View.OnClickListener, StickerAdapter.OnClickStickerListener, CropFragment.OnCropPhoto, BrushColorListener, BrushMagicListener, RatioFragment.RatioSaveListener, SplashFragment.SplashDialogListener, BeautyFragment.OnBeautySave, MosaicFragment.MosaicDialogListener, ToolsAdapter.OnItemSelected, FilterListener, AdjustListener {
    private static final String TAG = "EditImageActivity";
    private ConstraintLayout constraint_layout_adjust;
    private ConstraintLayout constraint_layout_brush;
    private ConstraintLayout constraint_layout_confirm_text;
    public ConstraintLayout constraint_layout_filter;
    public ConstraintLayout constraint_layout_overlay;
    private ConstraintLayout constraint_layout_root_view;
    private ConstraintLayout constraint_layout_sticker;
    private ConstraintLayout constraint_save_control;
    public ToolType currentMode = ToolType.NONE;
    private ImageView image_view_compare_adjust;
    public ImageView image_view_compare_filter;
    public ImageView image_view_compare_overlay;
    private ImageView image_view_erase;
    private ImageView image_view_redo;
    private ImageView image_view_undo;
    private KeyboardHeightProvider keyboardHeightProvider;
    public LinearLayout linear_layout_wrapper_sticker_list;
    public ArrayList lstBitmapWithFilter = new ArrayList();
    public List<Bitmap> lstBitmapWithOverlay = new ArrayList();
    public AdjustAdapter mAdjustAdapter;
    private final ToolsAdapter mEditingToolsAdapter = new ToolsAdapter(this);
    public CGENativeLibrary.LoadImageCallback mLoadImageCallback = new CGENativeLibrary.LoadImageCallback() {
        public Bitmap loadImage(String str, Object obj) {
            try {
                return BitmapFactory.decodeStream(EditImageActivity.this.getAssets().open(str));
            } catch (IOException e) {
                return null;
            }
        }

        public void loadImageOK(Bitmap bitmap, Object obj) {
            bitmap.recycle();
        }
    };
    View.OnTouchListener onCompareTouchListener = new View.OnTouchListener() {
        public final boolean onTouch(View view, MotionEvent motionEvent) {
            return EditImageActivity.this.lambda$new$0$EditImageActivity(view, motionEvent);
        }
    };
    public PhotoEditor photoEditor;
    public PhotoEditorView photo_editor_view;
    private RecyclerView recycler_view_adjust;
    private RecyclerView recycler_view_brush_color;
    public RecyclerView recycler_view_filter;
    private RecyclerView recycler_view_magic_brush;
    public RecyclerView recycler_view_overlay;
    public RecyclerView recycler_view_tools;
    public RelativeLayout relative_layout_add_sticker;
    private RelativeLayout relative_layout_add_text;
    public RelativeLayout relative_layout_wrapper_photo;
    private SeekBar seekbar_adjust;
    public SeekBar seekbar_alpha;
    private SeekBar seekbar_brush_size;
    private SeekBar seekbar_erase_size;
    public SeekBar seekbar_filter;
    public SeekBar seekbar_overlay;
    public AddTextFragment.TextEditor textEditor;
    public AddTextFragment textEditorDialogFragment;
    private TextView text_view_draw;
    private TextView text_view_magic;
    private TextView text_view_neon;

    public /* synthetic */ boolean lambda$new$0$EditImageActivity(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.photo_editor_view.getGLSurfaceView().setAlpha(0.0f);
            return true;
        } else if (action != 1) {
            return true;
        } else {
            this.photo_editor_view.getGLSurfaceView().setAlpha(1.0f);
            return false;
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        makeFullScreen();
        setContentView((int) R.layout.activity_edit_image);
        if (Constants.SHOW_ADS) {
            AdmobAds.loadBanner(this);
        } else {
            findViewById(R.id.adsContainer).setVisibility(View.GONE);
        }
        initViews();
        CGENativeLibrary.setLoadImageCallback(this.mLoadImageCallback, (Object) null);
        if (Build.VERSION.SDK_INT < 26) {
            getWindow().setSoftInputMode(48);
        }
        this.recycler_view_tools.setLayoutManager(new GridLayoutManager(this, 6));
        this.recycler_view_tools.setAdapter(this.mEditingToolsAdapter);
        this.recycler_view_tools.setHasFixedSize(true);
        this.recycler_view_tools.setAdapter(this.mEditingToolsAdapter);
        this.recycler_view_filter.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_filter.setHasFixedSize(true);
        this.recycler_view_overlay.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_overlay.setHasFixedSize(true);
        new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        this.recycler_view_adjust.setLayoutManager(new GridLayoutManager(this, 4));
        this.recycler_view_adjust.setHasFixedSize(true);
        AdjustAdapter adjustAdapter = new AdjustAdapter(getApplicationContext(), this);
        this.mAdjustAdapter = adjustAdapter;
        this.recycler_view_adjust.setAdapter(adjustAdapter);
        this.recycler_view_brush_color.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_brush_color.setHasFixedSize(true);
        this.recycler_view_brush_color.setAdapter(new ColorAdapter(getApplicationContext(), this));
        this.recycler_view_magic_brush.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_magic_brush.setHasFixedSize(true);
        this.recycler_view_magic_brush.setAdapter(new MagicBrushAdapter(getApplicationContext(), this));
        PhotoEditor build = new PhotoEditor.Builder(this, this.photo_editor_view).setPinchTextScalable(true).build();
        this.photoEditor = build;
        build.setOnPhotoEditorListener(this);
        toogleDrawBottomToolbar(false);
        this.constraint_layout_brush.setAlpha(0.0f);
        this.constraint_layout_adjust.setAlpha(0.0f);
        this.constraint_layout_filter.setAlpha(0.0f);
        this.constraint_layout_sticker.setAlpha(0.0f);
        this.constraint_layout_confirm_text.setAlpha(0.0f);
        this.constraint_layout_overlay.setAlpha(0.0f);
        findViewById(R.id.linear_layout_editor).post(new Runnable() {
            public final void run() {
                EditImageActivity.this.lambda$onCreate$1$EditImageActivity();
            }
        });
        new Handler().postDelayed(new Runnable() {
            public final void run() {
                EditImageActivity.this.lambda$onCreate$2$EditImageActivity();
            }
        }, 1000);
        SharePreferenceUtil.setHeightOfKeyboard(getApplicationContext(), 0);
        KeyboardHeightProvider keyboardHeightProvider2 = new KeyboardHeightProvider(this);
        this.keyboardHeightProvider = keyboardHeightProvider2;
        keyboardHeightProvider2.addKeyboardListener(new KeyboardHeightProvider.KeyboardListener() {
            public final void onHeightChanged(int i) {
                EditImageActivity.this.lambda$onCreate$3$EditImageActivity(i);
            }
        });
        if (SharePreferenceUtil.isPurchased(getApplicationContext())) {
            ((ConstraintLayout.LayoutParams) this.relative_layout_wrapper_photo.getLayoutParams()).topMargin = SystemUtil.dpToPx(getApplicationContext(), 5);
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            new OnLoadBitmapFromUri().execute(new String[]{extras.getString(PhotoPicker.KEY_SELECTED_PHOTOS)});
        }
    }

    public /* synthetic */ void lambda$onCreate$1$EditImageActivity() {
        slideDown(this.constraint_layout_brush);
        slideDown(this.constraint_layout_adjust);
        slideDown(this.constraint_layout_filter);
        slideDown(this.constraint_layout_sticker);
        slideDown(this.constraint_layout_confirm_text);
        slideDown(this.constraint_layout_overlay);
    }

    public /* synthetic */ void lambda$onCreate$2$EditImageActivity() {
        this.constraint_layout_brush.setAlpha(1.0f);
        this.constraint_layout_adjust.setAlpha(1.0f);
        this.constraint_layout_filter.setAlpha(1.0f);
        this.constraint_layout_sticker.setAlpha(1.0f);
        this.constraint_layout_confirm_text.setAlpha(1.0f);
        this.constraint_layout_overlay.setAlpha(1.0f);
    }

    public /* synthetic */ void lambda$onCreate$3$EditImageActivity(int i) {
        if (i <= 0) {
            SharePreferenceUtil.setHeightOfNotch(getApplicationContext(), -i);
            return;
        }
        AddTextFragment addTextFragment = this.textEditorDialogFragment;
        if (addTextFragment != null) {
            addTextFragment.updateAddTextBottomToolbarHeight(SharePreferenceUtil.getHeightOfNotch(getApplicationContext()) + i);
            SharePreferenceUtil.setHeightOfKeyboard(getApplicationContext(), SharePreferenceUtil.getHeightOfNotch(getApplicationContext()) + i);
        }
    }

    private void toogleDrawBottomToolbar(boolean z) {
        int i = 8;
        if (z) {
            i = 0;
        }
//        int i = !z ? 8 : 0;
        this.text_view_draw.setVisibility(i);
        this.text_view_magic.setVisibility(i);
        this.text_view_neon.setVisibility(i);
        this.image_view_erase.setVisibility(i);
        this.image_view_undo.setVisibility(i);
        this.image_view_redo.setVisibility(i);
    }

    public void showEraseBrush() {
        this.seekbar_brush_size.setVisibility(View.GONE);
        this.recycler_view_brush_color.setVisibility(View.GONE);
        this.seekbar_erase_size.setVisibility(View.VISIBLE);
        this.recycler_view_magic_brush.setVisibility(View.GONE);
        this.text_view_draw.setBackgroundResource(0);
        this.text_view_draw.setTextColor(ContextCompat.getColor(this, R.color.itemColor));
        this.text_view_magic.setBackgroundResource(0);
        this.text_view_magic.setTextColor(ContextCompat.getColor(this, R.color.itemColor));
        this.text_view_neon.setBackgroundResource(0);
        this.text_view_neon.setTextColor(ContextCompat.getColor(this, R.color.itemColor));
        this.image_view_erase.setImageResource(R.drawable.erase_selected);
        this.photoEditor.brushEraser();
        this.seekbar_erase_size.setProgress(20);
    }

    public void showColorBlurBrush() {
        this.seekbar_brush_size.setVisibility(View.VISIBLE);
        this.recycler_view_brush_color.setVisibility(View.VISIBLE);
        ColorAdapter colorAdapter = (ColorAdapter) this.recycler_view_brush_color.getAdapter();
        if (colorAdapter != null) {
            colorAdapter.setSelectedColorIndex(0);
        }
        this.recycler_view_brush_color.scrollToPosition(0);
        if (colorAdapter != null) {
            colorAdapter.notifyDataSetChanged();
        }
        this.seekbar_erase_size.setVisibility(View.GONE);
        this.recycler_view_magic_brush.setVisibility(View.GONE);
        this.image_view_erase.setImageResource(R.drawable.erase);
        this.text_view_magic.setBackgroundResource(0);
        this.text_view_magic.setTextColor(ContextCompat.getColor(this, R.color.itemColor));
        this.text_view_draw.setBackgroundResource(0);
        this.text_view_draw.setTextColor(ContextCompat.getColor(this, R.color.itemColor));
        this.text_view_neon.setTextColor(ContextCompat.getColor(this, R.color.mainColor));
        this.photoEditor.setBrushMode(2);
        this.photoEditor.setBrushDrawingMode(true);
        this.seekbar_brush_size.setProgress(20);
    }

    public void showColorBrush() {
        this.seekbar_brush_size.setVisibility(View.VISIBLE);
        this.recycler_view_brush_color.setVisibility(View.VISIBLE);
        this.recycler_view_brush_color.scrollToPosition(0);
        ColorAdapter colorAdapter = (ColorAdapter) this.recycler_view_brush_color.getAdapter();
        if (colorAdapter != null) {
            colorAdapter.setSelectedColorIndex(0);
        }
        if (colorAdapter != null) {
            colorAdapter.notifyDataSetChanged();
        }
        this.seekbar_erase_size.setVisibility(View.VISIBLE);
        this.recycler_view_magic_brush.setVisibility(View.GONE);
        this.image_view_erase.setImageResource(R.drawable.erase);
        this.text_view_magic.setBackgroundResource(0);
        this.text_view_magic.setTextColor(ContextCompat.getColor(this, R.color.itemColor));
        this.text_view_draw.setTextColor(ContextCompat.getColor(this, R.color.mainColor));
        this.text_view_neon.setBackgroundResource(0);
        this.text_view_neon.setTextColor(ContextCompat.getColor(this, R.color.itemColor));
        this.photoEditor.setBrushMode(1);
        this.photoEditor.setBrushDrawingMode(true);
        this.seekbar_brush_size.setProgress(20);
    }

    public void showMagicBrush() {
        this.seekbar_brush_size.setVisibility(View.VISIBLE);
        this.recycler_view_brush_color.setVisibility(View.GONE);
        this.seekbar_erase_size.setVisibility(View.GONE);
        this.recycler_view_magic_brush.setVisibility(View.VISIBLE);
        this.image_view_erase.setImageResource(R.drawable.erase);
        this.text_view_magic.setTextColor(ContextCompat.getColor(this, R.color.mainColor));
        this.text_view_draw.setBackgroundResource(0);
        this.text_view_draw.setTextColor(ContextCompat.getColor(this, R.color.itemColor));
        this.text_view_neon.setBackgroundResource(0);
        this.text_view_neon.setTextColor(ContextCompat.getColor(this, R.color.itemColor));
        this.photoEditor.setBrushMagic(MagicBrushAdapter.lstDrawBitmapModel(getApplicationContext()).get(0));
        this.photoEditor.setBrushMode(3);
        this.photoEditor.setBrushDrawingMode(true);
        MagicBrushAdapter magicBrushAdapter = (MagicBrushAdapter) this.recycler_view_magic_brush.getAdapter();
        if (magicBrushAdapter != null) {
            magicBrushAdapter.setSelectedColorIndex(0);
        }
        this.recycler_view_magic_brush.scrollToPosition(0);
        if (magicBrushAdapter != null) {
            magicBrushAdapter.notifyDataSetChanged();
        }
    }

    private void initViews() {
        this.linear_layout_wrapper_sticker_list = (LinearLayout) findViewById(R.id.linear_layout_wrapper_sticker_list);
        PhotoEditorView photoEditorView = (PhotoEditorView) findViewById(R.id.photo_editor_view);
        this.photo_editor_view = photoEditorView;
        photoEditorView.setVisibility(View.INVISIBLE);
        this.recycler_view_tools = (RecyclerView) findViewById(R.id.recycler_view_tools);
        this.recycler_view_filter = (RecyclerView) findViewById(R.id.recycler_view_filter);
        this.recycler_view_overlay = (RecyclerView) findViewById(R.id.recycler_view_overlay);
        this.recycler_view_adjust = (RecyclerView) findViewById(R.id.recycler_view_adjust);
        this.constraint_layout_root_view = (ConstraintLayout) findViewById(R.id.constraint_layout_root_view);
        this.constraint_layout_filter = (ConstraintLayout) findViewById(R.id.constraint_layout_filter);
        this.constraint_layout_overlay = (ConstraintLayout) findViewById(R.id.constraint_layout_overlay);
        this.constraint_layout_adjust = (ConstraintLayout) findViewById(R.id.constraint_layout_adjust);
        this.constraint_layout_sticker = (ConstraintLayout) findViewById(R.id.constraint_layout_sticker);
        this.constraint_layout_confirm_text = (ConstraintLayout) findViewById(R.id.constraint_layout_confirm_text);
        ViewPager sticker_viewpaper = (ViewPager) findViewById(R.id.sticker_viewpaper);
        this.seekbar_filter = (SeekBar) findViewById(R.id.seekbar_filter);
        this.seekbar_overlay = (SeekBar) findViewById(R.id.seekbar_overlay);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar_alpha);
        this.seekbar_alpha = seekBar;
        seekBar.setVisibility(View.GONE);
        this.constraint_layout_brush = (ConstraintLayout) findViewById(R.id.constraint_layout_brush);
        this.recycler_view_brush_color = (RecyclerView) findViewById(R.id.recycler_view_brush_color);
        this.recycler_view_magic_brush = (RecyclerView) findViewById(R.id.recycler_view_magic_brush);
        this.relative_layout_wrapper_photo = (RelativeLayout) findViewById(R.id.relative_layout_wrapper_photo);
        this.text_view_draw = (TextView) findViewById(R.id.text_view_draw);
        this.text_view_magic = (TextView) findViewById(R.id.text_view_magic);
        this.image_view_erase = (ImageView) findViewById(R.id.image_view_erase);
        ImageView imageView = (ImageView) findViewById(R.id.image_view_undo);
        this.image_view_undo = imageView;
        imageView.setVisibility(View.GONE);
        ImageView imageView2 = (ImageView) findViewById(R.id.image_view_redo);
        this.image_view_redo = imageView2;
        imageView2.setVisibility(View.GONE);
        this.text_view_neon = (TextView) findViewById(R.id.text_view_neon);
        this.seekbar_brush_size = (SeekBar) findViewById(R.id.seekbar_brush_size);
        this.seekbar_erase_size = (SeekBar) findViewById(R.id.seekbar_erase_size);
        this.constraint_save_control = (ConstraintLayout) findViewById(R.id.constraint_save_control);
        ((TextView) findViewById(R.id.text_view_save)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EditImageActivity.this.lambda$initViews$4$EditImageActivity(view);
            }
        });
        ImageView imageView3 = (ImageView) findViewById(R.id.image_view_compare_adjust);
        this.image_view_compare_adjust = imageView3;
        imageView3.setOnTouchListener(this.onCompareTouchListener);
        this.image_view_compare_adjust.setVisibility(View.GONE);
        ImageView imageView4 = (ImageView) findViewById(R.id.image_view_compare_filter);
        this.image_view_compare_filter = imageView4;
        imageView4.setOnTouchListener(this.onCompareTouchListener);
        this.image_view_compare_filter.setVisibility(View.GONE);
        ImageView imageView5 = (ImageView) findViewById(R.id.image_view_compare_overlay);
        this.image_view_compare_overlay = imageView5;
        imageView5.setOnTouchListener(this.onCompareTouchListener);
        this.image_view_compare_overlay.setVisibility(View.GONE);
        findViewById(R.id.image_view_exit).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EditImageActivity.this.lambda$initViews$5$EditImageActivity(view);
            }
        });
        this.image_view_erase.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EditImageActivity.this.lambda$initViews$6$EditImageActivity(view);
            }
        });
        this.text_view_draw.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EditImageActivity.this.lambda$initViews$7$EditImageActivity(view);
            }
        });
        this.text_view_magic.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EditImageActivity.this.lambda$initViews$8$EditImageActivity(view);
            }
        });
        this.text_view_neon.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EditImageActivity.this.lambda$initViews$9$EditImageActivity(view);
            }
        });
        this.seekbar_erase_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                EditImageActivity.this.photoEditor.setBrushEraserSize((float) i);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                EditImageActivity.this.photoEditor.brushEraser();
            }
        });
        this.seekbar_brush_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                EditImageActivity.this.photoEditor.setBrushSize((float) (i + 10));
            }
        });
        this.seekbar_alpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Sticker currentSticker = EditImageActivity.this.photo_editor_view.getCurrentSticker();
                if (currentSticker != null) {
                    currentSticker.setAlpha(i);
                }
            }
        });
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout_add_sticker);
        this.relative_layout_add_sticker = relativeLayout;
        relativeLayout.setVisibility(View.GONE);
        this.relative_layout_add_sticker.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EditImageActivity.this.lambda$initViews$10$EditImageActivity(view);
            }
        });
        RelativeLayout relativeLayout2 = (RelativeLayout) findViewById(R.id.relative_layout_add_text);
        this.relative_layout_add_text = relativeLayout2;
        relativeLayout2.setVisibility(View.GONE);
        this.relative_layout_add_text.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EditImageActivity.this.lambda$initViews$11$EditImageActivity(view);
            }
        });
        SeekBar seekBar2 = (SeekBar) findViewById(R.id.seekbar_adjust);
        this.seekbar_adjust = seekBar2;
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                EditImageActivity.this.mAdjustAdapter.getCurrentAdjustModel().setSeekBarIntensity(EditImageActivity.this.photoEditor, ((float) i) / ((float) seekBar.getMax()), true);
            }
        });
        BitmapStickerIcon bitmapStickerIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.sticker_ic_close_white_18dp), 0, BitmapStickerIcon.REMOVE);
        bitmapStickerIcon.setIconEvent(new DeleteIconEvent());
        BitmapStickerIcon bitmapStickerIcon2 = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.ic_sticker_ic_scale_black_18dp), 3, BitmapStickerIcon.ZOOM);
        bitmapStickerIcon2.setIconEvent(new ZoomIconEvent());
        BitmapStickerIcon bitmapStickerIcon3 = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.ic_sticker_ic_flip_black_18dp), 1, BitmapStickerIcon.FLIP);
        bitmapStickerIcon3.setIconEvent(new FlipHorizontallyEvent());
        BitmapStickerIcon bitmapStickerIcon4 = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.ic_rotate_black_18dp), 3, BitmapStickerIcon.ROTATE);
        bitmapStickerIcon4.setIconEvent(new ZoomIconEvent());
        BitmapStickerIcon bitmapStickerIcon5 = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.ic_edit_black_18dp), 1, BitmapStickerIcon.EDIT);
        bitmapStickerIcon5.setIconEvent(new EditTextIconEvent());
        BitmapStickerIcon bitmapStickerIcon6 = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.ic_center_black_18dp), 2, BitmapStickerIcon.ALIGN_HORIZONTALLY);
        bitmapStickerIcon6.setIconEvent(new AlignHorizontallyEvent());
        this.photo_editor_view.setIcons(Arrays.asList(new BitmapStickerIcon[]{bitmapStickerIcon, bitmapStickerIcon2, bitmapStickerIcon3, bitmapStickerIcon5, bitmapStickerIcon4, bitmapStickerIcon6}));
        this.photo_editor_view.setBackgroundColor(-16777216);
        this.photo_editor_view.setLocked(false);
        this.photo_editor_view.setConstrained(true);
        this.photo_editor_view.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            public void onStickerDragFinished(Sticker sticker) {
            }

            public void onStickerFlipped(Sticker sticker) {
            }

            public void onStickerTouchedDown(Sticker sticker) {
            }

            public void onStickerZoomFinished(Sticker sticker) {
            }

            public void onTouchDownForBeauty(float f, float f2) {
            }

            public void onTouchDragForBeauty(float f, float f2) {
            }

            public void onTouchUpForBeauty(float f, float f2) {
            }

            public void onStickerAdded(Sticker sticker) {
                EditImageActivity.this.seekbar_alpha.setVisibility(View.VISIBLE);
                EditImageActivity.this.seekbar_alpha.setProgress(sticker.getAlpha());
            }

            public void onStickerClicked(Sticker sticker) {
                if (sticker instanceof TextSticker) {
                    ((TextSticker) sticker).setTextColor(SupportMenu.CATEGORY_MASK);
                    EditImageActivity.this.photo_editor_view.replace(sticker);
                    EditImageActivity.this.photo_editor_view.invalidate();
                }
                EditImageActivity.this.seekbar_alpha.setVisibility(View.VISIBLE);
                EditImageActivity.this.seekbar_alpha.setProgress(sticker.getAlpha());
            }

            public void onStickerDeleted(Sticker sticker) {
                EditImageActivity.this.seekbar_alpha.setVisibility(View.GONE);
            }

            public void onStickerTouchOutside() {
                EditImageActivity.this.seekbar_alpha.setVisibility(View.GONE);
            }

            public void onStickerDoubleTapped(Sticker sticker) {
                if (sticker instanceof TextSticker) {
                    sticker.setShow(false);
                    EditImageActivity.this.photo_editor_view.setHandlingSticker((Sticker) null);
                    EditImageActivity editImageActivity = EditImageActivity.this;
                    editImageActivity.textEditorDialogFragment = AddTextFragment.show(editImageActivity, ((TextSticker) sticker).getAddTextProperties());
                    EditImageActivity.this.textEditor = new AddTextFragment.TextEditor() {
                        public void onDone(AddTextProperties addTextProperties) {
                            EditImageActivity.this.photo_editor_view.getStickers().remove(EditImageActivity.this.photo_editor_view.getLastHandlingSticker());
                            EditImageActivity.this.photo_editor_view.addSticker(new TextSticker(EditImageActivity.this, addTextProperties));
                        }

                        public void onBackButton() {
                            EditImageActivity.this.photo_editor_view.showLastHandlingSticker();
                        }
                    };
                    EditImageActivity.this.textEditorDialogFragment.setOnTextEditorListener(EditImageActivity.this.textEditor);
                }
            }
        });
        this.seekbar_filter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                EditImageActivity.this.photo_editor_view.setFilterIntensity(((float) i) / 100.0f);
            }
        });
        this.seekbar_overlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                EditImageActivity.this.photo_editor_view.setFilterIntensity(((float) i) / 100.0f);
            }
        });
        getWindowManager().getDefaultDisplay().getSize(new Point());
        sticker_viewpaper.setAdapter(new PagerAdapter() {
            public int getCount() {
                return 13;
            }

            public boolean isViewFromObject(View view, Object obj) {
                return view.equals(obj);
            }

            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            public Object instantiateItem(ViewGroup viewGroup, int i) {
                View inflate = LayoutInflater.from(EditImageActivity.this.getBaseContext()).inflate(R.layout.sticker_list, (ViewGroup) null, false);
                RecyclerView recycler_view_sticker = (RecyclerView) inflate.findViewById(R.id.recycler_view_sticker);
                recycler_view_sticker.setHasFixedSize(true);
                recycler_view_sticker.setLayoutManager(new GridLayoutManager(EditImageActivity.this.getApplicationContext(), 6));
                switch (i) {
                    case 0:
                        recycler_view_sticker.setAdapter(new StickerAdapter(EditImageActivity.this.getApplicationContext(), AssetUtils.lstEmoj(), i, EditImageActivity.this));
                        break;
                    case 1:
                        recycler_view_sticker.setAdapter(new StickerAdapter(EditImageActivity.this.getApplicationContext(), AssetUtils.lstGiddy(), i, EditImageActivity.this));
                        break;
                    case 2:
                        recycler_view_sticker.setAdapter(new StickerAdapter(EditImageActivity.this.getApplicationContext(), AssetUtils.lstTexts(), i, EditImageActivity.this));
                        break;
                    case 3:
                        recycler_view_sticker.setAdapter(new StickerAdapter(EditImageActivity.this.getApplicationContext(), AssetUtils.lstOthers(), i, EditImageActivity.this));
                        break;
                    case 4:
                        recycler_view_sticker.setAdapter(new StickerAdapter(EditImageActivity.this.getApplicationContext(), AssetUtils.lstDiadems(), i, EditImageActivity.this));
                        break;
                    case 5:
                        recycler_view_sticker.setAdapter(new StickerAdapter(EditImageActivity.this.getApplicationContext(), AssetUtils.lstTies(), i, EditImageActivity.this));
                        break;
                    case 6:
                        recycler_view_sticker.setAdapter(new StickerAdapter(EditImageActivity.this.getApplicationContext(), AssetUtils.lstHeardes(), i, EditImageActivity.this));
                        break;
                    case 7:
                        recycler_view_sticker.setAdapter(new StickerAdapter(EditImageActivity.this.getApplicationContext(), AssetUtils.lstCatFaces(), i, EditImageActivity.this));
                        break;
                    case 8:
                        recycler_view_sticker.setAdapter(new StickerAdapter(EditImageActivity.this.getApplicationContext(), AssetUtils.lstEyes(), i, EditImageActivity.this));
                        break;
                    case 9:
                        recycler_view_sticker.setAdapter(new StickerAdapter(EditImageActivity.this.getApplicationContext(), AssetUtils.lstGlasses(), i, EditImageActivity.this));
                        break;
                    case 10:
                        recycler_view_sticker.setAdapter(new StickerAdapter(EditImageActivity.this.getApplicationContext(), AssetUtils.lstTatoos(), i, EditImageActivity.this));
                        break;
                }
                viewGroup.addView(inflate);
                return inflate;
            }
        });
        RecyclerTabLayout recycler_tab_layout = (RecyclerTabLayout) findViewById(R.id.recycler_tab_layout);
        recycler_tab_layout.setUpWithAdapter(new TopTabEditAdapter(sticker_viewpaper, getApplicationContext()));
        recycler_tab_layout.setPositionThreshold(0.5f);
        recycler_tab_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.cardColorDark));
    }

    public /* synthetic */ void lambda$initViews$4$EditImageActivity(View view) {
        if (PermissionsUtils.checkWriteStoragePermission((Activity) this)) {
            new SaveBitmapAsFile().execute(new Void[0]);
        }
    }

    public /* synthetic */ void lambda$initViews$5$EditImageActivity(View view) {
        onBackPressed();
    }

    public /* synthetic */ void lambda$initViews$6$EditImageActivity(View view) {
        showEraseBrush();
    }

    public /* synthetic */ void lambda$initViews$7$EditImageActivity(View view) {
        showColorBrush();
    }

    public /* synthetic */ void lambda$initViews$8$EditImageActivity(View view) {
        showMagicBrush();
    }

    public /* synthetic */ void lambda$initViews$9$EditImageActivity(View view) {
        showColorBlurBrush();
    }

    public /* synthetic */ void lambda$initViews$10$EditImageActivity(View view) {
        this.relative_layout_add_sticker.setVisibility(View.GONE);
        slideUp(this.linear_layout_wrapper_sticker_list);
    }

    public /* synthetic */ void lambda$initViews$11$EditImageActivity(View view) {
        this.photo_editor_view.setHandlingSticker((Sticker) null);
        openTextFragment();
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    public void onAddViewListener(ViewType viewType, int i) {
        Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + i + "]");
    }

    public void onRemoveViewListener(int i) {
        Log.d(TAG, "onRemoveViewListener() called with: numberOfAddedViews = [" + i + "]");
    }

    public void onRemoveViewListener(ViewType viewType, int i) {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + i + "]");
    }

    public void onStartViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    public void onStopViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.image_view_redo) {
            this.photoEditor.redoBrush();
        } else if (id == R.id.image_view_save_adjust) {
            new SaveFilterAsBitmap().execute(new Void[0]);
            this.image_view_compare_adjust.setVisibility(View.GONE);
            slideDown(this.constraint_layout_adjust);
            slideUp(this.recycler_view_tools);
            slideDownSaveView();
            this.currentMode = ToolType.NONE;
        } else if (id != R.id.image_view_undo) {
            switch (id) {
                case R.id.image_view_close_adjust:
                case R.id.image_view_close_filter:
                case R.id.image_view_close_overlay:
                case R.id.image_view_close_paint:
                case R.id.image_view_close_sticker:
                case R.id.image_view_close_text:
                    slideDownSaveView();
                    onBackPressed();
                    return;
                default:
                    switch (id) {
                        case R.id.image_view_save_filter:
                            new SaveFilterAsBitmap().execute(new Void[0]);
                            this.image_view_compare_filter.setVisibility(View.GONE);
                            slideDown(this.constraint_layout_filter);
                            slideUp(this.recycler_view_tools);
                            slideDownSaveView();
                            this.currentMode = ToolType.NONE;
                            return;
                        case R.id.image_view_save_overlay:
                            new SaveFilterAsBitmap().execute(new Void[0]);
                            slideDown(this.constraint_layout_overlay);
                            slideUp(this.recycler_view_tools);
                            this.image_view_compare_overlay.setVisibility(View.GONE);
                            slideDownSaveView();
                            this.currentMode = ToolType.NONE;
                            return;
                        case R.id.image_view_save_paint:
                            runOnUiThread(new Runnable() {
                                public final void run() {
                                    EditImageActivity.this.lambda$onClick$12$EditImageActivity();
                                }
                            });
                            slideDownSaveView();
                            this.currentMode = ToolType.NONE;
                            return;
                        case R.id.image_view_save_sticker:
                            this.photo_editor_view.setHandlingSticker((Sticker) null);
                            this.photo_editor_view.setLocked(true);
                            this.seekbar_alpha.setVisibility(View.GONE);
                            this.relative_layout_add_sticker.setVisibility(View.GONE);
                            if (!this.photo_editor_view.getStickers().isEmpty()) {
                                new SaveStickerAsBitmap().execute(new Void[0]);
                            }
                            slideUp(this.linear_layout_wrapper_sticker_list);
                            slideDown(this.constraint_layout_sticker);
                            slideUp(this.recycler_view_tools);
                            slideDownSaveView();
                            this.currentMode = ToolType.NONE;
                            return;
                        case R.id.image_view_save_text:
                            this.photo_editor_view.setHandlingSticker((Sticker) null);
                            this.photo_editor_view.setLocked(true);
                            this.relative_layout_add_text.setVisibility(View.GONE);
                            if (!this.photo_editor_view.getStickers().isEmpty()) {
                                new SaveStickerAsBitmap().execute(new Void[0]);
                            }
                            slideDown(this.constraint_layout_confirm_text);
                            slideUp(this.recycler_view_tools);
                            slideDownSaveView();
                            this.currentMode = ToolType.NONE;
                            return;
                        default:
                            return;
                    }
            }
        } else {
            this.photoEditor.undoBrush();
        }
    }

    public /* synthetic */ void lambda$onClick$12$EditImageActivity() {
        this.photoEditor.setBrushDrawingMode(false);
        this.image_view_undo.setVisibility(View.GONE);
        this.image_view_redo.setVisibility(View.GONE);
        this.image_view_erase.setVisibility(View.GONE);
        slideDown(this.constraint_layout_brush);
        slideUp(this.recycler_view_tools);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraint_layout_root_view);
        if (!SharePreferenceUtil.isPurchased(getApplicationContext())) {
            constraintSet.connect(this.relative_layout_wrapper_photo.getId(), 3, this.constraint_layout_root_view.getId(), 3, SystemUtil.dpToPx(getApplicationContext(), 50));
        } else {
            constraintSet.connect(this.relative_layout_wrapper_photo.getId(), 3, this.constraint_layout_root_view.getId(), 3, 0);
        }
        constraintSet.connect(this.relative_layout_wrapper_photo.getId(), 1, this.constraint_layout_root_view.getId(), 1, 0);
        constraintSet.connect(this.relative_layout_wrapper_photo.getId(), 4, this.recycler_view_tools.getId(), 3, 0);
        constraintSet.connect(this.relative_layout_wrapper_photo.getId(), 2, this.constraint_layout_root_view.getId(), 2, 0);
        constraintSet.applyTo(this.constraint_layout_root_view);
        this.photo_editor_view.setImageSource(this.photoEditor.getBrushDrawingView().getDrawBitmap(this.photo_editor_view.getCurrentBitmap()));
        this.photoEditor.clearBrushAllViews();
        updateLayout();
    }

    public void onPause() {
        super.onPause();
        this.keyboardHeightProvider.onPause();
    }

    public void onResume() {
        super.onResume();
        this.keyboardHeightProvider.onResume();
    }

    public void isPermissionGranted(boolean z, String str) {
        if (z) {
            new SaveBitmapAsFile().execute(new Void[0]);
        }
    }

    public void onFilterSelected(String str) {
        this.photoEditor.setFilterEffect(str);
        this.seekbar_filter.setProgress(100);
        this.seekbar_overlay.setProgress(70);
        if (this.currentMode == ToolType.OVERLAY) {
            this.photo_editor_view.getGLSurfaceView().setFilterIntensity(0.7f);
        }
    }

    public void openTextFragment() {
        this.textEditorDialogFragment = AddTextFragment.show(this);
        AddTextFragment.TextEditor r0 = new AddTextFragment.TextEditor() {
            public void onDone(AddTextProperties addTextProperties) {
                EditImageActivity.this.photo_editor_view.addSticker(new TextSticker(EditImageActivity.this.getApplicationContext(), addTextProperties));
            }

            public void onBackButton() {
                if (EditImageActivity.this.photo_editor_view.getStickers().isEmpty()) {
                    EditImageActivity.this.onBackPressed();
                }
            }
        };
        this.textEditor = r0;
        this.textEditorDialogFragment.setOnTextEditorListener(r0);
    }

    /* renamed from: com.pic.shot.activities.EditImageActivity$11 */
    static /* synthetic */ class C107611 {
        static final /* synthetic */ int[] $SwitchMap$com$pic$shot$type$ToolType;

        static {
            int[] iArr = new int[ToolType.values().length];
            $SwitchMap$com$pic$shot$type$ToolType = iArr;
            try {
                iArr[ToolType.BRUSH.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$pic$shot$type$ToolType[ToolType.TEXT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$pic$shot$type$ToolType[ToolType.ADJUST.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$pic$shot$type$ToolType[ToolType.FILTER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$pic$shot$type$ToolType[ToolType.STICKER.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$pic$shot$type$ToolType[ToolType.OVERLAY.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$pic$shot$type$ToolType[ToolType.INSTA.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$pic$shot$type$ToolType[ToolType.SPLASH.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$pic$shot$type$ToolType[ToolType.BLUR.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$pic$shot$type$ToolType[ToolType.MOSAIC.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$pic$shot$type$ToolType[ToolType.COLOR.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$pic$shot$type$ToolType[ToolType.CROP.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$pic$shot$type$ToolType[ToolType.BEAUTY.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$pic$shot$type$ToolType[ToolType.NONE.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
        }
    }

    public void onToolSelected(ToolType toolType) {
        this.currentMode = toolType;
        switch (C107611.$SwitchMap$com$pic$shot$type$ToolType[toolType.ordinal()]) {
            case 1:
                showColorBrush();
                this.photoEditor.setBrushDrawingMode(true);
                slideDown(this.recycler_view_tools);
                slideUp(this.constraint_layout_brush);
                slideUpSaveControl();
                toogleDrawBottomToolbar(true);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(this.constraint_layout_root_view);
                if (!SharePreferenceUtil.isPurchased(getApplicationContext())) {
                    constraintSet.connect(this.relative_layout_wrapper_photo.getId(), 3, this.constraint_layout_root_view.getId(), 3, SystemUtil.dpToPx(getApplicationContext(), 50));
                } else {
                    constraintSet.connect(this.relative_layout_wrapper_photo.getId(), 3, this.constraint_layout_root_view.getId(), 3, 0);
                }
                constraintSet.connect(this.relative_layout_wrapper_photo.getId(), 1, this.constraint_layout_root_view.getId(), 1, 0);
                constraintSet.connect(this.relative_layout_wrapper_photo.getId(), 4, this.constraint_layout_brush.getId(), 3, 0);
                constraintSet.connect(this.relative_layout_wrapper_photo.getId(), 2, this.constraint_layout_root_view.getId(), 2, 0);
                constraintSet.applyTo(this.constraint_layout_root_view);
                this.photoEditor.setBrushMode(1);
                updateLayout();
                break;
            case 2:
                slideUpSaveView();
                this.photo_editor_view.setLocked(false);
                openTextFragment();
                slideDown(this.recycler_view_tools);
                slideUp(this.constraint_layout_confirm_text);
                this.relative_layout_add_text.setVisibility(View.VISIBLE);
                break;
            case 3:
                slideUpSaveView();
                this.image_view_compare_adjust.setVisibility(View.VISIBLE);
                AdjustAdapter adjustAdapter = new AdjustAdapter(getApplicationContext(), this);
                this.mAdjustAdapter = adjustAdapter;
                this.recycler_view_adjust.setAdapter(adjustAdapter);
                this.mAdjustAdapter.setSelectedAdjust(0);
                this.photoEditor.setAdjustFilter(this.mAdjustAdapter.getFilterConfig());
                slideUp(this.constraint_layout_adjust);
                slideDown(this.recycler_view_tools);
                break;
            case 4:
                slideUpSaveView();
                new LoadFilterBitmap().execute(new Void[0]);
                break;
            case 5:
                slideUpSaveView();
                this.photo_editor_view.setLocked(false);
                slideDown(this.recycler_view_tools);
                slideUp(this.constraint_layout_sticker);
                break;
            case 6:
                slideUpSaveView();
                new LoadOverlayBitmap().execute(new Void[0]);
                break;
            case 7:
                new ShowInstaDialog().execute(new Void[0]);
                break;
            case 8:
                new ShowSplashDialog(true).execute(new Void[0]);
                break;
            case 9:
                new ShowSplashDialog(false).execute(new Void[0]);
                break;
            case 10:
                new ShowMosaicDialog().execute(new Void[0]);
                break;
            case 11:
                ColorSplashFragment.show(this, this.photo_editor_view.getCurrentBitmap());
                break;
            case 12:
                CropFragment.show(this, this, this.photo_editor_view.getCurrentBitmap());
                break;
            case 13:
                BeautyFragment.show(this, this.photo_editor_view.getCurrentBitmap(), this);
                break;
        }
        this.photo_editor_view.setHandlingSticker((Sticker) null);
    }

    public void slideUp(View view) {
        ObjectAnimator.ofFloat(view, "translationY", new float[]{(float) view.getHeight(), 0.0f}).start();
    }

    public void slideUpSaveView() {
        this.constraint_save_control.setVisibility(View.GONE);
    }

    public void slideUpSaveControl() {
        this.constraint_save_control.setVisibility(View.GONE);
    }

    public void slideDownSaveControl() {
        this.constraint_save_control.setVisibility(View.VISIBLE);
    }

    public void slideDownSaveView() {
        this.constraint_save_control.setVisibility(View.VISIBLE);
    }

    public void slideDown(View view) {
        ObjectAnimator.ofFloat(view, "translationY", new float[]{0.0f, (float) view.getHeight()}).start();
    }

    public void onBackPressed() {
        if (this.currentMode != null) {
            try {
                switch (C107611.$SwitchMap$com$pic$shot$type$ToolType[this.currentMode.ordinal()]) {
                    case 1:
                        slideDown(this.constraint_layout_brush);
                        slideUp(this.recycler_view_tools);
                        slideDownSaveControl();
                        this.image_view_undo.setVisibility(View.GONE);
                        this.image_view_redo.setVisibility(View.GONE);
                        this.image_view_erase.setVisibility(View.GONE);
                        this.photoEditor.setBrushDrawingMode(false);
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(this.constraint_layout_root_view);
                        if (!SharePreferenceUtil.isPurchased(getApplicationContext())) {
                            constraintSet.connect(this.relative_layout_wrapper_photo.getId(), 3, this.constraint_layout_root_view.getId(), 3, SystemUtil.dpToPx(getApplicationContext(), 50));
                        } else {
                            constraintSet.connect(this.relative_layout_wrapper_photo.getId(), 3, this.constraint_layout_root_view.getId(), 3, 0);
                        }
                        constraintSet.connect(this.relative_layout_wrapper_photo.getId(), 1, this.constraint_layout_root_view.getId(), 1, 0);
                        constraintSet.connect(this.relative_layout_wrapper_photo.getId(), 4, this.recycler_view_tools.getId(), 3, 0);
                        constraintSet.connect(this.relative_layout_wrapper_photo.getId(), 2, this.constraint_layout_root_view.getId(), 2, 0);
                        constraintSet.applyTo(this.constraint_layout_root_view);
                        this.photoEditor.clearBrushAllViews();
                        slideDownSaveView();
                        this.currentMode = ToolType.NONE;
                        updateLayout();
                        return;
                    case 2:
                        if (!this.photo_editor_view.getStickers().isEmpty()) {
                            this.photo_editor_view.getStickers().clear();
                            this.photo_editor_view.setHandlingSticker((Sticker) null);
                        }
                        slideDown(this.constraint_layout_confirm_text);
                        this.relative_layout_add_text.setVisibility(View.GONE);
                        this.photo_editor_view.setHandlingSticker((Sticker) null);
                        slideUp(this.recycler_view_tools);
                        this.photo_editor_view.setLocked(true);
                        slideDownSaveView();
                        this.currentMode = ToolType.NONE;
                        return;
                    case 3:
                        this.photoEditor.setFilterEffect("");
                        this.image_view_compare_adjust.setVisibility(View.GONE);
                        slideDown(this.constraint_layout_adjust);
                        slideUp(this.recycler_view_tools);
                        slideDownSaveView();
                        this.currentMode = ToolType.NONE;
                        return;
                    case 4:
                        slideDown(this.constraint_layout_filter);
                        slideUp(this.recycler_view_tools);
                        slideDownSaveView();
                        this.photoEditor.setFilterEffect("");
                        this.image_view_compare_filter.setVisibility(View.GONE);
                        this.lstBitmapWithFilter.clear();
                        if (this.recycler_view_filter.getAdapter() != null) {
                            this.recycler_view_filter.getAdapter().notifyDataSetChanged();
                        }
                        this.currentMode = ToolType.NONE;
                        return;
                    case 5:
                        if (this.photo_editor_view.getStickers().size() <= 0) {
                            slideUp(this.linear_layout_wrapper_sticker_list);
                            slideDown(this.constraint_layout_sticker);
                            this.relative_layout_add_sticker.setVisibility(View.GONE);
                            this.photo_editor_view.setHandlingSticker((Sticker) null);
                            slideUp(this.recycler_view_tools);
                            this.photo_editor_view.setLocked(true);
                            this.currentMode = ToolType.NONE;
                        } else if (this.relative_layout_add_sticker.getVisibility() == View.VISIBLE) {
                            this.photo_editor_view.getStickers().clear();
                            this.relative_layout_add_sticker.setVisibility(View.GONE);
                            this.photo_editor_view.setHandlingSticker((Sticker) null);
                            slideUp(this.linear_layout_wrapper_sticker_list);
                            slideDown(this.constraint_layout_sticker);
                            slideUp(this.recycler_view_tools);
                            this.currentMode = ToolType.NONE;
                        } else {
                            slideDown(this.linear_layout_wrapper_sticker_list);
                            this.relative_layout_add_sticker.setVisibility(View.VISIBLE);
                        }
                        slideDownSaveView();
                        return;
                    case 6:
                        this.photoEditor.setFilterEffect("");
                        this.image_view_compare_overlay.setVisibility(View.GONE);
                        this.lstBitmapWithOverlay.clear();
                        slideUp(this.recycler_view_tools);
                        slideDown(this.constraint_layout_overlay);
                        slideDownSaveView();
                        this.recycler_view_overlay.getAdapter().notifyDataSetChanged();
                        this.currentMode = ToolType.NONE;
                        return;
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 13:
                        showDiscardDialog();
                        return;
                    case 14:
                        showDiscardDialog();
                        return;
                    default:
                        super.onBackPressed();
                        return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void showDiscardDialog() {
        new AlertDialog.Builder(this).setMessage(R.string.dialog_discard_title).setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                EditImageActivity.this.lambda$showDiscardDialog$13$EditImageActivity(dialogInterface, i);
            }
        }).setNegativeButton("Cancel", LambdaEditImageActivity.INSTANCE).create().show();
    }

    public /* synthetic */ void lambda$showDiscardDialog$13$EditImageActivity(DialogInterface dialogInterface, int i) {
        this.currentMode = null;
        finish();
        AdmobAds.showFullAds((AdmobAds.OnAdsCloseListener) null);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onAdjustSelected(AdjustAdapter.AdjustModel adjustModel) {
        Log.d("XXXXXXXX", "onAdjustSelected " + adjustModel.seekbarIntensity + " " + this.seekbar_adjust.getMax());
        this.seekbar_adjust.setProgress((int) (adjustModel.seekbarIntensity * ((float) this.seekbar_adjust.getMax())));
    }

    public void addSticker(Bitmap bitmap) {
        this.photo_editor_view.addSticker(new DrawableSticker(new BitmapDrawable(getResources(), bitmap)));
        slideDown(this.linear_layout_wrapper_sticker_list);
        this.relative_layout_add_sticker.setVisibility(View.VISIBLE);
    }

    public void finishCrop(Bitmap bitmap) {
        this.photo_editor_view.setImageSource(bitmap);
        this.currentMode = ToolType.NONE;
        updateLayout();
    }

    public void onColorChanged(String str) {
        this.photoEditor.setBrushColor(Color.parseColor(str));
    }

    public void ratioSavedBitmap(Bitmap bitmap) {
        this.photo_editor_view.setImageSource(bitmap);
        this.currentMode = ToolType.NONE;
        updateLayout();
    }

    public void onMagicChanged(DrawBitmapModel drawBitmapModel) {
        this.photoEditor.setBrushMagic(drawBitmapModel);
    }

    public void onSaveSplash(Bitmap bitmap) {
        this.photo_editor_view.setImageSource(bitmap);
        this.currentMode = ToolType.NONE;
    }

    public void onSaveMosaic(Bitmap bitmap) {
        this.photo_editor_view.setImageSource(bitmap);
        this.currentMode = ToolType.NONE;
    }

    public void onBeautySave(Bitmap bitmap) {
        this.photo_editor_view.setImageSource(bitmap);
        this.currentMode = ToolType.NONE;
    }

    class LoadFilterBitmap extends AsyncTask<Void, Void, Void> {
        LoadFilterBitmap() {
        }

        public Void doInBackground(Void... voidArr) {
            EditImageActivity.this.lstBitmapWithFilter.clear();
            EditImageActivity.this.lstBitmapWithFilter.addAll(FilterUtils.getLstBitmapWithFilter(ThumbnailUtils.extractThumbnail(EditImageActivity.this.photo_editor_view.getCurrentBitmap(), 100, 100)));
            Log.d("XXXXXXXX", "LoadFilterBitmap " + EditImageActivity.this.lstBitmapWithFilter.size());
            return null;
        }

        public void onPostExecute(Void voidR) {
            RecyclerView recyclerView = EditImageActivity.this.recycler_view_filter;
            ArrayList arrayList = EditImageActivity.this.lstBitmapWithFilter;
            EditImageActivity editImageActivity = EditImageActivity.this;
            recyclerView.setAdapter(new FilterViewAdapter(arrayList, editImageActivity, editImageActivity.getApplicationContext(), Arrays.asList(FilterUtils.FILTERS_CONFIGS)));
            EditImageActivity editImageActivity2 = EditImageActivity.this;
            editImageActivity2.slideDown(editImageActivity2.recycler_view_tools);
            EditImageActivity editImageActivity3 = EditImageActivity.this;
            editImageActivity3.slideUp(editImageActivity3.constraint_layout_filter);
            EditImageActivity.this.image_view_compare_filter.setVisibility(View.VISIBLE);
            EditImageActivity.this.seekbar_filter.setProgress(100);
        }
    }

    class ShowInstaDialog extends AsyncTask<Void, Bitmap, Bitmap> {
        ShowInstaDialog() {
        }

        public Bitmap doInBackground(Void... voidArr) {
            return FilterUtils.getBlurImageFromBitmap(EditImageActivity.this.photo_editor_view.getCurrentBitmap(), 5.0f);
        }

        public void onPostExecute(Bitmap bitmap) {
            EditImageActivity editImageActivity = EditImageActivity.this;
            RatioFragment.show(editImageActivity, editImageActivity, editImageActivity.photo_editor_view.getCurrentBitmap(), bitmap);
        }
    }

    class ShowMosaicDialog extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        ShowMosaicDialog() {
        }

        public List<Bitmap> doInBackground(Void... voidArr) {
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(FilterUtils.cloneBitmap(EditImageActivity.this.photo_editor_view.getCurrentBitmap()));
            arrayList.add(FilterUtils.getBlurImageFromBitmap(EditImageActivity.this.photo_editor_view.getCurrentBitmap(), 8.0f));
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            MosaicFragment.show(EditImageActivity.this, list.get(0), list.get(1), EditImageActivity.this);
        }
    }

    class LoadOverlayBitmap extends AsyncTask<Void, Void, Void> {
        LoadOverlayBitmap() {
        }

        public Void doInBackground(Void... voidArr) {
            EditImageActivity.this.lstBitmapWithOverlay.clear();
            EditImageActivity.this.lstBitmapWithOverlay.addAll(FilterUtils.getLstBitmapWithOverlay(ThumbnailUtils.extractThumbnail(EditImageActivity.this.photo_editor_view.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            RecyclerView recyclerView = EditImageActivity.this.recycler_view_overlay;
            List<Bitmap> list = EditImageActivity.this.lstBitmapWithOverlay;
            EditImageActivity editImageActivity = EditImageActivity.this;
            recyclerView.setAdapter(new FilterViewAdapter(list, editImageActivity, editImageActivity.getApplicationContext(), Arrays.asList(FilterUtils.EFFECTS_CONFIGS)));
            EditImageActivity editImageActivity2 = EditImageActivity.this;
            editImageActivity2.slideDown(editImageActivity2.recycler_view_tools);
            EditImageActivity editImageActivity3 = EditImageActivity.this;
            editImageActivity3.slideUp(editImageActivity3.constraint_layout_overlay);
            EditImageActivity.this.image_view_compare_overlay.setVisibility(View.VISIBLE);
            EditImageActivity.this.seekbar_overlay.setProgress(100);
        }
    }

    class ShowSplashDialog extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        boolean isSplash;

        public ShowSplashDialog(boolean z) {
            this.isSplash = z;
        }

        public List<Bitmap> doInBackground(Void... voidArr) {
            Bitmap currentBitmap = EditImageActivity.this.photo_editor_view.getCurrentBitmap();
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(currentBitmap);
            if (this.isSplash) {
                arrayList.add(FilterUtils.getBlackAndWhiteImageFromBitmap(currentBitmap));
            } else {
                arrayList.add(FilterUtils.getBlurImageFromBitmap(currentBitmap, 3.0f));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            List<Bitmap> list2 = list;
            if (this.isSplash) {
                SplashFragment.show(EditImageActivity.this, list2.get(0), (Bitmap) null, list2.get(1), EditImageActivity.this, true);
            } else {
                SplashFragment.show(EditImageActivity.this, list2.get(0), list2.get(1), (Bitmap) null, EditImageActivity.this, false);
            }
        }
    }

    class SaveFilterAsBitmap extends AsyncTask<Void, Void, Bitmap> {

        SaveFilterAsBitmap() {

        }

        public Bitmap doInBackground(Void... voidArr) {
            Bitmap[] bitmapArr = {null};
            photo_editor_view.saveGLSurfaceViewAsBitmap(new OnSaveBitmap() {
                public Bitmap[] f$0;


                public final void onBitmapReady(Bitmap bitmap) {
                    bitmapArr[0] = bitmap;
                }
            });
            while (bitmapArr[0] == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return bitmapArr[0];
        }


        public void onPostExecute(Bitmap bitmap) {
            EditImageActivity.this.photo_editor_view.setImageSource(bitmap);
            EditImageActivity.this.photo_editor_view.setFilterEffect("");
        }
    }

    class SaveStickerAsBitmap extends AsyncTask<Void, Void, Bitmap> {
        SaveStickerAsBitmap() {
        }

        public void onPreExecute() {
            EditImageActivity.this.photo_editor_view.getGLSurfaceView().setAlpha(0.0f);
        }

        public Bitmap doInBackground(Void... voidArr) {
            Bitmap[] bitmapArr = {null};
            while (bitmapArr[0] == null) {
                try {
                    EditImageActivity.this.photoEditor.saveStickerAsBitmap(new OnSaveBitmap() {

                        public final void onBitmapReady(Bitmap bitmap) {
                            bitmapArr[0] = bitmap;
                        }
                    });
                    while (bitmapArr[0] == null) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e2) {
                }
            }
            return bitmapArr[0];
        }

        public void onPostExecute(Bitmap bitmap) {
            EditImageActivity.this.photo_editor_view.setImageSource(bitmap);
            EditImageActivity.this.photo_editor_view.getStickers().clear();
            EditImageActivity.this.photo_editor_view.getGLSurfaceView().setAlpha(1.0f);
            EditImageActivity.this.updateLayout();
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != 123) {
            return;
        }
        if (i2 == -1) {
            try {
                InputStream openInputStream = getContentResolver().openInputStream(intent.getData());
                Bitmap decodeStream = BitmapFactory.decodeStream(openInputStream);
                float width = (float) decodeStream.getWidth();
                float height = (float) decodeStream.getHeight();
                float max = Math.max(width / 1280.0f, height / 1280.0f);
                if (max > 1.0f) {
                    decodeStream = Bitmap.createScaledBitmap(decodeStream, (int) (width / max), (int) (height / max), false);
                }
                if (SystemUtil.rotateBitmap(decodeStream, new ExifInterface(openInputStream).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)) != decodeStream) {
                    decodeStream.recycle();
                    decodeStream = null;
                }
                this.photo_editor_view.setImageSource(decodeStream);
                updateLayout();
            } catch (Exception e) {
                e.printStackTrace();
                MsgUtil.toastMsg(this, "Error: Can not open image");
            }
        } else {
            finish();
        }
    }

    class OnLoadBitmapFromUri extends AsyncTask<String, Bitmap, Bitmap> {
        OnLoadBitmapFromUri() {
        }

        public Bitmap doInBackground(String... strArr) {
            try {
                Uri fromFile = Uri.fromFile(new File(strArr[0]));
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(EditImageActivity.this.getContentResolver(), fromFile);
                float width = (float) bitmap.getWidth();
                float height = (float) bitmap.getHeight();
                float max = Math.max(width / 1280.0f, height / 1280.0f);
                if (max > 1.0f) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / max), (int) (height / max), false);
                }
                Bitmap rotateBitmap = SystemUtil.rotateBitmap(bitmap, new ExifInterface(EditImageActivity.this.getContentResolver().openInputStream(fromFile)).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1));
                if (rotateBitmap != bitmap) {
                    bitmap.recycle();
                }
                return rotateBitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(Bitmap bitmap) {
            EditImageActivity.this.photo_editor_view.setImageSource(bitmap);
            EditImageActivity.this.updateLayout();
        }
    }

    public void updateLayout() {
        this.photo_editor_view.postDelayed(new Runnable() {
            public final void run() {
                EditImageActivity.this.lambda$updateLayout$15$EditImageActivity();
            }
        }, 300);
    }

    public /* synthetic */ void lambda$updateLayout$15$EditImageActivity() {
        try {
            Display defaultDisplay = getWindowManager().getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            int i = point.x;
            int height = this.relative_layout_wrapper_photo.getHeight();
            int i2 = this.photo_editor_view.getGLSurfaceView().getRenderViewport().width;
            float f = (float) this.photo_editor_view.getGLSurfaceView().getRenderViewport().height;
            float f2 = (float) i2;
            if (((int) ((((float) i) * f) / f2)) <= height) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
                layoutParams.addRule(13);
                this.photo_editor_view.setLayoutParams(layoutParams);
                this.photo_editor_view.setVisibility(View.VISIBLE);
                return;
            }
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams((int) ((((float) height) * f2) / f), -1);
            layoutParams2.addRule(13);
            this.photo_editor_view.setLayoutParams(layoutParams2);
            this.photo_editor_view.setVisibility(View.VISIBLE);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    class SaveBitmapAsFile extends AsyncTask<Void, String, String> {
        SaveBitmapAsFile() {
        }

        public String doInBackground(Void... voidArr) {
            File saveBitmapAsFile = FileUtils.saveBitmapAsFile(EditImageActivity.this.photo_editor_view.getCurrentBitmap());
            try {
                MediaScannerConnection.scanFile(EditImageActivity.this.getApplicationContext(), new String[]{saveBitmapAsFile.getAbsolutePath()}, (String[]) null, new MediaScannerConnection.MediaScannerConnectionClient() {
                    public void onMediaScannerConnected() {
                    }

                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
                EditImageActivity.this.notifyMediaStoreScanner(EditImageActivity.this, saveBitmapAsFile);
                return saveBitmapAsFile.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(String str) {
            if (str == null) {
                Toast.makeText(EditImageActivity.this.getApplicationContext(), "Oop! Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(EditImageActivity.this, ShareActivity.class);
            intent.putExtra("path", str);
            EditImageActivity.this.startActivity(intent);
        }
    }

    /* access modifiers changed from: private */
    public void notifyMediaStoreScanner(Context mContext, File file) {
        try {
            MediaStore.Images.Media.insertImage(mContext.getContentResolver(), file.getAbsolutePath(), file.getName(), (String) null);
            mContext.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
