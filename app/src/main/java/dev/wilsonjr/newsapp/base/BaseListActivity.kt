package dev.wilsonjr.newsapp.base

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dev.wilsonjr.newsapp.base.delegates.LoadingDelegate
import dev.wilsonjr.newsapp.base.delegates.NewsLoadingDelegate
import kotlinx.android.synthetic.main.empty_state.*
import kotlinx.android.synthetic.main.empty_state.view.*
import kotlinx.android.synthetic.main.error_state.*
import kotlinx.android.synthetic.main.error_state.view.*

abstract class BaseListActivity : AppCompatActivity(), LoadingDelegate by NewsLoadingDelegate() {

    abstract val emptyStateTitle: Int
    abstract val emptyStateSubTitle: Int
    abstract val errorStateTitle: Int
    abstract val errorStateSubTitle: Int
    abstract val mainList: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupEmptyState(emptyStateTitle, emptyStateSubTitle)
        setupErrorState(errorStateTitle, errorStateSubTitle)

        configureOrientation(resources.configuration.orientation)
    }

    private fun setupEmptyState(title: Int, subtitle: Int) {
        empty_state.empty_state_title.text = getString(title)
        empty_state.empty_state_subtitle.text = getString(subtitle)
    }

    private fun setupErrorState(title: Int, subtitle: Int) {
        error_state.error_state_title.text = getString(title)
        error_state.error_state_subtitle.text = getString(subtitle)
        error_state.error_state_retry.setOnClickListener{
            executeRetry()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        newConfig?.orientation?.let {
            configureOrientation(it)
        }
    }

    private fun configureOrientation(orientation: Int) {
        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> setupLandscape()
            else -> setupPortrait()
        }
    }

    protected val networkStateObserver = Observer<NetworkState> { networkState ->
        if (NetworkState.RUNNING == networkState) {
            showLoading(this)
        } else {
            hideLoading()
        }
        when (networkState) {
            NetworkState.SUCCESS -> {
                hide(empty_state)
                hide(error_state)
                show(mainList)
            }
            NetworkState.EMPTY -> {
                show(empty_state)
                hide(error_state)
                hide(mainList)
            }
            NetworkState.ERROR -> {
                hide(empty_state)
                show(error_state)
                hide(mainList)
            }
            else -> {
            }
        }
    }

    private fun show(view: View) {
        view.visibility = VISIBLE
    }

    private fun hide(view: View) {
        view.visibility = GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    protected abstract fun setupLandscape()

    protected abstract fun setupPortrait()

    protected abstract fun executeRetry()
}