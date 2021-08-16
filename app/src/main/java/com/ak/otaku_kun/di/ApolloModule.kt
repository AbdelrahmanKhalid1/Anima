package com.ak.otaku_kun.di

import com.ak.otaku_kun.utils.Keys
import com.apollographql.apollo.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApolloModule {

    @Singleton
    @Provides
    fun provideOkHttpClient() : OkHttpClient = OkHttpClient.Builder().build()

    @Singleton
    @Provides
    fun provideApolloClient(): ApolloClient =
        ApolloClient.builder().serverUrl(Keys.BASE_URL).build()

}