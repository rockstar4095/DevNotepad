package com.example.devnotepad.data.data_handlers

import com.example.devnotepad.ArticleCodeSnippet
import com.example.devnotepad.DynamicArticlePiece
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.repositories.RepositoryContract
import com.example.devnotepad.data.repositories.RepositoryContractForDynamicHeightViews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Класс реализует некоторые стандартные методы для работы с данными приложения,
 * которые впоследствии используются классами-наследниками.
 *
 * Остальные методы объявлены абстрактными и требуют переопределения.
 * */
abstract class HandlerForAppData {

    /**
     * Проверяет таблицу на наличие данных.
     * */
    abstract suspend fun isLocalDataTableEmpty(): Boolean

    /**
     * Сопоставляет данные с сервера с локальными.
     * */
    abstract suspend fun matchDataFromServerWithLocal(dataFromServer: List<NotepadData>)

    /**
     * Принимает к первичной обработке данные с сервера.
     * */
    protected suspend fun handleServerData(
        dataFromServer: List<NotepadData>,
        repository: RepositoryContract
    ) {
        if (isLocalDataTableEmpty()) {
            insertDataInEmptyTable(dataFromServer, repository)
        } else {
            matchDataFromServerWithLocal(dataFromServer)
        }
    }

    /**
     * Вставляет данные в пустую таблицу.
     * */
    private fun insertDataInEmptyTable(
        dataFromServer: List<NotepadData>,
        repository: RepositoryContract
    ) {
        for (dataElement in dataFromServer) {
            repository.insertElement(dataElement)
        }
    }

    /**
     * Вставляет новые данные в непустую таблицу.
     * */
    protected fun insertNewData(
        serverDataHashMap: java.util.HashMap<Int, NotepadData>,
        localDataHashMap: java.util.HashMap<Int, NotepadData>,
        repository: RepositoryContract
    ) {
        for ((idFromServer, dataElement) in serverDataHashMap) {
            if (!localDataHashMap.containsKey(idFromServer)) {
                repository.insertElement(dataElement)
            }
        }
    }

    /**
     * Заменяет обновленные данные.
     * */
    protected fun replaceRenewedData(
        serverDataHashMap: java.util.HashMap<Int, NotepadData>,
        localDataHashMap: java.util.HashMap<Int, NotepadData>,
        repository: RepositoryContract
    ) {
        for ((idFromServer, dataElement) in serverDataHashMap) {
            if (localDataHashMap.containsKey(idFromServer) &&
                dataElement.timeWhenDataChanged != localDataHashMap[idFromServer]!!.timeWhenDataChanged
            ) {
                updateHeightDataForDynamicViews(repository, dataElement)
                println("debug: REPLACE !!!!!")
                repository.insertElement(dataElement)
            }
        }
    }

    /**TODO: rename*/
    private fun updateHeightDataForDynamicViews(
        repository: RepositoryContract,
        dataElement: NotepadData
    ) {
        if (dataElement is DynamicArticlePiece) {
            CoroutineScope(Dispatchers.IO).launch {
                (repository as RepositoryContractForDynamicHeightViews).resetViewHeight()
            }
        }
    }

    /**
     * Удаляет данные из локальной БД в том случае, если их больше нет в БД сервера.
     * */
    protected fun deleteAbsentDataFormLocalStorage(
        serverDataHashMap: java.util.HashMap<Int, NotepadData>,
        localDataHashMap: java.util.HashMap<Int, NotepadData>,
        repository: RepositoryContract
    ) {
        for ((idFromServer, dataElement) in localDataHashMap) {
            if (!serverDataHashMap.containsKey(idFromServer)) {
                repository.deleteElement(dataElement)
            }
        }
    }
}