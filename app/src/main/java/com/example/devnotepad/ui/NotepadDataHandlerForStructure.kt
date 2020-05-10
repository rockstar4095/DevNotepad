package com.example.devnotepad.ui

import com.example.devnotepad.*
import com.example.devnotepad.data.rest.DevNotepadApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotepadDataHandlerForStructure(
    private val viewModel: NotepadViewModelContractForStructure,
    private val restApi: DevNotepadApi
) : NotepadDataHandler() {

    private var elementType: String? = null

    fun makeRequestForStructureData(elementType: String) {
        this.elementType = elementType

        when (elementType) {
            "article" -> {
                restApi.getArticles()
                    .enqueue(object : Callback<List<Article>> {
                        override fun onResponse(
                            call: Call<List<Article>>, response: Response<List<Article>>
                        ) {
                            CoroutineScope(Dispatchers.IO).launch {
                                handleServerData(response.body()!!, viewModel)
                            }
                        }

                        override fun onFailure(call: Call<List<Article>>, t: Throwable) {
                            println("debug: response unsuccessful: $t")
                        }
                    })
            }
            "topic" -> {
                restApi.getTopics()
                    .enqueue(object : Callback<List<Topic>> {
                        override fun onResponse(
                            call: Call<List<Topic>>, response: Response<List<Topic>>
                        ) {
                            CoroutineScope(Dispatchers.IO).launch {
                                handleServerData(response.body()!!, viewModel)
                            }
                        }

                        override fun onFailure(call: Call<List<Topic>>, t: Throwable) {
                            println("debug: response unsuccessful: $t")
                        }
                    })
            }
            "direction" -> {
                restApi.getDirections()
                    .enqueue(object : Callback<List<DirectionOfStudy>> {
                        override fun onResponse(
                            call: Call<List<DirectionOfStudy>>, response: Response<List<DirectionOfStudy>>
                        ) {
                            CoroutineScope(Dispatchers.IO).launch {
                                handleServerData(response.body()!!, viewModel)
                            }
                        }

                        override fun onFailure(call: Call<List<DirectionOfStudy>>, t: Throwable) {
                            println("debug: response unsuccessful: $t")
                        }
                    })
            }
        }
    }

    override suspend fun isDataTableEmpty(): Boolean {
        return viewModel.notepadRepository.getAllElementsSync().isEmpty()
    }

    override suspend fun matchDataFromServerAndLocal(dataFromServer: List<NotepadData>) {
        val serverDataHashMap: HashMap<Int, NotepadData> = HashMap()
        val localDataHashMap: HashMap<Int, NotepadData> = HashMap()

        for (dataElement in dataFromServer) {
            serverDataHashMap[dataElement.idFromServer] = dataElement
        }

        for (dataElement in viewModel.notepadRepository.getAllElementsSync()) {
            localDataHashMap[dataElement.idFromServer] = dataElement
        }

        insertNewData(serverDataHashMap, localDataHashMap, viewModel)
        replaceRenewedData(serverDataHashMap, localDataHashMap, viewModel)
        deleteAbsentData(serverDataHashMap, localDataHashMap, viewModel)
    }
}