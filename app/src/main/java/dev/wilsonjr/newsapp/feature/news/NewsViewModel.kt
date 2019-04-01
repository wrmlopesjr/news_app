package dev.wilsonjr.newsapp.feature.sources

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.wilsonjr.faire.base.delegates.CompositeDisposableDelegate
import dev.wilsonjr.faire.base.delegates.DisposableDelegate
import dev.wilsonjr.newsapp.api.datasource.ArticleDataSource
import dev.wilsonjr.newsapp.api.model.Article
import dev.wilsonjr.newsapp.api.model.Source
import dev.wilsonjr.newsapp.api.repository.NewsRepository
import dev.wilsonjr.newsapp.base.NetworkState
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import androidx.paging.PagedList
import androidx.paging.LivePagedListBuilder
import androidx.paging.RxPagedListBuilder
import dev.wilsonjr.newsapp.api.datasource.ArticleDataSourceFactory


class NewsViewModel(private val articleDataSourceFactory: ArticleDataSourceFactory) : ViewModel(),
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

    fun loadArticles(source: Source) {





//        addDisposable(newsRepository.getTopHeadlines(source.id).subscribe({
//            articles.value = it.articles
//        }, {
//            loadArticles(source) //retry the load
//        }))
    }

    override fun onCleared() {
        clearDisposables()
        super.onCleared()
    }
}
