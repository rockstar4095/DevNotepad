package com.example.devnotepad

import android.os.Parcel
import android.os.Parcelable
import android.webkit.WebView
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Интерфейс, объединяющий сущности с данными. Содержит в себе общие для всех сущностей поля -
 * время того, когда изменились данные и id сущности, полученный с сервера. В это поле записывается
 * время изменения данных в БД сервера. Оно используется для сравнения в качестве условия
 * необходимости обновления данных.
 * */
interface NotepadData {
    val timeWhenDataChanged: Long
    val idFromServer: Int
}

interface StructureData

/**
 * Направление для изучения, например: Java, Kotlin, Android и т.д..
 * */
@Entity(tableName = "directions_table")
class DirectionOfStudy(
    @PrimaryKey
    @SerializedName("id")
    override val idFromServer: Int,
    val name: String,
    @SerializedName("time_when_data_changed")
    override val timeWhenDataChanged: Long
) : NotepadData, StructureData

/**
 * Тема для изучения, например: SOLID, OOP и т.д.
 * */
@Entity(tableName = "topics_table")
class Topic(
    @PrimaryKey
    @SerializedName("id")
    override val idFromServer: Int,
    @SerializedName("direction_id")
    val directionIdFromServer: Int,
    val name: String,
    val views: Int,
    val progress: Int,
    @SerializedName("time_when_data_changed")
    override val timeWhenDataChanged: Long
) : NotepadData, StructureData

/**
 * Отдельная мини-статья с краткой информацией о предмете, например: Inheritance, Encapsulation и т.д.
 *
 * Содержимое статьи: заголовок, текст. В случае, если материал сложно воспинимается, предусмотрена
 * возможность пометить данный фрагмент информации как сложный.
 *
 * В статье хранится ее версия. Версии статей сверяются с версиями на сервере и, в случае если
 * версия на устройстве устарела, загружается новое содержимое с сервера.
 *
 * Расширяет класс Parcelable, для того, чтобы была возможность передать статью в Intent.
 * */
@Entity(tableName = "articles_table")
class Article(
    @PrimaryKey
    @SerializedName("id")
    override val idFromServer: Int,
    @SerializedName("topic_id")
    val topicIdFromServer: Int,
    val version: Int,
    val name: String,
    val views: Int,
    val isDifficult: Boolean,
    @SerializedName("time_when_data_changed")
    override val timeWhenDataChanged: Long
) : NotepadData, StructureData, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idFromServer)
        parcel.writeInt(topicIdFromServer)
        parcel.writeInt(version)
        parcel.writeString(name)
        parcel.writeInt(views)
        parcel.writeByte(if (isDifficult) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * Объединяет сущности, которые созданы для отображения содержимого статей.
 * Объявляет общие поля и классы для работы с сущностями.
 * */
interface ArticlePiece {
    /**
     * Хранит номер позиции данного элемента в статье. Используется для последовательного
     * отображения содержимого.
     * */
    val positionInArticle: Int

    /**
     * Метод возвращает уникальное содержимое элемента статьи,
     * которое несет в себе основную необходимую информацию.
     * */
    fun getEssentialDataOfPiece(): String
    override fun equals(other: Any?): Boolean
}

interface DynamicArticlePiece : ArticlePiece {

    /**
     * Хранит высоту элемента. Данные обновляются всякий раз после его загрузки.
     * Данные о высоте элемента необходимы до момента его полной загрузки для того, чтобы установить
     * высоту placeholder'а. Это способстувет лучшему виду статьи в момент загрузки элементов.
     * */
    val viewHeight: Int
    val url: String
}

/**
 * Выражает заголовки в статье.
 * Методы equals() и hashcode() переопределены для сравнения данных локальной БД с данными сервера.
 * */
@Entity(tableName = "articles_headers_table")
class ArticleHeader(
    @PrimaryKey
    @SerializedName("id")
    override val idFromServer: Int,
    @SerializedName("article_id")
    val articleIdFromServer: Int,
    @SerializedName("position_in_article")
    override val positionInArticle: Int,
    val header: String,
    @SerializedName("time_when_data_changed")
    override val timeWhenDataChanged: Long
) : NotepadData, ArticlePiece {
    override fun getEssentialDataOfPiece(): String {
        return this.header
    }

    override fun equals(other: Any?): Boolean {
        if (other is ArticleHeader && other.idFromServer == this.idFromServer) return true
        return false
    }

    override fun hashCode(): Int {
        return idFromServer
    }
}

/**
 * Выражает параграфы в статье.
 * Методы equals() и hashcode() переопределены для сравнения данных локальной БД с данными сервера.
 * */
@Entity(tableName = "articles_paragraphs_table")
class ArticleParagraph(
    @PrimaryKey
    @SerializedName("id")
    override val idFromServer: Int,
    @SerializedName("article_id")
    val articleIdFromServer: Int,
    @SerializedName("position_in_article")
    override val positionInArticle: Int,
    val paragraph: String,
    @SerializedName("time_when_data_changed")
    override val timeWhenDataChanged: Long
) : NotepadData, ArticlePiece {
    override fun getEssentialDataOfPiece(): String {
        return this.paragraph
    }

    override fun equals(other: Any?): Boolean {
        if (other is ArticleParagraph && other.idFromServer == this.idFromServer) return true
        return false
    }

    override fun hashCode(): Int {
        return idFromServer
    }
}

@Entity(tableName = "articles_code_snippets_table")
class ArticleCodeSnippet(
    @PrimaryKey
    @SerializedName("id")
    override val idFromServer: Int,
    @SerializedName("article_id")
    val articleIdFromServer: Int,
    @SerializedName("position_in_article")
    override val positionInArticle: Int,
    override val url: String,
    @SerializedName("time_when_data_changed")
    override val timeWhenDataChanged: Long,
    override val viewHeight: Int,
    @SerializedName("code_source")
    val codeSource: String
) : NotepadData, DynamicArticlePiece {
    override fun getEssentialDataOfPiece(): String {
        return this.codeSource
    }

    override fun equals(other: Any?): Boolean {
        if (other is ArticleCodeSnippet && other.idFromServer == this.idFromServer) return true
        return false
    }

    override fun hashCode(): Int {
        return idFromServer
    }
}

/**
 * Таблица с этой сущностью содержится в отдельной БД.
 * */
@Entity(tableName = "gist_css_style_table")
class GistCSSStyle(
    @PrimaryKey
    val id: Int,
    @SerializedName("gist_css_style")
    val styleCode: String
)

@Entity(tableName = "articles_images_table")
class ArticleImage(
    @PrimaryKey
    @SerializedName("id")
    override val idFromServer: Int,
    @SerializedName("article_id")
    val articleIdFromServer: Int,
    @SerializedName("position_in_article")
    override val positionInArticle: Int,
    override val url: String,
    @SerializedName("time_when_data_changed")
    override val timeWhenDataChanged: Long,
    override val viewHeight: Int
) : NotepadData, DynamicArticlePiece {
    override fun getEssentialDataOfPiece(): String {
        return this.url
    }

    override fun equals(other: Any?): Boolean {
        if (other is ArticleImage && other.idFromServer == this.idFromServer) return true
        return false
    }

    override fun hashCode(): Int {
        return idFromServer
    }
}