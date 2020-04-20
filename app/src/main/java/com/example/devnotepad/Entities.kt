package com.example.devnotepad

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * TODO: inherit Entities from one parent, to implement one OnItemClickListener.
 * */

/**
 * Направление для изучения, например: Java, Kotlin, Android и т.д..
 * */
@Entity(tableName = "directions_table")
class DirectionOfStudy(
    @PrimaryKey
    @SerializedName("id")
    val idFromServer: Int,
    val name: String
)

/**
 * Тема для изучения, например: SOLID, OOP и т.д.
 * */
@Entity(tableName = "topics_table")
class Topic(
    @PrimaryKey
    @SerializedName("id")
    val idFromServer: Int,
    val directionOfStudy: String,
    val name: String,
    val views: Int,
    val progress: Int
)

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
    val topic: String,
    val version: Int,
    val name: String,
    /**TODO: remove everywhere.*/
    val text: String,
    val views: Int,
    val isDifficult: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idFromServer)
        parcel.writeString(topic)
        parcel.writeInt(version)
        parcel.writeString(name)
        parcel.writeString(text)
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
abstract class ArticlePiece {
    /**
     * Хранит номер позиции данного элемента в статье. Используется для последовательного
     * отображения содержимого.
     * */
    abstract val positionInArticle: Int

    /**
     * Метод возвращает содержимое элемента стати для установки в item recyclerView.
     * */
    abstract fun getContentOfPiece(): String
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
    @SerializedName("articleId")
    val articleIdFromServer: Int,
    override val positionInArticle: Int,
    val header: String
) : ArticlePiece() {

    override fun equals(other: Any?): Boolean {

        if (other !is ArticleHeader) return false

        if (other.hashCode() != this.hashCode()) return false

        return other.header == this.header &&
                other.positionInArticle == this.positionInArticle
    }

    override fun hashCode(): Int {
        var result = idFromServer
        result = 31 * result + articleIdFromServer
        result = 31 * result + positionInArticle
        result = 31 * result + header.hashCode()
        return result
    }

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
    @SerializedName("articleId")
    val articleIdFromServer: Int,
    override val positionInArticle: Int,
    val paragraph: String
) : ArticlePiece() {

    override fun equals(other: Any?): Boolean {

        if (other !is ArticleParagraph) return false

        if (other.hashCode() != this.hashCode()) return false

        return other.paragraph == this.paragraph &&
                other.positionInArticle == this.positionInArticle
    }

    override fun hashCode(): Int {
        var result = idFromServer
        result = 31 * result + articleIdFromServer
        result = 31 * result + positionInArticle
        result = 31 * result + paragraph.hashCode()
        return result
    }

    override fun getContentOfPiece(): String {
        return this.paragraph
    }
}