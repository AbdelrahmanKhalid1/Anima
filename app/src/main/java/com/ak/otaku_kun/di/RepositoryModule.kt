package com.ak.otaku_kun.di

import com.ak.otaku_kun.model.converter.StaffMapper
import com.ak.otaku_kun.remote.mapper.*
import com.ak.otaku_kun.repository.*
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
        mediaMapper: MediaMapper
    ): MediaRepository {
        return MediaRepository(apolloClient, mediaMapper)
    }

    @Singleton
    @Provides
    fun provideCharacterRepository(
        apolloClient: ApolloClient,
        characterMapper: CharacterMapper
    ): CharacterRepository {
        return CharacterRepository(apolloClient, characterMapper)
    }

    @Singleton
    @Provides
    fun provideStudioRepository(
        apolloClient: ApolloClient,
        studioMapper: StudioMapper
    ): StudioRepository {
        return StudioRepository(apolloClient, studioMapper)
    }

    @Singleton
    @Provides
    fun provideStaffRepository(
        apolloClient: ApolloClient,
        staffMapper: StaffMapper
    ): StaffRepository {
        return StaffRepository(apolloClient, staffMapper)
    }

    @Singleton
    @Provides
    fun provideUserRepository(
        apolloClient: ApolloClient,
        userMapper: UserMapper
    ): UserRepository {
        return UserRepository(apolloClient, userMapper)
    }
}