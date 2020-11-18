package com.pic.editor.adapters;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pic.editor.R;
import com.pic.editor.activities.EditImageActivity;
import com.pic.editor.activities.ShareActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
import static com.pic.editor.activities.EditImageActivity.KEY_IMAGE_PATH;
import static com.pic.editor.picker.PhotoPicker.KEY_SELECTED_PHOTOS;


public class MyCreationAdapter extends BaseAdapter {
    public static ArrayList<String> imagegallary = new ArrayList();
    private static LayoutInflater inflater = null;
    private Activity activity;

    public MyCreationAdapter(Activity dAct, ArrayList<String> dUrl) {
        this.activity = dAct;
        imagegallary = dUrl;
        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return imagegallary.size();
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;
        int width = this.activity.getResources().getDisplayMetrics().widthPixels;
        if (row == null) {
            row = LayoutInflater.from(this.activity).inflate(R.layout.item_creation, parent, false);
            holder = new ViewHolder();
            row.setLayoutParams(new LayoutParams(width, -2));
            row.setPadding(8, 8, 8, 8);
            holder.imgIcon = row.findViewById(R.id.Iv_details_img);
            holder.imgDelete = row.findViewById(R.id.Iv_deledt_img_list);
            holder.imgShare = row.findViewById(R.id.Iv_share_img_list);
            holder.imgSetAs = row.findViewById(R.id.Iv_set_as_img_list);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        holder.imgIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new Options().inPreferredConfig = Config.ARGB_8888;
                Intent intent = new Intent(MyCreationAdapter.this.activity, EditImageActivity.class);
                intent.putExtra(KEY_SELECTED_PHOTOS, MyCreationAdapter.imagegallary.get(position));
                MyCreationAdapter.this.activity.startActivity(intent);
                MyCreationAdapter.this.activity.finish();
            }
        });
        holder.imgShare.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                new Options().inPreferredConfig = Config.ARGB_8888;
                Intent intent = new Intent(MyCreationAdapter.this.activity, ShareActivity.class);
                intent.putExtra(KEY_IMAGE_PATH, MyCreationAdapter.imagegallary.get(position));
                MyCreationAdapter.this.activity.startActivity(intent);
                MyCreationAdapter.this.activity.finish();
            }
        });
        holder.imgSetAs.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Builder alertDialog = new Builder(MyCreationAdapter.this.activity);
                alertDialog.setTitle("Set As Wallpaper!");
                alertDialog.setMessage("Do you want to Set As Wallpaper??");
                alertDialog.setIcon(R.drawable.ic_wallpaper);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyCreationAdapter myCreationAdapter = MyCreationAdapter.this;
                        myCreationAdapter.setWallpaper("", MyCreationAdapter.imagegallary.get(position));
                        MyCreationAdapter.this.notifyDataSetChanged();
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
        holder.imgDelete.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Builder alertDialog = new Builder(MyCreationAdapter.this.activity);
                alertDialog.setTitle("Confirm Delete!");
                alertDialog.setMessage("Are you sure you want delete this?");
                alertDialog.setIcon(R.drawable.ic_baseline_delete_24);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File fD = new File(MyCreationAdapter.imagegallary.get(position));
                        if (fD.exists()) {
                            fD.delete();
                        }
                        MyCreationAdapter.imagegallary.remove(position);
                        MyCreationAdapter.this.notifyDataSetChanged();
                        if (MyCreationAdapter.imagegallary.size() == 0) {
                            Toast.makeText(MyCreationAdapter.this.activity, "No Images Found", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
        Glide.with(this.activity).load(imagegallary.get(position)).centerCrop().into(holder.imgIcon);
        System.gc();
        return row;
    }

    private void setWallpaper(String diversity, String s) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this.activity);
        DisplayMetrics metrics = new DisplayMetrics();
        this.activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        try {
            Options options = new Options();
            options.inPreferredConfig = Config.ARGB_8888;
            wallpaperManager.setBitmap(BitmapFactory.decodeFile(s, options));
            wallpaperManager.suggestDesiredDimensions(width / 2, height / 2);
            Toast.makeText(this.activity, "Wallpaper Set", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shareImage(final String title, String path) {
        MediaScannerConnection.scanFile(this.activity, new String[]{path}, null, new OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
                Intent shareIntent = new Intent("android.intent.action.SEND");
                shareIntent.setType("image/*");
                shareIntent.putExtra("android.intent.extra.TEXT", title);
                shareIntent.putExtra("android.intent.extra.STREAM", uri);
                shareIntent.addFlags(FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                MyCreationAdapter.this.activity.startActivity(Intent.createChooser(shareIntent, "Share Image Using"));
            }
        });
    }

    static class ViewHolder {
        ImageView imgDelete;
        ImageView imgIcon;
        ImageView imgSetAs;
        ImageView imgShare;

        ViewHolder() {
        }
    }
}