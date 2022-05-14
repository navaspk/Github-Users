package com.sample.githubuser.di

import android.content.Context
import com.sample.core.business.data.cache.PrefDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    @Singleton
    fun providePreferenceManager(@ApplicationContext appContext: Context) =
        PrefDataStore(appContext)
}