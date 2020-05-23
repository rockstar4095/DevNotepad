package com.example.devnotepad.data.repositories

/**
 * Интерфейс объявляет общие методы для работы с view, содержимое которых продолжает загружаться
 * после загрузки общего содержимого статьи.
 * */
interface RepositoryContractForDynamicHeightViews {

    /**
     * Метод общего интерфейса. Обновляет информацию о высоте view.
     * */
    suspend fun updateViewHeight(viewHeight: Int, elementId: Int)

    /**
     * Метод общего интерфейса. Предоставляет информацию о высоте view.
     * */
    suspend fun getViewHeight(elementId: Int): Int
    fun resetViewHeight()
}