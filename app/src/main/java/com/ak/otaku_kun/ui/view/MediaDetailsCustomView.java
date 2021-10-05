package com.ak.otaku_kun.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class MediaDetailsCustomView extends LinearLayout {

    private TextView title, format, genre;

    public MediaDetailsCustomView(Context context) {
        super(context);
    }

    public MediaDetailsCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MediaDetailsCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MediaDetailsCustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void onCreateView() {
        setOrientation(LinearLayout.HORIZONTAL);
        setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.CENTER);
        setPadding(8, 8, 8, 8);

        //to prevent editText from auto focusing and opening keyboard
        setFocusableInTouchMode(true);

        title = new TextView(getContext());
        title.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        title.setMaxLines(2);
        title.setEllipsize(TextUtils.TruncateAt.END);
        title.setTextColor(Color.WHITE);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        addView(title);

//        editText = new EditText(getContext());
//        editText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.4f));
//        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
//        editText.setGravity(Gravity.CENTER);
//        editText.addTextChangedListener(textWatcher);
//        addView(editText);
//
//
//        increment = new ImageView(getContext());
//        increment.setImageResource(R.drawable.ic_up);
//        increment.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.3f));
//        increment.setOnClickListener(onIncrementClickListener);
//        addView(increment);
    }
}
