package ru.devnotepad.articlecontent.adapters

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class ArticlePiecesViewHolder<T>(
    viewBinding: ViewBinding
) : RecyclerView.ViewHolder(viewBinding.root) {
    abstract fun bindPieceItem(pieceItem: T)
}