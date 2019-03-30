package dev.wilsonjr.faire.base.delegates

import android.content.Context

interface LoadingDelegate {

    fun showLoading(context: Context)

    fun hideLoading()

}