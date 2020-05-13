package com.example.devnotepad.ui.fragment_article_content

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import com.example.devnotepad.ArticleCodeSnippet
import com.example.devnotepad.utils.InternetConnectionChecker
import kotlinx.android.synthetic.main.article_code_snippet_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

@SuppressLint("SetJavaScriptEnabled")
class CodeSnippetController(
    item: ArticleCodeSnippet,
    private val webView: WebView,
    private val loadingPlaceholder: View,
    private val noInternetConnectionPlaceholder: View
) {

    private val url = item.getContentOfPiece()
    private val tryAgainButton: Button = noInternetConnectionPlaceholder.tryAgainButton

    init {
        tryAgainButton.setOnClickListener {
            loadCodeSnippet()
        }

        observeInternetConnection()
        observeFragmentPaused()
    }

    companion object {

        private const val DELAY_BEFORE_EXPANSION_STARTS: Long = 250
        private const val PLACEHOLDERS_HIDING_DURATION: Long = 150
        private const val EXPANSION_SPEED_COEFFICIENT = 4
        var EXPANSION_DURATION: Long = 0

        var isInternetConnected: Boolean = false
        var wasLoadingStarted: Boolean = false
    }
    fun loadCodeSnippet() {
        if (isInternetConnected && !wasLoadingStarted) {

            showLoadingPlaceholder()
            hideNoInternetConnectionPlaceholder()
            prepareWebView()
            loadWebView()

        } else {
            showNoInternetConnectionPlaceholder()
        }
    }

    private fun loadWebView() {
        webView.loadUrl(url)

        Timer().schedule(200) {
            wasLoadingStarted = true
        }
    }

    private fun observeInternetConnection() {
        // создание объекта для работы
        val internetConnectionChecker = InternetConnectionChecker(webView.context)
        InternetConnectionChecker.isInternetConnected.observe(webView.context as LifecycleOwner, androidx.lifecycle.Observer {
            isInternetConnected = it
            println("debug: хуесос isInternetConnected: $isInternetConnected onDetach: $wasLoadingStarted")
            loadCodeSnippet()
        })
    }

    private fun observeFragmentPaused() {
        ArticleContentFragment.wasFragmentPaused.observe(webView.context as LifecycleOwner, androidx.lifecycle.Observer {
            wasLoadingStarted = it
            println("debug: isInternetConnected: $isInternetConnected onDetach: $wasLoadingStarted")
            loadCodeSnippet()
        })
    }

    private fun prepareWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webView.setBackgroundColor(Color.TRANSPARENT)

        /**TODO: implement OnTouchListener*/

//            webView.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
//                when (motionEvent.touchMajor){
//                    1.0f -> webView.parent.parent.parent.requestDisallowInterceptTouchEvent(false)
//                }
//                return@OnTouchListener true
//            })

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {

                Timer().schedule(DELAY_BEFORE_EXPANSION_STARTS) {
                    expandView(webView)
                }

                CoroutineScope(Dispatchers.Main).launch {
                    webView.alpha = 0f
                    webView.animate().alpha(1f).duration = PLACEHOLDERS_HIDING_DURATION * 12
                }
            }
        }
    }

    private fun expandView(viewToExpand: View) {

        val parentView = viewToExpand.parent as View
        val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(parentView.width, View.MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        viewToExpand.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = viewToExpand.measuredHeight
        EXPANSION_DURATION =
            (targetHeight / viewToExpand.context.resources.displayMetrics.density).toLong() * EXPANSION_SPEED_COEFFICIENT

        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                viewToExpand.layoutParams.height = if (interpolatedTime == 1f) {
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                } else {
                    (targetHeight * interpolatedTime).toInt()
                }

                viewToExpand.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        animation.duration = EXPANSION_DURATION
        hideLoadingPlaceholder()
        CoroutineScope(Dispatchers.Main).launch {
            viewToExpand.startAnimation(animation)
            parentView.startAnimation(animation)
        }
    }

    private fun showLoadingPlaceholder() {
        loadingPlaceholder.visibility = View.VISIBLE
    }

    private fun showNoInternetConnectionPlaceholder() {
        noInternetConnectionPlaceholder.visibility = View.VISIBLE
    }

    private fun hideLoadingPlaceholder() {
        Timer().schedule(EXPANSION_DURATION) {
            CoroutineScope(Dispatchers.Main).launch {
                loadingPlaceholder.visibility = View.GONE
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            loadingPlaceholder.animate().alpha(0f).duration = PLACEHOLDERS_HIDING_DURATION
        }
    }

    private fun hideNoInternetConnectionPlaceholder() {
        Timer().schedule(EXPANSION_DURATION) {
            CoroutineScope(Dispatchers.Main).launch {
                noInternetConnectionPlaceholder.visibility = View.GONE
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            noInternetConnectionPlaceholder.animate().alpha(0f).duration = PLACEHOLDERS_HIDING_DURATION
        }
    }
}