package com.example.devnotepad.ui.fragment_article_content

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewStub
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.core.view.GestureDetectorCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.devnotepad.ArticleCodeSnippet
import com.example.devnotepad.BaseApplication
import com.example.devnotepad.R
import com.example.devnotepad.data.data_handlers.HandlerForContentData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.schedule

/**TODO: refactor this class later...*/
/**
 * Класс контролирует логику установки и поведения элемента статьи типа CodeSnippet.
 * Общее поведение динамических View наследуется от класса DynamicHeightViewsController.
 * */
@SuppressLint("SetJavaScriptEnabled")
class CodeSnippetController(
    private val item: ArticleCodeSnippet,
    private var viewStub: ViewStub,
    loadingPlaceholder: View,
    noInternetConnectionPlaceholder: View,
    heightPlaceholder: View
) : DynamicHeightViewsController(
    item,
    loadingPlaceholder,
    noInternetConnectionPlaceholder,
    heightPlaceholder
),
    GestureDetector.OnGestureListener {

    private val viewsContext = heightPlaceholder.context
    private val snippetContentCode = item.getEssentialDataOfPiece()
    private var webViewParent: LinearLayout? = null
    private var webView: View? = null
    private val snippetsIdHeightHashMap: HashMap<String, Int> = HashMap()

    private var mDetector: GestureDetectorCompat

    private val onTouchListenerAllowInterception: View.OnTouchListener =
        initOnTouchListenerAllowInterception()
    private val onTouchListenerDisallowInterception: View.OnTouchListener =
        initOnTouchListenerDisallowInterception()

    companion object {
        private const val DELAY_TO_LET_CODE_SNIPPETS_CONTENT_BE_WRITTEN_IN_DB = 150L
        private const val DELAY_BEFORE_INFLATE_WEB_VIEW = 100L
        private const val WEB_VIEW_SCROLLING_MODE_OFF = false
        private const val WEB_VIEW_SCROLLING_MODE_ON = true

        var webViewsHeightIdHashMap: MutableLiveData<HashMap<String, Int>> = MutableLiveData()
    }

    init {
        setPlaceholdersHeight()
        showLoadingPlaceholder()
        observeWereElementsLoaded()

        mDetector = GestureDetectorCompat(viewsContext, this)
    }

    /**
     * Инициализирует слушатель для webView, разрешающий перехват событий родительским элементом.
     * */
    private fun initOnTouchListenerAllowInterception(): View.OnTouchListener {
        return View.OnTouchListener { _, event ->
            mDetector.onTouchEvent(event)
            webView!!.parent.parent.parent.requestDisallowInterceptTouchEvent(false)
            false
        }
    }

    /**
     * Инициализирует слушатель для webView, запрещающий перехват событий родительским элементом.
     * */
    private fun initOnTouchListenerDisallowInterception(): View.OnTouchListener {
        return View.OnTouchListener { _, event ->
            mDetector.onTouchEvent(event)
            webView!!.parent.parent.parent.requestDisallowInterceptTouchEvent(true)
            false
        }
    }

    /**
     * Переключает слушатель событий на webView, разрешая или запрещая перехват событий
     * родительским элементом.
     * */
    private fun switchWebViewScrollingMode(isWebViewScrollingModeOn: Boolean) {
        if (isWebViewScrollingModeOn) {
            webView!!.setOnTouchListener(onTouchListenerDisallowInterception)
        } else {
            webView!!.setOnTouchListener(onTouchListenerAllowInterception)
        }
    }

    /**
     * Поскольку раздутие webView замораживает главный поток, данное событие откладывается до
     * момента, пока не завершится transition фрагмента с содержимым статьи.
     * */
    private fun inflateWebViewAfterFragmentTransitionAnimation() {
        Timer().schedule(DELAY_BEFORE_INFLATE_WEB_VIEW) {
            CoroutineScope(Dispatchers.Main).launch {
                inflateWebView()

                if (wasWebViewInflated()) {
                    callNecessaryWebViewBehaviorAfterInflation(webView as WebView)
                    hideLoadingPlaceholder()
                    switchWebViewScrollingMode(WEB_VIEW_SCROLLING_MODE_ON)
                }
            }
        }
    }

    /**
     * Раздувает webView. Вызывается после завершения transition фрагмента.
     * */
    private fun inflateWebView() {
        if (viewStub.parent != null) {
            webViewParent = viewStub.inflate() as LinearLayout
            webView = webViewParent!!.findViewById(R.id.webView)
        }
    }

    /**
     * Возвращает информацию о том, было ли раздуто webView, сравнивая ссылку в переменной с null.
     * */
    private fun wasWebViewInflated(): Boolean {
        return webView != null
    }

    /**
     * Устанавливает необходимые настройки webView.
     * */
    private fun setWebViewSettings(webView: WebView) {
        webView.settings.javaScriptEnabled = true
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.setBackgroundColor(Color.TRANSPARENT)
    }

    /**
     * Вызывает необходимое поведение webView, после того, как оно было раздуто.
     * */
    private fun callNecessaryWebViewBehaviorAfterInflation(webView: WebView) {
        setWebViewSettings(webView)
        setWebViewContent(webView)
        setWebViewStyle(webView)
        showView(webView)
        saveViewHeight(webView)
    }

    /**
     * Устанавливает содержимое webView.
     * */
    private fun setWebViewContent(webView: WebView) {
        webView.loadUrl(formJSToSetWebViewContent())
    }

    /**
     * Формирует JS код для применения на webView.
     * */
    private fun formJSToSetWebViewContent(): String? {
        return "javascript:$snippetContentCode;"
    }

    private fun setWebViewStyle(webView: WebView) {
        webView.loadUrl(formCSSToSetWebViewStyle())
    }

    /**
     * Получает CSS стиль для применения с webView.
     * TODO: установить наблюдатель на тот случай, если таблица будет изначально пуста.
     * */
    private fun getCodeSnippetStyle(): String? {
        return BaseApplication.cssCodeSource.gistCSSStyle.styleCode
    }

    /**
     * Формирует CSS код для применения на webView.
     * */
    private fun formCSSToSetWebViewStyle(): String {
        return "javascript:document.write('<style type=\"text/css\">${getCodeSnippetStyle()}</style>');"
    }

    override fun observeWereElementsLoaded() {
        HandlerForContentData.wereCodeSnippetsLoadedLiveData.observe(
            viewsContext as LifecycleOwner,
            androidx.lifecycle.Observer { wereCodeSnippetsLoaded ->
                if (!wereCodeSnippetsLoaded) {
                    // show "data from DB" message
                }

                Timer().schedule(DELAY_TO_LET_CODE_SNIPPETS_CONTENT_BE_WRITTEN_IN_DB) {
                    inflateWebViewAfterFragmentTransitionAnimation()
                }

                removeObserverWereElementsLoaded()
            })
    }

    override fun removeObserverWereElementsLoaded() {
        CoroutineScope(Dispatchers.Main).launch {
            HandlerForContentData.wereCodeSnippetsLoadedLiveData.removeObservers(viewsContext as LifecycleOwner)
        }
    }

    override fun saveViewHeight(view: View) {
        Timer().schedule(1500) {
            val webViewMeasuredHeight = view.measuredHeight
            println("debug: webViewMeasuredHeight = $webViewMeasuredHeight from controller")

            snippetsIdHeightHashMap[ArticleContentFragment.KEY_ID_FOR_DYNAMIC_VIEWS] =
                item.idFromServer
            snippetsIdHeightHashMap[ArticleContentFragment.KEY_HEIGHT_FOR_DYNAMIC_VIEWS] =
                webViewMeasuredHeight

            webViewsHeightIdHashMap.postValue(snippetsIdHeightHashMap)
        }
    }

    /*
    * Используется для корректной прокрутки RecyclerView. Если не использовать данное событие,
    * RecyclerView будет игнорировать прокрутку, при возникновении события прокрутки на CodeSnippet.
    * */
    override fun onDown(e: MotionEvent?): Boolean {
        switchWebViewScrollingMode(WEB_VIEW_SCROLLING_MODE_OFF)
        return true
    }

    /*
    * При возникновении события прокрутки на CodeSnippet, в том случае, если была зарегистрирована
    * горизонтальная прокрутка, будет проигнорирована прокрутка RecyclerView.
    * */
    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        if (distanceX != 0f) {
            switchWebViewScrollingMode(WEB_VIEW_SCROLLING_MODE_ON)
        }
        return true
    }

    /*
    * I don't use following motion events on my CodeSnippets.
    * */
    override fun onShowPress(e: MotionEvent?) {
        // do nothing
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        // do nothing
        return false
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        // do nothing
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
        // do nothing
    }
}