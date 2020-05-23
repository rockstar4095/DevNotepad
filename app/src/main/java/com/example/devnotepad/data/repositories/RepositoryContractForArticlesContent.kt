package com.example.devnotepad.data.repositories

import com.example.devnotepad.NotepadData

/**
 * Общий интерфейс репозиториев, которые работают с содержимым статей.
 * */
interface RepositoryContractForArticlesContent : RepositoryContract {

    /**
     * Метод общего интерфейса. Отвечает за синхронное получение данных из таблицы. Используется
     * для получения информации о размере таблицы.
     * */
    suspend fun getAllElementsSync(parentIdFromServer: Int): List<NotepadData>

    /**
     * Метод общего интерфейса. Отвечает за запрос на сервер, для получения данных с конечной
     * информацией - элементов статей.
     * */
    fun makeRequestForElements(parentElementId: Int)
}