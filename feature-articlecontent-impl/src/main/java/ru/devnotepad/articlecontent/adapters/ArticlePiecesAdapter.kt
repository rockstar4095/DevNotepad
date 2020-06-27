package ru.devnotepad.articlecontent.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.devnotepad.articlecontent.databinding.ArticleHeaderItemBinding
import ru.devnotepad.articlecontent.databinding.ArticleParagraphItemBinding
import ru.devnotepad.articlecontent.entities.ArticleHeader
import ru.devnotepad.articlecontent.entities.ArticleParagraph
import ru.devnotepad.articlecontent.entities.ArticlePiece

class ArticlePiecesAdapter(context: Context) : RecyclerView.Adapter<ArticlePiecesViewHolder<*>>() {

    private val layoutInflater = LayoutInflater.from(context)
    private var articlePieces = ArrayList<ArticlePiece>()

    fun setArticlePieces(articlePieces: List<ArticlePiece>) {
        this.articlePieces.apply {
            clear()
            addAll(articlePieces)
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlePiecesViewHolder<*> =
        when (viewType) {
            ArticleHeader.VIEW_TYPE -> inflateHeaderViewHolder(parent)
            ArticleParagraph.VIEW_TYPE -> inflateParagraphViewHolder(parent)
            else -> throw IllegalArgumentException("Invalid view type.")
        }

    private fun inflateHeaderViewHolder(parent: ViewGroup): ArticleHeaderViewHolder =
        ArticleHeaderViewHolder(
            ArticleHeaderItemBinding.inflate(layoutInflater, parent,false)
        )

    private fun inflateParagraphViewHolder(parent: ViewGroup): ArticleParagraphViewHolder =
        ArticleParagraphViewHolder(
            ArticleParagraphItemBinding.inflate(layoutInflater, parent, false)
        )

    override fun getItemCount(): Int = articlePieces.size

    override fun getItemViewType(position: Int): Int =
        when (articlePieces[position]) {
            is ArticleHeader -> ArticleHeader.VIEW_TYPE
            is ArticleParagraph -> ArticleParagraph.VIEW_TYPE
            else -> throw IllegalArgumentException("Invalid type of data.")
        }

    override fun onBindViewHolder(holder: ArticlePiecesViewHolder<*>, position: Int) {
        when (holder) {
            is ArticleHeaderViewHolder ->
                holder.bindPieceItem(articlePieces[position] as ArticleHeader)

            is ArticleParagraphViewHolder ->
                holder.bindPieceItem(articlePieces[position] as ArticleParagraph)
        }
    }
}