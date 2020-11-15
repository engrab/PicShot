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
import com.pic.shot.picker.fragment.ImagePagerFragment;
import java.util.ArrayList;

public class PhotoPagerActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private ImagePagerFragment fragment_photo_pager;
    private boolean showDelete;

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_photo_pager);
        int intExtra = getIntent().getIntExtra(PhotoPreview.EXTRA_CURRENT_ITEM, 0);
        ArrayList<String> stringArrayListExtra = getIntent().getStringArrayListExtra(PhotoPreview.EXTRA_PHOTOS);
        this.showDelete = getIntent().getBooleanExtra(PhotoPreview.EXTRA_SHOW_DELETE, true);
        if (this.fragment_photo_pager == null) {
            this.fragment_photo_pager = (ImagePagerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_photo_pager);
        }
        this.fragment_photo_pager.setPhotos(stringArrayListExtra, intExtra);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar supportActionBar = getSupportActionBar();
        this.actionBar = supportActionBar;
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                this.actionBar.setElevation(25.0f);
            }
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(PhotoPicker.KEY_SELECTED_PHOTOS, this.fragment_photo_pager.getPaths());
        setResult(-1, intent);
        finish();
        super.onBackPressed();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        onBackPressed();
        return true;
    }
}
