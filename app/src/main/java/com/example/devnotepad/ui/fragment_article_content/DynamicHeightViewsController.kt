package com.example.devnotepad.ui.fragment_article_content

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.devnotepad.DynamicArticlePiece
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

/**
 * Класс контролирует поведение View статьи, которые подгружаются отдельно от основного содержимого,
 * то есть фрагменты кода и изображения. В случае, если в локальной БД нет данных о высоте, которую
 * будет занимать View с полным содержимым для указанных элементов статьи, или данные о высоте
 * будут неактуальны, необходимая высота View будет установлена уже в момент, когда основное
 * содержимое статьи будет отображено и статья будет просматриваться пользователем.
 * */
abstract class DynamicHeightViewsController(
    private val item: DynamicArticlePiece,
    private val loadingPlaceholder: View,
    private val noInternetConnectionPlaceholder: View,
    private val heightPlaceholder: View
) {

    companion object {
        private const val DELAY_BEFORE_PLACEHOLDERS_GONE = 3000L
        private const val PLACEHOLDERS_HIDING_DURATION = 150L
        private const val EXPANSION_SPEED_COEFFICIENT = 2
    }

    /**
     * Наблюдает за тем, был ли загружен с сервера определённый вид элементов для данной статьи.
     * */
    protected abstract fun observeWereElementsLoaded()

    /**
     * Удаляет наблюдатель статуса загрузки элементов статьи. Вызывается после получения информации
     * об окончании загрузки, либо в соответствии с жизненным циклом ответственного View-контроллера.
     * Отписываться необходимо во избежание утечки памяти.
     * */
    protected abstract fun removeObserverWereElementsLoaded()

    /**
     * Обеспечивает сохранение высоты полностью загруженного View.
     * */
    protected abstract fun saveViewHeight(view: View)

    /**
     * Устанавливает высоту placeholder'ов, чтобы она соответствовала высоте загружаемого View.
     * */
    protected fun setPlaceholdersHeight() {
        if (item.viewHeight != 0) {
            println("debug: item.viewHeight = ${item.viewHeight}")
            heightPlaceholder.layoutParams.height = item.viewHeight
            loadingPlaceholder.layoutParams.height = item.viewHeight
            noInternetConnectionPlaceholder.layoutParams.height = item.viewHeight
        }
    }

    /**
     * Управляет выводом загруженного View на экран.
     * */
    protected fun showView(view: View) {
        expandViewHeight(view, item.viewHeight)
        view.alpha = 0f
        view.animate().alpha(1f).duration = 300
    }

    /**
     * Производит анимацию, которая увеличивает высоту View до величины, позволяющей
     * вместить содержимое.
     * */
    private fun expandViewHeight(view: View, targetHeight: Int) {
        val parentView = view.parent as View
        val matchParentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(parentView.width, View.MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(matchParentMeasureSpec, wrapContentMeasureSpec)

        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                view.layoutParams.height = if (interpolatedTime == 1f) {
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                } else {
                    (targetHeight * interpolatedTime).toInt()
                }

                view.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // nothing to do
            }

            override fun onAnimationEnd(animation: Animation?) {
                collapseHeightPlaceholderHeight()
                saveViewHeight(view)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // nothing to do
            }
        })

        val expansionDuration = (targetHeight / view.context.resources.displayMetrics.density)
            .toLong() * EXPANSION_SPEED_COEFFICIENT

        animation.duration = expansionDuration
        CoroutineScope(Dispatchers.Main).launch {
            view.startAnimation(animation)
        }
    }

    /**
     * Скрывает placeholder загрузки.
     * */
    protected fun hideLoadingPlaceholder() {
        Timer().schedule(DELAY_BEFORE_PLACEHOLDERS_GONE) {
            CoroutineScope(Dispatchers.Main).launch {
                loadingPlaceholder.visibility = View.GONE
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            loadingPlaceholder.animate().alpha(0f).duration = PLACEHOLDERS_HIDING_DURATION
        }
    }

    /**
     * Сжимает высоту placeholder'а, который отвечает за удержание высоты для динамического View.
     * */
    protected fun collapseHeightPlaceholderHeight() {
        heightPlaceholder.layoutParams.height = 0
        heightPlaceholder.requestLayout()
    }

    /**
     * Выводит на экран загрузочный placeholder.
     * */
    protected fun showLoadingPlaceholder() {
        loadingPlaceholder.visibility = View.VISIBLE
    }
}