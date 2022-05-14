package com.sample.githubuser.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.sample.githubuser.business.data.network.GithubServiceApi
import com.sample.githubuser.business.usecase.GithubUserListUseCase
import com.sample.githubuser.business.data.repository.GithubRepository
import com.sample.githubuser.business.framework.implementation.GithubRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

//@RunWith(JUnit4::class)
class GithubRepositoryTest {

    private lateinit var repository: GithubRepository
    private val service = mock(GithubServiceApi::class.java)
    private val remoteDataSource = GithubUserListUseCase(service)

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        repository = GithubRepositoryImpl(remoteDataSource)
    }

    @Test
    fun loadGithubUsersFromNetwork() {
        runBlocking {
            val data = repository.getUserList(coroutineScope = coroutineScope)
            Truth.assertThat(data).isNotNull()
        }
    }
}
