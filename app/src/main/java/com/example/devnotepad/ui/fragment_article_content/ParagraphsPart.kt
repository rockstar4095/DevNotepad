package com.example.devnotepad.ui.fragment_article_content

import com.example.devnotepad.ArticleParagraph
import com.example.devnotepad.data.rest.DevNotepadApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class ParagraphsPart(
    private val articleContentViewModel: ArticleContentViewModel,
    private val api: DevNotepadApi
) {

    private var articleId by Delegates.notNull<Int>()

    /**
     * Осуществляет запрос на сервер для получения направлений.
     * */
    fun makeRequestForParagraphs(articleId: Int) {
        this.articleId = articleId

        api.getArticleParagraphs(articleId)
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
     * Обрабатывает параграфы, полученные от сервера.
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
     * Проверяет таблицу с параграфами для статьи с articleId на наличие данных.
     * */
    private suspend fun isParagraphsTableEmpty(): Boolean {
        return articleContentViewModel.articlesContentRepository.getArticleParagraphsSync(articleId).isEmpty()
    }

    /**
     * Вставляет список параграфов в таблицу.
     * */
    private fun insertParagraphs(paragraphs: List<ArticleParagraph>) {
        for (paragraph in paragraphs) {
            articleContentViewModel.insertParagraph(paragraph)
        }
    }

    /**
     * Сравнивает параграфы с сервера с локальными и вызывает методы вставки/обновления/удаления.
     * В локальной БД ищет только заголовки для конкретной статьи по ее id.
     * */
    private suspend fun matchParagraphsFromServerAndLocal(paragraphsFromServer: List<ArticleParagraph>) {

        val serverParagraphsHashMap: HashMap<Int, ArticleParagraph> = HashMap()
        val localParagraphsHashMap: HashMap<Int, ArticleParagraph> = HashMap()

        for (paragraph in paragraphsFromServer) {
            serverParagraphsHashMap[paragraph.idFromServer] = paragraph
        }

        for (paragraph in articleContentViewModel.articlesContentRepository.getArticleParagraphsSync(articleId)) {
            localParagraphsHashMap[paragraph.idFromServer] = paragraph
        }

        insertNewParagraphs(serverParagraphsHashMap, localParagraphsHashMap)
        replaceRenewedParagraphs(serverParagraphsHashMap, localParagraphsHashMap)
        deleteAbsentParagraphs(serverParagraphsHashMap, localParagraphsHashMap)
    }

    /**
     * Вставляет новые параграфы в локальную БД.
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
     * Обновляет параграфы в локальной БД, в случае, если у них изменились данные.
     * */
    private fun replaceRenewedParagraphs(
        serverParagraphsHashMap: HashMap<Int, ArticleParagraph>,
        localParagraphsHashMap: HashMap<Int, ArticleParagraph>
    ) {
        for ((id, paragraph) in serverParagraphsHashMap) {
            if (localParagraphsHashMap.containsKey(id) && paragraph != localParagraphsHashMap[id]!!) {
                articleContentViewModel.insertParagraph(paragraph)
            }
        }
    }

    /**
     * Удаляет параграфы из БД, в случае, если их больше нет на сервере.
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