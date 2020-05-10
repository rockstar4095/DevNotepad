//package com.example.devnotepad.ui.fragment_article_content
//
//import com.example.devnotepad.ArticleHeader
//import com.example.devnotepad.data.rest.DevNotepadApi
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import kotlin.properties.Delegates
//
//class HeadersPart(
//    private val articleContentViewModelForHeaders: ArticleContentViewModelForHeaders,
//    private val api: DevNotepadApi
//) {
//
//    private var articleId by Delegates.notNull<Int>()
//
//    /**
//     * Осуществляет запрос на сервер для получения заголовков.
//     * Запрашивает данные для конкретной статьи, используя ее id.
//     * */
//    fun makeRequestForHeaders(articleId: Int) {
//        this.articleId = articleId
//
//        api.getArticleHeaders(articleId)
//            .enqueue(object : Callback<List<ArticleHeader>> {
//                override fun onResponse(call: Call<List<ArticleHeader>>, response: Response<List<ArticleHeader>>) {
//                    CoroutineScope(Dispatchers.IO).launch {
//                        handleServerHeaders(response.body()!!)
//                    }
//                }
//
//                override fun onFailure(call: Call<List<ArticleHeader>>, t: Throwable) {
//                    println("debug: response unsuccessful: $t")
//                }
//            })
//    }
//
//    /**
//     * Обрабатывает заголовки, полученные от сервера.
//     * */
//    private suspend fun handleServerHeaders(headersFromServer: List<ArticleHeader>) {
//
//        // Проверка на отсутствие данных в таблице.
//        if (isHeadersTableEmpty()) {
//            // Вставка заголовков в пустую таблицу.
//            insertHeaders(headersFromServer)
//            // Выход из метода.
//            return
//        }
//
//        // Сравнение данных с сервера с локальными.
//        matchHeadersFromServerAndLocal(headersFromServer)
//    }
//
//    /**
//     * Проверяет таблицу с загловками для статьи с articleId на наличие данных.
//     * */
//    private suspend fun isHeadersTableEmpty(): Boolean {
//        return articleContentViewModelForHeaders.articlesContentRepository.getArticleHeadersSync(articleId).isEmpty()
//    }
//
//    /**
//     * Вставляет список заголовков в таблицу.
//     * */
//    private fun insertHeaders(headers: List<ArticleHeader>) {
//        for (header in headers) {
//            articleContentViewModelForHeaders.insertHeader(header)
//        }
//    }
//
//    /**
//     * Сравнивает заголовки с сервера с локальными и вызывает методы вставки/обновления/удаления.
//     * В локальной БД ищет только заголовки для конкретной статьи по ее id.
//     * */
//    private suspend fun matchHeadersFromServerAndLocal(headersFromServer: List<ArticleHeader>) {
//
//        val serverHeadersHashMap: HashMap<Int, ArticleHeader> = HashMap()
//        val localHeadersHashMap: HashMap<Int, ArticleHeader> = HashMap()
//
//        for (header in headersFromServer) {
//            serverHeadersHashMap[header.idFromServer] = header
//        }
//
//        for (header in articleContentViewModelForHeaders.articlesContentRepository.getArticleHeadersSync(articleId)) {
//            localHeadersHashMap[header.idFromServer] = header
//        }
//
//        insertNewHeaders(serverHeadersHashMap, localHeadersHashMap)
//        replaceRenewedHeaders(serverHeadersHashMap, localHeadersHashMap)
//        deleteAbsentHeaders(serverHeadersHashMap, localHeadersHashMap)
//    }
//
//    /**
//     * Вставляет новые заголовки в локальную БД.
//     * */
//    private fun insertNewHeaders(
//        serverHeadersHashMap: HashMap<Int, ArticleHeader>,
//        localHeadersHashMap: HashMap<Int, ArticleHeader>
//    ) {
//        for ((idFromServer, header) in serverHeadersHashMap) {
//            if (!localHeadersHashMap.containsKey(idFromServer)) {
//                articleContentViewModelForHeaders.insertHeader(header)
//            }
//        }
//    }
//
//    /**
//     * Обновляет заголовки в локальной БД, в случае, если у них изменились данные.
//     * */
//    private fun replaceRenewedHeaders(
//        serverHeadersHashMap: HashMap<Int, ArticleHeader>,
//        localHeadersHashMap: HashMap<Int, ArticleHeader>
//    ) {
//        for ((idFromServer, header) in serverHeadersHashMap) {
//            if (localHeadersHashMap.containsKey(idFromServer) &&
//                header.timeWhenDataChanged != localHeadersHashMap[idFromServer]!!.timeWhenDataChanged) {
//                articleContentViewModelForHeaders.insertHeader(header)
//            }
//        }
//    }
//
//    /**
//     * Удаляет заголовки из БД, в случае, если их больше нет на сервере.
//     * */
//    private fun deleteAbsentHeaders(
//        serverHeadersHashMap: HashMap<Int, ArticleHeader>,
//        localHeadersHashMap: HashMap<Int, ArticleHeader>
//    ) {
//        for ((idFromServer, header) in localHeadersHashMap) {
//            if (!serverHeadersHashMap.containsKey(idFromServer)) {
//                articleContentViewModelForHeaders.deleteHeader(header)
//            }
//        }
//    }
//}