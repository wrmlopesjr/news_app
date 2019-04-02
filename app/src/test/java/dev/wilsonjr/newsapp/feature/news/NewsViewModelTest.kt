package dev.wilsonjr.newsapp.feature.news

import dev.wilsonjr.newsapp.TestConstants
import dev.wilsonjr.newsapp.api.model.ArticlesResponse
import dev.wilsonjr.newsapp.api.model.Source
import dev.wilsonjr.newsapp.base.BaseTest
import dev.wilsonjr.newsapp.base.NetworkState
import dev.wilsonjr.newsapp.base.TestSuite
import dev.wilsonjr.newsapp.feature.sources.NewsViewModel
import dev.wilsonjr.newsapp.utils.JsonUtils
import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import org.junit.Before
import org.junit.Test
import org.koin.test.get
import java.lang.RuntimeException


class NewsViewModelTest : BaseTest() {

    val emptyResponse = ArticlesResponse(ArrayList(), "ok", 0)

    lateinit var viewModel: NewsViewModel

    @Before
    fun setupTest() {
        viewModel = TestSuite.get()
    }

    @Test
    fun testGetNews() {
        viewModel.configureSource(Source("test", "br", "test123", "12", "pt", "Teste", ""))

        viewModel.loadNews()

        assert(viewModel.articles.value?.size == 11)
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)

        viewModel.onCleared()

        assert(viewModel.getDisposables().isEmpty())
    }

    @Test
    fun testLoadMoreNews() {
        viewModel.configureSource(Source("test", "br", "test123", "12", "pt", "Teste", ""))

        viewModel.loadNews()

        assert(viewModel.articles.value?.size == 11)
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)

        viewModel.loadMoreNews(2)

        assert(viewModel.articles.value?.size == 22)
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)

        //no more pages
        TestSuite.mock(TestConstants.newsURL).body(JsonUtils.toJson(emptyResponse)).apply()

        viewModel.loadMoreNews(3)

        assert(viewModel.articles.value?.size == 22)
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)

        viewModel.onCleared()

        assert(viewModel.getDisposables().isEmpty())
    }

    @Test
    fun testSourceNotSet() {
        try{
            viewModel.loadNews()
        } catch (e: RuntimeException){
            return
        }

        fail()
    }

    @Test
    fun testEmptyNews() {
        viewModel.configureSource(Source("test", "br", "test123", "12", "pt", "Teste", ""))

        TestSuite.mock(TestConstants.newsURL).body(JsonUtils.toJson(emptyResponse)).apply()

        viewModel.loadNews()

        assert(viewModel.articles.value?.size == 0)
        assertEquals(NetworkState.EMPTY, viewModel.networkState.value)
    }

    @Test
    fun testErrorSources() {
        viewModel.configureSource(Source("test", "br", "test123", "12", "pt", "Teste", ""))

        TestSuite.mock(TestConstants.newsURL).throwConnectionError().apply()

        viewModel.loadNews()

        assert(viewModel.articles.value == null)
        assertEquals(NetworkState.ERROR, viewModel.networkState.value)
    }
}