package com.example.devnotepad.data.data_handlers

import com.example.devnotepad.NotepadData

abstract class NotepadDataHandler {

    abstract suspend fun isDataTableEmpty(): Boolean

    abstract suspend fun matchDataFromServerAndLocal(dataFromServer: List<NotepadData>)

    protected suspend fun handleServerData(
        dataFromServer: List<NotepadData>,
        viewModel: NotePadViewModelContract
    ) {
        if (isDataTableEmpty()) {
            insertDataInEmptyTable(dataFromServer, viewModel)
        } else {
            matchDataFromServerAndLocal(dataFromServer)
        }
    }

    private fun insertDataInEmptyTable(
        dataFromServer: List<NotepadData>,
        viewModel: NotePadViewModelContract
    ) {
        for (dataElement in dataFromServer) {
            viewModel.insertElement(dataElement)
        }
    }

    protected fun insertNewData(
        serverDataHashMap: java.util.HashMap<Int, NotepadData>,
        localDataHashMap: java.util.HashMap<Int, NotepadData>,
        viewModel: NotePadViewModelContract
    ) {
        for ((idFromServer, dataElement) in serverDataHashMap) {
            if (!localDataHashMap.containsKey(idFromServer)) {
                viewModel.insertElement(dataElement)
            }
        }
    }

    protected fun replaceRenewedData(
        serverDataHashMap: java.util.HashMap<Int, NotepadData>,
        localDataHashMap: java.util.HashMap<Int, NotepadData>,
        viewModel: NotePadViewModelContract
    ) {
        for ((idFromServer, dataElement) in serverDataHashMap) {
            if (localDataHashMap.containsKey(idFromServer) &&
                dataElement.timeWhenDataChanged != localDataHashMap[idFromServer]!!.timeWhenDataChanged
            ) {
                viewModel.insertElement(dataElement)
            }
        }
    }

    protected fun deleteAbsentData(
        serverDataHashMap: java.util.HashMap<Int, NotepadData>,
        localDataHashMap: java.util.HashMap<Int, NotepadData>,
        viewModel: NotePadViewModelContract
    ) {
        for ((idFromServer, dataElement) in localDataHashMap) {
            if (!serverDataHashMap.containsKey(idFromServer)) {
                viewModel.deleteElement(dataElement)
            }
        }
    }
}