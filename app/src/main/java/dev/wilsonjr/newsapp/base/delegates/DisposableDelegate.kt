package dev.wilsonjr.faire.base.delegates

import io.reactivex.disposables.Disposable

interface DisposableDelegate {

    fun addDisposable(disposable: Disposable)

    fun clearDisposables()

    fun getDisposables(): List<Disposable>

}