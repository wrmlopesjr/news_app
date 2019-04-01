package dev.wilsonjr.newsapp.feature.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.wilsonjr.newsapp.R
import dev.wilsonjr.newsapp.base.NetworkState

class ArticleListNetworkAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(article: NetworkState?){

    }


    companion object {
        fun create(
            parent: ViewGroup,
            listener: ArticleListAdapter.ArticleListAdapterItemListener
        ): ArticleListNetworkAdapterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_network_article, parent, false)
            return ArticleListNetworkAdapterViewHolder(view)
        }
    }

}