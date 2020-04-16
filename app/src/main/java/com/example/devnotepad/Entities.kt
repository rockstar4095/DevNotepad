package com.example.devnotepad

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

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
 * */
@Entity(tableName = "articles_table")
class Article(
    @PrimaryKey
    @SerializedName("id")
    val idFromServer: Int,
    val topic: String,
    val version: Int,
    val name: String,
    val text: String,
    val views: Int,
    val isDifficult: Boolean
)