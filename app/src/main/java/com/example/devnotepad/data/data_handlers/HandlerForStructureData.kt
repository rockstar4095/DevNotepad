package com.example.devnotepad.data.data_handlers

import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.repositories.RepositoryContractForStructureData
import com.example.devnotepad.data.rest.DevNotepadApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HandlerForStructureData(
    val repositoryForStructureData: RepositoryContractForStructureData,
    private val devNotepadApi: DevNotepadApi
) : HandlerForAppData() {

    private var elementType: String? = null

    fun makeRequestForStructureData(elementType: String) {
        this.elementType = elementType

        devNotepadApi.getStructureData(elementType).enqueue(object : Callback<List<NotepadData>> {
            override fun onResponse(call: Call<List<NotepadData>>, response: Response<List<NotepadData>>) {
                CoroutineScope(Dispatchers.IO).launch {
                    handleServerData(response.body()!!, repositoryForStructureData)
                }

            }

            override fun onFailure(call: Call<List<NotepadData>>, t: Throwable) {
                println("debug: response unsuccessful: $t")

            }
        })
    }

    override suspend fun isLocalDataTableEmpty(): Boolean {
        return repositoryForStructureData.getAllElementsSync()
            .isEmpty()
    }

    override suspend fun matchDataFromServerWithLocal(dataFromServer: List<NotepadData>) {
        val serverDataHashMap: HashMap<Int, NotepadData> = HashMap()
        val localDataHashMap: HashMap<Int, NotepadData> = HashMap()

        for (dataElement in dataFromServer) {
            serverDataHashMap[dataElement.idFromServer] = dataElement
        }

        for (dataElement in repositoryForStructureData.getAllElementsSync()) {
            localDataHashMap[dataElement.idFromServer] = dataElement
        }

        insertNewData(serverDataHashMap, localDataHashMap, repositoryForStructureData)
        replaceRenewedData(serverDataHashMap, localDataHashMap, repositoryForStructureData)
        deleteAbsentDataFormLocalStorage(serverDataHashMap, localDataHashMap, repositoryForStructureData)
    }
}