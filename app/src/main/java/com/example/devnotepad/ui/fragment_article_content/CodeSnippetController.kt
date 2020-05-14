package com.example.devnotepad.ui.fragment_article_content

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.devnotepad.ArticleCodeSnippet
import com.example.devnotepad.utils.InternetConnectionChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.schedule

@SuppressLint("SetJavaScriptEnabled")
class CodeSnippetController(
    private val item: ArticleCodeSnippet,
    private val webView: WebView,
    private val loadingPlaceholder: View,
    private val noInternetConnectionPlaceholder: View
) {

    private val url = item.getContentOfPiece()
    private val heightIdHashMap: HashMap<String, Int> = HashMap()

    init {
        observeInternetConnection()
        observeFragmentAttached()
    }

    companion object {
        const val KEY_ID = "id"
        const val KEY_HEIGHT = "height"

        private const val DELAY_BEFORE_PLACEHOLDERS_GONE: Long = 3000
        private const val DELAY_BEFORE_EXPANSION_STARTS: Long = 250
        private const val PLACEHOLDERS_HIDING_DURATION: Long = 150
        private const val EXPANSION_SPEED_COEFFICIENT = 4

        private var EXPANSION_DURATION: Long = 0
        private var isInternetConnected: Boolean = false
        private var wasFragmentAttached: Boolean = true

        var webViewHeightIdHashMap: MutableLiveData<HashMap<String, Int>> = MutableLiveData()
    }

    fun loadCodeSnippet() {
        prepareWebView()

        if (isInternetConnected && wasFragmentAttached) {
            showLoadingPlaceholder()
            hideNoInternetConnectionPlaceholder()
            loadWebView()
        } else {
            showNoInternetConnectionPlaceholder()
        }
    }

    private fun loadWebView() {
        webView.loadUrl(url)
    }

    private fun observeInternetConnection() {
        InternetConnectionChecker.isInternetConnectedLiveData.observe(webView.context as LifecycleOwner, androidx.lifecycle.Observer {
            isInternetConnected = it
            loadCodeSnippet()
        })
    }

    private fun observeFragmentAttached() {
        ArticleContentFragment.wasFragmentAttached.observe(webView.context as LifecycleOwner, androidx.lifecycle.Observer {
            wasFragmentAttached = it
        })
    }

    private fun prepareWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webView.setBackgroundColor(Color.TRANSPARENT)

        /**TODO: implement OnTouchListener, make code cleaner.*/

//            webView.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
//                when (motionEvent.touchMajor){
//                    1.0f -> webView.parent.parent.parent.requestDisallowInterceptTouchEvent(false)
//                }
//                return@OnTouchListener true
//            })

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                showWebView()
                hideLoadingPlaceholder()
                saveWebViewHeight()
            }
        }
    }

    private fun saveWebViewHeight() {
        Timer().schedule(3000) {
            val webViewMeasuredHeight = webView.measuredHeight

            heightIdHashMap[KEY_ID] = item.idFromServer
            heightIdHashMap[KEY_HEIGHT] = webViewMeasuredHeight

            webViewHeightIdHashMap.postValue(heightIdHashMap)
        }
    }

    private fun showWebView() {

        Timer().schedule(DELAY_BEFORE_EXPANSION_STARTS) {
            if (item.webViewHeight == 0 || item.webViewHeight == 1) {
                expandView(webView)
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            webView.alpha = 0f
            webView.animate().alpha(1f).duration = PLACEHOLDERS_HIDING_DURATION * 5
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
        Timer().schedule(DELAY_BEFORE_PLACEHOLDERS_GONE) {
            CoroutineScope(Dispatchers.Main).launch {
                loadingPlaceholder.visibility = View.GONE
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            loadingPlaceholder.animate().alpha(0f).duration = PLACEHOLDERS_HIDING_DURATION
        }
    }

    private fun hideNoInternetConnectionPlaceholder() {
        Timer().schedule(DELAY_BEFORE_PLACEHOLDERS_GONE) {
            CoroutineScope(Dispatchers.Main).launch {
                noInternetConnectionPlaceholder.visibility = View.GONE
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            noInternetConnectionPlaceholder.animate().alpha(0f).duration = PLACEHOLDERS_HIDING_DURATION
        }
    }
}