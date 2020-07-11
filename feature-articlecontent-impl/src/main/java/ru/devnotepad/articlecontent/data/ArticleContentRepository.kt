package ru.devnotepad.articlecontent.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import retrofit2.Retrofit
import ru.devnotepad.articlecontent.data.local.ArticleCodeSnippetDao
import ru.devnotepad.articlecontent.data.local.ArticleHeaderDao
import ru.devnotepad.articlecontent.data.local.ArticleImageDao
import ru.devnotepad.articlecontent.data.local.ArticleParagraphDao
import ru.devnotepad.articlecontent.entities.ArticlePiece
import javax.inject.Inject

class ArticleContentRepository @Inject constructor(
    private val articleHeaderDao: ArticleHeaderDao,
    private val articleParagraphDao: ArticleParagraphDao,
    private val articleCodeSnippetDao: ArticleCodeSnippetDao,
    private val articleImageDao: ArticleImageDao,
    val retrofit: Retrofit
) {

    fun articlePieces(articleId: Int): LiveData<List<ArticlePiece>> =
        getArticlePiecesFromMediator(articleId)

    private fun getArticlePiecesFromMediator(articleId: Int): LiveData<List<ArticlePiece>> =
        MediatorLiveData<List<ArticlePiece>>().apply {

            addSource(articleHeaderDao.getArticleHeaders(articleId)) { articleHeaders ->
                value = articleHeaders
            }

            addSource(articleParagraphDao.getArticleParagraphs(articleId)) { articleParagraphs ->
                value = articleParagraphs
            }

            addSource(articleCodeSnippetDao.getArticleCodeSnippets(articleId)) { articleCodeSnippets ->
                value = articleCodeSnippets
            }

            addSource(articleImageDao.getArticleImages(articleId)) { articleImages ->
                value = articleImages
            }
        }
}