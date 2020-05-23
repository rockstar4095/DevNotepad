package com.example.devnotepad.data.repositories

import com.example.devnotepad.NotepadData

/**
 * Общий интерфейс репозиториев.
 * */
interface RepositoryContract {

    /**
     * Метод общего интерфейса. Отвечает за вставку элемента в БД.
     * Вставка производится с заменой.
     * */
    fun insertElement(notepadData: NotepadData)

    /**
     * Метод общего интерфейса. Отвечает за удаление элемента из БД.
     * */
    fun deleteElement(notepadData: NotepadData)
}