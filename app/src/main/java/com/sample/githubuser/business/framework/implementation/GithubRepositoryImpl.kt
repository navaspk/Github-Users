package com.sample.githubuser.business.framework.implementation

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.sample.githubuser.business.data.network.result.Data
import com.sample.githubuser.business.data.repository.GithubRepository
import com.sample.githubuser.business.usecase.GithubUserListUseCase
import com.sample.githubuser.business.domain.model.ResponseItem
import com.sample.githubuser.business.framework.datasource.GithubPageDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    private val useCase: GithubUserListUseCase
) : GithubRepository {

    override fun getUserList(
        coroutineScope: CoroutineScope
    ): Data<ResponseItem> {
        val dataSourceFactory = GithubPageDataSourceFactory(
            useCase,
            coroutineScope
        )

        val networkState = Transformations.switchMap(dataSourceFactory.liveData) {
            it.networkState
        }
        return Data(
            LivePagedListBuilder(
                dataSourceFactory,
                GithubPageDataSourceFactory.pagedListConfig()
            ).build(), networkState
        )
    }
}
