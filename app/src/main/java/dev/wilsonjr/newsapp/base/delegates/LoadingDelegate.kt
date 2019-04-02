package dev.wilsonjr.newsapp.base.delegates

import android.content.Context

interface LoadingDelegate {

    fun showLoading(context: Context)

    fun hideLoading()

}