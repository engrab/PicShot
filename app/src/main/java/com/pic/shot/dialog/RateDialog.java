package com.pic.shot.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.pic.shot.R;
import com.pic.shot.utils.SharePreferenceUtil;

public class RateDialog extends Dialog implements View.OnClickListener {
    private final Activity context;
    private final boolean exit;
    private final ImageView[] imageViewStars = new ImageView[5];
    private ViewGroup linear_layout_RatingBar;
    private LottieAnimationView lottie_animation_view;
    private int star_number;
    private TextView text_view_submit;

    public RateDialog(Activity activity, boolean z) {
        super(activity);
        this.context = activity;
        this.exit = z;
        setContentView(R.layout.dialog_rate);
        initView();
        this.linear_layout_RatingBar.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.shake));
        this.star_number = 0;
    }

    private void initView() {
        this.text_view_submit = (TextView) findViewById(R.id.text_view_submit);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R.id.lottie_animation_view);
        this.lottie_animation_view = lottieAnimationView;
        lottieAnimationView.setProgress(1.0f);
        this.linear_layout_RatingBar = (ViewGroup) findViewById(R.id.linear_layout_RatingBar);
        this.text_view_submit.setOnClickListener(this);
        ((TextView) findViewById(R.id.text_view_later)).setOnClickListener(this);
        ImageView iv_star_1 = (ImageView) findViewById(R.id.star_1);
        ImageView iv_star_2 = (ImageView) findViewById(R.id.star_2);
        ImageView iv_star_3 = (ImageView) findViewById(R.id.star_3);
        ImageView iv_star_4 = (ImageView) findViewById(R.id.star_4);
        ImageView iv_star_5 = (ImageView) findViewById(R.id.star_5);
        iv_star_1.setOnClickListener(this);
        iv_star_2.setOnClickListener(this);
        iv_star_3.setOnClickListener(this);
        iv_star_4.setOnClickListener(this);
        iv_star_5.setOnClickListener(this);
        ImageView[] imageViewArr = this.imageViewStars;
        imageViewArr[0] = iv_star_1;
        imageViewArr[1] = iv_star_2;
        imageViewArr[2] = iv_star_3;
        imageViewArr[3] = iv_star_4;
        imageViewArr[4] = iv_star_5;
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.text_view_later) {
            if (id != R.id.text_view_submit) {
                switch (id) {
                    case R.id.star_1:
                        this.star_number = 1;
                        setStarBar();
                        return;
                    case R.id.star_2:
                        this.star_number = 2;
                        setStarBar();
                        return;
                    case R.id.star_3:
                        this.star_number = 3;
                        setStarBar();
                        return;
                    case R.id.star_4:
                        this.star_number = 4;
                        setStarBar();
                        return;
                    case R.id.star_5:
                        this.star_number = 5;
                        setStarBar();
                        return;
                    default:
                        return;
                }
            } else {
                int i = this.star_number;
                if (i >= 4) {
                    this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.pic.shot")));
                    SharePreferenceUtil.setRated(this.context, true);
                    dismiss();
                } else if (i > 0) {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("message/rfc822");
                    intent.putExtra("android.intent.extra.EMAIL", this.context.getResources().getString(R.string.feedback));
                    intent.putExtra("android.intent.extra.SUBJECT", this.context.getString(R.string.app_name));
                    this.context.startActivity(Intent.createChooser(intent, "Send Email"));
                    if (this.exit) {
                        this.context.finish();
                    } else {
                        dismiss();
                    }
                } else {
                    this.linear_layout_RatingBar.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.shake));
                }
            }
        } else if (this.exit) {
            this.context.finish();
        } else {
            dismiss();
        }
    }

    private void setStarBar() {
        int i = 0;
        while (true) {
            ImageView[] imageViewArr = this.imageViewStars;
            if (i >= imageViewArr.length) {
                break;
            }
            if (i < this.star_number) {
                imageViewArr[i].setImageResource(R.drawable.ic_star);
            } else {
                imageViewArr[i].setImageResource(R.drawable.ic_star_blur);
            }
            i++;
        }
        if (this.star_number < 4) {
            this.text_view_submit.setText(R.string.rating_dialog_feedback_title);
        } else {
            this.text_view_submit.setText(R.string.rating_dialog_submit);
        }
        this.lottie_animation_view.setProgress(((float) this.star_number) / 5.0f);
    }
}
