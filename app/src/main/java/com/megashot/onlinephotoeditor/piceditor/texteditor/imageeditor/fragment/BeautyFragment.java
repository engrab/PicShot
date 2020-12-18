package com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.R;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.filters.DegreeSeekBar;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.photoeditor.OnSaveBitmap;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.photoeditor.PhotoEditorView;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.sticker.BeautySticker;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.sticker.BitmapStickerIcon;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.sticker.Sticker;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.sticker.StickerView;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.sticker.event.ZoomIconEvent;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.utils.SharePreferenceUtil;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.utils.SystemUtil;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.wysaid.nativePort.CGEDeformFilterWrapper;
import org.wysaid.nativePort.CGEImageHandler;
import org.wysaid.texUtils.TextureRenderer;
import org.wysaid.view.ImageGLSurfaceView;

public class BeautyFragment extends DialogFragment {

    public Bitmap bitmap;
    public CGEDeformFilterWrapper cgeDeformFilterWrapper;
    public int currentType = 7;
    private DegreeSeekBar degree_seekBar;
    public ImageGLSurfaceView imageGLSurfaceView;
    private ImageView image_view_boobs;
    private ImageView image_view_compare_beauty;
    private ImageView image_view_face;
    private ImageView image_view_seat;
    private ImageView image_view_waist;
    private LinearLayout linear_layout_seekbar;
    private float mTouchRadiusForWaist;
    public OnBeautySave onBeautySave;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.relative_layout_boobs:
                    BeautyFragment.this.showAdjustBoobs();
                    return;
                case R.id.relative_layout_face:
                    BeautyFragment.this.showAdjustFace();
                    return;
                case R.id.relative_layout_hip:
                    BeautyFragment.this.showAdjustHipOne();
                    return;
                case R.id.relative_layout_reset:
                    BeautyFragment.this.imageGLSurfaceView.flush(true, new Runnable() {
                        public void run() {
                            if (BeautyFragment.this.cgeDeformFilterWrapper != null) {
                                BeautyFragment.this.cgeDeformFilterWrapper.restore();
                                BeautyFragment.this.imageGLSurfaceView.requestRender();
                            }
                        }
                    });
                    return;
                case R.id.relative_layout_waist:
                    BeautyFragment.this.showWaist();
                    return;
                default:
                    return;
            }
        }
    };
    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (BeautyFragment.this.seekbar_smooth.getProgress() == 0) {
                BeautyFragment.this.imageGLSurfaceView.setFilterWithConfig("");
                return;
            }
            ImageGLSurfaceView imageGLSurfaceView = BeautyFragment.this.imageGLSurfaceView;
            imageGLSurfaceView.setFilterWithConfig(MessageFormat.format("@beautify face 1 {0} 640", BeautyFragment.this.seekbar_smooth.getProgress() + ""));
        }
    };
    public PhotoEditorView photo_editor_view;
    private RelativeLayout relative_layout_boobs;
    private RelativeLayout relative_layout_face;
    private RelativeLayout relative_layout_hip;
    private RelativeLayout relative_layout_reset;
    private RelativeLayout relative_layout_waist;
    private List<Retouch> retouchList;
    public SeekBar seekbar_smooth;
    public float startX;
    public float startY;
    private TextView text_view_Seat;
    private TextView text_view_Waist;
    private TextView text_view_boobs;
    private TextView text_view_face;
    private ViewGroup viewGroup;

    public interface OnBeautySave {
        void onBeautySave(Bitmap bitmap);
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public void showWaist() {
        if (SharePreferenceUtil.isFirstAdjustWaise(getContext())) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_waise, this.viewGroup, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(false);
            builder.setView(inflate);
            final AlertDialog create = builder.create();
            inflate.findViewById(R.id.text_view_done).setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    create.dismiss();
                    SharePreferenceUtil.setFirstAdjustWaist(BeautyFragment.this.getContext(), false);
                }
            });
            create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            create.show();
        }
        saveCurrentState();
        selectFunction(1);
        this.linear_layout_seekbar.setVisibility(View.VISIBLE);
        this.photo_editor_view.setHandlingSticker(null);
        this.photo_editor_view.setDrawCirclePoint(true);
        this.relative_layout_reset.setVisibility(View.VISIBLE);
        this.degree_seekBar.setVisibility(View.GONE);
        this.currentType = 3;
        this.degree_seekBar.setCurrentDegrees(0);
        float dpToPx = (float) SystemUtil.dpToPx(getContext(), 20);
        this.mTouchRadiusForWaist = dpToPx;
        this.photo_editor_view.setCircleRadius((int) dpToPx);
        this.photo_editor_view.getStickers().clear();
    }

    private void saveCurrentState() {
        new SaveCurrentState().execute();
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void showAdjustBoobs() {
        if (SharePreferenceUtil.isFirstAdjustBoob(getContext())) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_boobs, this.viewGroup, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(false);
            builder.setView(inflate);
            final AlertDialog create = builder.create();
            inflate.findViewById(R.id.text_view_done).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    create.dismiss();
                    SharePreferenceUtil.setFirstAdjustBoob(BeautyFragment.this.getContext(), false);
                }
            });
            create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            create.show();
        }
        saveCurrentState();
        this.degree_seekBar.setVisibility(View.VISIBLE);
        this.degree_seekBar.setDegreeRange(-30, 30);
        this.relative_layout_reset.setVisibility(View.GONE);
        this.photo_editor_view.setDrawCirclePoint(false);
        selectFunction(0);
        this.linear_layout_seekbar.setVisibility(View.GONE);
        this.currentType = 7;
        this.degree_seekBar.setCurrentDegrees(0);
        this.photo_editor_view.getStickers().clear();
        this.photo_editor_view.addSticker(new BeautySticker(getContext(), 0, ContextCompat.getDrawable(getContext(), R.drawable.circle)));
        this.photo_editor_view.addSticker(new BeautySticker(getContext(), 1, ContextCompat.getDrawable(getContext(), R.drawable.circle)));
    }

    public void showAdjustFace() {
        if (SharePreferenceUtil.isFirstAdjusFace(getContext())) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_chin, this.viewGroup, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(false);
            builder.setView(inflate);
            final AlertDialog create = builder.create();
            inflate.findViewById(R.id.text_view_done).setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    create.dismiss();
                    SharePreferenceUtil.setFirstAdjustFace(BeautyFragment.this.getContext(), false);
                }
            });
            create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            create.show();
        }
        saveCurrentState();
        this.degree_seekBar.setVisibility(View.VISIBLE);
        this.degree_seekBar.setDegreeRange(-15, 15);
        this.relative_layout_reset.setVisibility(View.GONE);
        this.photo_editor_view.setDrawCirclePoint(false);
        selectFunction(3);
        this.currentType = 4;
        this.linear_layout_seekbar.setVisibility(View.GONE);
        this.degree_seekBar.setCurrentDegrees(0);
        this.photo_editor_view.getStickers().clear();
        this.photo_editor_view.addSticker(new BeautySticker(getContext(), 4, ContextCompat.getDrawable(getContext(), R.drawable.chin)));
    }

    public void showAdjustHipOne() {
        if (SharePreferenceUtil.isFirstAdjusHip(getContext())) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_hip, this.viewGroup, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(false);
            builder.setView(inflate);
            final AlertDialog create = builder.create();
            inflate.findViewById(R.id.text_view_done).setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    create.dismiss();
                    SharePreferenceUtil.setFirstAdjustHip(BeautyFragment.this.getContext(), false);
                }
            });
            create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            create.show();
        }
        saveCurrentState();
        this.degree_seekBar.setVisibility(View.VISIBLE);
        this.degree_seekBar.setDegreeRange(-30, 30);
        this.relative_layout_reset.setVisibility(View.GONE);
        this.photo_editor_view.setDrawCirclePoint(false);
        selectFunction(2);
        this.degree_seekBar.setCurrentDegrees(0);
        this.currentType = 9;
        this.photo_editor_view.getStickers().clear();
        this.photo_editor_view.addSticker(new BeautySticker(getContext(), 2, ContextCompat.getDrawable(getContext(), R.drawable.hip_1)));
    }

    public void hideAllFunction() {
        this.degree_seekBar.setVisibility(View.GONE);
        this.relative_layout_reset.setVisibility(View.GONE);
        this.linear_layout_seekbar.setVisibility(View.GONE);
    }

    public void setOnBeautySave(OnBeautySave onBeautySave2) {
        this.onBeautySave = onBeautySave2;
    }

    public static BeautyFragment show(AppCompatActivity appCompatActivity, Bitmap bitmap2, OnBeautySave onBeautySave2) {
        BeautyFragment beautyDialog = new BeautyFragment();
        beautyDialog.setBitmap(bitmap2);
        beautyDialog.setOnBeautySave(onBeautySave2);
        beautyDialog.show(appCompatActivity.getSupportFragmentManager(), "BeautyDialog");
        return beautyDialog;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup2, Bundle bundle) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View inflate = layoutInflater.inflate(R.layout.fragment_beauty, viewGroup2, false);
        DegreeSeekBar degreeSeekBar = inflate.findViewById(R.id.degree_seekBar);
        this.degree_seekBar = degreeSeekBar;
        degreeSeekBar.setDegreeRange(-20, 20);
        PhotoEditorView photoEditorView = inflate.findViewById(R.id.photo_editor_view);
        this.photo_editor_view = photoEditorView;
        this.imageGLSurfaceView = photoEditorView.getGLSurfaceView();
        this.image_view_boobs = inflate.findViewById(R.id.image_view_boobs);
        RelativeLayout relativeLayout = inflate.findViewById(R.id.relative_layout_boobs);
        this.relative_layout_boobs = relativeLayout;
        relativeLayout.setOnClickListener(this.onClickListener);
        this.text_view_boobs = inflate.findViewById(R.id.text_view_boobs);
        this.image_view_waist = inflate.findViewById(R.id.image_view_waist);
        RelativeLayout relativeLayout2 = inflate.findViewById(R.id.relative_layout_waist);
        this.relative_layout_waist = relativeLayout2;
        relativeLayout2.setOnClickListener(this.onClickListener);
        this.text_view_Waist = inflate.findViewById(R.id.text_view_Waist);
        RelativeLayout relativeLayout3 = inflate.findViewById(R.id.relative_layout_reset);
        this.relative_layout_reset = relativeLayout3;
        relativeLayout3.setOnClickListener(this.onClickListener);
        this.image_view_seat = inflate.findViewById(R.id.image_view_seat);
        RelativeLayout relativeLayout4 = inflate.findViewById(R.id.relative_layout_hip);
        this.relative_layout_hip = relativeLayout4;
        relativeLayout4.setOnClickListener(this.onClickListener);
        this.text_view_Seat = inflate.findViewById(R.id.text_view_Seat);
        this.image_view_face = inflate.findViewById(R.id.image_view_face);
        RelativeLayout relativeLayout5 = inflate.findViewById(R.id.relative_layout_face);
        this.relative_layout_face = relativeLayout5;
        relativeLayout5.setOnClickListener(this.onClickListener);
        this.text_view_face = inflate.findViewById(R.id.text_view_face);
        this.linear_layout_seekbar = inflate.findViewById(R.id.linear_layout_seekbar);
        this.viewGroup = viewGroup2;
        SeekBar seekBar = inflate.findViewById(R.id.seekbar_smooth);
        this.seekbar_smooth = seekBar;
        seekBar.setOnSeekBarChangeListener(this.onSeekBarChangeListener);
        ArrayList arrayList = new ArrayList();
        this.retouchList = arrayList;
        arrayList.add(new Retouch(this.image_view_boobs, this.text_view_boobs, R.drawable.boobs, R.drawable.boobs_selected));
        this.retouchList.add(new Retouch(this.image_view_waist, this.text_view_Waist, R.drawable.waist, R.drawable.waist_selected));
        this.retouchList.add(new Retouch(this.image_view_seat, this.text_view_Seat, R.drawable.seat, R.drawable.seat_selected));
        this.retouchList.add(new Retouch(this.image_view_face, this.text_view_face, R.drawable.beauty_face, R.drawable.beauty_face_selected));
        this.degree_seekBar.setScrollingListener(new DegreeSeekBar.ScrollingListener() {
            public void onScrollStart() {
                Iterator<Sticker> it = BeautyFragment.this.photo_editor_view.getStickers().iterator();
                while (it.hasNext()) {
                    ((BeautySticker) it.next()).updateRadius();
                }
            }

            public void onScroll(final int currentDegrees) {
                TextureRenderer.Viewport renderViewport = BeautyFragment.this.imageGLSurfaceView.getRenderViewport();
                final float w = (float) renderViewport.width;
                final float h = (float) renderViewport.height;
                if (BeautyFragment.this.currentType == 7) {
                    BeautyFragment.this.imageGLSurfaceView.lazyFlush(true, new Runnable() {
                        public final void run() {
                            if (BeautyFragment.this.cgeDeformFilterWrapper != null) {
                                BeautyFragment.this.cgeDeformFilterWrapper.restore();
                                for (Sticker next : BeautyFragment.this.photo_editor_view.getStickers()) {
                                    PointF mappedCenterPoint2 = ((BeautySticker) next).getMappedCenterPoint2();
                                    Log.i("CURRENT", BeautyFragment.this.currentType + "");
                                    for (int i2 = 0; i2 < Math.abs(BeautyFragment.this.currentType); i2++) {
                                        if (BeautyFragment.this.currentType > 0) {
                                            BeautyFragment.this.cgeDeformFilterWrapper.bloatDeform(mappedCenterPoint2.x, mappedCenterPoint2.y, w, h, (float) (next.getWidth() / 2), 0.03f);
                                        } else if (BeautyFragment.this.currentType < 0) {
                                            BeautyFragment.this.cgeDeformFilterWrapper.wrinkleDeform(mappedCenterPoint2.x, mappedCenterPoint2.y, w, h, (float) (next.getWidth() / 2), 0.03f);
                                        }
                                    }
                                }
                            }
                        }
                    });
                } else if (BeautyFragment.this.currentType == 9) {
                    BeautyFragment.this.imageGLSurfaceView.lazyFlush(true, new Runnable() {
                        public void run() {
                            if (BeautyFragment.this.cgeDeformFilterWrapper != null) {
                                BeautyFragment.this.cgeDeformFilterWrapper.restore();
                                Iterator<Sticker> it = BeautyFragment.this.photo_editor_view.getStickers().iterator();
                                while (it.hasNext()) {
                                    BeautySticker beautySticker = (BeautySticker) it.next();
                                    PointF mappedCenterPoint2 = beautySticker.getMappedCenterPoint2();
                                    RectF mappedBound = beautySticker.getMappedBound();
                                    for (int i = 0; i < Math.abs(currentDegrees); i++) {
                                        if (currentDegrees > 0) {
                                            BeautyFragment.this.cgeDeformFilterWrapper.forwardDeform(mappedBound.right - 20.0f, mappedCenterPoint2.y, mappedBound.right + 20.0f, mappedCenterPoint2.y, 20.0f, 20.0f, (float) beautySticker.getRadius(), 0.01f);
                                            BeautyFragment.this.cgeDeformFilterWrapper.forwardDeform(mappedBound.left + 20.0f, mappedCenterPoint2.y, mappedBound.left - 20.0f, mappedCenterPoint2.y, 20.0f, 20.0f, (float) beautySticker.getRadius(), 0.01f);
                                        } else {
                                            BeautyFragment.this.cgeDeformFilterWrapper.forwardDeform(mappedBound.right + 20.0f, mappedCenterPoint2.y, mappedBound.right - 20.0f, mappedCenterPoint2.y, w, h, (float) beautySticker.getRadius(), 0.01f);
                                            BeautyFragment.this.cgeDeformFilterWrapper.forwardDeform(mappedBound.left - 20.0f, mappedCenterPoint2.y, mappedBound.left + 20.0f, mappedCenterPoint2.y, w, h, (float) beautySticker.getRadius(), 0.01f);
                                        }
                                    }
                                }
                            }
                        }
                    });
                } else if (BeautyFragment.this.currentType == 4) {
                    BeautyFragment.this.imageGLSurfaceView.lazyFlush(true, new Runnable() {
                        public void run() {
                            int i;
                            PointF pointF;
                            Iterator<Sticker> iterator;
                            int j;
                            if (BeautyFragment.this.cgeDeformFilterWrapper != null) {
                                BeautyFragment.this.cgeDeformFilterWrapper.restore();
                                Iterator<Sticker> iterator2 = BeautyFragment.this.photo_editor_view.getStickers().iterator();
                                while (iterator2.hasNext()) {
                                    BeautySticker beautySticker = (BeautySticker) iterator2.next();
                                    PointF pointF2 = beautySticker.getMappedCenterPoint2();
                                    RectF rectF = beautySticker.getMappedBound();
                                    int i2 = beautySticker.getRadius() / 2;
                                    float f1 = (rectF.left + pointF2.x) / 2.0f;
                                    float f2 = rectF.left + ((f1 - rectF.left) / 2.0f);
                                    float f3 = (rectF.bottom + rectF.top) / 2.0f;
                                    float f4 = rectF.top + ((f3 - rectF.top) / 2.0f);
                                    float f5 = (rectF.right + pointF2.x) / 2.0f;
                                    float f6 = rectF.right - ((rectF.right - f5) / 2.0f);
                                    float f7 = (rectF.bottom + rectF.top) / 2.0f;
                                    float f8 = rectF.top + ((f7 - rectF.top) / 2.0f);
                                    int j2 = 0;
                                    Iterator<Sticker> iterator1 = iterator2;
                                    while (true) {
                                        iterator2 = iterator1;
                                        if (j2 >= Math.abs(currentDegrees)) {
                                            break;
                                        }
                                        if (currentDegrees > 0) {
                                            CGEDeformFilterWrapper cGEDeformFilterWrapper = BeautyFragment.this.cgeDeformFilterWrapper;
                                            float f9 = rectF.right;
                                            float f10 = rectF.top;
                                            float f11 = rectF.right;
                                            float f12 = (float) i2;
                                            float f = rectF.top;
                                            float f13 = w;
                                            iterator = iterator2;
                                            pointF = pointF2;
                                            float f92 = f13;
                                            float f122 = f12;
                                            float f14 = f11;
                                            float f15 = f10;
                                            float f102 = f;
                                            float f16 = f9;
                                            cGEDeformFilterWrapper.forwardDeform(f9, f10, f11 - f12, f102, f92, h, (float) beautySticker.getRadius(), 0.002f);
                                            float f17 = f6;
                                            float f18 = f8;
                                            BeautyFragment.this.cgeDeformFilterWrapper.forwardDeform(f17, f18, f6 - f122, f8, w, h, (float) beautySticker.getRadius(), 0.005f);
                                            CGEDeformFilterWrapper cGEDeformFilterWrapper2 = cGEDeformFilterWrapper;
                                            BeautyFragment.this.cgeDeformFilterWrapper.forwardDeform(f5, f7, f5 - f122, f7, w, h, (float) beautySticker.getRadius(), 0.007f);
                                            BeautyFragment.this.cgeDeformFilterWrapper.forwardDeform(rectF.left, rectF.top, rectF.left + f122, rectF.top, w, h, (float) beautySticker.getRadius(), 0.002f);
                                            float f19 = f2;
                                            float f20 = f4;
                                            BeautyFragment.this.cgeDeformFilterWrapper.forwardDeform(f19, f20, f2 + f122, f4, w, h, (float) beautySticker.getRadius(), 0.005f);
                                            CGEDeformFilterWrapper cGEDeformFilterWrapper3 = BeautyFragment.this.cgeDeformFilterWrapper;
                                            j = j2;
                                            CGEDeformFilterWrapper cGEDeformFilterWrapper4 = cGEDeformFilterWrapper3;
                                            float f21 = f1;
                                            float f22 = f3;
                                            cGEDeformFilterWrapper4.forwardDeform(f21, f22, f1 + f122, f3, w, h, (float) beautySticker.getRadius(), 0.007f);
                                            i = i2;
                                        } else {
                                            iterator = iterator2;
                                            pointF = pointF2;
                                            j = j2;
                                            CGEDeformFilterWrapper cGEDeformFilterWrapper5 = BeautyFragment.this.cgeDeformFilterWrapper;
                                            float f93 = rectF.right;
                                            float f103 = rectF.top;
                                            float f112 = rectF.right;
                                            float f123 = (float) i2;
                                            float f23 = rectF.top;
                                            float f24 = w;
                                            i = i2;
                                            float f113 = f24;
                                            float f124 = f123;
                                            float f125 = f23;
                                            float f25 = f112;
                                            float f26 = f103;
                                            float f27 = f93;
                                            cGEDeformFilterWrapper5.forwardDeform(f93, f103, f112 + f123, f125, f113, h, (float) beautySticker.getRadius(), 0.002f);
                                            float f28 = f6;
                                            float f29 = f8;
                                            BeautyFragment.this.cgeDeformFilterWrapper.forwardDeform(f28, f29, f6 + f124, f8, w, h, (float) beautySticker.getRadius(), 0.005f);
                                            BeautyFragment.this.cgeDeformFilterWrapper.forwardDeform(f5, f7, f5 + f124, f7, w, h, (float) beautySticker.getRadius(), 0.007f);
                                            BeautyFragment.this.cgeDeformFilterWrapper.forwardDeform(rectF.left + f124, rectF.top, rectF.left, rectF.top, w, h, (float) beautySticker.getRadius(), 0.002f);
                                            float f30 = f2;
                                            float f31 = f4;
                                            BeautyFragment.this.cgeDeformFilterWrapper.forwardDeform(f30, f31, f2 - f124, f4, w, h, (float) beautySticker.getRadius(), 0.005f);
                                            BeautyFragment.this.cgeDeformFilterWrapper.forwardDeform(f1, f3, f1 - f124, f3, w, h, (float) beautySticker.getRadius(), 0.007f);
                                        }
                                        j2 = j + 1;
                                        Iterator<Sticker> it = iterator;
                                        pointF2 = pointF;
                                        i2 = i;
                                    }
                                    PointF pointF3 = pointF2;
                                    int i3 = i2;
                                    int i4 = j2;
                                }
                            }
                        }
                    });
                }
            }

            public void onScrollEnd() {
                BeautyFragment.this.imageGLSurfaceView.requestRender();
            }
        });
        BitmapStickerIcon bitmapStickerIcon = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_sticker_ic_scale_black_18dp), 3, BitmapStickerIcon.ZOOM);
        bitmapStickerIcon.setIconEvent(new ZoomIconEvent());
        BitmapStickerIcon bitmapStickerIcon2 = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_sticker_ic_scale_black_2_18dp), 2, BitmapStickerIcon.ZOOM);
        bitmapStickerIcon2.setIconEvent(new ZoomIconEvent());
        this.photo_editor_view.setIcons(Arrays.asList(bitmapStickerIcon, bitmapStickerIcon2));
        this.photo_editor_view.setBackgroundColor(-16777216);
        this.photo_editor_view.setLocked(false);
        this.photo_editor_view.setConstrained(true);
        this.photo_editor_view.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            public void onStickerAdded(Sticker sticker) {
            }

            public void onStickerClicked(Sticker sticker) {
            }

            public void onStickerDoubleTapped(Sticker sticker) {
            }

            public void onStickerDragFinished(Sticker sticker) {
            }

            public void onStickerFlipped(Sticker sticker) {
            }

            public void onStickerTouchOutside() {
            }

            public void onStickerTouchedDown(Sticker sticker) {
            }

            public void onStickerZoomFinished(Sticker sticker) {
            }

            public void onTouchUpForBeauty(float f, float f2) {
            }

            public void onStickerDeleted(Sticker sticker) {
            }

            public void onTouchDownForBeauty(float f, float f2) {
                BeautyFragment.this.startX = f;
                BeautyFragment.this.startY = f2;
            }

            public void onTouchDragForBeauty(float f, float f2) {
                TextureRenderer.Viewport renderViewport = BeautyFragment.this.imageGLSurfaceView.getRenderViewport();
                final float f3 = f;
                final float f4 = f2;
                final TextureRenderer.Viewport viewport = renderViewport;
                final float f5 = (float) renderViewport.height;
                BeautyFragment.this.imageGLSurfaceView.lazyFlush(true, new Runnable() {
                    public final void run() {
                        if (BeautyFragment.this.cgeDeformFilterWrapper != null) {
                            BeautyFragment.this.cgeDeformFilterWrapper.forwardDeform(BeautyFragment.this.startX, BeautyFragment.this.startY, f3, f4, (float) viewport.width, f5, 200.0f, 0.02f);
                        }
                    }
                });
                BeautyFragment.this.startX = f;
                BeautyFragment.this.startY = f2;
            }
        });
        ImageView imageView = inflate.findViewById(R.id.image_view_compare_beauty);
        this.image_view_compare_beauty = imageView;
        imageView.setOnTouchListener(new View.OnTouchListener() {
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                int actionMasked = motionEvent.getActionMasked();
                if (actionMasked == 0) {
                    BeautyFragment.this.photo_editor_view.getGLSurfaceView().setAlpha(0.0f);
                    return true;
                } else if (actionMasked != 1) {
                    return true;
                } else {
                    BeautyFragment.this.photo_editor_view.getGLSurfaceView().setAlpha(1.0f);
                    return false;
                }
            }
        });
        inflate.findViewById(R.id.image_view_save).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                new SaveCurrentState(true).execute();
            }
        });
        inflate.findViewById(R.id.image_view_close).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                BeautyFragment.this.dismiss();
            }
        });
        this.photo_editor_view.setImageSource(this.bitmap, new ImageGLSurfaceView.OnSurfaceCreatedCallback() {
            public final void surfaceCreated() {
                if (BeautyFragment.this.bitmap != null) {
                    BeautyFragment.this.imageGLSurfaceView.setImageBitmap(BeautyFragment.this.bitmap);
                    BeautyFragment.this.imageGLSurfaceView.queueEvent(new Runnable() {
                        public final void run() {
                            float width = (float) BeautyFragment.this.bitmap.getWidth();
                            float height = (float) BeautyFragment.this.bitmap.getHeight();
                            float min = Math.min(((float) BeautyFragment.this.imageGLSurfaceView.getRenderViewport().width) / width, ((float) BeautyFragment.this.imageGLSurfaceView.getRenderViewport().height) / height);
                            if (min < 1.0f) {
                                width *= min;
                                height *= min;
                            }
                            BeautyFragment.this.cgeDeformFilterWrapper = CGEDeformFilterWrapper.create((int) width, (int) height, 10.0f);
                            BeautyFragment.this.cgeDeformFilterWrapper.setUndoSteps(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
                            if (BeautyFragment.this.cgeDeformFilterWrapper != null) {
                                CGEImageHandler imageHandler = BeautyFragment.this.imageGLSurfaceView.getImageHandler();
                                imageHandler.setFilterWithAddres(BeautyFragment.this.cgeDeformFilterWrapper.getNativeAddress());
                                imageHandler.processFilters();
                            }
                        }
                    });
                }
            }
        });
        this.photo_editor_view.post(new Runnable() {
            public final void run() {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(BeautyFragment.this.imageGLSurfaceView.getRenderViewport().width, BeautyFragment.this.imageGLSurfaceView.getRenderViewport().height);
                layoutParams.addRule(13);
                BeautyFragment.this.photo_editor_view.setLayoutParams(layoutParams);
            }
        });
        hideAllFunction();
        return inflate;
    }

    private void selectFunction(int i) {
        for (int i2 = 0; i2 < this.retouchList.size(); i2++) {
            if (i2 == i) {
                Retouch retouch = this.retouchList.get(i2);
                retouch.imageView.setImageResource(retouch.drawableSelected);
                retouch.textView.setTextColor(ContextCompat.getColor(getContext(), R.color.mainColor));
            } else {
                Retouch retouch2 = this.retouchList.get(i2);
                retouch2.imageView.setImageResource(retouch2.drawable);
                retouch2.textView.setTextColor(ContextCompat.getColor(getContext(), R.color.itemColor));
            }
        }
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
        }
    }

    class Retouch {
        int drawable;
        int drawableSelected;
        ImageView imageView;
        TextView textView;

        Retouch(ImageView imageView2, TextView textView2, int i, int i2) {
            this.drawable = i;
            this.drawableSelected = i2;
            this.imageView = imageView2;
            this.textView = textView2;
        }
    }

    class SaveCurrentState extends AsyncTask<Void, Void, Bitmap> {
        boolean isCloseDialog;

        SaveCurrentState() {
        }

        SaveCurrentState(boolean z) {
            this.isCloseDialog = z;
        }

        public void onPreExecute() {
            BeautyFragment.this.getDialog().getWindow().setFlags(16, 16);
        }

        public Bitmap doInBackground(Void... voidArr) {
            final Bitmap[] bitmapArr = {null};
            BeautyFragment.this.photo_editor_view.saveGLSurfaceViewAsBitmap(new OnSaveBitmap() {
                public void onFailure(Exception exc) {
                }

                public void onBitmapReady(Bitmap bitmap) {
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
            BeautyFragment.this.photo_editor_view.setImageSource(bitmap);
            try {
                BeautyFragment.this.getDialog().getWindow().clearFlags(16);
            } catch (Exception e) {
            }
            BeautyFragment.this.imageGLSurfaceView.flush(true, new Runnable() {
                public final void run() {
                    if (BeautyFragment.this.cgeDeformFilterWrapper != null) {
                        BeautyFragment.this.cgeDeformFilterWrapper.restore();
                        BeautyFragment.this.imageGLSurfaceView.requestRender();
                    }
                }
            });
            if (this.isCloseDialog) {
                BeautyFragment.this.onBeautySave.onBeautySave(bitmap);
                BeautyFragment.this.dismiss();
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        CGEDeformFilterWrapper cGEDeformFilterWrapper = this.cgeDeformFilterWrapper;
        if (cGEDeformFilterWrapper != null) {
            cGEDeformFilterWrapper.release(true);
            this.cgeDeformFilterWrapper = null;
        }
        this.imageGLSurfaceView.release();
        this.imageGLSurfaceView.onPause();
    }
}
