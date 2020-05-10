package com.example.devnotepad.ui

import com.example.devnotepad.*
import com.example.devnotepad.data.rest.DevNotepadApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class NotepadDataHandlerForContent(
    private val viewModel: NotepadViewModelContractForContent,
    private val restApi: DevNotepadApi
) : NotepadDataHandler() {

    private var parentElementId: Int = 0
    private var elementType = ""

    fun makeRequestForContentData(elementType: String, parentElementId: Int) {
        this.parentElementId = parentElementId
        this.elementType = elementType

        when (elementType) {
            "header" -> {
                restApi.getArticleHeaders(parentElementId)
                    .enqueue(object : Callback<List<ArticleHeader>> {
                        override fun onResponse(
                            call: Call<List<ArticleHeader>>, response: Response<List<ArticleHeader>>
                        ) {
                            CoroutineScope(Dispatchers.IO).launch {
                                handleServerData(response.body()!!, viewModel)
                            }
                        }

                        override fun onFailure(call: Call<List<ArticleHeader>>, t: Throwable) {
                            println("debug: response unsuccessful: $t")
                        }
                    })
            }
            "paragraph" -> {
                restApi.getArticleParagraphs(parentElementId)
                    .enqueue(object : Callback<List<ArticleParagraph>> {
                        override fun onResponse(
                            call: Call<List<ArticleParagraph>>,
                            response: Response<List<ArticleParagraph>>
                        ) {
                            CoroutineScope(Dispatchers.IO).launch {
                                handleServerData(response.body()!!, viewModel)
                            }
                        }

                        override fun onFailure(call: Call<List<ArticleParagraph>>, t: Throwable) {
                            println("debug: response unsuccessful: $t")
                        }
                    })
            }
        }
    }

    override suspend fun isDataTableEmpty(): Boolean {
        return viewModel.notepadRepository.getAllElementsSync(parentElementId).isEmpty()
    }

    override suspend fun matchDataFromServerAndLocal(dataFromServer: List<NotepadData>) {
        val serverDataHashMap: HashMap<Int, NotepadData> = HashMap()
        val localDataHashMap: HashMap<Int, NotepadData> = HashMap()

        for (dataElement in dataFromServer) {
            serverDataHashMap[dataElement.idFromServer] = dataElement
        }

        for (dataElement in viewModel.notepadRepository.getAllElementsSync(parentElementId)) {
            localDataHashMap[dataElement.idFromServer] = dataElement
        }

        insertNewData(serverDataHashMap, localDataHashMap, viewModel)
        replaceRenewedData(serverDataHashMap, localDataHashMap, viewModel)
        deleteAbsentData(serverDataHashMap, localDataHashMap, viewModel)
    }
}