package ru.devnotepad.articlecontent.di

import dagger.Component
import ru.devnotepad.articlecontent.data.ArticleContentRepository
import ru.devnotepad.articlecontent.di.viewmodelfactory.ViewModelFactoryModule
import ru.devnotepad.articlecontent.ui.ArticleContentFragment

@Component(
    modules = [
        RetrofitModule::class,
        DaoModule::class,
        ViewModelFactoryModule::class,
        ViewModelModule::class
    ]
)
interface ArticleContentComponent {
    fun inject(articleContentRepository: ArticleContentRepository)
    fun inject(articleContentFragment: ArticleContentFragment)
}