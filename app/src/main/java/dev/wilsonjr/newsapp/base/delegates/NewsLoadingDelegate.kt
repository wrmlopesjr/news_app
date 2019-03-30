package dev.wilsonjr.faire.base.delegates

import android.app.Dialog
import android.content.Context
import android.view.Window
import dev.wilsonjr.newsapp.R


class NewsLoadingDelegate : LoadingDelegate {

    var loadingDialog: Dialog? = null

    override fun showLoading(context: Context) {
        if(loadingDialog==null){
            initDialog(context)
        }
        loadingDialog?.show()
    }

    private fun initDialog(context: Context) {
        loadingDialog = Dialog(context)
        loadingDialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            window.setBackgroundDrawableResource(android.R.color.transparent)
            setContentView(R.layout.dialog_loading)
        }
    }

    override fun hideLoading() {
        loadingDialog?.dismiss()
    }


}