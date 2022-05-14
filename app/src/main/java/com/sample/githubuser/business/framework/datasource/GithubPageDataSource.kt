package com.sample.githubuser.business.framework.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.sample.core.GithubLogger
import com.sample.core.business.controller.ItemResult
import com.sample.githubuser.business.domain.state.NetworkState
import com.sample.githubuser.business.usecase.GithubUserListUseCase
import com.sample.githubuser.business.domain.model.ResponseItem
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class GithubPageDataSource @Inject constructor(
    private val remoteDataSource: GithubUserListUseCase,
    private val coroutineScope: CoroutineScope
) : PageKeyedDataSource<Int, ResponseItem>() {

    val networkState = MutableLiveData<NetworkState>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ResponseItem>
    ) {
        networkState.postValue(NetworkState.LOADING)
        fetchData(page = 0) {
            it?.let {
                callback.onResult(it, null, 1)
            }

        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ResponseItem>) {
        networkState.postValue(NetworkState.LOADING)
        val page = params.key
        fetchData(page = page) {
            it?.let { it1 -> callback.onResult(it1, page + 1) }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ResponseItem>) {
        val page = params.key
        fetchData(page) {
            it?.let { it1 -> callback.onResult(it1, page - 1) }
        }
    }

    private fun fetchData(page: Int, callback: (List<ResponseItem?>?) -> Unit) {
        coroutineScope.launch(getJobErrorHandler()) {
            when (val response =
                remoteDataSource.execute(GithubUserListUseCase.Params(page = page))) {

                is ItemResult.Error -> {
                    networkState.postValue(NetworkState.ERROR(response.message ?: "Unknown error"))
                    postError(response.message)
                }

                is ItemResult.Success -> {
                    val results = response.data
                    callback(results)
                    networkState.postValue(NetworkState.LOADED)
                }

            }
        }
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        postError(e.message ?: e.toString())
    }

    private fun postError(message: String?) {
        GithubLogger.e("GithubPageDataSource", "An error happened: $message")
    }
}
