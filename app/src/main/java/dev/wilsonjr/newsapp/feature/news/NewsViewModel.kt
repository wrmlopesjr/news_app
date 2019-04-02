package dev.wilsonjr.newsapp.feature.sources

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import dev.wilsonjr.newsapp.base.delegates.CompositeDisposableDelegate
import dev.wilsonjr.newsapp.base.delegates.DisposableDelegate
import dev.wilsonjr.newsapp.api.datasource.ArticleDataSourceFactory
import dev.wilsonjr.newsapp.api.model.Article
import dev.wilsonjr.newsapp.api.model.Source
import dev.wilsonjr.newsapp.base.NetworkState
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class NewsViewModel(val articleDataSourceFactory: ArticleDataSourceFactory) : ViewModel(),
    DisposableDelegate by CompositeDisposableDelegate() {

    val articles: LiveData<PagedList<Article>>
    private val executor: Executor = Executors.newFixedThreadPool(5)
    val networkState: LiveData<NetworkState> = Transformations.switchMap(articleDataSourceFactory.mutableLiveData) {
        it.networkState
    }

    init {

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(10)
            .setPageSize(20).build()

        articles = LivePagedListBuilder(articleDataSourceFactory, pagedListConfig)
            .setFetchExecutor(executor)
            .build()

    }

    fun configureSource(source: Source) {
        articleDataSourceFactory.source = source
    }

    public override fun onCleared() {
        clearDisposables()
        super.onCleared()
    }

    fun invalidateDataSource() {
        articleDataSourceFactory.mutableLiveData.value?.invalidate()
    }
}
