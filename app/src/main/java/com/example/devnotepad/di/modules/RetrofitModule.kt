package com.example.devnotepad.di.modules

import com.example.devnotepad.*
import com.example.devnotepad.utils.RuntimeTypeAdapterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Singleton
    @Provides
    fun provideRuntimeTypeAdapter(): RuntimeTypeAdapterFactory<NotepadData> {
        return RuntimeTypeAdapterFactory
            .of(NotepadData::class.java, "type")
            .registerSubtype(DirectionOfStudy::class.java, DirectionOfStudy::class.java.name)
            .registerSubtype(Topic::class.java, Topic::class.java.name)
            .registerSubtype(Article::class.java, Article::class.java.name)
            .registerSubtype(ArticleHeader::class.java, ArticleHeader::class.java.name)
            .registerSubtype(ArticleParagraph::class.java, ArticleParagraph::class.java.name)
            .registerSubtype(ArticleCodeSnippet::class.java, ArticleCodeSnippet::class.java.name)
    }

    @Singleton
    @Provides
    fun provideGson(runtimeTypeAdapter: RuntimeTypeAdapterFactory<NotepadData>): Gson {
        return GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapterFactory(runtimeTypeAdapter)
            .setLenient()
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://xn--e1aabjrgcf6b.xn--p1ai/")
            .addConverterFactory(ScalarsConverterFactory.create()) //important
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}