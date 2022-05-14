package com.sample.githubuser.business.framework.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.sample.githubuser.business.usecase.GithubUserListUseCase
import com.sample.githubuser.business.domain.model.ResponseItem
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GithubPageDataSourceFactory @Inject constructor(
    private val dataSource: GithubUserListUseCase,
    private val scope: CoroutineScope

) : DataSource.Factory<Int, ResponseItem>() {

    val liveData = MutableLiveData<GithubPageDataSource>()

    override fun create(): DataSource<Int, ResponseItem> {

        val source = GithubPageDataSource(dataSource, scope)
        liveData.postValue(source)
        return source
    }

    companion object {
        private const val PAGE_SIZE = 10
        fun pagedListConfig() = PagedList.Config.Builder()
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(true)
            .build()
    }
}
