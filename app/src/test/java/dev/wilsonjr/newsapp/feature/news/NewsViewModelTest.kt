package dev.wilsonjr.newsapp.feature.news

import dev.wilsonjr.newsapp.TestConstants
import dev.wilsonjr.newsapp.api.model.ArticlesResponse
import dev.wilsonjr.newsapp.api.model.Source
import dev.wilsonjr.newsapp.api.model.SourceResponse
import dev.wilsonjr.newsapp.api.model.enums.Category
import dev.wilsonjr.newsapp.api.model.enums.Country
import dev.wilsonjr.newsapp.base.BaseTest
import dev.wilsonjr.newsapp.base.FileUtils
import dev.wilsonjr.newsapp.base.NetworkState
import dev.wilsonjr.newsapp.base.TestSuite
import dev.wilsonjr.newsapp.base.mock.endpoint.ResponseHandler
import dev.wilsonjr.newsapp.feature.sources.NewsViewModel
import dev.wilsonjr.newsapp.utils.JsonUtils
import junit.framework.Assert.assertEquals
import okhttp3.Request
import org.junit.Before
import org.junit.Test
import org.koin.test.get


class NewsViewModelTest : BaseTest() {

    val emptyResponse = ArticlesResponse(ArrayList(), "ok", 0)

    lateinit var viewModel: NewsViewModel

    @Before
    fun setupTest() {
        viewModel = TestSuite.get()
    }

    @Test
    fun testGetSources() {
        viewModel.configureSource(Source("test", "br", "test123", "12", "pt", "Teste", ""))

        viewModel.invalidateDataSource()

        assert(viewModel.articles.value?.size == 136)
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)

        viewModel.onCleared()

        assert(viewModel.getDisposables().isEmpty())
    }
}