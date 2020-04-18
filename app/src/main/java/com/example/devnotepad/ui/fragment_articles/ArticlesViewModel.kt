package com.example.devnotepad.ui.fragment_articles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.devnotepad.Article
import com.example.devnotepad.data.ArticlesRepository
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import com.example.devnotepad.data.rest.DevNotepadApi
import com.example.devnotepad.data.rest.RetrofitCreator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticlesViewModel(application: Application) : AndroidViewModel(application) {
    private val articlesRepository: ArticlesRepository
    val allArticles: LiveData<List<Article>>
    private val api: DevNotepadApi

    init {

        val retrofitInstance = RetrofitCreator.getRetrofit()
        api = retrofitInstance.create(DevNotepadApi::class.java)

        val articleDao = KnowledgeRoomDatabase.getDatabase(application).articleDao()
        articlesRepository = ArticlesRepository(articleDao)
        allArticles = articlesRepository.allArticles
    }

    private fun insertArticle(article: Article) =
        viewModelScope.launch(Dispatchers.IO) {
            articlesRepository.insertArticle(article)
        }

    private fun deleteArticle(article: Article) =
        viewModelScope.launch(Dispatchers.IO) {
            articlesRepository.deleteArticle(article)
        }

    /**
     * Осуществляет запрос на сервер для получения направлений.
     * */
    fun makeRequestForArticles() {
        api.getArticles().enqueue(object: Callback<List<Article>> {
            override fun onResponse(call: Call<List<Article>>, response: Response<List<Article>>) {
                CoroutineScope(Dispatchers.IO).launch {
                    handleServerArticles(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Article>>, t: Throwable) {
                println("response unsuccessful: $t")
            }
        })
    }

    /**
     * Обрабатывает направления, полученные от сервера, с направлениями, хранящимися в локальной БД.
     * */
    private suspend fun handleServerArticles(articlesFromServer: List<Article>) {

        // Проверка на отсутствие данных в таблице.
        if (isTableEmpty()) {
            // Вставка направлений в пустую таблицу.
            insertArticles(articlesFromServer)
            // Выход из метода.
            return
        }

        // Сравнение данных с сервера с локальными.
        matchArticlesFromServerAndLocal(articlesFromServer)
    }

    /**
     * Проверяет таблицу с направлениями на наличие данных.
     * */
    private suspend fun isTableEmpty(): Boolean {
        return articlesRepository.getAllArticlesSync().isEmpty()
    }

    /**
     * Вставляет список направлений в таблицу.
     * */
    private fun insertArticles(articles: List<Article>) {
        for (article in articles) {
            insertArticle(article)
        }
    }

    /**
     * Сравнивает направления с сервера с локальными и вызывает методы вставки/обновления/удаления.
     * */
    private suspend fun matchArticlesFromServerAndLocal(articlesFromServer: List<Article>) {

        val serverArticlesHashMap: HashMap<Int, Article> = HashMap()
        val localArticlesHashMap: HashMap<Int, Article> = HashMap()

        for (article in articlesFromServer) {
            serverArticlesHashMap[article.idFromServer] = article
        }

        for (article in articlesRepository.getAllArticlesSync()) {
            localArticlesHashMap[article.idFromServer] = article
        }

        insertNewArticles(serverArticlesHashMap, localArticlesHashMap)
        replaceRenewedArticles(serverArticlesHashMap, localArticlesHashMap)
        deleteAbsentArticles(serverArticlesHashMap, localArticlesHashMap)
    }

    /**
     * Вставляет новые направления в локальную БД.
     * */
    private fun insertNewArticles(
        serverArticlesHashMap: HashMap<Int, Article>,
        localArticlesHashMap: HashMap<Int, Article>
    ) {
        for ((id, article) in serverArticlesHashMap) {
            if (!localArticlesHashMap.containsKey(id)) {
                insertArticle(article)
            }
        }
    }

    /**
     * Обновляет направления в локальной БД, в случае, если у них изменились данные.
     * */
    private fun replaceRenewedArticles(
        serverArticlesHashMap: HashMap<Int, Article>,
        localArticlesHashMap: HashMap<Int, Article>
    ) {
        for ((id, article) in serverArticlesHashMap) {
            if (localArticlesHashMap.containsKey(id) && article.name != localArticlesHashMap[id]!!.name) {
                insertArticle(article)
            }
        }
    }

    /**
     * Удаляет направления из БД, в случае, если их больше нет на сервере.
     * */
    private fun deleteAbsentArticles(
        serverArticlesHashMap: HashMap<Int, Article>,
        localArticlesHashMap: HashMap<Int, Article>
    ) {
        for ((id, article) in localArticlesHashMap) {
            if (!serverArticlesHashMap.containsKey(id)) {
                deleteArticle(article)
            }
        }
    }
}