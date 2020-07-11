package com.example.devnotepad.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelProviderFactory @Inject constructor(var creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var creator = creators[modelClass]

        // If the ViewModel has not been created
        if (creator == null) {

            // Loop through the allowed keys
            for (entry: Map.Entry<Class<out ViewModel>, Provider<ViewModel>> in creators.entries) {

                // If it's not allowed, set the Provider<ViewModel>
                if (modelClass.isAssignableFrom(entry.key)) {
                    creator = entry.value
                    break
                }
            }
        }

        // If this is not one of the allowed keys, throw exception
        if (creator == null) {
            throw IllegalArgumentException("Unknown model class: $modelClass")
        }

        // Return the Provider
        try {
            /**TODO: unchecked cast*/
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}