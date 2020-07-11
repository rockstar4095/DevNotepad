package com.example.devnotepad.di.modules.models_modules

import androidx.lifecycle.ViewModel
import com.example.devnotepad.di.ViewModelKey
import com.example.devnotepad.ui.fragment_directions.DirectionsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DirectionsFragmentViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DirectionsViewModel::class)
    abstract fun bindDirectionsFragmentViewModel(directionsViewModel: DirectionsViewModel): ViewModel
}