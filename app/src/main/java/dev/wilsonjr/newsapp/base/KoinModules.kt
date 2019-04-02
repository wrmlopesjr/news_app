package dev.wilsonjr.newsapp.base

import dev.wilsonjr.newsapp.api.repository.NewsRepository
import dev.wilsonjr.newsapp.base.repository.EndpointService
import dev.wilsonjr.newsapp.feature.sources.NewsViewModel
import dev.wilsonjr.newsapp.feature.sources.SourcesViewModel
import org.koin.core.module.Module
import org.koin.dsl.module


val viewModelModules = module {
    factory { SourcesViewModel(get()) }
    factory { NewsViewModel(get()) }
}

val serviceModules = module {
    single { EndpointService() }
}

val repositoryModules = module {
    single { NewsRepository(get()) }
}

val appComponent: List<Module> = listOf(viewModelModules, serviceModules, repositoryModules)