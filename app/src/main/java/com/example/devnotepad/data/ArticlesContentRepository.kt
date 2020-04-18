package com.example.devnotepad.data

import androidx.lifecycle.LiveData
import com.example.devnotepad.ArticleHeader
import com.example.devnotepad.ArticleParagraph
import com.example.devnotepad.data.local.ArticleContentDao

/**
 * Репозиторий обрабатывает запросы к содержимому статей.
 * */
class ArticlesContentRepository(
    private val articleContentDao: ArticleContentDao,
    private val articleIdFromServer: Int
) {

    val allArticleHeaders: LiveData<List<ArticleHeader>> =
        articleContentDao.getArticleHeaders(articleIdFromServer)

    suspend fun getArticleHeadersSync(): List<ArticleHeader> {
        return articleContentDao.getArticleHeadersSync(articleIdFromServer)
    }

    suspend fun insertArticleHeader(articleHeader: ArticleHeader) {
        println("debug: repository: ${articleHeader.header}")
        articleContentDao.insertArticleHeader(articleHeader)
    }

    suspend fun deleteArticleHeader(articleHeader: ArticleHeader) {
        articleContentDao.deleteArticleHeader(articleHeader)
    }


    val allArticleParagraphs: LiveData<List<ArticleParagraph>> =
        articleContentDao.getArticleParagraphs(articleIdFromServer)

    suspend fun getArticleParagraphsSync(): List<ArticleParagraph> {
        return articleContentDao.getArticleParagraphsSync(articleIdFromServer)
    }

    suspend fun insertArticleParagraph(articleParagraph: ArticleParagraph) {
        articleContentDao.insertArticleParagraph(articleParagraph)
    }

    suspend fun deleteArticleParagraph(articleParagraph: ArticleParagraph) {
        articleContentDao.deleteArticleParagraph(articleParagraph)
    }
}