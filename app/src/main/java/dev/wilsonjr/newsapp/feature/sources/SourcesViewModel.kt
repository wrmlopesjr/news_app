package dev.wilsonjr.newsapp.feature.sources

import androidx.lifecycle.MutableLiveData
import dev.wilsonjr.newsapp.api.model.Source
import dev.wilsonjr.newsapp.api.model.enums.Category
import dev.wilsonjr.newsapp.api.model.enums.Country
import dev.wilsonjr.newsapp.api.repository.NewsRepository
import dev.wilsonjr.newsapp.base.BaseViewModel
import dev.wilsonjr.newsapp.base.NetworkState

class SourcesViewModel(private val newsRepository: NewsRepository) : BaseViewModel() {

    val sources = MutableLiveData<List<Source>>()
    val networkState = MutableLiveData<NetworkState>()

    private var selectedCountry: Country? = null
    private var selectedCategory: Category? = null

    fun loadSources() {
        networkState.postValue(NetworkState.RUNNING)
        addDisposable(
            newsRepository.getSources(
                selectedCountry?.name?.toLowerCase(),
                selectedCategory?.name?.toLowerCase()
            ).subscribe({
                sources.postValue(it.sources)
                if (it.sources.isEmpty()) {
                    networkState.postValue(NetworkState.EMPTY)
                } else {
                    networkState.postValue(NetworkState.SUCCESS)
                }
            }, {
                networkState.postValue(NetworkState.ERROR)
            })
        )
    }

    fun changeCountry(country: Country?) {
        if (Country.ALL.equals(country)) selectedCountry = null
        else selectedCountry = country
        loadSources()
    }

    fun changeCategory(category: Category) {
        if (Category.ALL.equals(category)) selectedCategory = null
        else selectedCategory = category
        loadSources()
    }
}
