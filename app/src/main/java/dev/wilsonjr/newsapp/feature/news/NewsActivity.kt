package dev.wilsonjr.newsapp.feature.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.wilsonjr.newsapp.R
import dev.wilsonjr.newsapp.api.model.Article
import dev.wilsonjr.newsapp.api.model.Source
import dev.wilsonjr.newsapp.base.BaseListActivity
import dev.wilsonjr.newsapp.components.LoadPageScrollListener
import dev.wilsonjr.newsapp.feature.news.adapter.ArticleListAdapter
import dev.wilsonjr.newsapp.feature.sources.NewsViewModel
import kotlinx.android.synthetic.main.activity_news.*
import org.koin.android.ext.android.inject


const val NEWS_ACTIVITY_SOURCE = "NEWS_ACTIVITY_SOURCE"

class NewsActivity : BaseListActivity(), ArticleListAdapter.ArticleListAdapterItemListener,
    LoadPageScrollListener.LoadPageScrollLoadMoreListener {

    override val emptyStateTitle: Int = R.string.empty_state_title_news
    override val emptyStateSubTitle: Int = R.string.empty_state_subtitle_news
    override val errorStateTitle: Int = R.string.error_state_title_news
    override val errorStateSubTitle: Int = R.string.error_state_subtitle_news
    override val mainList: View
        get() = news_list

    private val newsViewModel: NewsViewModel by inject()

    private var viewAdapter: ArticleListAdapter = ArticleListAdapter(this)
    private var viewManager: RecyclerView.LayoutManager = GridLayoutManager(this, 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_news)

        setupList()

        (intent?.extras?.getSerializable(NEWS_ACTIVITY_SOURCE) as Source).let { source ->
            title = source.name

            loadNews(source)
        }

        super.onCreate(savedInstanceState)

    }

    private fun loadNews(source: Source) {
        newsViewModel.configureSource(source)

        newsViewModel.articles.observe(this, Observer { articles ->
            viewAdapter.apply {
                articles?.let {
                    set(articles)
                    notifyDataSetChanged()
                }
            }
        })
        newsViewModel.networkState.observe(this, networkStateObserver)

        newsViewModel.loadNews()
    }

    private fun setupList() {
        news_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        news_list.addOnScrollListener(LoadPageScrollListener(this))

    }

    override fun onClick(article: Article) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(article.url)
        startActivity(i)
    }

    override fun setupPortrait() {
        setListColumns(1)
    }

    override fun setupLandscape() {
        setListColumns(2)
    }

    private fun setListColumns(columns: Int) {
        val layoutManager = news_list.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanCount = columns
            viewAdapter.notifyDataSetChanged()
        }
    }

    override fun executeRetry() {
        newsViewModel.loadNews()
    }

    override fun onLoadMore(currentPage: Int, totalItemCount: Int, recyclerView: RecyclerView) {
        newsViewModel.loadMoreNews(currentPage)
    }

}
