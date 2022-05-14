package com.sample.githubuser.business.data.network.result

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.sample.githubuser.business.domain.state.NetworkState

/**
 * Data class that is necessary for a UI to show a listing and interact
 */
data class Data<T>(
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>
)
