package com.example.devnotepad.ui.fragment_articles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.devnotepad.Article
import com.example.devnotepad.R
import com.example.devnotepad.ui.OnItemClickListener
import kotlinx.android.synthetic.main.article_item.view.*

class ArticlesAdapter internal constructor(
    context: Context,
    private val onArticleClickListener: OnItemClickListener
) : RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var articles = emptyList<Article>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        val itemView = inflater.inflate(R.layout.article_item, parent, false)
        return ArticlesViewHolder(itemView)
    }

    override fun getItemCount() = articles.size

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        val current = articles[position]
        holder.articleItemView.text = current.name
        holder.bind(current, onArticleClickListener)
    }

    internal fun setArticles(articles: List<Article>) {
        this.articles = articles
        notifyDataSetChanged()
    }

    inner class ArticlesViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val articleItemView: TextView = itemView.txtArticle

        fun bind(
            article: Article,
            onItemClickListener: OnItemClickListener
        ) {
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(article)
            }
        }
    }
}
