package com.example.devnotepad.data

import androidx.lifecycle.LiveData
import com.example.devnotepad.DirectionOfStudy
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.local.DirectionDao

class DirectionsRepository(private val directionDao: DirectionDao): NotepadRepositoryContractForStructure {

    /**
     * LiveData список направлений для наблюдения из модели фрагмента.
     * */
    val allDirections: LiveData<List<DirectionOfStudy>> = directionDao.getAllDirections()

    /**
     * Синхронное получение списка направлений.
     * */
    override suspend fun getAllElementsSync(): List<NotepadData> {
        return directionDao.getAllDirectionsSync()
    }

    /**
     * Вставляет тему в БД с заменой содержимого, если она уже существует.
     * */
    override suspend fun insertElement(notepadData: NotepadData) {
        directionDao.insertDirection(notepadData as DirectionOfStudy)
    }

    /**
     * Удаляет тему из БД.
     * */
    override suspend fun deleteElement(notepadData: NotepadData) {
        directionDao.deleteDirection(notepadData as DirectionOfStudy)
    }
}