package dev.wilsonjr.newsapp.feature.news

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.wilsonjr.faire.base.delegates.LoadingDelegate
import dev.wilsonjr.faire.base.delegates.NewsLoadingDelegate
import dev.wilsonjr.newsapp.R
import dev.wilsonjr.newsapp.api.model.Article
import dev.wilsonjr.newsapp.api.model.Source
import dev.wilsonjr.newsapp.base.NetworkState
import dev.wilsonjr.newsapp.feature.news.adapter.ArticleListAdapter
import dev.wilsonjr.newsapp.feature.sources.NewsViewModel
import kotlinx.android.synthetic.main.activity_news.*
import org.koin.android.ext.android.inject


const val NEWS_ACTIVITY_SOURCE = "NEWS_ACTIVITY_SOURCE"

class NewsActivity : AppCompatActivity(), ArticleListAdapter.ArticleListAdapterItemListener,
    LoadingDelegate by NewsLoadingDelegate() {

    private val newsViewModel: NewsViewModel by inject()

    private var viewAdapter: ArticleListAdapter = ArticleListAdapter(this)
    private var viewManager: RecyclerView.LayoutManager = GridLayoutManager(this, 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        (intent?.extras?.getSerializable(NEWS_ACTIVITY_SOURCE) as Source).let { source ->
            title = source.name

            newsViewModel.configureSource(source)

            newsViewModel.articles.observe(this, Observer { articles ->
                viewAdapter.apply {
                    submitList(articles)
                }
            })
            newsViewModel.networkState.observe(this, Observer { networkState ->
                if (NetworkState.RUNNING == networkState) {
                    showLoading(this)
                } else {
                    hideLoading()
                }
                viewAdapter.setNetworkState(networkState)
            })
        }

        setupList()
        configureOrientation(resources.configuration.orientation)

    }

    private fun setupList() {
        news_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun onClick(article: Article) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(article.url)
        startActivity(i)
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

    private fun setupPortrait() {
        setListColumns(1)
    }

    private fun setupLandscape() {
        setListColumns(2)
    }

    private fun setListColumns(columns: Int) {
        val layoutManager = news_list.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanCount = columns
            viewAdapter.notifyDataSetChanged()
        }
    }

}
