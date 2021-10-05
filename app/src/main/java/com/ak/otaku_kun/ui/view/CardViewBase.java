package com.ak.otaku_kun.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.ak.otaku_kun.R;

/**
 * A base custom card view for overview components
 */
public class CardViewBase extends CardView {

    public CardViewBase(@NonNull Context context) {
        super(context);
        onInit();
    }

    public CardViewBase(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onInit();
    }

    public CardViewBase(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInit();
    }

    public void onInit() {
        int paddingValue = getResources().getDimensionPixelSize(R.dimen.xl_margin);
        setRadius(getResources().getDimensionPixelSize(R.dimen.lg_margin));
        setUseCompatPadding(true);
        setPreventCornerOverlap(false);
        setContentPadding(paddingValue, paddingValue, paddingValue, paddingValue);
        requestLayout();
    }
}
