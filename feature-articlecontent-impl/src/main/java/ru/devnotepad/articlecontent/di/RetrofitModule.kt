package ru.devnotepad.articlecontent.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.devnotepad.articlecontent.entities.*
import ru.devnotepad.articlecontent.utils.RuntimeTypeAdapterFactory

@Module
class RetrofitModule {

    companion object {
        private const val BASE_URL = "https://xn--e1aabjrgcf6b.xn--p1ai/"
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient().newBuilder().build()

    @Provides
    fun provideGson(runtimeTypeAdapterFactory: RuntimeTypeAdapterFactory<ArticlePiece>): Gson =
        GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapterFactory(runtimeTypeAdapterFactory)
            .setLenient()
            .create()

    @Provides
    fun provideRuntimeTypeAdapter(): RuntimeTypeAdapterFactory<ArticlePiece> =
        RuntimeTypeAdapterFactory.of(ArticlePiece::class.java, "type")
            .registerSubtype(ArticleHeader::class.java, ArticleHeader::class.java.name)
            .registerSubtype(ArticleParagraph::class.java, ArticleParagraph::class.java.name)
            .registerSubtype(ArticleCodeSnippet::class.java, ArticleCodeSnippet::class.java.name)
            .registerSubtype(ArticleImage::class.java, ArticleImage::class.java.name)
}