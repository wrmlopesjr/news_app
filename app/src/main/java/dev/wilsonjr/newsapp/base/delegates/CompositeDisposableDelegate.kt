package dev.wilsonjr.faire.base.delegates

import io.reactivex.disposables.Disposable

class CompositeDisposableDelegate : DisposableDelegate {

    private val disposables = mutableListOf<Disposable>()

    override fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun clearDisposables() {
        disposables.forEach{
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        disposables.clear()
    }

    override fun getDisposables(): List<Disposable> {
        return disposables
    }
}