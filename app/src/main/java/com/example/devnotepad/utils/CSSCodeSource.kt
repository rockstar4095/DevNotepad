package com.example.devnotepad.utils

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.devnotepad.BaseApplication
import com.example.devnotepad.GistCSSStyle
import com.example.devnotepad.data.local.GistCSSStyleRoomDatabase
import com.example.devnotepad.data.rest.DevNotepadApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class CSSCodeSource(application: Application) {

    @Inject
    lateinit var retrofit: Retrofit

    private val devNotepadApi: DevNotepadApi
    private val gistCSSStyleDao =
        GistCSSStyleRoomDatabase.getDatabase(application).gistCSSStyleDao()
    var gistCSSStyleLiveData: LiveData<List<GistCSSStyle>>
    val gistCSSStyle: GistCSSStyle

    companion object {
        private const val NO_CHANGES_CODE = "0"

        // Одинаковый id при вставке с БД обеспечит то, что строка таблицы будет каждый раз самозаменяться
        private const val ID_OF_STYLE_IN_DB = 1
    }

    init {
        val daggerAppComponent = BaseApplication.appComponent
        daggerAppComponent.inject(this)

        devNotepadApi = retrofit.create(DevNotepadApi::class.java)

        gistCSSStyleLiveData = getCSSStyle()
        gistCSSStyle = getGistCSSStyleSync()

        makeRequestForNewVersionOfCode()
    }

    private fun getGistCSSStyleSync() = runBlocking {
        gistCSSStyleDao.getCSSStyleSync()
    }

    private fun getCSSStyle(): LiveData<List<GistCSSStyle>> {
        return gistCSSStyleDao.getCSSStyle()
    }

    private fun makeRequestForNewVersionOfCode() {
        println("debug: makeRequestForOldVersionOfCode()")
        devNotepadApi.getGistNewVersionOfCSSStyle().enqueue(object : Callback<List<GistCSSStyle>> {
            override fun onResponse(
                call: Call<List<GistCSSStyle>>,
                response: Response<List<GistCSSStyle>>
            ) {
                val versionOfStyleFromServer = response.body()?.get(0)?.styleCode
                if (versionOfStyleFromServer == null) {
                    println("debug: versionOfStyleFromServer == null")
                    return
                }

                if (versionOfStyleFromServer == NO_CHANGES_CODE) {
                    setOldStyle()
                } else {
                    setNewStyle(versionOfStyleFromServer)
                }
            }

            override fun onFailure(call: Call<List<GistCSSStyle>>, t: Throwable) {
                println("debug: makeRequestForNewVersionOfStyles(): onFailure: $t")
            }
        })
    }

    private fun setOldStyle() {
        gistCSSStyleLiveData.observeForever { gistCSSStyleTable ->
            if (gistCSSStyleTable.isEmpty()) {
                makeRequestForOldVersionOfCode()
            } else {
                println("debug: gistCSSStyleLiveData: ${gistCSSStyleLiveData.value?.get(0)?.styleCode}")
            }
        }
    }

    private fun makeRequestForOldVersionOfCode() {
        println("debug: makeRequestForOldVersionOfCode()")
        devNotepadApi.getGistOldVersionOfCSSStyle().enqueue(object : Callback<List<GistCSSStyle>> {
            override fun onResponse(
                call: Call<List<GistCSSStyle>>,
                response: Response<List<GistCSSStyle>>
            ) {
                println("debug: response: ${response.body()?.get(0)!!.styleCode}")
                CoroutineScope(Dispatchers.IO).launch {
                    gistCSSStyleDao.insertCSSStyle(response.body()!![0])
                }
            }

            override fun onFailure(call: Call<List<GistCSSStyle>>, t: Throwable) {
                println("debug: makeRequestForOldVersionOfStyle(): onFailure: $t")
            }

        })
    }

    private fun setNewStyle(versionOfStyleFromServer: String) {
        CoroutineScope(Dispatchers.IO).launch {
            gistCSSStyleDao.insertCSSStyle(GistCSSStyle(ID_OF_STYLE_IN_DB, versionOfStyleFromServer))
        }
    }
}
