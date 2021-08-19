package com.ak.otaku_kun.di

import android.content.Context
import android.content.SharedPreferences
import com.ak.otaku_kun.R
import com.ak.otaku_kun.utils.QueryFilters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @ViewModelScoped
    @Provides
    fun provideQueryFilters(): QueryFilters = QueryFilters()
}