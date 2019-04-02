package dev.wilsonjr.newsapp.feature.sources

import dev.wilsonjr.newsapp.TestConstants
import dev.wilsonjr.newsapp.api.model.SourceResponse
import dev.wilsonjr.newsapp.api.model.enums.Category
import dev.wilsonjr.newsapp.api.model.enums.Country
import dev.wilsonjr.newsapp.base.BaseTest
import dev.wilsonjr.newsapp.base.FileUtils
import dev.wilsonjr.newsapp.base.NetworkState
import dev.wilsonjr.newsapp.base.TestSuite
import dev.wilsonjr.newsapp.base.mock.endpoint.ResponseHandler
import dev.wilsonjr.newsapp.utils.JsonUtils
import junit.framework.Assert.assertEquals
import okhttp3.Request
import org.junit.Before
import org.junit.Test
import org.koin.test.get


class SourcesViewModelTest : BaseTest() {

    val emptyResponse = SourceResponse(ArrayList(), "ok")

    lateinit var viewModel: SourcesViewModel

    @Before
    fun setupTest() {
        viewModel = TestSuite.get()
    }

    @Test
    fun testGetSources() {
        viewModel.loadSources()

        assert(viewModel.sources.value?.size == 136)
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)

        viewModel.onCleared()

        assert(viewModel.getDisposables().isEmpty())
    }

    @Test
    fun testEmptySources() {
        TestSuite.mock(TestConstants.sourcesURL).body(JsonUtils.toJson(emptyResponse)).apply()

        viewModel.loadSources()

        assert(viewModel.sources.value?.size == 0)
        assertEquals(NetworkState.EMPTY, viewModel.networkState.value)
    }

    @Test
    fun testErrorSources() {
        TestSuite.mock(TestConstants.sourcesURL).throwConnectionError().apply()

        viewModel.loadSources()

        assert(viewModel.sources.value == null)
        assertEquals(NetworkState.ERROR, viewModel.networkState.value)
    }

    @Test
    fun testSelectCountry() {
        TestSuite.mock(TestConstants.sourcesURL).body(object : ResponseHandler {
            override fun getResponse(request: Request, path: String): String {
                return request.url().queryParameter("country")?.let {
                    JsonUtils.toJson(emptyResponse)
                } ?: FileUtils.readJson(path.substring(1) + ".json")!!
            }
        }).apply()

        viewModel.changeCountry(Country.BR)

        assert(viewModel.sources.value?.size == 0)
        assertEquals(NetworkState.EMPTY, viewModel.networkState.value)

        viewModel.changeCountry(Country.ALL)

        assert(viewModel.sources.value?.size == 136)
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)

    }

    @Test
    fun testSelectCategory() {
        TestSuite.mock(TestConstants.sourcesURL).body(object : ResponseHandler {
            override fun getResponse(request: Request, path: String): String {
                return request.url().queryParameter("category")?.let {
                    JsonUtils.toJson(emptyResponse)
                } ?: FileUtils.readJson(path.substring(1) + ".json")!!
            }
        }).apply()

        viewModel.changeCategory(Category.BUSINESS)

        assert(viewModel.sources.value?.size == 0)
        assertEquals(NetworkState.EMPTY, viewModel.networkState.value)

        viewModel.changeCategory(Category.ALL)

        assert(viewModel.sources.value?.size == 136)
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)

    }
}