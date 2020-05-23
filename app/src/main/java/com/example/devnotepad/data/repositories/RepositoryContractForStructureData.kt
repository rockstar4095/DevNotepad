package com.example.devnotepad.data.repositories

import com.example.devnotepad.NotepadData

/**
 * Общий интерфейс репозиториев, которые работают со структурными данными.
 * */
interface RepositoryContractForStructureData : RepositoryContract {

    /**
     * Метод общего интерфейса. Отвечает за синхронное получение данных из таблицы. Используется
     * для получения информации о размере таблицы.
     * */
    suspend fun getAllElementsSync(): List<NotepadData>

    /**
     * Метод общего интерфейса. Отвечает за запрос на сервер, для получения структурных данных:
     * направления, темы, статьи.
     * */
    fun makeRequestForElements()
}