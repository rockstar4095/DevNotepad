package com.example.devnotepad.data.rest

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**TODO временный класс.*/
public abstract class RetrofitCreator {

    companion object {
        // Singleton prevents multiple instances of Retrofit creating at the same time.
        @Volatile
        private var INSTANCE: Retrofit? = null

        fun getRetrofit(): Retrofit {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val gson = GsonBuilder()
                    .setLenient()
                    .create()

                val instance = Retrofit.Builder()
                    .baseUrl("https://xn--e1aabjrgcf6b.xn--p1ai/")
                    .addConverterFactory(ScalarsConverterFactory.create()) //important
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}