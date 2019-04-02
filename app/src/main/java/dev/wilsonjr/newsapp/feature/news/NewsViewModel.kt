package dev.wilsonjr.newsapp.feature.sources

import addListValues
import androidx.lifecycle.MutableLiveData
import clear
import dev.wilsonjr.newsapp.api.model.Article
import dev.wilsonjr.newsapp.api.model.Source
import dev.wilsonjr.newsapp.api.repository.NewsRepository
import dev.wilsonjr.newsapp.base.BaseViewModel
import dev.wilsonjr.newsapp.base.NetworkState


class NewsViewModel(private val newsRepository: NewsRepository) : BaseViewModel() {

    val articles = MutableLiveData<ArrayList<Article>>()
    val networkState = MutableLiveData<NetworkState>()
    private var source: Source? = null

    fun configureSource(source: Source) {
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
