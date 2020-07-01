package ru.devnotepad.articlecontent.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import retrofit2.Retrofit
import ru.devnotepad.articlecontent.data.local.ArticleCodeSnippetDao
import ru.devnotepad.articlecontent.data.local.ArticleHeaderDao
import ru.devnotepad.articlecontent.data.local.ArticleImageDao
import ru.devnotepad.articlecontent.data.local.ArticleParagraphDao
import ru.devnotepad.articlecontent.entities.ArticlePiece
import java.util.*
import javax.inject.Inject

class ArticleContentRepository @Inject constructor(
    private val articleHeaderDao: ArticleHeaderDao,
    private val articleParagraphDao: ArticleParagraphDao,
    private val articleCodeSnippetDao: ArticleCodeSnippetDao,
    private val articleImageDao: ArticleImageDao,
    val retrofit: Retrofit
) {

    private val articlePiecesMediator = MediatorLiveData<List<ArticlePiece>>()
    val articlePieces: LiveData<List<ArticlePiece>> get() = articlePiecesMediator

    init {
        addSourcesToArticlePiecesMediator()
    }

    companion object {
        private const val TEMP_ARTICLE_ID = 13
    }

    private fun addSourcesToArticlePiecesMediator() {
        articlePiecesMediator.addSource(articleHeaderDao.getArticleHeaders()) { allHeaders ->
            articlePiecesMediator.value = getFilteredPieces(allHeaders)
        }

        articlePiecesMediator.addSource(
            articleParagraphDao.getArticleParagraphs()
        ) { allParagraphs ->
            articlePiecesMediator.value = getFilteredPieces(allParagraphs)
        }

        articlePiecesMediator.addSource(
            articleCodeSnippetDao.getArticleCodeSnippets()
        ) { allCodeSnippets ->
            articlePiecesMediator.value = getFilteredPieces(allCodeSnippets)
        }

        articlePiecesMediator.addSource(articleImageDao.getArticleImages()) { allImages ->
            articlePiecesMediator.value = getFilteredPieces(allImages)
        }
    }

    private fun getFilteredPieces(pieces: List<ArticlePiece>): ArrayList<ArticlePiece> {
        val filteredPieces = ArrayList<ArticlePiece>()
        for (piece in pieces) {
            if (piece.articleIdFromServer == TEMP_ARTICLE_ID) {
                filteredPieces.add(piece)
            }
        }
        return filteredPieces
    }
}