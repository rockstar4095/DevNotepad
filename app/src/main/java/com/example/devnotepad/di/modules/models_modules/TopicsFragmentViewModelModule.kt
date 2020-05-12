package com.example.devnotepad.di.modules.models_modules

import androidx.lifecycle.ViewModel
import com.example.devnotepad.di.ViewModelKey
import com.example.devnotepad.ui.fragment_topics.TopicsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TopicsFragmentViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TopicsViewModel::class)
    abstract fun bindTopicsFragmentViewModel(topicsViewModel: TopicsViewModel): ViewModel
}