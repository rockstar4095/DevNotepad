package ru.devnotepad.articlecontent.data.local

import androidx.lifecycle.MediatorLiveData
import ru.devnotepad.articlecontent.entities.ArticlePiece

class ArticleContentRepository(
    private val articleHeaderDao: ArticleHeaderDao,
    private val articleParagraphDao: ArticleParagraphDao,
    private val articleCodeSnippetDao: ArticleCodeSnippetDao,
    private val articleImageDao: ArticleImageDao
) {

    var articleContentMediator = MediatorLiveData<List<ArticlePiece>>()

    init {
        addSourcesToArticleContentMediator()
    }

    private fun addSourcesToArticleContentMediator() {
        articleContentMediator.addSource(articleHeaderDao.getArticleHeaders()) { articleHeaders ->

        }
    }
}