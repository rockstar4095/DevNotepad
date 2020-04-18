package com.example.devnotepad.ui.fragment_article_content

import com.example.devnotepad.ArticleHeader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HeadersPart(private val articleContentViewModel: ArticleContentViewModel) {

    /**
     * Осуществляет запрос на сервер для получения направлений.
     * */
    fun makeRequestForHeaders() {
        articleContentViewModel.api.getArticleHeaders(articleContentViewModel.article.idFromServer)
            .enqueue(object : Callback<List<ArticleHeader>> {
                override fun onResponse(
                    call: Call<List<ArticleHeader>>,
                    response: Response<List<ArticleHeader>>
                ) {
                    CoroutineScope(Dispatchers.IO).launch {
                        handleServerHeaders(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<List<ArticleHeader>>, t: Throwable) {
                    println("response unsuccessful: $t")
                }
            })
    }

    /**
     * Обрабатывает направления, полученные от сервера, с направлениями, хранящимися в локальной БД.
     * */
    private suspend fun handleServerHeaders(headersFromServer: List<ArticleHeader>) {

        // Проверка на отсутствие данных в таблице.
        if (isHeadersTableEmpty()) {
            // Вставка направлений в пустую таблицу.
            insertHeaders(headersFromServer)
            // Выход из метода.
            return
        }

        // Сравнение данных с сервера с локальными.
        matchHeadersFromServerAndLocal(headersFromServer)
    }

    /**
     * Проверяет таблицу с направлениями на наличие данных.
     * */
    private suspend fun isHeadersTableEmpty(): Boolean {
        return articleContentViewModel.articlesContentRepository.getArticleHeadersSync().isEmpty()
    }

    /**
     * Вставляет список направлений в таблицу.
     * */
    private fun insertHeaders(headers: List<ArticleHeader>) {
        for (header in headers) {
            articleContentViewModel.insertHeader(header)
        }
    }

    /**
     * Сравнивает направления с сервера с локальными и вызывает методы вставки/обновления/удаления.
     * */
    private suspend fun matchHeadersFromServerAndLocal(headersFromServer: List<ArticleHeader>) {

        val serverHeadersHashMap: HashMap<Int, ArticleHeader> = HashMap()
        val localHeadersHashMap: HashMap<Int, ArticleHeader> = HashMap()

        for (header in headersFromServer) {
            serverHeadersHashMap[header.idFromServer] = header
        }

        for (header in articleContentViewModel.articlesContentRepository.getArticleHeadersSync()) {
            localHeadersHashMap[header.idFromServer] = header
        }

        insertNewHeaders(serverHeadersHashMap, localHeadersHashMap)
        replaceRenewedHeaders(serverHeadersHashMap, localHeadersHashMap)
        deleteAbsentHeaders(serverHeadersHashMap, localHeadersHashMap)
    }

    /**
     * Вставляет новые направления в локальную БД.
     * */
    private fun insertNewHeaders(
        serverHeadersHashMap: HashMap<Int, ArticleHeader>,
        localHeadersHashMap: HashMap<Int, ArticleHeader>
    ) {
        for ((id, header) in serverHeadersHashMap) {
            if (!localHeadersHashMap.containsKey(id)) {
                articleContentViewModel.insertHeader(header)
            }
        }
    }

    /**
     * Обновляет направления в локальной БД, в случае, если у них изменились данные.
     * */
    private fun replaceRenewedHeaders(
        serverHeadersHashMap: HashMap<Int, ArticleHeader>,
        localHeadersHashMap: HashMap<Int, ArticleHeader>
    ) {
        for ((id, header) in serverHeadersHashMap) {
            if (localHeadersHashMap.containsKey(id) && header.header != localHeadersHashMap[id]!!.header) {
                articleContentViewModel.insertHeader(header)
            }
        }
    }

    /**
     * Удаляет направления из БД, в случае, если их больше нет на сервере.
     * */
    private fun deleteAbsentHeaders(
        serverHeadersHashMap: HashMap<Int, ArticleHeader>,
        localHeadersHashMap: HashMap<Int, ArticleHeader>
    ) {
        for ((id, header) in localHeadersHashMap) {
            if (!serverHeadersHashMap.containsKey(id)) {
                articleContentViewModel.deleteHeader(header)
            }
        }
    }
}