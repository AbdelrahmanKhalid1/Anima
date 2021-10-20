package com.ak.otaku_kun.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.ak.otaku_kun.model.details.Media;
import com.ak.otaku_kun.utils.Utils;

public class MediaInfoCustomView extends LinearLayout {

    private TextView title, genre, format, status, score;

    public MediaInfoCustomView(Context context) {
        super(context);
        onCreateView();
    }

    public MediaInfoCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onCreateView();
    }

    public MediaInfoCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreateView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MediaInfoCustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreateView();
    }

    private void onCreateView() {
        setOrientation(LinearLayout.VERTICAL);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(params);
        setGravity(Gravity.BOTTOM);

        title = new TextView(getContext());
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,  (int) Utils.getDpValue(3f, getContext()));
        title.setLayoutParams(params);
        title.setTextColor(Color.WHITE);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        addView(title, params);

        genre = new TextView(getContext());
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,  (int) Utils.getDpValue(4f, getContext()));
        genre.setLayoutParams(params);
        genre.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
        genre.setTextColor(Color.WHITE);
        addView(genre, params);

        LinearLayout linearLayout = new LinearLayout(getContext());
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(linearLayout);

        CardView cardView = new CardView(getContext());
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.weight = 0.115f;
        cardView.setLayoutParams(params);
        cardView.setCardBackgroundColor(Color.WHITE);
        cardView.setCardElevation(1f);
        cardView.setRadius(Utils.getDpValue(18f, getContext()));
        linearLayout.addView(cardView);

        format = new TextView(getContext());
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        format.setLayoutParams(params);
        format.setPadding(0,2,0,2);
        format.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
        format.setTextColor(Color.BLACK);
        format.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        cardView.addView(format);

        status = new TextView(getContext());
        params = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        params.weight = 0.725f;
        status.setLayoutParams(params);
        status.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
        status.setTextColor(Color.WHITE);
        status.setPadding(7,0,0,0);
        linearLayout.addView(status, params);

        cardView = new CardView(getContext());
        params = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        params.setMarginEnd((int) Utils.getDpValue(2f, getContext()));
        params.weight = 0.16f;
        cardView.setLayoutParams(params);
        cardView.setRadius(Utils.getDpValue(4f, getContext()));
        cardView.setCardElevation(2);
        cardView.setCardBackgroundColor(Color.YELLOW);
        linearLayout.addView(cardView);

        score = new TextView(getContext());
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        score.setLayoutParams(params);
        score.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
        score.setTextColor(Color.BLACK);
        score.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        score.setPadding(2, 2, 2, 2);
        cardView.addView(score,params);

//        title.setText("Attack On Titan Final Season Part 2");
//        genre.setText("Not yet released");
//        format.setText("Tv");
//        status.setText("13 Ep 24 Min");
//        score.setText("8.8");
    }

    public void setMedia(Media media){
        if (media == null)
            return;
        title.setText(media.getTitle().getUserPreferred());
        genre.setText(media.getGenre());
        format.setText(media.getFormat());
        score.setText(media.getMeanScore());
//        StringBuilder builder = new StringBuilder();
//        builder.append()
        status.setText(media.getStatus());
    }
}
