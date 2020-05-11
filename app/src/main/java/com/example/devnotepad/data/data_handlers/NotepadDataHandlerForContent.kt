package com.example.devnotepad.data.data_handlers

import com.example.devnotepad.*
import com.example.devnotepad.data.rest.DevNotepadApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotepadDataHandlerForContent(
    private val notepadViewModelForContent: NotepadViewModelContractForContent,
    private val devNotepadApi: DevNotepadApi
) : NotepadDataHandler() {

    private var parentElementId: Int = 0
    private var elementType = ""

    fun makeRequestForContentData(elementType: String, parentElementId: Int) {
        this.parentElementId = parentElementId
        this.elementType = elementType

        devNotepadApi.getContentData(elementType, parentElementId)
            .enqueue(object : Callback<List<NotepadData>> {
                override fun onResponse(
                    call: Call<List<NotepadData>>, response: Response<List<NotepadData>>) {
                    CoroutineScope(Dispatchers.IO).launch {
                        handleServerData(response.body()!!, notepadViewModelForContent)
                    }
                }

                override fun onFailure(call: Call<List<NotepadData>>, t: Throwable) {
                    println("debug: response unsuccessful: $t")
                }
            })
    }

    override suspend fun isDataTableEmpty(): Boolean {
        return notepadViewModelForContent.repositoryForArticlesContent.getAllElementsSync(parentElementId).isEmpty()
    }

    override suspend fun matchDataFromServerAndLocal(dataFromServer: List<NotepadData>) {
        val serverDataHashMap: HashMap<Int, NotepadData> = HashMap()
        val localDataHashMap: HashMap<Int, NotepadData> = HashMap()

        for (dataElement in dataFromServer) {
            serverDataHashMap[dataElement.idFromServer] = dataElement
        }

        for (dataElement in notepadViewModelForContent.repositoryForArticlesContent.getAllElementsSync(parentElementId)) {
            localDataHashMap[dataElement.idFromServer] = dataElement
        }

        insertNewData(serverDataHashMap, localDataHashMap, notepadViewModelForContent)
        replaceRenewedData(serverDataHashMap, localDataHashMap, notepadViewModelForContent)
        deleteAbsentData(serverDataHashMap, localDataHashMap, notepadViewModelForContent)
    }
}