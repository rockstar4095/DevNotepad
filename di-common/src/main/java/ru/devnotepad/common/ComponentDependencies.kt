package ru.devnotepad.common

import androidx.fragment.app.Fragment
import dagger.MapKey
import dagger.Module
import dagger.multibindings.Multibinds
import javax.inject.Scope
import kotlin.reflect.KClass

/**
 * Компоненты модулей приложения должны описывать свои зависимости в интерфейсе наследуясь от
 * [ComponentDependencies].
 *
 * В корневом экране заводится [ComponentDependenciesProvider], в который с помощью multibinding'а
 * инжектятся необходимые зависимости. Ключ - [ComponentDependenciesKey].
 *
 * Поскольку входная точка модуля - фрагмент, компонент инициализируется во фрагменте и ищет
 * зависимости вызовом [Fragment.findComponentDependencies].
 *
 * идея - https://github.com/Tagakov/modules-and-dagger
 * */

interface ComponentDependencies

inline fun <reified T : ComponentDependencies> Fragment.findComponentDependencies(): T =
    findComponentDependenciesProvider()[T::class.java] as T

typealias ComponentDependenciesProvider =
        Map<Class<out ComponentDependencies>, @JvmSuppressWildcards ComponentDependencies>

interface ComponentDependenciesHolder { val dependencies: ComponentDependenciesProvider }

@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class ComponentDependenciesKey(val value: KClass<out ComponentDependencies>)

@Module
abstract class DummyComponentDependenciesModule private constructor() {
    @Multibinds
    abstract fun componentDependencies(): ComponentDependenciesProvider
}

fun Fragment.findComponentDependenciesProvider(): ComponentDependenciesProvider {
    var current: Fragment? = parentFragment
    while (current !is ComponentDependenciesHolder?) {
        current = current?.parentFragment
    }

    val hasDaggerProviders = current ?: when {
        activity is ComponentDependenciesHolder -> activity as ComponentDependenciesHolder
        activity?.application is ComponentDependenciesHolder ->
            activity?.application as ComponentDependenciesHolder
        else -> throw IllegalStateException("Cannot find suitable dagger provider for $this")
    }
    return hasDaggerProviders.dependencies
}

@Scope
annotation class PerFeature