package ru.devnotepad.articlecontent.di

import ru.devnotepad.articlecontent.ui.ArticleContentFragment

object ComponentHolder {
    var component: ArticleContentComponent? = null
        private set

    fun initComponent(fragment: ArticleContentFragment) {
        component = DaggerArticleContentComponent.builder()
            .daoModule(DaoModule(fragment.requireContext()))
            .build().apply {
                inject(fragment)
            }
    }

    fun clearComponent() {
        component = null
    }
}