package com.ak.otaku_kun.ui.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class RatingCustom extends LinearLayout {
    private ImageView favorite, plan;
    private TextView rate;

    public RatingCustom(Context context) {
        super(context);
    }

    public RatingCustom(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RatingCustom(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RatingCustom(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void onCreateView(){
        //init linear layout
        setOrientation(LinearLayout.HORIZONTAL);
        setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.CENTER);
        setPadding(4,4,4,4);

        //init ImageViews
        //favorite
        favorite = new ImageView(getContext());
//        favorite.setLayoutParams();
    }
}
