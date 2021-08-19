package com.ak.otaku_kun.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ak.otaku_kun.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ak.type.MediaType.*

class DataBinding {

    companion object {

        @JvmStatic
        @BindingAdapter("image")
        fun setImage(view: ImageView, image: String) {
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
        fun setDiscoverCategoryTitle(view: TextView, position: Int, type : String) {
            when(type) {
                ANIME.rawValue -> {
                    view.text = when(position){
                        0 -> "Upcoming Next Season"
                        1 -> "All Time Popular"
                        2 -> "Top Scored"
                        else ->""
                    }
                }
                MANGA.rawValue -> {
                    view.text = when(position){
                        0 -> "All Time Popular"
                        1 -> "Top Scored"
                        else ->""
                    }
                }
            }

        }
    }
}