package com.ak.anima.di

import android.content.Context
import androidx.preference.PreferenceManager
import com.ak.anima.model.details.AuthUser
import com.ak.anima.utils.Const
import com.ak.anima.utils.Keys
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OAuthModule {

    @Singleton
    @Provides
    fun provideToken(@ApplicationContext context: Context): String {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        return sp.getString(Keys.KEY_TOKEN, Const.NO_VALUE) ?: Const.NO_VALUE //token
    }

    @Singleton
    @Provides
    fun provideAuthUser(@ApplicationContext context: Context): AuthUser? {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val jsonUser = sp.getString(Keys.KEY_OAUTH_USER, null)
//        Log.d("jsonUser", "provideAuthUser: $jsonUser")
        return Gson().fromJson(jsonUser, AuthUser::class.java)
    }
}