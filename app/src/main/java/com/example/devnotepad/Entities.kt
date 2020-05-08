package com.example.devnotepad

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * TODO: inherit navigation Entities from one parent, to implement one OnItemClickListener.
 * */

/**
 * Интерфейс, объединяющий сущности с данными. Содержит в себе общее для всех сущностей поле -
 * время того, когда изменились данные. В это поле записывается время изменения данных в БД
 * сервера. Оно используется для сравнения в качестве условия необходимости обновления данных.
 * */
interface NotepadData {
    val timeWhenDataChanged: Long
}

/**
 * Направление для изучения, например: Java, Kotlin, Android и т.д..
 * */
@Entity(tableName = "directions_table")
class DirectionOfStudy(
    @PrimaryKey
    @SerializedName("id")
    val idFromServer: Int,
    val name: String,
    @SerializedName("time_when_data_changed")
    override val timeWhenDataChanged: Long
) : NotepadData

/**
 * Тема для изучения, например: SOLID, OOP и т.д.
 * */
@Entity(tableName = "topics_table")
class Topic(
    @PrimaryKey
    @SerializedName("id")
    val idFromServer: Int,
    @SerializedName("direction_id")
    val directionIdFromServer: Int,
    val name: String,
    val views: Int,
    val progress: Int,
    @SerializedName("time_when_data_changed")
    override val timeWhenDataChanged: Long
) : NotepadData

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
    val idFromServer: Int,
    @SerializedName("topic_id")
    val topicIdFromServer: Int,
    val version: Int,
    val name: String,
    val views: Int,
    val isDifficult: Boolean,
    @SerializedName("time_when_data_changed")
    override val timeWhenDataChanged: Long
) : NotepadData, Parcelable {
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
     * Метод возвращает содержимое элемента стати для установки в item recyclerView.
     * */
    fun getContentOfPiece(): String
}

/**
 * Выражает заголовки в статье.
 * Методы equals() и hashcode() переопределены для сравнения данных локальной БД с данными сервера.
 * */
@Entity(tableName = "articles_headers_table")
class ArticleHeader(
    @PrimaryKey
    @SerializedName("id")
    val idFromServer: Int,
    @SerializedName("article_id")
    val articleIdFromServer: Int,
    @SerializedName("position_in_article")
    override val positionInArticle: Int,
    val header: String,
    @SerializedName("time_when_data_changed")
    override val timeWhenDataChanged: Long
) : NotepadData, ArticlePiece {

    override fun toString(): String {
        return "idFromServer: ${this.idFromServer}\n" +
                "articleIdFromServer: ${this.articleIdFromServer}\n" +
                "positionInArticle: ${this.positionInArticle}\n" +
                "header: ${this.header}"
    }

    override fun getContentOfPiece(): String {
        return this.header
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
    val idFromServer: Int,
    @SerializedName("article_id")
    val articleIdFromServer: Int,
    @SerializedName("position_in_article")
    override val positionInArticle: Int,
    val paragraph: String,
    @SerializedName("time_when_data_changed")
    override val timeWhenDataChanged: Long
) : NotepadData, ArticlePiece {

    override fun getContentOfPiece(): String {
        return this.paragraph
    }
}