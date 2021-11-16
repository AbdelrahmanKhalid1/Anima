package com.ak.anima.di

import android.content.Context
import com.ak.anima.db.AnimaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnimaDatabaseProvider {
    @Singleton
    @Provides
    fun provideAnimeDatabase(
        @ApplicationContext context: Context,
    ): AnimaDatabase = AnimaDatabase.getInstance(context)
}