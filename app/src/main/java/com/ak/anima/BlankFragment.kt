package com.ak.anima

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class BlankFragment : Fragment(R.layout.fragment_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            Toast.makeText(requireContext(), "${it.getInt("pos", -1)}", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int): BlankFragment = BlankFragment().apply {
            arguments = bundleOf(
                "pos" to position
            )
        }
    }
}