package com.sample.githubuser.business.data.network

import com.sample.githubuser.business.domain.model.ResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubServiceApi {

    @GET("users")
    suspend fun fetchUsers(
        @Query("since") since: Int = 0,
        @Query("per_page") page : Int = 20
    ): Response<List<ResponseItem>>

}
