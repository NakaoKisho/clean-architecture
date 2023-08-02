package com.vegcale.architecture.data.source

import com.vegcale.architecture.data.P2pquakeRepository
import com.vegcale.architecture.data.Result
import com.vegcale.architecture.data.network.retrofit.RetrofitP2pquakeNetwork
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.apache.commons.io.IOUtils
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets

class P2pquakeRepositoryTest {
    private lateinit var p2pquakeRepository: P2pquakeRepository
    private val mockWebServer = MockWebServer()

    @Before
    fun createRepository() {
        mockWebServer.start(8080)
        val retrofitP2pquakeNetwork = RetrofitP2pquakeNetwork(mockWebServer.url("/").toString())

        p2pquakeRepository = P2pquakeRepository(retrofitP2pquakeNetwork)
    }

    @After
    fun cleanUp() {
        mockWebServer.shutdown()
    }

    @Test
    fun getInfo_completed_returnsListOfP2pquakeInfo() {
        val jsonResponseResource = javaClass.classLoader?.getResource("p2pquakeResponse.json")
        val jsonResponse = IOUtils.toString(jsonResponseResource, StandardCharsets.UTF_8)
        val response = MockResponse().apply {
            setResponseCode(200)
            setBody(jsonResponse)
        }
        mockWebServer.enqueue(response)

        runTest {
            val data = p2pquakeRepository.getInfo(10, 0)

            MatcherAssert.assertThat((data as Result.Success).data, notNullValue())
        }
    }

    @Test
    fun getInfo_noCompleted_throwsError() {
        val response = MockResponse().apply {
            setResponseCode(400)
        }
        mockWebServer.enqueue(response)

        runTest {
            val data = p2pquakeRepository.getInfo(10, 0)

            MatcherAssert.assertThat((data as Result.Error).exception, notNullValue())
        }
    }
}