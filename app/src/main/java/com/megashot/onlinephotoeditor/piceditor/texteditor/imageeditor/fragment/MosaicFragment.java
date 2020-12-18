package com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.R;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.adapters.MosaicAdapter;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.filters.FilterUtils;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.utils.MosaicUtil;
import com.megashot.onlinephotoeditor.piceditor.texteditor.imageeditor.views.MosaicView;

public class MosaicFragment extends DialogFragment implements MosaicAdapter.MosaicChangeListener {
    private static final String TAG = "MosaicFragment";
    public Bitmap adjustBitmap;
    private ImageView backgroundView;
    public Bitmap bitmap;
    public MosaicDialogListener mosaicDialogListener;
    public MosaicView mosaic_view;
    private RecyclerView recycler_view_mosaic;
    private SeekBar seekbar_mosaic;

    public interface MosaicDialogListener {
        void onSaveMosaic(Bitmap bitmap);
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static MosaicFragment show(AppCompatActivity appCompatActivity, Bitmap bitmap2, Bitmap bitmap3, MosaicDialogListener mosaicDialogListener2) {
        MosaicFragment mosaicDialog = new MosaicFragment();
        mosaicDialog.setBitmap(bitmap2);
        mosaicDialog.setAdjustBitmap(bitmap3);
        mosaicDialog.setMosaicListener(mosaicDialogListener2);
        mosaicDialog.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return mosaicDialog;
    }

    public void setMosaicListener(MosaicDialogListener mosaicDialogListener2) {
        this.mosaicDialogListener = mosaicDialogListener2;
    }

    public void setAdjustBitmap(Bitmap bitmap2) {
        this.adjustBitmap = bitmap2;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.fragment_mosaic, viewGroup, false);
        MosaicView mosaicView = (MosaicView) inflate.findViewById(R.id.mosaic_view);
        this.mosaic_view = mosaicView;
        mosaicView.setImageBitmap(this.bitmap);
        this.mosaic_view.setMosaicItem(new MosaicAdapter.MosaicItem(R.drawable.blue_mosoic, 0, MosaicAdapter.Mode.BLUR));
        this.backgroundView = (ImageView) inflate.findViewById(R.id.image_view_background);
        Bitmap blurImageFromBitmap = FilterUtils.getBlurImageFromBitmap(this.bitmap);
        this.adjustBitmap = blurImageFromBitmap;
        this.backgroundView.setImageBitmap(blurImageFromBitmap);
        this.seekbar_mosaic = (SeekBar) inflate.findViewById(R.id.seekbar_mosaic);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recycler_view_mosaic);
        this.recycler_view_mosaic = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.recycler_view_mosaic.setHasFixedSize(true);
        this.recycler_view_mosaic.setAdapter(new MosaicAdapter(getContext(), this));
        inflate.findViewById(R.id.image_view_save).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new SaveMosaicView().execute(new Void[0]);
            }
        });
        inflate.findViewById(R.id.image_view_close).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MosaicFragment.this.dismiss();
            }
        });
        this.seekbar_mosaic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                MosaicFragment.this.mosaic_view.setBrushBitmapSize(i + 25);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                MosaicFragment.this.mosaic_view.updateBrush();
            }
        });
        inflate.findViewById(R.id.image_view_undo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MosaicFragment.this.mosaic_view.undo();
            }
        });
        inflate.findViewById(R.id.image_view_redo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MosaicFragment.this.mosaic_view.redo();
            }
        });
        return inflate;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.bitmap.recycle();
        this.bitmap = null;
        this.adjustBitmap.recycle();
        this.adjustBitmap = null;
    }

    public void onStop() {
        super.onStop();
    }

    public void onSelected(MosaicAdapter.MosaicItem mosaicItem) {
        if (mosaicItem.mode == MosaicAdapter.Mode.BLUR) {
            Bitmap blurImageFromBitmap = FilterUtils.getBlurImageFromBitmap(this.bitmap);
            this.adjustBitmap = blurImageFromBitmap;
            this.backgroundView.setImageBitmap(blurImageFromBitmap);
        } else if (mosaicItem.mode == MosaicAdapter.Mode.MOSAIC) {
            Bitmap mosaic = MosaicUtil.getMosaic(this.bitmap);
            this.adjustBitmap = mosaic;
            this.backgroundView.setImageBitmap(mosaic);
        }
        this.mosaic_view.setMosaicItem(mosaicItem);
    }

    class SaveMosaicView extends AsyncTask<Void, Bitmap, Bitmap> {
        SaveMosaicView() {
        }

        public Bitmap doInBackground(Void... voidArr) {
            return MosaicFragment.this.mosaic_view.getBitmap(MosaicFragment.this.bitmap, MosaicFragment.this.adjustBitmap);
        }

        public void onPostExecute(Bitmap bitmap) {
            MosaicFragment.this.mosaicDialogListener.onSaveMosaic(bitmap);
            MosaicFragment.this.dismiss();
        }
    }
}
