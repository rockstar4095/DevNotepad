package ru.devnotepad.articlecontent.adapters

import android.widget.TextView
import ru.devnotepad.articlecontent.databinding.ArticleParagraphItemBinding
import ru.devnotepad.articlecontent.entities.ArticleParagraph

class ArticleParagraphViewHolder(
    binding: ArticleParagraphItemBinding
) : ArticlePiecesViewHolder<ArticleParagraph>(binding) {

    private val paragraphTextView: TextView = binding.paragraph

    override fun bindPieceItem(pieceItem: ArticleParagraph) {
        paragraphTextView.text = pieceItem.getEssentialDataOfPiece()
    }
}