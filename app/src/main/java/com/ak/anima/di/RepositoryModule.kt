package com.ak.anima.di

import com.ak.anima.db.AnimaDatabase
import com.ak.anima.remote.mapper.StaffMapper
import com.ak.anima.remote.mapper.*
import com.ak.anima.repository.*
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
        mediaMapper: MediaMapper,
        animaDatabase: AnimaDatabase,
    ): MediaRepository {
        return MediaRepository(apolloClient, mediaMapper, animaDatabase)
    }

    @Singleton
    @Provides
    fun provideCharacterRepository(
        apolloClient: ApolloClient,
        characterMapper: CharacterMapper,
    ): CharacterRepository {
        return CharacterRepository(apolloClient, characterMapper)
    }

    @Singleton
    @Provides
    fun provideStudioRepository(
        apolloClient: ApolloClient,
        studioMapper: StudioMapper,
    ): StudioRepository {
        return StudioRepository(apolloClient, studioMapper)
    }

    @Singleton
    @Provides
    fun provideStaffRepository(
        apolloClient: ApolloClient,
        staffMapper: StaffMapper,
    ): StaffRepository {
        return StaffRepository(apolloClient, staffMapper)
    }

    @Singleton
    @Provides
    fun provideUserRepository(
        apolloClient: ApolloClient,
        userMapper: UserMapper,
    ): UserRepository {
        return UserRepository(apolloClient, userMapper)
    }
}