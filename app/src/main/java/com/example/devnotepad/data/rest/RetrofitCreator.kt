package com.example.devnotepad.data.rest

import com.example.devnotepad.*
import com.example.devnotepad.utils.RuntimeTypeAdapterFactory
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

                /**
                 * Данный адаптер помогает GSON сопоставить типы сущностей, получаемых с сервера,
                 * с типами сущностей в приложении. Он необходим потому, что все запросы к БД
                 * сервера приложение делает, используя не конкретные классы сущностей, а
                 * объединяющий их родительский класс - NotepadData.
                 * */
                val adapter: RuntimeTypeAdapterFactory<NotepadData> = RuntimeTypeAdapterFactory
                    .of(NotepadData::class.java, "type")
                    .registerSubtype(DirectionOfStudy::class.java, DirectionOfStudy::class.java.name) // "DirectionOfStudy"
                    .registerSubtype(Topic::class.java, Topic::class.java.name) // "Topic"
                    .registerSubtype(Article::class.java, Article::class.java.name) // "Article"
                    .registerSubtype(ArticleHeader::class.java, ArticleHeader::class.java.name) // "ArticleHeader"
                    .registerSubtype(ArticleParagraph::class.java, ArticleParagraph::class.java.name) // "ArticleParagraph"
                    .registerSubtype(ArticleCodeSnippet::class.java, ArticleCodeSnippet::class.java.name) // "ArticleCodeSnippet"

                val gson = GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapterFactory(adapter)
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