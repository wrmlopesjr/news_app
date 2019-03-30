package dev.wilsonjr.faire.base

import dev.wilsonjr.newsapp.base.repository.EndpointService
import org.koin.core.module.Module
import org.koin.dsl.module


val viewModelModules = module {
    //    factory { CategoryViewModel(get()) }
//    factory { MakersViewModel(get()) }
//    factory { ProductViewModel(get()) }
}

val serviceModules = module {
    single { EndpointService() }
}

val repositoryModules = module {
    //    single { ProductsRepository(get()) }
}


val appComponent: List<Module> = listOf(viewModelModules, serviceModules, repositoryModules)