package com.example.devnotepad.data.data_handlers

import com.example.devnotepad.*
import com.example.devnotepad.data.rest.DevNotepadApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotepadDataHandlerForStructure(
    private val notepadViewModelForStructure: NotepadViewModelContractForStructure,
    private val devNotepadApi: DevNotepadApi
) : NotepadDataHandler() {

    private var elementType: String? = null

    fun makeRequestForStructureData(elementType: String) {
        this.elementType = elementType

        devNotepadApi.getStructureData(elementType).enqueue(object : Callback<List<NotepadData>> {
            override fun onResponse(call: Call<List<NotepadData>>, response: Response<List<NotepadData>>) {
                CoroutineScope(Dispatchers.IO).launch {
                    handleServerData(response.body()!!, notepadViewModelForStructure)
                }
            }

            override fun onFailure(call: Call<List<NotepadData>>, t: Throwable) {
                println("debug: response unsuccessful: $t")
            }
        })
    }

    override suspend fun isDataTableEmpty(): Boolean {
        return notepadViewModelForStructure.repositoryForStructureData.getAllElementsSync()
            .isEmpty()
    }

    override suspend fun matchDataFromServerAndLocal(dataFromServer: List<NotepadData>) {
        val serverDataHashMap: HashMap<Int, NotepadData> = HashMap()
        val localDataHashMap: HashMap<Int, NotepadData> = HashMap()

        for (dataElement in dataFromServer) {
            serverDataHashMap[dataElement.idFromServer] = dataElement
        }

        for (dataElement in notepadViewModelForStructure.repositoryForStructureData.getAllElementsSync()) {
            localDataHashMap[dataElement.idFromServer] = dataElement
        }

        insertNewData(serverDataHashMap, localDataHashMap, notepadViewModelForStructure)
        replaceRenewedData(serverDataHashMap, localDataHashMap, notepadViewModelForStructure)
        deleteAbsentData(serverDataHashMap, localDataHashMap, notepadViewModelForStructure)
    }
}