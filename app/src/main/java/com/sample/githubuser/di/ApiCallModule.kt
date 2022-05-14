package com.sample.githubuser.di

import com.sample.githubuser.business.data.network.GithubServiceApi
import com.sample.core.supporter.NetworkUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ApiCallModule {

    @Provides
    fun provideArticleService(networkUtil: NetworkUtil) =
        networkUtil.create(GithubServiceApi::class.java)
}
