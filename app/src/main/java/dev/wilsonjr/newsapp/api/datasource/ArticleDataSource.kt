package dev.wilsonjr.newsapp.api.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import dev.wilsonjr.newsapp.base.delegates.CompositeDisposableDelegate
import dev.wilsonjr.newsapp.base.delegates.DisposableDelegate
import dev.wilsonjr.newsapp.api.model.Article
import dev.wilsonjr.newsapp.api.model.Source
import dev.wilsonjr.newsapp.api.repository.NewsRepository
import dev.wilsonjr.newsapp.base.NetworkState
import dev.wilsonjr.newsapp.base.NetworkState.*


class ArticleDataSource(private val newsRepository: NewsRepository, private val source: Source?) :
    PageKeyedDataSource<Long, Article>(),
    DisposableDelegate by CompositeDisposableDelegate() {

    var networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Article>) {
        networkState.postValue(RUNNING)

        source?.let { source ->
            addDisposable(newsRepository.getTopHeadlinesSameThread(source.id, 1, params.requestedLoadSize).subscribe({
                if (it.articles.isNotEmpty()) {
                    callback.onResult(it.articles, null, 2L)
                    networkState.postValue(SUCCESS)
                } else networkState.postValue(EMPTY)
            }, {
                networkState.postValue(ERROR)
            }))
        } ?: networkState.postValue(ERROR)
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Article>) {
        source?.let { source ->
            addDisposable(newsRepository.getTopHeadlinesSameThread(source.id, params.key.toInt(), params.requestedLoadSize).subscribe({
                val nextKey =
                    (if (params.key == Math.ceil(it.totalResults / params.requestedLoadSize.toDouble()).toLong()) null else params.key + 1)
                callback.onResult(it.articles, nextKey)
                networkState.postValue(SUCCESS)
            }, {}))
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Article>) {
        //not used
    }


}