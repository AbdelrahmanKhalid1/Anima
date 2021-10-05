package com.ak.otaku_kun.utils

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.SectionMediaDetailsBinding
import com.ak.otaku_kun.model.details.Anime
import com.ak.otaku_kun.model.details.Manga
import com.ak.otaku_kun.model.details.Media as MediaDetails
import com.ak.otaku_kun.ui.view.MediaFullDetailsCustomView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ak.type.MediaType.*
import com.borjabravo.readmoretextview.ReadMoreTextView

class DataBinding {

    companion object {

        @JvmStatic
        @BindingAdapter("image")
        fun setImage(view: ImageView, image: String?) {
            Glide.with(view)
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.drawable)
//                .error()
                .into(view)
        }

        @JvmStatic
        @BindingAdapter("isFavorite")
        fun setImage(view: ImageView, isFavorite: Boolean) {
            val imageResource = if (isFavorite) R.drawable.ic_stared else R.drawable.ic_star
            view.setImageResource(imageResource)
        }

        @JvmStatic
        @BindingAdapter("format", "status", requireAll = true)
        fun setDesc(view: TextView, format: String, status: String) {
            "$format - $status".also { view.text = it }
        }

        @JvmStatic
        @BindingAdapter("position", "type", requireAll = true)
        fun setDiscoverCategoryTitle(view: TextView, position: Int, type: String) {
            when (type) {
                ANIME.rawValue -> {
                    view.text = when (position) {
                        0 -> "Upcoming Next Season"
                        1 -> "All Time Popular"
                        2 -> "Top Scored"
                        else -> ""
                    }
                }
                MANGA.rawValue -> {
                    view.text = when (position) {
                        0 -> "All Time Popular"
                        1 -> "Top Scored"
                        else -> ""
                    }
                }
            }

        }

        @JvmStatic
        @BindingAdapter("score", "mediaListEntry", requireAll = true)
        fun setRating(view: TextView, score: String, mediaListEntry: MediaDetails.MediaListEntry?) {
            view.text = mediaListEntry?.score?.toString() ?: score
        }

        @JvmStatic
        @BindingAdapter("mediaListEntry")
        fun setPlan(view: ImageView, mediaListEntry: MediaDetails.MediaListEntry?) {
            mediaListEntry?.let {
                view.visibility = View.VISIBLE
                //TODO set plan icon
//                val imageId = when(mediaListEntry.status){
//
//                }
//                view.setImageResource()
            }
        }

        /**
         * sets section media details with it's parameters
         *      ==> sets title, status, format, duration
         *      and mean score
         *
         * @param media Media Details
         * @param view MediaFullDetailsCustomView
         * */
        @JvmStatic
        @BindingAdapter("mediaDetails")
        fun setMediaDetails(view: MediaFullDetailsCustomView, media: MediaDetails?) {
            view.setMedia(media)
        }

        @JvmStatic
        @BindingAdapter("desc")
        fun setDesc(view: ReadMoreTextView, desc: String?) {
            if (desc == null)
                return
            view.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(desc, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(desc)
            }
            view.setTrimLength(view.text.length * 3 / 4)
        }

        /**
         * sets section media details with it's parameters
         *      ==> sets studio, origin of media and media season
         *
         * @param media Media Details
         * @param view R.layout.section_media_details
         * */
        @JvmStatic
        @BindingAdapter("mediaInfo")
        fun setMediaInfo(view: View, media: MediaDetails?) {
            if (media == null)
                return
            val binding = DataBindingUtil.bind<SectionMediaDetailsBinding>(view)
            binding?.media = media
        }

        /**
         * sets text view title for number of episode for anime
         * or chapter for manga
         *
         * @param media Media Details
         * @param view TextView
         * */
        @JvmStatic
        @BindingAdapter("episodeTitle")
        fun setEpisodeTextTitle(view: TextView, media: MediaDetails?) {
            view.text = when (media) {
                is Anime -> "Total Episodes"
                is Manga -> "Total Chapters"
                else -> Const.NO_VALUE
            }
        }

        /**
         * sets text view for number of episode for anime
         * or chapter for anime
         *
         * @param media Media Details
         * @param view TextView
         * */
        @JvmStatic
        @BindingAdapter("episode")
        fun setEpisodeText(view: TextView, media: MediaDetails?) {
            view.text = when (media) {
                is Anime -> "${media.episodes} Episodes"
                is Manga -> "${media.chapters} Chapters"
                else -> Const.NO_VALUE
            }
        }

        /**
         * sets text view title for episode duration for anime
         * or volumes for manga
         *
         * @param media Media Details
         * @param view TextView
         * */
        @JvmStatic
        @BindingAdapter("durationTitle")
        fun setEpisodeDurationTitle(view: TextView, media: MediaDetails?) {
            view.text = when (media) {
                is Anime -> "Episode Duration"
                is Manga -> "Total Volumes"
                else -> Const.NO_VALUE
            }
        }

        /**
         * sets text view text with episode duration for anime
         * or number of volumes for manga
         *
         * @param media Media Details
         * @param view TextView
         * */
        @JvmStatic
        @BindingAdapter("duration")
        fun setEpisodeDuration(view: TextView, media: MediaDetails?) {
            view.text = when(media){
                is Anime -> "${media.duration} Minutes"
                is Manga -> "${media.volumes} Volumes"
                else -> Const.NO_VALUE
            }
        }
    }
}