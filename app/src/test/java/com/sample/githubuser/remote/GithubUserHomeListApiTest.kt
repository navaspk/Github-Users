package com.sample.githubuser.remote


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertWithMessage
import com.sample.githubuser.business.data.network.GithubServiceApi
import com.sample.core.supporter.GsonProvider
import com.sample.githubuser.MockResponseFileReader
import com.sample.util.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.CoreMatchers
import org.junit.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@ExperimentalCoroutinesApi
class GithubUserHomeListApiTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private var mockWebServer = MockWebServer()
    private lateinit var githubService: GithubServiceApi

    @Before
    fun setUp() {
        mockWebServer.start()
        githubService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(
                GsonConverterFactory.create(GsonProvider().instance)
            ).build()
            .create(GithubServiceApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `fetch github response is successful`() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("githubUser.json").content)

        mockWebServer.enqueue(response)
        runBlocking {
            val articleListResponse = githubService.fetchUsers()
            Truth.assertThat(articleListResponse.isSuccessful).isTrue()
        }
    }

    @Test
    fun `fetch github response body has desired num_results`() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("githubUser.json").content)

        mockWebServer.enqueue(response)
        runBlocking {
            val articleListResponse = githubService.fetchUsers()
            assertWithMessage("Ohh, There is an issue")
                .that(articleListResponse.body()?.size).isEqualTo(20)
        }
    }

    @Test
    fun requestUserList() {
        runBlocking {
            enqueueResponse("githubUser.json")
            val resultResponse = githubService.fetchUsers().body()

            val request = mockWebServer.takeRequest()
            Assert.assertNotNull(resultResponse)

            assertWithMessage("There is an error").that(request.path).contains("users")

            //Assert.assertThat(request.path, CoreMatchers.`is`("users"))
        }
    }

    @Test
    fun getGithubResponse() {
        runBlocking {
            enqueueResponse("githubUser.json")
            val resultResponse = githubService.fetchUsers().body()

            Assert.assertThat(resultResponse?.size, CoreMatchers.`is`(20))
        }
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader
            ?.getResourceAsStream("$fileName")
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse.setBody(
                source.readString(Charsets.UTF_8)
            )
        )
    }
}