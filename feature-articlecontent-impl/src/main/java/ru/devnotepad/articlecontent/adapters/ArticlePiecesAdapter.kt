package ru.devnotepad.articlecontent.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.devnotepad.articlecontent.databinding.ArticleCodeSnippetItemBinding
import ru.devnotepad.articlecontent.databinding.ArticleHeaderItemBinding
import ru.devnotepad.articlecontent.databinding.ArticleImageItemBinding
import ru.devnotepad.articlecontent.databinding.ArticleParagraphItemBinding
import ru.devnotepad.articlecontent.entities.*

class ArticlePiecesAdapter(context: Context) : RecyclerView.Adapter<ArticlePiecesViewHolder<*>>() {

    private val layoutInflater = LayoutInflater.from(context)
    private val articleItems = ArrayList<ArticlePiece>()

    fun setArticlePieces(articlePieces: List<ArticlePiece>) {
        articleItems.addAll(articlePieces)
        articleItems.sortBy { it.positionInArticle }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlePiecesViewHolder<*> =
        when (viewType) {
            ArticleHeader.VIEW_TYPE -> getHeaderViewHolder(parent)
            ArticleParagraph.VIEW_TYPE -> getParagraphViewHolder(parent)
            ArticleCodeSnippet.VIEW_TYPE -> getCodeSnippetViewHolder(parent)
            ArticleImage.VIEW_TYPE -> getImageViewHolder(parent)
            else -> throw IllegalArgumentException("Invalid view type.")
        }

    private fun getHeaderViewHolder(parent: ViewGroup): ArticleHeaderViewHolder =
        ArticleHeaderViewHolder(
            ArticleHeaderItemBinding.inflate(layoutInflater, parent,false)
        )

    private fun getParagraphViewHolder(parent: ViewGroup): ArticleParagraphViewHolder =
        ArticleParagraphViewHolder(
            ArticleParagraphItemBinding.inflate(layoutInflater, parent, false)
        )

    private fun getCodeSnippetViewHolder(parent: ViewGroup): ArticleCodeSnippetViewHolder =
        ArticleCodeSnippetViewHolder(
            ArticleCodeSnippetItemBinding.inflate(layoutInflater, parent, false)
        )

    private fun getImageViewHolder(parent: ViewGroup): ArticleImageViewHolder =
        ArticleImageViewHolder(
            ArticleImageItemBinding.inflate(layoutInflater, parent, false)
        )

    override fun getItemCount(): Int = articleItems.size

    override fun getItemViewType(position: Int): Int =
        when (articleItems[position]) {
            is ArticleHeader -> ArticleHeader.VIEW_TYPE
            is ArticleParagraph -> ArticleParagraph.VIEW_TYPE
            is ArticleCodeSnippet -> ArticleCodeSnippet.VIEW_TYPE
            is ArticleImage -> ArticleImage.VIEW_TYPE
            else -> throw IllegalArgumentException("Invalid type of data.")
        }

    override fun onBindViewHolder(holder: ArticlePiecesViewHolder<*>, position: Int) {
        when (holder) {
            is ArticleHeaderViewHolder ->
                holder.bindPieceItem(articleItems[position] as ArticleHeader)

            is ArticleParagraphViewHolder ->
                holder.bindPieceItem(articleItems[position] as ArticleParagraph)

            is ArticleCodeSnippetViewHolder ->
                holder.bindPieceItem(articleItems[position] as ArticleCodeSnippet)

            is ArticleImageViewHolder ->
                holder.bindPieceItem(articleItems[position] as ArticleImage)
        }
    }
}