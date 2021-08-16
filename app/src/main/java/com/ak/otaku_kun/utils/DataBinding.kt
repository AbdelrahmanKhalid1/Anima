package com.ak.otaku_kun.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class DataBinding {

    companion object {

        @JvmStatic
        @BindingAdapter("image")
        fun setImage(view: ImageView, image : String){
            Glide.with(view)
                .load(image)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .placeholder(R.drawable)
//                .error()
                .into(view)
        }
    }
}