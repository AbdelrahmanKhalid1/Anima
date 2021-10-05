package com.ak.otaku_kun.ui.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ak.otaku_kun.R
import com.ak.otaku_kun.model.details.Media.Trailer
import com.ak.otaku_kun.ui.details.media.MediaViewModel
import com.ak.otaku_kun.utils.Const
import com.ak.otaku_kun.utils.DataBinding
import com.ak.otaku_kun.utils.Keys
import kotlinx.android.synthetic.main.section_media_trailer.*

class YoutubeView : Fragment(R.layout.section_media_trailer) {

    private val viewModel: MediaViewModel by viewModels(ownerProducer = { requireActivity() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.mediaLiveData.value?.run {
            trailer?.let { trailer ->
                DataBinding.setImage(thumbnail, trailer.thumbnail)

                feed_play_back.setOnClickListener {
                    try {
                        val youtubeLink: String = buildYoutube(trailer.id)
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(youtubeLink)
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                        Toast.makeText(
                            context,
                            R.string.init_youtube_missing,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun buildYoutube(id: String): String {
        return if (!id.contains("youtube")) {
            if (id.contains(Const.YOUTUBE_SHORT)) Const.YOUTUBE + id.replace(
                Const.YOUTUBE_SHORT, ""
            ) else Const.YOUTUBE + id
        } else id
    }
}