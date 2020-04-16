package com.example.devnotepad.ui.fragment_articles

import com.example.devnotepad.Article

/**
 * Слушатель кликов по элементам списка.
 * */
interface OnArticleClickListener {
    fun onArticleClick(article: Article)
}