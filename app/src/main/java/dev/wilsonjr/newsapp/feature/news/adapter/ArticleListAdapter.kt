package dev.wilsonjr.newsapp.feature.news.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.wilsonjr.newsapp.R
import dev.wilsonjr.newsapp.api.model.Article
import dev.wilsonjr.newsapp.base.NetworkState

class ArticleListAdapter(val listener: ArticleListAdapterItemListener) :
    PagedListAdapter<Article, RecyclerView.ViewHolder>(Article.DIFF_CALLBACK) {

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        return when (viewType) {
            R.layout.item_article -> ArticleListAdapterViewHolder.create(parent, listener)
            R.layout.item_network_article -> ArticleListNetworkAdapterViewHolder.create(parent, listener)
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_article -> (holder as ArticleListAdapterViewHolder).bind(getItem(position))
            R.layout.item_network_article -> (holder as ArticleListNetworkAdapterViewHolder).bind(networkState)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        onBindViewHolder(holder, position)
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.SUCCESS

    override fun getItemCount(): Int = super.getItemCount() + if (hasExtraRow()) 1 else 0

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_network_article
        } else {
            R.layout.item_article
        }
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    interface ArticleListAdapterItemListener {

        fun onClick(article: Article)

    }


}