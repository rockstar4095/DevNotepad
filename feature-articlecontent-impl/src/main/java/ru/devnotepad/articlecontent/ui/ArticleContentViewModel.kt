package ru.devnotepad.articlecontent.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.devnotepad.articlecontent.data.ArticleContentRepository
import ru.devnotepad.articlecontent.entities.ArticlePiece
import javax.inject.Inject

class ArticleContentViewModel @Inject constructor(
    private val articleContentRepository: ArticleContentRepository
) : ViewModel() {

    fun articlePiecesLiveData(articleId: Int): LiveData<List<ArticlePiece>> =
        articleContentRepository.articlePieces(articleId)
}