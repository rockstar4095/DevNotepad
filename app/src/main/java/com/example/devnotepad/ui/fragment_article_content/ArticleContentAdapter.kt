package com.example.devnotepad.ui.fragment_article_content

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.devnotepad.*
import kotlinx.android.synthetic.main.article_code_snippet_item.view.*
import kotlinx.android.synthetic.main.article_header_item.view.*
import kotlinx.android.synthetic.main.article_paragraph_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class ArticleContentAdapter(
    context: Context
) : RecyclerView.Adapter<ArticleContentAdapter.BaseViewHolder<*>>() {

    companion object {
        private const val headerType = 1
        private const val paragraphType = 2
        private const val codeSnippetType = 3
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var articlePieces = ArrayList<ArticlePiece>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            headerType -> ArticleHeaderViewHolder(
                inflater.inflate(
                    R.layout.article_header_item,
                    parent,
                    false
                )
            )
            paragraphType -> ArticleParagraphViewHolder(
                inflater.inflate(
                    R.layout.article_paragraph_item,
                    parent,
                    false
                )
            )
            codeSnippetType -> ArticleCodeSnippetViewHolder(
                inflater.inflate(
                    R.layout.article_code_snippet_item,
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid view type.")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (articlePieces[position]) {
            is ArticleHeader -> headerType
            is ArticleParagraph -> paragraphType
            is ArticleCodeSnippet -> codeSnippetType
            else -> throw IllegalArgumentException("Invalid type of data $position.")
        }
    }

    override fun getItemCount() = articlePieces.size

    internal fun addArticlePieces(articlePieces: List<ArticlePiece>) {
        this.articlePieces = getAllSortedPieces(this.articlePieces, articlePieces)
        notifyDataSetChanged()
    }

    /**
     * Поскольку фрагмент устанавливает несколько источников информации, они добавляются поочередно.
     * Данный метод объединяет элементы статьи и сортирует их по позициям.
     * */
    private fun getAllSortedPieces(
        formerPieces: List<ArticlePiece>,
        newPieces: List<ArticlePiece>
    ): ArrayList<ArticlePiece> {
        val allSortedPieces = ArrayList<ArticlePiece>()
        allSortedPieces.addAll(formerPieces)
        allSortedPieces.addAll(newPieces)
        allSortedPieces.sortBy { A -> A.positionInArticle }
        return allSortedPieces
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    class ArticleHeaderViewHolder(itemView: View) :
        BaseViewHolder<ArticleHeader>(itemView) {
        private val articleHeader: TextView = itemView.header

        override fun bind(item: ArticleHeader) {
            articleHeader.text = item.getContentOfPiece()
        }
    }

    class ArticleParagraphViewHolder(itemView: View) :
        BaseViewHolder<ArticleParagraph>(itemView) {
        private val articleParagraph: TextView = itemView.paragraph

        override fun bind(item: ArticleParagraph) {
            val text = "\t ${item.getContentOfPiece()}"
            articleParagraph.text = text
        }
    }

    /**TODO: replace with appropriate clean code
     * handle scenario when network is not available*/
    class ArticleCodeSnippetViewHolder(itemView: View) :
        BaseViewHolder<ArticleCodeSnippet>(itemView) {
        private val webView: WebView = itemView.webView

        @SuppressLint("SetJavaScriptEnabled")
        override fun bind(item: ArticleCodeSnippet) {
            webView.settings.javaScriptEnabled = true
            webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            webView.setBackgroundColor(Color.TRANSPARENT)

//            webView.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
//                when (motionEvent.touchMajor){
//                    1.0f -> webView.parent.parent.parent.requestDisallowInterceptTouchEvent(false)
//                }
//                return@OnTouchListener true
//            })

            val url = "\t ${item.getContentOfPiece()}"
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    Timer().schedule(500) {
                        expandView(webView)
                        CoroutineScope(Dispatchers.Main).launch {
                            webView.visibility = View.VISIBLE
                        }
                    }
                }
            }
            webView.layoutParams.height = 1
            webView.loadUrl(url)
        }

        /**TODO: replace with appropriate clean code*/
        private fun expandView(view: View) {
            val parentView = view.parent as View
            val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(parentView.width, View.MeasureSpec.EXACTLY)
            val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
            val targetHeight = view.measuredHeight

            view.layoutParams.height = 1
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

            animation.duration = (targetHeight / view.context.resources.displayMetrics.density).toLong() * 10
            view.startAnimation(animation)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val piece = articlePieces[position]
        when (holder) {
            is ArticleHeaderViewHolder -> holder.bind(piece as ArticleHeader)
            is ArticleParagraphViewHolder -> holder.bind(piece as ArticleParagraph)
            is ArticleCodeSnippetViewHolder -> holder.bind(piece as ArticleCodeSnippet)
            else -> throw IllegalArgumentException()
        }
    }
}