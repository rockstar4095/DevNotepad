package com.example.devnotepad.data.data_handlers

import androidx.lifecycle.MutableLiveData
import com.example.devnotepad.NotepadData
import com.example.devnotepad.data.repositories.*
import com.example.devnotepad.data.rest.DevNotepadApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HandlerForContentData(
    val repositoryForArticlesContent: RepositoryContractForArticlesContent,
    private val devNotepadApi: DevNotepadApi
) : HandlerForAppData() {

    private var parentElementId: Int = 0
    private var elementType = ""

    companion object {
        var wereCodeSnippetsLoadedLiveData = MutableLiveData<Boolean>()
        var wereImagesLoadedLiveData = MutableLiveData<Boolean>()
        var wasBasicContentLoadedLiveData = MutableLiveData<Boolean>()
        var wereHeadersLoaded = false
        var wereParagraphsLoaded = false
//        var wereCodeSnippetsLoaded = false
//        var wereImagesLoaded = false

        fun resetWasBasicContentLoadedStatus() {
            wereHeadersLoaded = false
            wereParagraphsLoaded = false
        }

        fun resetWereCodeSnippetsLoadedStatus() {
            wereCodeSnippetsLoadedLiveData.postValue(false)
        }
    }

    fun makeRequestForContentData(elementType: String, parentElementId: Int) {
        this.parentElementId = parentElementId
        this.elementType = elementType

        devNotepadApi.getContentData(elementType, parentElementId)
            .enqueue(object : Callback<List<NotepadData>> {
                override fun onResponse(
                    call: Call<List<NotepadData>>, response: Response<List<NotepadData>>
                ) {
                    CoroutineScope(Dispatchers.IO).launch {
                        handleServerData(response.body()!!, repositoryForArticlesContent)
                    }

                    setBasicContentWasLoadedLiveData(elementType)
                    setDynamicContentWasLoadedLiveData(elementType, true)
                }

                override fun onFailure(call: Call<List<NotepadData>>, t: Throwable) {
                    println("debug: getContentData: onFailure: $t")
                    setDynamicContentWasLoadedLiveData(elementType, false)
                }
            })
    }

    /**TODO: probably, I should refactor this method using Open-Closed Principle*/
    private fun setBasicContentWasLoadedLiveData(elementType: String) {
        if (elementType == ArticlesHeadersRepository.TYPE_HEADER) {
            wereHeadersLoaded = true
        }

        if (elementType == ArticlesParagraphsRepository.TYPE_PARAGRAPH) {
            wereParagraphsLoaded = true
        }

//        if (elementType == ArticlesCodeSnippetsRepository.TYPE_CODE_SNIPPET) {
//            wereCodeSnippetsLoaded = true
//        }
//
//        if (elementType == ArticlesImagesRepository.TYPE_IMAGE) {
//            wereImagesLoaded = true
//        }

        if (wasBasicContentLoaded()) {
            wasBasicContentLoadedLiveData.postValue(true)
        }
    }

    private fun setDynamicContentWasLoadedLiveData(elementType: String, wasLoadedSuccessful: Boolean) {
        if (elementType == ArticlesCodeSnippetsRepository.TYPE_CODE_SNIPPET) {
            wereCodeSnippetsLoadedLiveData.postValue(wasLoadedSuccessful)
        } else if (elementType == ArticlesImagesRepository.TYPE_IMAGE) {
            wereImagesLoadedLiveData.postValue(wasLoadedSuccessful)
        }
    }

    private fun wasBasicContentLoaded(): Boolean {
        return wereHeadersLoaded && wereParagraphsLoaded // && wereCodeSnippetsLoaded && wereImagesLoaded
    }

    override suspend fun isLocalDataTableEmpty(): Boolean {
        return repositoryForArticlesContent.getAllElementsSync(parentElementId).isEmpty()
    }

    override suspend fun matchDataFromServerWithLocal(dataFromServer: List<NotepadData>) {
        val serverDataHashMap: HashMap<Int, NotepadData> = HashMap()
        val localDataHashMap: HashMap<Int, NotepadData> = HashMap()

        for (dataElement in dataFromServer) {
            serverDataHashMap[dataElement.idFromServer] = dataElement
        }

        for (dataElement in repositoryForArticlesContent.getAllElementsSync(parentElementId)) {
            localDataHashMap[dataElement.idFromServer] = dataElement
        }

        insertNewData(serverDataHashMap, localDataHashMap, repositoryForArticlesContent)
        replaceRenewedData(serverDataHashMap, localDataHashMap, repositoryForArticlesContent)
        deleteAbsentDataFormLocalStorage(
            serverDataHashMap,
            localDataHashMap,
            repositoryForArticlesContent
        )
    }
}