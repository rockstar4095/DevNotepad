package ru.devnotepad.articlecontent.adapters

import androidx.recyclerview.widget.RecyclerView
import ru.devnotepad.articlecontent.databinding.ArticleHeaderItemBinding

abstract class ArticlePiecesViewHolder<T>(
    articleHeaderItemBinding: ArticleHeaderItemBinding
) : RecyclerView.ViewHolder(articleHeaderItemBinding.root) {
    abstract fun bindItem(item: T)
}