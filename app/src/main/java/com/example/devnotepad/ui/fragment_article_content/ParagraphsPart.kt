package com.example.devnotepad.ui.fragment_article_content

import com.example.devnotepad.ArticleParagraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ParagraphsPart(private val articleContentViewModel: ArticleContentViewModel) {

    /**
     * Осуществляет запрос на сервер для получения направлений.
     * */
    fun makeRequestForParagraphs() {
        articleContentViewModel.api.getArticleParagraphs(articleContentViewModel.article.idFromServer)
            .enqueue(object : Callback<List<ArticleParagraph>> {
                override fun onResponse(
                    call: Call<List<ArticleParagraph>>,
                    response: Response<List<ArticleParagraph>>
                ) {
                    CoroutineScope(Dispatchers.IO).launch {
                        handleServerParagraphs(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<List<ArticleParagraph>>, t: Throwable) {
                    println("response unsuccessful: $t")
                }
            })
    }

    /**
     * Обрабатывает направления, полученные от сервера, с направлениями, хранящимися в локальной БД.
     * */
    private suspend fun handleServerParagraphs(paragraphsFromServer: List<ArticleParagraph>) {

        // Проверка на отсутствие данных в таблице.
        if (isParagraphsTableEmpty()) {
            // Вставка направлений в пустую таблицу.
            insertParagraphs(paragraphsFromServer)
            // Выход из метода.
            return
        }

        // Сравнение данных с сервера с локальными.
        matchParagraphsFromServerAndLocal(paragraphsFromServer)
    }

    /**
     * Проверяет таблицу с направлениями на наличие данных.
     * */
    private suspend fun isParagraphsTableEmpty(): Boolean {
        return articleContentViewModel.articlesContentRepository.getArticleParagraphsSync().isEmpty()
    }

    /**
     * Вставляет список направлений в таблицу.
     * */
    private fun insertParagraphs(paragraphs: List<ArticleParagraph>) {
        for (paragraph in paragraphs) {
            articleContentViewModel.insertParagraph(paragraph)
        }
    }

    /**
     * Сравнивает направления с сервера с локальными и вызывает методы вставки/обновления/удаления.
     * */
    private suspend fun matchParagraphsFromServerAndLocal(paragraphsFromServer: List<ArticleParagraph>) {

        val serverParagraphsHashMap: HashMap<Int, ArticleParagraph> = HashMap()
        val localParagraphsHashMap: HashMap<Int, ArticleParagraph> = HashMap()

        for (paragraph in paragraphsFromServer) {
            serverParagraphsHashMap[paragraph.idFromServer] = paragraph
        }

        for (paragraph in articleContentViewModel.articlesContentRepository.getArticleParagraphsSync()) {
            localParagraphsHashMap[paragraph.idFromServer] = paragraph
        }

        insertNewParagraphs(serverParagraphsHashMap, localParagraphsHashMap)
        replaceRenewedParagraphs(serverParagraphsHashMap, localParagraphsHashMap)
        deleteAbsentParagraphs(serverParagraphsHashMap, localParagraphsHashMap)
    }

    /**
     * Вставляет новые направления в локальную БД.
     * */
    private fun insertNewParagraphs(
        serverParagraphsHashMap: HashMap<Int, ArticleParagraph>,
        localParagraphsHashMap: HashMap<Int, ArticleParagraph>
    ) {
        for ((id, paragraph) in serverParagraphsHashMap) {
            if (!localParagraphsHashMap.containsKey(id)) {
                articleContentViewModel.insertParagraph(paragraph)
            }
        }
    }

    /**
     * Обновляет направления в локальной БД, в случае, если у них изменились данные.
     * */
    private fun replaceRenewedParagraphs(
        serverParagraphsHashMap: HashMap<Int, ArticleParagraph>,
        localParagraphsHashMap: HashMap<Int, ArticleParagraph>
    ) {
        for ((id, paragraph) in serverParagraphsHashMap) {
            if (localParagraphsHashMap.containsKey(id) && paragraph.paragraph != localParagraphsHashMap[id]!!.paragraph) {
                articleContentViewModel.insertParagraph(paragraph)
            }
        }
    }

    /**
     * Удаляет направления из БД, в случае, если их больше нет на сервере.
     * */
    private fun deleteAbsentParagraphs(
        serverParagraphsHashMap: HashMap<Int, ArticleParagraph>,
        localParagraphsHashMap: HashMap<Int, ArticleParagraph>
    ) {
        for ((id, paragraph) in localParagraphsHashMap) {
            if (!serverParagraphsHashMap.containsKey(id)) {
                articleContentViewModel.deleteParagraph(paragraph)
            }
        }
    }
}