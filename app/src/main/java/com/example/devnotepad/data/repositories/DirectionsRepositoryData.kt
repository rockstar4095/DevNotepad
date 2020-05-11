package com.example.devnotepad.data.repositories

import androidx.lifecycle.LiveData
import com.example.devnotepad.DirectionOfStudy
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.local.DirectionDao

class DirectionsRepositoryData(private val directionDao: DirectionDao) :
    RepositoryContractForStructureData {

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
     * Вставляет направление в БД с заменой содержимого, если оно уже существует.
     * */
    override suspend fun insertElement(notepadData: NotepadData) {
        directionDao.insertDirection(notepadData as DirectionOfStudy)
    }

    /**
     * Удаляет направление из БД.
     * */
    override suspend fun deleteElement(notepadData: NotepadData) {
        directionDao.deleteDirection(notepadData as DirectionOfStudy)
    }
}