package ru.devnotepad.articlecontent.data.local

import androidx.lifecycle.MediatorLiveData
import ru.devnotepad.articlecontent.entities.ArticlePiece
import java.util.*

class ArticleContentRepository(
    private val articleHeaderDao: ArticleHeaderDao,
    private val articleParagraphDao: ArticleParagraphDao,
    private val articleCodeSnippetDao: ArticleCodeSnippetDao,
    private val articleImageDao: ArticleImageDao
) {

    var articlePiecesMediator = MediatorLiveData<List<ArticlePiece>>()

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
            if (piece.idFromServer == TEMP_ARTICLE_ID) {
                filteredPieces.add(piece)
            }
        }
        return filteredPieces
    }
}