package ru.devnotepad.articlecontent.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.devnotepad.articlecontent.data.local.ArticleContentDatabase
import ru.devnotepad.articlecontent.data.local.ArticleContentRepository
import ru.devnotepad.articlecontent.entities.ArticleHeader
import ru.devnotepad.articlecontent.entities.ArticlePiece

class ArticleContentViewModel(
    /**articleIdFromServer: Int,*/
    application: Application
) : ViewModel() {

    val articlePiecesLiveData: LiveData<List<ArticlePiece>> get() = testArticlePiecesMutableLiveData
    private val testArticlePiecesMutableLiveData =
            MutableLiveData(FakeArticlesRepository().obtainFakeArticlePieces())

    private val articleContentRepository: ArticleContentRepository

    init {
        val articleHeaderDao = ArticleContentDatabase.getDatabase(application).articleHeaderDao()
        val articleParagraphDao =
            ArticleContentDatabase.getDatabase(application).articleParagraphDao()

        val articleCodeSnippetDao =
            ArticleContentDatabase.getDatabase(application).articleCodeSnippetDao()

        val articleImageDao = ArticleContentDatabase.getDatabase(application).articleImageDao()

        articleContentRepository = ArticleContentRepository(
            articleHeaderDao,
            articleParagraphDao,
            articleCodeSnippetDao,
            articleImageDao
        )

//        articlePiecesLiveData = articleContentRepository.articlePiecesMediator
    }
}

class FakeArticlesRepository {

    fun obtainFakeArticlePieces(): List<ArticlePiece> = fakeArticlePieces

    private val fakeArticlePieces = listOf(
        ArticleHeader(
            1,
            13,
            1,
            "Header 1",
            1
        )
//        ,
//        ArticleParagraph(
//            1,
//            13,
//            2,
//            "Paragraph 1",
//            1
//        )
    )
}