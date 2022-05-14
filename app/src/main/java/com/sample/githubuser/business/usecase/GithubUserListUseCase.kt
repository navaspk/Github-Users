package com.sample.githubuser.business.usecase

import com.sample.core.business.usecase.MainUseCase
import com.sample.githubuser.business.data.network.GithubServiceApi
import com.sample.githubuser.business.domain.model.ResponseItem
import retrofit2.Response
import javax.inject.Inject

class GithubUserListUseCase @Inject constructor(
    private val apiService: GithubServiceApi
) : MainUseCase<List<ResponseItem>, GithubUserListUseCase.Params>() {

    override suspend fun createUseCase(params: Params?): Response<List<ResponseItem>> {
        return apiService.fetchUsers(since = params?.page ?: 0)
    }

    data class Params constructor(val page: Int)
}
