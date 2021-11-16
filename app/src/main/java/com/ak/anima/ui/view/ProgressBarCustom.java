package com.ak.anima.ui.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.ak.anima.databinding.CustomViewLoadBinding;

public class ProgressBarCustom extends FrameLayout {

    private CustomViewLoadBinding binding;

    public ProgressBarCustom(Context context) {
        super(context);
        onInit();
    }

    public ProgressBarCustom(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onInit();
    }

    public ProgressBarCustom(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInit();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProgressBarCustom(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onInit();
    }

    private void onInit(){
        binding = CustomViewLoadBinding.inflate(LayoutInflater.from(getContext()), this, true);
    }
}
