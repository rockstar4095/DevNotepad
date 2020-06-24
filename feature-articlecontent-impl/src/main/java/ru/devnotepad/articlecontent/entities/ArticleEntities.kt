package ru.devnotepad.articlecontent.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


/**
 * Объединяет сущности, которые созданы для отображения содержимого статей.
 * Объявляет общие поля и классы для работы с сущностями.
 * */
abstract class ArticlePiece {
    /**
     * Хранит номер позиции данного элемента в статье. Используется для последовательного
     * отображения содержимого.
     * */
    abstract val idFromServer: Int
    abstract val positionInArticle: Int
    abstract val timeWhenDataChanged: Long

    /**
     * Метод возвращает уникальное содержимое элемента статьи,
     * которое несет в себе основную необходимую информацию.
     * */
    abstract fun getEssentialDataOfPiece(): String

    final override fun equals(other: Any?): Boolean {
        return (other is ArticlePiece && other.idFromServer == this.idFromServer)
    }

    final override fun hashCode(): Int {
        return idFromServer
    }
}

abstract class DynamicArticlePiece : ArticlePiece() {

    /**
     * Хранит высоту элемента. Данные обновляются всякий раз после его загрузки.
     * Данные о высоте элемента необходимы до момента его полной загрузки для того, чтобы установить
     * высоту placeholder'а. Это способстувет лучшему виду статьи в момент загрузки элементов.
     * */
    abstract val viewHeight: Int
    abstract val url: String
}

/**
 * Выражает заголовки в статье.
 * Методы equals() и hashcode() переопределены для сравнения данных локальной БД с данными сервера.
 * */
@Entity(tableName = "articles_headers_table")
data class ArticleHeader(
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
) : ArticlePiece() {
    override fun getEssentialDataOfPiece(): String {
        return this.header
    }

    companion object {
        const val VIEW_TYPE = 1
    }
}

/**
 * Выражает параграфы в статье.
 * Методы equals() и hashcode() переопределены для сравнения данных локальной БД с данными сервера.
 * */
@Entity(tableName = "articles_paragraphs_table")
data class ArticleParagraph(
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
) : ArticlePiece() {
    override fun getEssentialDataOfPiece(): String {
        return this.paragraph
    }

    companion object {
        const val VIEW_TYPE = 2
    }
}

@Entity(tableName = "articles_code_snippets_table")
data class ArticleCodeSnippet(
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
) : DynamicArticlePiece() {
    override fun getEssentialDataOfPiece(): String {
        return this.codeSource
    }

    companion object {
        const val VIEW_TYPE = 3
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
data class ArticleImage(
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
) : DynamicArticlePiece() {
    override fun getEssentialDataOfPiece(): String {
        return this.url
    }

    companion object {
        const val VIEW_TYPE = 4
    }
}