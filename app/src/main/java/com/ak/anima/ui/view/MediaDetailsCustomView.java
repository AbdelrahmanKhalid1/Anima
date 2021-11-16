package com.ak.anima.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.ak.anima.R;

public class MediaDetailsCustomView extends LinearLayout {

    private TextView title, format, genre;

    public MediaDetailsCustomView(Context context) {
        super(context);
    }

    public MediaDetailsCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onCreateView();
    }

    public MediaDetailsCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreateView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MediaDetailsCustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreateView();
    }

    private void onCreateView() {
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        setPadding(4, 4, 4, 4);
        setBackgroundColor(getResources().getColor(R.color.transparentBg));

        title = new TextView(getContext());
        title.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        title.setMaxLines(2);
        title.setEllipsize(TextUtils.TruncateAt.END);
        title.setTextColor(Color.WHITE);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setHint("Shingeki no Kyojin");
        title.setHintTextColor(Color.WHITE);
        addView(title);

        format = new TextView(getContext());
        format.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        format.setEllipsize(TextUtils.TruncateAt.END);
        format.setTextColor(Color.WHITE);
        format.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
        format.setHint("Tv - Releasing");
        format.setHintTextColor(Color.WHITE);
        addView(format);

        genre = new TextView(getContext());
        genre.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        genre.setMaxLines(1);
        genre.setEllipsize(TextUtils.TruncateAt.END);
        genre.setTextColor(Color.WHITE);
        genre.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
        genre.setHint("Action, Drama, Fantasy, Mystery");
        genre.setHintTextColor(Color.WHITE);
        addView(genre);
    }

    public void setTitle(String mediaTitle) {
        title.setText(mediaTitle);
    }

    public void setFormat(String mediaFormat) {
        format.setText(mediaFormat);
    }

    public void setStatus(String mediaStatus){
        if(mediaStatus.isEmpty())
            return;
        String text = format.getText().toString();
        text = text + " - " + mediaStatus;
        format.setText(text);
    }

    public void setGenre(String mediaGenre) {
        if (mediaGenre.isEmpty())
            genre.setVisibility(View.GONE);
        else
            genre.setText(mediaGenre);
    }
}
