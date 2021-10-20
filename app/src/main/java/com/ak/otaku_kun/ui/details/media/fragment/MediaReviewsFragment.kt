package com.ak.otaku_kun.ui.details.media.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.CustomViewVoteBinding
import com.ak.otaku_kun.databinding.FragmentListBinding
import com.ak.otaku_kun.databinding.ItemReviewBinding
import com.ak.otaku_kun.model.index.Review
import com.ak.otaku_kun.model.index.ReviewVote
import com.ak.otaku_kun.ui.base.adapter.BasePagingAdapter
import com.ak.otaku_kun.ui.base.custom.BaseViewHolder
import com.ak.otaku_kun.ui.base.fragment.BasePagingListFragment
import com.ak.otaku_kun.ui.details.media.MediaViewModel
import com.ak.otaku_kun.utils.StateEvent

private const val TAG = "MediaReviewsFragment"

class MediaReviewsFragment :
    BasePagingListFragment<FragmentListBinding, Review>(R.layout.fragment_list),
    ReviewAdapter.OnVoteClickListener {

    private val viewModel: MediaViewModel by viewModels(ownerProducer = { requireActivity() })
    private lateinit var reviewAdapter: ReviewAdapter

    override fun setUpUI() {
        reviewAdapter = ReviewAdapter()
        super.setUpUI()
    }

    override fun setObservers() {
        viewModel.mediaLiveData.observe(viewLifecycleOwner) { media ->
            Log.d(TAG, "setObservers: ${media.id}")
            media?.run {
                viewModel.triggerStateEvent(StateEvent.LoadMediaReviews)
                viewModel.mediaReviewLiveData.observe(viewLifecycleOwner) {
                    displayData(it)
                }
            }
        }
    }

    override fun onVote(review: Review) {
        //todo mutate
        Toast.makeText(requireContext(), "${review.id} ${review.voteStatus}", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onClick(reviewId: Int) {
        //todo get full body
        Toast.makeText(requireContext(), "$id", Toast.LENGTH_SHORT).show()
    }

    override fun getRecycler(): RecyclerView = binding.recycler
    override fun getRecyclerAdapter(): BasePagingAdapter<Review> = reviewAdapter
    override fun getRecyclerLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(requireContext())

    override fun getProgressBar(): View = binding.progressBar
    override fun getErrorView(): View = binding.viewError
}

private class ReviewAdapter : BasePagingAdapter<Review>(PHOTO_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Review> {
        val binding =
            ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    private class ReviewViewHolder(private val binding: ItemReviewBinding) :
        BaseViewHolder<Review>(binding.root) {

        override fun bind(item: Review) {
            binding.review = item
            binding.vote.setReview(item)
        }
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean =
                oldItem.equals(newItem)
        }
    }

    interface OnVoteClickListener {
        fun onVote(review: Review)
        fun onClick(reviewId: Int)
    }
}