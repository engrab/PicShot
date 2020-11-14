package com.pic.shot.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.widget.AppCompatEditText;
import com.pic.shot.fragment.AddTextFragment;

public class CustomEditText extends AppCompatEditText {
    private AddTextFragment dialogFragment;

    public CustomEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setDialogFragment(AddTextFragment textEditorDialogFragment) {
        this.dialogFragment = textEditorDialogFragment;
    }

    public boolean onKeyPreIme(int i, KeyEvent keyEvent) {
        if (i == 4) {
            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getWindowToken(), 0);
            this.dialogFragment.dismissAndShowSticker();
        }
        return false;
    }
}
