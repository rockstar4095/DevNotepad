package ru.devnotepad.articlecontent.adapters

import android.widget.TextView
import ru.devnotepad.articlecontent.databinding.ArticleHeaderItemBinding
import ru.devnotepad.articlecontent.entities.ArticleHeader

class ArticleHeaderViewHolder(
    binding: ArticleHeaderItemBinding
) : ArticlePiecesViewHolder<ArticleHeader>(binding) {

    private val headerTextView: TextView = binding.header

    override fun bindItem(item: ArticleHeader) {
        headerTextView.text = item.getEssentialDataOfPiece()
    }
}