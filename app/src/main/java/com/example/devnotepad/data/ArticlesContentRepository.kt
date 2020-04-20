package com.example.devnotepad.data

import androidx.lifecycle.LiveData
import com.example.devnotepad.ArticleHeader
import com.example.devnotepad.ArticleParagraph
import com.example.devnotepad.data.local.ArticleContentDao

/**
 * Репозиторий обрабатывает запросы к содержимому статей.
 * */
class ArticlesContentRepository(
    private val articleContentDao: ArticleContentDao
) {

    val allArticleHeaders: LiveData<List<ArticleHeader>> =
        articleContentDao.getArticleHeaders()

    suspend fun getArticleHeadersSync(articleId: Int): List<ArticleHeader> {
        return articleContentDao.getArticleHeadersSync(articleId)
    }

    suspend fun insertArticleHeader(articleHeader: ArticleHeader) {
        articleContentDao.insertArticleHeader(articleHeader)
    }

    suspend fun deleteArticleHeader(articleHeader: ArticleHeader) {
        articleContentDao.deleteArticleHeader(articleHeader)
    }


    val allArticleParagraphs: LiveData<List<ArticleParagraph>> =
        articleContentDao.getArticleParagraphs()

    suspend fun getArticleParagraphsSync(articleId: Int): List<ArticleParagraph> {
        return articleContentDao.getArticleParagraphsSync(articleId)
    }

    suspend fun insertArticleParagraph(articleParagraph: ArticleParagraph) {
        articleContentDao.insertArticleParagraph(articleParagraph)
    }

    suspend fun deleteArticleParagraph(articleParagraph: ArticleParagraph) {
        articleContentDao.deleteArticleParagraph(articleParagraph)
    }
}