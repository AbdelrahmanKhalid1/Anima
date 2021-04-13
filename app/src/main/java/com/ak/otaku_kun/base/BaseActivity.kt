package com.ak.otaku_kun.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ak.otaku_kun.R
import com.ak.type.MediaSort
import com.apollographql.apollo.ApolloClient
import okhttp3.MediaType

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

    }
}