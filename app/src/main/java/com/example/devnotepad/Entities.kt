package com.example.devnotepad

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Направление для изучения, например: Java, Kotlin, Android и т.д.
 * */
class DirectionOfStudy(private val name: String)

/**
 * Тема для изучения, например: SOLID, OOP и т.д.
 * */
class Topic(
    private val directionOfStudy: String,
    private val name: String,
    private val views: Int,
    private val progress: Int
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
    @PrimaryKey(autoGenerate = true)
    public val id: Int,
    public val version: Int,
    public val views: Int,
    public val topic: String,
    public val title: String,
    public val text: String,
    public val isDifficult: Boolean
)