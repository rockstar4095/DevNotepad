package com.example.devnotepad.ui.fragment_article_content

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devnotepad.*
import com.example.devnotepad.ui.ViewModelProviderFactory

import com.example.devnotepad.ui.fragment_articles.ArticlesFragment
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ArticleContentFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    companion object {
        fun newInstance() = ArticleContentFragment()
        private const val RECYCLER_VIEW_CACHE_SIZE = 64
    }

    private lateinit var viewModelForHeadersForHeaders: ArticleContentViewModelForHeaders
    private lateinit var viewModelForHeadersForParagraphs: ArticleContentViewModelForParagraphs
    private lateinit var viewModelForHeadersForCodeSnippets: ArticleContentViewModelForCodeSnippets
    private lateinit var adapter: ArticleContentAdapter
    private lateinit var gottenArticle: Article
    private lateinit var recyclerView: RecyclerView

    /**
     * Переменные для наблюдения и объединения разного вида контента.
     * */
    private var articleContentMediator = MediatorLiveData<List<Any>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gottenArticle = arguments!!.getParcelable(ArticlesFragment.articleKey)!!

        val view: View = inflater.inflate(R.layout.article_content_fragment, container, false)
        recyclerView = view.findViewById(R.id.articlePiecesRecyclerView)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModelForHeadersForHeaders = ViewModelProvider(this, viewModelProviderFactory)
            .get(ArticleContentViewModelForHeaders::class.java)

        viewModelForHeadersForParagraphs = ViewModelProvider(this, viewModelProviderFactory)
            .get(ArticleContentViewModelForParagraphs::class.java)

        viewModelForHeadersForCodeSnippets = ViewModelProvider(this, viewModelProviderFactory)
            .get(ArticleContentViewModelForCodeSnippets::class.java)

        adapter = ArticleContentAdapter(requireContext())

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setItemViewCacheSize(RECYCLER_VIEW_CACHE_SIZE)

        // Запрос на сервер содержимого данной статьи.
        viewModelForHeadersForHeaders.makeRequestForElements(gottenArticle.idFromServer)
        viewModelForHeadersForParagraphs.makeRequestForElements(gottenArticle.idFromServer)
        viewModelForHeadersForCodeSnippets.makeRequestForElements(gottenArticle.idFromServer)

        addSourcesToMediator()
        observeMediator()
    }

    /**
     * Добавляет в медиатор источники данных - элементы статьи.
     * Данные фильтруются из списка всех элементов по id статьи.
     * */
    private fun addSourcesToMediator() {
        articleContentMediator.addSource(viewModelForHeadersForHeaders.allArticlesHeaders, Observer { allHeaders ->
            val filteredHeaders = ArrayList<ArticleHeader>()
            for (header in allHeaders) {
                if (header.articleIdFromServer == gottenArticle.idFromServer) {
                    filteredHeaders.add(header)
                }
            }
            articleContentMediator.value = filteredHeaders
        })

        articleContentMediator.addSource(viewModelForHeadersForParagraphs.allArticlesParagraphs, Observer { allParagraphs ->
            val filteredParagraphs = ArrayList<ArticleParagraph>()
            for (paragraph in allParagraphs) {
                if (paragraph.articleIdFromServer == gottenArticle.idFromServer) {
                    filteredParagraphs.add(paragraph)
                }
            }
            articleContentMediator.value = filteredParagraphs
        })

        articleContentMediator.addSource(viewModelForHeadersForCodeSnippets.allArticlesCodeSnippets, Observer { allCodeSnippets ->
            val filteredCodeSnippets = ArrayList<ArticleCodeSnippet>()
            for (codeSnippet in allCodeSnippets) {
                if (codeSnippet.articleIdFromServer == gottenArticle.idFromServer) {
                    filteredCodeSnippets.add(codeSnippet)
                }
            }
            articleContentMediator.value = filteredCodeSnippets
        })
    }

    /**
     * Устанавливает наблюдатель за медиатором.
     * */
    private fun observeMediator() {
        articleContentMediator.observe(viewLifecycleOwner, Observer { articlePieces ->
            val filteredPieces = ArrayList<ArticlePiece>()

            if(articlePieces.isNotEmpty()) {
                for (piece in articlePieces) {
                    if (piece is ArticlePiece) {
                        filteredPieces.add(piece)
                    }
                }

            }
            val piecesSortedByPosition = filteredPieces.sortedBy { piece -> piece.positionInArticle }
            adapter.addArticlePieces(piecesSortedByPosition)
        })
    }
}
