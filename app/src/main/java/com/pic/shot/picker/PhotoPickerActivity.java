package com.pic.shot.picker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.pic.shot.R;
import com.pic.shot.activities.EditImageActivity;
import com.pic.shot.picker.entity.Photo;
import com.pic.shot.picker.event.OnItemCheckListener;
import com.pic.shot.picker.fragment.ImagePagerFragment;
import com.pic.shot.picker.fragment.PhotoPickerFragment;
import java.util.ArrayList;

public class PhotoPickerActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public boolean forwardMain;
    private ImagePagerFragment imagePagerFragment;
    private int maxCount = 9;
    private ArrayList<String> originalPhotos = null;
    private PhotoPickerFragment pickerFragment;
    private boolean showGif = false;

    public PhotoPickerActivity getActivity() {
        return this;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        boolean booleanExtra = getIntent().getBooleanExtra(PhotoPicker.EXTRA_SHOW_CAMERA, true);
        boolean booleanExtra2 = getIntent().getBooleanExtra(PhotoPicker.EXTRA_SHOW_GIF, false);
        boolean booleanExtra3 = getIntent().getBooleanExtra(PhotoPicker.EXTRA_PREVIEW_ENABLED, true);
        this.forwardMain = getIntent().getBooleanExtra(PhotoPicker.MAIN_ACTIVITY, false);
        setShowGif(booleanExtra2);
        setContentView((int) R.layout.activity_photo_picker);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setTitle(getResources().getString(R.string.tap_to_select));
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            supportActionBar.setElevation(25.0f);
        }
        this.maxCount = getIntent().getIntExtra(PhotoPicker.EXTRA_MAX_COUNT, 9);
        int intExtra = getIntent().getIntExtra(PhotoPicker.EXTRA_GRID_COLUMN, 3);
        this.originalPhotos = getIntent().getStringArrayListExtra(PhotoPicker.EXTRA_ORIGINAL_PHOTOS);
        PhotoPickerFragment photoPickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentByTag("tag");
        this.pickerFragment = photoPickerFragment;
        if (photoPickerFragment == null) {
            this.pickerFragment = PhotoPickerFragment.newInstance(booleanExtra, booleanExtra2, booleanExtra3, intExtra, this.maxCount, this.originalPhotos);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, this.pickerFragment, "tag").commit();
            getSupportFragmentManager().executePendingTransactions();
        }
        this.pickerFragment.getPhotoGridAdapter().setOnItemCheckListener(new OnItemCheckListener() {
            public final boolean onItemCheck(int i, Photo photo, int i2) {
                if (!PhotoPickerActivity.this.forwardMain) {
                    Intent intent = new Intent(PhotoPickerActivity.this, EditImageActivity.class);
                    intent.putExtra(PhotoPicker.KEY_SELECTED_PHOTOS, photo.getPath());
                    PhotoPickerActivity.this.startActivity(intent);
                    PhotoPickerActivity.this.finish();
                    return true;
                }
                PhotoPickerActivity.this.finish();
                return true;
            }
        });
    }

    public void onBackPressed() {
        ImagePagerFragment imagePagerFragment2 = this.imagePagerFragment;
        if (imagePagerFragment2 == null || !imagePagerFragment2.isVisible()) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void setShowGif(boolean z) {
        this.showGif = z;
    }
}
