package ru.devnotepad.articlecontent.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.devnotepad.articlecontent.data.ArticleContentRepository
import ru.devnotepad.articlecontent.entities.ArticlePiece
import javax.inject.Inject

class ArticleContentViewModel @Inject constructor(
    /**articleIdFromServer: Int,*/
    private val articleContentRepository: ArticleContentRepository
) : ViewModel() {

    val articlePiecesLiveData: LiveData<List<ArticlePiece>> =
        articleContentRepository.articlePieces
}