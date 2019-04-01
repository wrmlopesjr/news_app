package dev.wilsonjr.newsapp.base.repository

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.reflect.ParameterizedType

open class Repository<E>(private val endpointService: EndpointService) {

    private fun <E> getEndpoint(endpoint: Class<E>): E {
        return endpointService[endpoint]
    }

    protected fun getEndpoint(): E {
        return getEndpoint(endpointClass())
    }

    protected fun <T> schedule(single: Single<T>): Single<T> {
        return single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    protected fun <T> scheduleSameThread(single: Single<T>): Single<T> {
        return single.subscribeOn(Schedulers.trampoline()).observeOn(Schedulers.trampoline())
    }

    @Suppress("UNCHECKED_CAST")
    private fun endpointClass(): Class<E> {
        val clazz: Class<out Repository<*>> = javaClass
        val types = (clazz.genericSuperclass as ParameterizedType).actualTypeArguments
        return types[0] as Class<E>
    }
}