package com.example.devnotepad.ui.fragment_article_content

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.devnotepad.Article

class MyViewModelFactory(
    private val application: Application,
    private val article: Article
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ArticleContentViewModel(this.application, this.article) as T
    }
}