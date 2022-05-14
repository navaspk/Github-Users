package com.sample.githubuser.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sample.githubuser.business.framework.implementation.GithubRepositoryImpl
import com.sample.githubuser.business.data.repository.GithubRepository
import com.sample.githubuser.business.usecase.GithubUserListUseCase
import com.sample.core.supporter.GsonProvider
import com.sample.githubuser.business.data.network.GithubServiceApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setDateFormat(GsonProvider.ISO_8601_DATE_FORMAT).create()
    }

    @Provides
    @Singleton
    fun provideGsonProvider(): GsonProvider = GsonProvider()

    @Provides
    @Singleton
    fun provideUserListUseCase(
        githubService: GithubServiceApi
    ) = GithubUserListUseCase(githubService)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindUserDataRepository(githubRepositoryImpl: GithubRepositoryImpl): GithubRepository
}
