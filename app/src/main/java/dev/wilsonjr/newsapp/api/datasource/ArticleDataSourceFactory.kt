package dev.wilsonjr.newsapp.api.datasource

import androidx.paging.DataSource
import dev.wilsonjr.newsapp.api.model.Article
import dev.wilsonjr.newsapp.api.repository.NewsRepository
import androidx.lifecycle.MutableLiveData
import dev.wilsonjr.newsapp.api.model.Source


class ArticleDataSourceFactory(private val newsRepository: NewsRepository) : DataSource.Factory<Long, Article>() {

    val mutableLiveData: MutableLiveData<ArticleDataSource> = MutableLiveData()

    var source: Source? = null

    override fun create(): DataSource<Long, Article> {
        val articleDataSource = ArticleDataSource(newsRepository, source)
        mutableLiveData.postValue(articleDataSource)
        return articleDataSource
    }

}