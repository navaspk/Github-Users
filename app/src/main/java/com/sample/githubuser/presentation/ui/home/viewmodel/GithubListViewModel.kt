package com.sample.githubuser.presentation.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.sample.githubuser.base.BaseNavigator
import com.sample.githubuser.base.BaseViewModel
import com.sample.githubuser.business.data.network.result.Data
import com.sample.githubuser.business.data.repository.GithubRepository
import com.sample.githubuser.business.domain.model.ResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * VM class responsible for making call to repository to get the data from data source.
 * Same view model can be use for future to get different data from dat source as we implement using
 * intention
 */
@HiltViewModel
class GithubListViewModel @Inject constructor(
    private val repository: GithubRepository
) : BaseViewModel<BaseNavigator>() {

    // region VARIABLES

    val userIntent = Channel<GithubIntent>(Channel.UNLIMITED)
    var githubUserList: Data<ResponseItem>? = null

    init {
        handleUserIntent()
    }

    // endregion

    // region UTIL

    private fun handleUserIntent() {
        viewModelScope.launch(Dispatchers.IO) {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is GithubIntent.GetSearchedGithub ->
                        getGithubUserList()
                }
            }
        }
    }

    private fun getGithubUserList() {
        if (githubUserList == null) {
            githubUserList = repository.getUserList(viewModelScope)
        }
    }

    // endregion


    sealed class GithubIntent {
        object GetSearchedGithub : GithubIntent()
    }
}
