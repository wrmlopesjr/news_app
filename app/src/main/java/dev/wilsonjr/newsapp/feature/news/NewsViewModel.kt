package dev.wilsonjr.newsapp.feature.sources

import addListValues
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import clear
import dev.wilsonjr.newsapp.api.model.Article
import dev.wilsonjr.newsapp.api.model.Source
import dev.wilsonjr.newsapp.api.repository.NewsRepository
import dev.wilsonjr.newsapp.base.NetworkState
import dev.wilsonjr.newsapp.base.delegates.CompositeDisposableDelegate
import dev.wilsonjr.newsapp.base.delegates.DisposableDelegate
import java.lang.RuntimeException


class NewsViewModel(val newsRepository: NewsRepository) : ViewModel(),
    DisposableDelegate by CompositeDisposableDelegate() {

    val articles = MutableLiveData<ArrayList<Article>>()
    val networkState = MutableLiveData<NetworkState>()
    private var source: Source? = null

    public override fun onCleared() {
        clearDisposables()
        super.onCleared()
    }

    fun configureSource(source: Source){
        this.source = source
    }

    fun loadNews() {
        articles.clear()
        networkState.postValue(NetworkState.RUNNING)
        source?.let {
            addDisposable(
                newsRepository.getEverything(it.id).subscribe({ response ->
                    articles.addListValues(response.articles)
                    if (response.articles.isEmpty()) {
                        networkState.postValue(NetworkState.EMPTY)
                    } else {
                        networkState.postValue(NetworkState.SUCCESS)
                    }
                },
                    {
                        networkState.postValue(NetworkState.ERROR)
                    })
            )
        } ?: throw RuntimeException("Source not set")

    }

    fun loadMoreNews(currentPage: Int) {
        addDisposable(
            newsRepository.getEverything(source?.id, currentPage).subscribe({
                articles.addListValues(it.articles)
            },
                {
                })
        )
    }
}
