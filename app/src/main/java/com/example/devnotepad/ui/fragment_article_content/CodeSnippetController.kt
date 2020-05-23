package com.example.devnotepad.ui.fragment_article_content

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.view.ViewStub
import android.view.animation.Animation
import android.view.animation.Transformation
import android.webkit.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.devnotepad.ArticleCodeSnippet
import com.example.devnotepad.BaseApplication
import com.example.devnotepad.data.data_handlers.HandlerForContentData
import com.example.devnotepad.utils.InternetConnectionChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.schedule

/**TODO: refactor this class later...*/
@SuppressLint("SetJavaScriptEnabled")
class CodeSnippetController(
    private val item: ArticleCodeSnippet,
    private var viewStub: ViewStub,
    private val loadingPlaceholder: View,
    private val noInternetConnectionPlaceholder: View,
    private val heightPlaceholder: View
) {

    private val snippetContentCode = item.getEssentialDataOfPiece()
    private val heightIdHashMap: HashMap<String, Int> = HashMap()
    private var webView: View? = null
    private val viewsContext = heightPlaceholder.context
    private var expansionDuration =
        (item.viewHeight / viewsContext.resources.displayMetrics.density).toLong() *
                EXPANSION_SPEED_COEFFICIENT

    companion object {
        private const val DELAY_BEFORE_INFLATE_WEB_VIEW = 100L
        private const val DELAY_BEFORE_PLACEHOLDERS_GONE = 3000L
        private const val PLACEHOLDERS_HIDING_DURATION = 150L

        private const val EXPANSION_SPEED_COEFFICIENT = 2
        private var isInternetConnected = false

        var webViewHeightIdHashMap: MutableLiveData<HashMap<String, Int>> = MutableLiveData()
    }

    init {
        setPlaceholdersHeight()
        showLoadingPlaceholder()
        observeInternetConnection()
        observeWereCodeSnippetsLoaded()
    }

    private fun setPlaceholdersHeight() {
        if (item.viewHeight != 0) {
            heightPlaceholder.layoutParams.height = item.viewHeight
            loadingPlaceholder.layoutParams.height = item.viewHeight
            noInternetConnectionPlaceholder.layoutParams.height = item.viewHeight
        }
    }

    private fun observeWereCodeSnippetsLoaded() {
        HandlerForContentData.wereCodeSnippetsLoadedLiveData.observe(
            viewsContext as LifecycleOwner,
            androidx.lifecycle.Observer { wereCodeSnippetsLoaded ->
                if (wereCodeSnippetsLoaded) {
                    Timer().schedule(150) {
                        inflateWebViewAfterTransitionAnimation()
                    }

                    removeObserverWereCodeSnippetsLoaded()
                }
            })
    }

    private fun removeObserverWereCodeSnippetsLoaded() {
        CoroutineScope(Dispatchers.Main).launch {
            HandlerForContentData.wereCodeSnippetsLoadedLiveData.removeObservers(viewsContext as LifecycleOwner)
        }
    }

    private fun inflateWebViewAfterTransitionAnimation() {
        Timer().schedule(DELAY_BEFORE_INFLATE_WEB_VIEW) {
            CoroutineScope(Dispatchers.Main).launch {
                inflateWebView()

                if (wasWebViewInflated()) {
                    setWebViewSettings(webView as WebView)
                    setCodeSnippetContent(webView as WebView)
                }
            }
        }
    }

    private fun inflateWebView() {
        if (viewStub.parent != null) {
            webView = viewStub.inflate()
        }
    }

    private fun wasWebViewInflated(): Boolean {
        return webView != null
    }

    private fun setWebViewSettings(webView: WebView) {
        webView.settings.javaScriptEnabled = true
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun setCodeSnippetContent(webView: WebView) {
        handleUsualScenario(webView)
//        handleNoInternetConnectionAndEmptyDbScenario(webView)
    }

    private fun handleUsualScenario(webView: WebView) {
        setWebViewContent(webView)
        setWebViewStyle(webView)
        showWebView(webView)
        saveWebViewHeight(webView)
    }

    private fun setWebViewContent(webView: WebView) {
        webView.loadUrl(formJSToApply(snippetContentCode))
    }

    private fun formJSToApply(snippetContentCode: String): String? {
        return "javascript:$snippetContentCode;"
    }

    private fun setWebViewStyle(webView: WebView) {
        BaseApplication.cssCodeSource.gistCSSStyleLiveData.observe(
            viewsContext as LifecycleOwner,
            androidx.lifecycle.Observer {
                webView.loadUrl(
                    "javascript:document.write('<style type=\"text/css\">" + it[0].styleCode + "</style>');"
                )
            })
    }

//    private fun handleNoInternetConnectionAndEmptyDbScenario(webView: WebView) {
//        /**TODO: add empty DB check*/
//        if (!isInternetConnected) {
//            hideWebView(webView)
//            showNoInternetConnectionPlaceholder()
//        }
//    }
//
//    private fun hideWebView(webView: WebView) {
//        webView.visibility = View.GONE
//    }

    private fun observeInternetConnection() {
        InternetConnectionChecker.isInternetConnectedLiveData.observe(
            viewsContext as LifecycleOwner, androidx.lifecycle.Observer {
                isInternetConnected = it
            })
    }

    private fun showWebView(webView: WebView) {
        hideLoadingPlaceholder()
        expandWebView(webView, item.viewHeight)
        webView.alpha = 0f
        webView.animate().alpha(1f).duration = 300
    }

    private fun expandWebView(webView: WebView, targetHeight: Int) {
        val parentView = webView.parent as View
        val matchParentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(parentView.width, View.MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        webView.measure(matchParentMeasureSpec, wrapContentMeasureSpec)

        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                webView.layoutParams.height = if (interpolatedTime == 1f) {
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                } else {
                    (targetHeight * interpolatedTime).toInt()
                }

                webView.requestLayout()
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
                collapseHeightPlaceholder()
                saveWebViewHeight(webView)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // nothing to do
            }
        })

        animation.duration = expansionDuration
        CoroutineScope(Dispatchers.Main).launch {
            webView.startAnimation(animation)
        }
    }

    private fun collapseHeightPlaceholder() {
        heightPlaceholder.layoutParams.height = 0
        heightPlaceholder.requestLayout()
    }

    private fun saveWebViewHeight(webView: WebView) {
        Timer().schedule(1500) {
            val webViewMeasuredHeight = webView.measuredHeight
            println("debug: webViewMeasuredHeight = ${webView.measuredHeight} from controller")

            heightIdHashMap[ArticleContentFragment.KEY_ID_FOR_DYNAMIC_VIEWS] = item.idFromServer
            heightIdHashMap[ArticleContentFragment.KEY_HEIGHT_FOR_DYNAMIC_VIEWS] =
                webViewMeasuredHeight

            webViewHeightIdHashMap.postValue(heightIdHashMap)
        }
    }

    private fun showLoadingPlaceholder() {
        loadingPlaceholder.visibility = View.VISIBLE
    }

//    private fun showNoInternetConnectionPlaceholder() {
//        noInternetConnectionPlaceholder.visibility = View.VISIBLE
//    }

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
//
//    private fun hideNoInternetConnectionPlaceholder() {
//        Timer().schedule(DELAY_BEFORE_PLACEHOLDERS_GONE) {
//            CoroutineScope(Dispatchers.Main).launch {
//                noInternetConnectionPlaceholder.visibility = View.GONE
//            }
//        }
//
//        CoroutineScope(Dispatchers.Main).launch {
//            noInternetConnectionPlaceholder.animate().alpha(0f).duration =
//                PLACEHOLDERS_HIDING_DURATION
//        }
//    }
}