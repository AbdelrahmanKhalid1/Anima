package com.ak.anima.ui.dialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.ak.anima.R
import com.ak.anima.databinding.DialogLoginBinding
import com.ak.anima.utils.Keys

private const val TAG = "LoginDialog"
class LoginDialog() : DialogFragment(R.layout.dialog_login) {
    private var _binding: DialogLoginBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogLoginBinding.bind(view)

        binding.authSignIn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse(Keys.AUTH_URL));
            startActivity(intent);
        }
    }
}