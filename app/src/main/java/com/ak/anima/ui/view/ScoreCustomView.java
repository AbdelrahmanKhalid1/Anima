package com.ak.anima.ui.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.ak.anima.R;
import com.ak.anima.databinding.CustomViewScoreBinding;
import com.ak.anima.model.details.Media;

public class ScoreCustomView extends LinearLayout {
    private CustomViewScoreBinding binding;
    private Media.MediaListEntry mediaListEntry;

    public ScoreCustomView(Context context) {
        super(context);
        onCreateView();
    }

    public ScoreCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onCreateView();
    }

    public ScoreCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreateView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScoreCustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreateView();
    }

    private void onCreateView() {
        setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        binding = CustomViewScoreBinding.inflate(LayoutInflater.from(getContext()), this, true);
    }

    public void setScore(String score) {
        binding.rate.setText(score);
    }

    public void setIsFavorite(boolean isFavorite) {
        int imageResource;
        if (isFavorite) imageResource = R.drawable.ic_stared;
        else imageResource = R.drawable.ic_star;
        binding.favorite.setImageResource(imageResource);
    }

    public void setMediaListEntry(Media.MediaListEntry mediaListEntry) {
        if (mediaListEntry == null)
            binding.plan.setVisibility(View.GONE);
        else
            binding.plan.setVisibility(View.VISIBLE);
        this.mediaListEntry = mediaListEntry;
    }

    public void setIsMyList(boolean isMyList){
        if(isMyList && mediaListEntry != null)
            binding.rate.setText(String.valueOf(mediaListEntry.getScore()));
    }
}