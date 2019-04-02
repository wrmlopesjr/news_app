package dev.wilsonjr.newsapp.base

import dev.wilsonjr.newsapp.base.mock.MockedEndpointService
import dev.wilsonjr.newsapp.base.mock.endpoint.EndpointMock
import dev.wilsonjr.newsapp.base.repository.EndpointService
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.mock.declare

object TestSuite : KoinTest{

    var endpointService: MockedEndpointService? = null
        private set

    fun mock(url: String): EndpointMock {
        var url = url
        if (!url.startsWith("/") && !url.startsWith("http")) {
            url = "/$url"
        }
        return EndpointMock(url, endpointService)
    }

    fun clearEndpointMocks() {
        endpointService!!.clearMocks()
    }

    private fun initMockedEndpointService() {
        endpointService = MockedEndpointService()

        declare {
            single { endpointService as EndpointService }
        }
    }

    fun init(instrumented: Boolean) {
        GlobalContext.getOrNull() ?: startKoin { modules(appComponent) }

        if(!instrumented) RxTestScheduler.init()

        initMockedEndpointService()
    }

    fun clear() {
        clearEndpointMocks()
        stopKoin()
    }

}
