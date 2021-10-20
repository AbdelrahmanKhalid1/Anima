package com.ak.otaku_kun.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.ak.otaku_kun.R;
import com.ak.otaku_kun.model.details.Media;
import com.ak.otaku_kun.utils.Utils;

public class RatingCustomView extends LinearLayout {
    private ImageView favorite, plan;
    private TextView rate;

    public RatingCustomView(Context context) {
        super(context);
        onCreateView();
    }

    public RatingCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onCreateView();
    }

    public RatingCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreateView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RatingCustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreateView();
    }

    private void onCreateView() {
        //init linear layout
        LayoutParams params;
        int marginVertical = getMarginVerticalValue();
        int marginHorizontal = getMarginHorizontalValue();
        setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
        int padding = getLinearLayoutPaddingValue();
        setPadding(padding, padding, padding, padding);
        setBackgroundResource(R.drawable.bg_rating_text);

        //init TextView rate
        rate = new TextView(getContext());
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(marginHorizontal, marginVertical, 0, marginVertical);
        rate.setLayoutParams(params);
        rate.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        rate.setTextColor(Color.WHITE);
        rate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        rate.setHint("0.0");
        rate.setHintTextColor(Color.WHITE);
        addView(rate);

        //init ImageViews
        //favorite
        favorite = new ImageView(getContext());
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.setMargins(0, marginVertical, 0, marginVertical);
        favorite.setLayoutParams(params);
        favorite.setImageResource(R.drawable.ic_star);
        addView(favorite);

        //plan
        plan = new ImageView(getContext());
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.setMargins(0, marginVertical, marginHorizontal, marginVertical);
        plan.setLayoutParams(params);
        plan.setVisibility(View.GONE);
        addView(plan);
    }

    private int getLinearLayoutPaddingValue() {
        return (int) Utils.getDpValue(4f, getContext());
    }

    private int getMarginVerticalValue() {
        return (int) Utils.getDpValue(2f, getContext());
    }

    private int getMarginHorizontalValue() {
        return (int) Utils.getDpValue(4f, getContext());
    }

    public void setRate(String score){
        rate.setText(score);
    }
    public void setFavorite(boolean isFavorite){
        int imageResource;
        if (isFavorite) imageResource = R.drawable.ic_stared;
        else imageResource = R.drawable.ic_star;
        favorite.setImageResource(imageResource);
    }
    public void setPlan(Media.MediaListEntry mediaListEntry){
        if(mediaListEntry == null)
            return;

        plan.setVisibility(View.VISIBLE);
//        mediaListEntry?.let {
            //TODO set plan icon
//                val imageId = when(mediaListEntry.status){
//
//                }
//                view.setImageResource()
//        }
    }
}

/*
            <LinearLayout
                android:id="@+id/linearLayout"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:background="@drawable/bg_rating_text"
                android:gravity="center"
                android:orientation="horizontal"
                android:padding="4dp"
                android:layout_margin="8dp"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent">

                <TextView
                    android:id="@+id/rate"
                    mediaListEntry="@{media.mediaListEntry}"
                    score="@{media.averageScore}"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginVertical="2dp"
                    android:layout_marginStart="4dp"
                    android:textColor="#fff"
                    android:textSize="10sp"
                    tools:text="7.7"
                    tools:ignore="SmallSp" />

                <ImageView
                    android:id="@+id/favorite"
                    isFavorite="@{media.isFavorite}"
                    android:layout_width="wrap_content"
                    android:layout_height="match_parent"
                    android:layout_marginVertical="2dp"
                    android:src="@drawable/ic_star"
                    tools:ignore="ContentDescription" />

                <ImageView
                    android:id="@+id/plan"
                    android:layout_width="wrap_content"
                    android:layout_height="match_parent"
                    android:layout_marginVertical="2dp"
                    android:layout_marginEnd="4dp"
                    android:textAlignment="center"
                    android:visibility="gone"
                    tools:ignore="ContentDescription" />
            </LinearLayout>
 */