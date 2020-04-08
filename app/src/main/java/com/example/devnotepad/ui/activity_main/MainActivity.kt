package com.example.devnotepad.ui.activity_main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.devnotepad.R
import com.example.devnotepad.data.rest.DevNotepadApi
import com.example.devnotepad.data.rest.RetrofitCreator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

/**
 * Главный экран показывает список направлений для изучения, например: Java, Kotlin, Android и т.д.
 * */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofitInstance = RetrofitCreator.getRetrofit()
        val api = retrofitInstance.create(DevNotepadApi::class.java)

        api.getArticles().enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                println("response successful: ${response.body()}")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                println("response unsuccessful: $t")
            }
        })
    }
}
