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
import kotlin.collections.ArrayList

class ArticleListAdapter(val listener: ArticleListAdapterItemListener) :
    RecyclerView.Adapter<ArticleListAdapter.ArticleListAdapterViewHolder>() {

    private val dateFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT)
    private val parseFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    private var dataset: ArrayList<Article> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)
        return ArticleListAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleListAdapterViewHolder, position: Int) {
        val article = dataset[position]
        holder.view.setOnClickListener { listener.onClick(article) }

        holder.view.article_name.text = article.title
        holder.view.article_description.text = article.description
        holder.view.article_author.text = article.author
        holder.view.article_date.text = dateFormat.format(parseFormat.parse(article.publishedAt))

        val imageURL = article.urlToImage ?: IMAGE_LOCATOR_URL.format(article.url)

        Picasso.get()
            .load(imageURL)
            .fit()
            .centerCrop(Gravity.CENTER)
            .placeholder(R.drawable.placeholder_image16_9)
            .error(R.drawable.placeholder_image16_9)
            .into(holder.view.article_image)
    }

    override fun onBindViewHolder(holder: ArticleListAdapterViewHolder, position: Int, payloads: MutableList<Any>) {
        onBindViewHolder(holder, position)
    }

    override fun getItemCount(): Int = dataset.size

    fun set(articles: ArrayList<Article>) {
        dataset = articles
    }

    class ArticleListAdapterViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface ArticleListAdapterItemListener {

        fun onClick(article: Article)

    }


}