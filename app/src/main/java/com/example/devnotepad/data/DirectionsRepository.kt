package com.example.devnotepad.data

import androidx.lifecycle.LiveData
import com.example.devnotepad.DirectionOfStudy
import com.example.devnotepad.data.local.DirectionDao

class DirectionsRepository(private val directionDao: DirectionDao) {

    /**
     * LiveData список направлений для наблюдения из модели фрагмента.
     * */
    val allDirections: LiveData<List<DirectionOfStudy>> = directionDao.getAllDirections()

    /**
     * Синхронное получение списка направлений.
     * */
    suspend fun getAllDirectionsSync(): List<DirectionOfStudy> {
        return directionDao.getAllDirectionsSync()
    }

    /**
     * Вставляет направление в БД с заменой содержимого, если оно уже существует.
     * */
    suspend fun insertDirection(direction: DirectionOfStudy) {
        directionDao.insertDirection(direction)
    }

    /**
     * Удаляет направление из БД.
     * */
    suspend fun deleteDirection(direction: DirectionOfStudy) {
        directionDao.deleteDirection(direction)
    }
}