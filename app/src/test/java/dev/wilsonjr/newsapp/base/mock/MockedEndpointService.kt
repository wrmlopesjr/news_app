package dev.wilsonjr.newsapp.base.mock

import android.util.Log
import dev.wilsonjr.newsapp.base.FileUtils
import dev.wilsonjr.newsapp.base.mock.endpoint.EndpointMock
import dev.wilsonjr.newsapp.base.repository.EndpointService
import okhttp3.*
import java.util.HashMap

class MockedEndpointService : EndpointService() {

    private val JSON_MEDIA_TYPE = MediaType.parse("application/json")
    private val mockedEndpoints = HashMap<String, EndpointMock>()

    //we override the get builder method and use a interceptor
    //so instead of getting the data from the api it's going to load the data from JSON files
    override fun getBuilder(): OkHttpClient.Builder {
        val builder = super.getBuilder()
        builder.addInterceptor(mockInterceptor())
        return builder
    }

    private fun mockInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val path = getPath(request)

            val endpoint = request.method().toUpperCase() + " " + path
            var mock: EndpointMock? = mockedEndpoints[endpoint]
            if (mock == null) {
                mock = mockedEndpoints[path]
            }
            if (mock != null) {
                return@Interceptor customResponse(chain, mock)
            }

            defaultResponse(chain, path)
        }
    }

    private fun getPath(request: Request): String {
        var path = request.url().encodedPath()
        if ("/" == path) {
            path = request.url().toString()
        }
        return path
    }

    private fun customResponse(chain: Interceptor.Chain, mock: EndpointMock): Response {
        val builder = defaultBuilder(chain)

        if (mock.getCode(chain.request()) == EndpointMock.FORCED_MOCK_EXCEPTION_CODE) {
            throw RuntimeException("EndpointMock test exception")
        }
        return mock.error?.let { error ->
            builder.code(mock.getCode(chain.request()))
                .body(error.response().errorBody())
                .build()
        } ?: builder.code(mock.getCode(chain.request()))
            .body(ResponseBody.create(JSON_MEDIA_TYPE, mock.getResponse(chain.request())))
            .build()
    }

    private fun defaultResponse(chain: Interceptor.Chain, endpoint: String): Response {
        val builder = defaultBuilder(chain)

        val content = FileUtils.readJson(endpoint.substring(1) + ".json") ?: return endpointNotMocked(endpoint)
        return builder.code(200)
            .body(ResponseBody.create(JSON_MEDIA_TYPE, content))
            .build()
    }

    private fun defaultBuilder(chain: Interceptor.Chain): Response.Builder {
        val builder = Response.Builder()
        return builder.message("")
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
    }

    private fun endpointNotMocked(endpoint: String): Response {
        Log.e("MockedEndpointService", "endpoint not mocked -> $endpoint")
        throw RuntimeException("endpoint not mocked -> $endpoint")
    }

    fun addMockedEndpoint(url: String, mock: EndpointMock) {
        mockedEndpoints[url] = mock
    }

    fun clearMocks() {
        mockedEndpoints.clear()
    }

    fun removeMock(url: String) {
        mockedEndpoints.remove(url)
    }
}