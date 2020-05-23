package com.example.devnotepad.ui.fragment_directions

import android.app.Application
import androidx.lifecycle.*
import com.example.devnotepad.DirectionOfStudy
import com.example.devnotepad.data.repositories.DirectionsRepository
import com.example.devnotepad.data.local.KnowledgeRoomDatabase
import javax.inject.Inject

class DirectionsViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    var allDirections: MutableLiveData<List<DirectionOfStudy>> = MutableLiveData()
    val repositoryForStructureData: DirectionsRepository

    init {
        val directionDao = KnowledgeRoomDatabase.getDatabase(application).directionDao()
        repositoryForStructureData = DirectionsRepository(directionDao)
        observeRepositoryDirections()
    }

    private fun observeRepositoryDirections() {
        println("debug: observeRepositoryDirections()")
        repositoryForStructureData.allDirections.observeForever(Observer {
            allDirections.postValue(it)
            println("debug: observeRepositoryDirections: $it")
        })
    }
}