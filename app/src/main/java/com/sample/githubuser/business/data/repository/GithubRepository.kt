package com.sample.githubuser.business.data.repository

import com.sample.githubuser.business.data.network.result.Data
import com.sample.githubuser.business.domain.model.ResponseItem
import kotlinx.coroutines.CoroutineScope

interface GithubRepository {

    fun getUserList(
        coroutineScope: CoroutineScope
    ): Data<ResponseItem>

}
