package com.ak.otaku_kun.di

import com.ak.otaku_kun.remote.MediaMapper
import com.ak.otaku_kun.repository.MediaRepository
import com.apollographql.apollo.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMediaRepository(
        apolloClient: ApolloClient,
        mediaMapper : MediaMapper
    ): MediaRepository {
        return MediaRepository(apolloClient, mediaMapper)
    }
}