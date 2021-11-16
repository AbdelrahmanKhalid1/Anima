package com.ak.anima.di

import com.ak.anima.utils.Const
import com.ak.anima.utils.Keys
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
    fun provideApolloClient(token: String): ApolloClient {
        val instance = ApolloClient.builder().serverUrl(Keys.BASE_URL)
        return if (token == Const.NO_VALUE)
            instance.build()
        else
            instance.okHttpClient(OkHttpClient.Builder().addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()

                chain.proceed(request)
            }.build())
                .build()
    }
}