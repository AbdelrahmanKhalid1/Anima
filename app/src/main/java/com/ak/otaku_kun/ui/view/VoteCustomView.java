package com.ak.otaku_kun.ui.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ak.otaku_kun.databinding.CustomViewVoteBinding;
import com.ak.otaku_kun.databinding.ItemReviewBinding;
import com.ak.otaku_kun.model.index.Review;
import com.ak.otaku_kun.model.index.ReviewVote;

public class VoteCustomView extends FrameLayout {

    private static final String TAG = "VoteCustomView";
    private @NonNull
    CustomViewVoteBinding binding;
    private Review review;

    public VoteCustomView(@NonNull Context context) {
        super(context);
        init();
    }

    public VoteCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VoteCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VoteCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        binding = CustomViewVoteBinding.inflate(LayoutInflater.from(getContext()), this, true);

        //todo check if user is logged in
        /*
        binding.widgetThumbUp.setOnClickListener(view -> {
            if (review == null)
                return;
            Log.d(TAG, "widgetThumbUp: " + review.getVoteStatus());
            switch (review.getVoteStatus()) {
                case NO_VOTE:
                    review.setVoteStatus(ReviewVote.UP_VOTE);
                    review.setUpVotes(review.getUpVotes() + 1);
                    view.setSelected(true);
                    break;
                case DOWN_VOTE:
                    review.setVoteStatus(ReviewVote.UP_VOTE);
                    review.setUpVotes(review.getUpVotes() + 1);
                    review.setDownVotes(review.getDownVotes() - 1);
                    view.setSelected(true);
                    binding.widgetThumbDown.setSelected(false);
                    binding.widgetThumbDown.setText(String.valueOf(review.getDownVotes()));
                    break;
                case UP_VOTE:
                    review.setVoteStatus(ReviewVote.NO_VOTE);
                    review.setUpVotes(review.getUpVotes() - 1);
                    view.setSelected(false);
                    break;
            }
            binding.widgetThumbUp.setText(String.valueOf(review.getUpVotes()));
        });

        binding.widgetThumbDown.setOnClickListener(view -> {
            if (review == null)
                return;
            Log.d(TAG, "widgetThumbDown: " + review.getVoteStatus());
            switch (review.getVoteStatus()) {
                case NO_VOTE:
                    review.setVoteStatus(ReviewVote.DOWN_VOTE);
                    review.setDownVotes(review.getDownVotes() + 1);
                    view.setSelected(true);
                    break;
                case UP_VOTE:
                    review.setVoteStatus(ReviewVote.DOWN_VOTE);
                    review.setDownVotes(review.getDownVotes() + 1);
                    review.setUpVotes(review.getUpVotes() - 1);
                    view.setSelected(true);
                    binding.widgetThumbUp.setSelected(false);
                    binding.widgetThumbUp.setText(String.valueOf(review.getUpVotes()));
                    break;
                case DOWN_VOTE:
                    review.setVoteStatus(ReviewVote.NO_VOTE);
                    review.setDownVotes(review.getDownVotes() - 1);
                    view.setSelected(false);
                    break;
            }
            binding.widgetThumbDown.setText(String.valueOf(review.getDownVotes()));
        });
         */
    }

    public void setReview(Review review){
        this.review = review;
        binding.setReview(review);
    }
}