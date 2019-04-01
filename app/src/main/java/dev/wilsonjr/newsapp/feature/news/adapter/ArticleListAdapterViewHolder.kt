package dev.wilsonjr.newsapp.feature.news.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.wilsonjr.newsapp.IMAGE_LOCATOR_URL
import dev.wilsonjr.newsapp.R
import dev.wilsonjr.newsapp.api.model.Article
import kotlinx.android.synthetic.main.item_article.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ArticleListAdapterViewHolder(
    private val view: View,
    private val listener: ArticleListAdapter.ArticleListAdapterItemListener
) : RecyclerView.ViewHolder(view) {



    private val dateFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT)
    private val parseFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    init {
        parseFormat.timeZone = TimeZone.getTimeZone("UTC")
    }

    fun bind(article: Article?) {

        article?.let {

            view.setOnClickListener { listener.onClick(article) }

            view.article_name.text = article.title
            view.article_description.text = article.description
            view.article_author.text = article.author
            view.article_date.text = dateFormat.format(parseFormat.parse(article.publishedAt))

            val imageURL = article.urlToImage ?: IMAGE_LOCATOR_URL.format(article.url)

            Picasso.get()
                .load(imageURL)
                .fit()
                .centerCrop(Gravity.CENTER)
                .placeholder(R.drawable.placeholder_image16_9)
                .error(R.drawable.placeholder_image16_9)
                .into(view.article_image)
        }
    }


    companion object {
        fun create(
            parent: ViewGroup,
            listener: ArticleListAdapter.ArticleListAdapterItemListener
        ): ArticleListAdapterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article, parent, false)
            return ArticleListAdapterViewHolder(view, listener)
        }
    }

}