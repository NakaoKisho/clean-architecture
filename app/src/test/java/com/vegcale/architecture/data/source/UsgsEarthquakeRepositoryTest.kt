package com.vegcale.architecture.data.source

import com.vegcale.architecture.data.Result
import com.vegcale.architecture.data.UsgsEarthquakeRepository
import com.vegcale.architecture.data.network.retrofit.RetrofitUsgsEarthquakeNetwork
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

class UsgsEarthquakeRepositoryTest {
    private lateinit var usgsEarthquakeRepository: UsgsEarthquakeRepository
    private val mockWebServer = MockWebServer()

    @Before
    fun createRepository() {
        mockWebServer.start(8080)
        val retrofitUsgsEarthquakeNetwork = RetrofitUsgsEarthquakeNetwork(mockWebServer.url("/").toString())

        usgsEarthquakeRepository = UsgsEarthquakeRepository(retrofitUsgsEarthquakeNetwork)
    }

    @After
    fun cleanUp() {
        mockWebServer.shutdown()
    }

    @Test
    fun getInfo_completed_returnsUsgsEarthquakeInfo() {
        val jsonResponseResource = javaClass.classLoader?.getResource("usgsEarthquakeResponse.json")
        val jsonResponse = IOUtils.toString(jsonResponseResource, StandardCharsets.UTF_8)
        val response = MockResponse().apply {
            setResponseCode(200)
            setBody(jsonResponse)
        }
        mockWebServer.enqueue(response)

        runTest {
            val data = usgsEarthquakeRepository.getInfo("geojson", 10, "time")

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
            val data = usgsEarthquakeRepository.getInfo("geojson", 10, "time")

            MatcherAssert.assertThat((data as Result.Error).exception, notNullValue())
        }
    }
}