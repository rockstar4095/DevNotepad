package com.example.devnotepad.ui.fragment_article_content

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
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

        var wasFragmentAttached: MutableLiveData<Boolean> = MutableLiveData()
    }

    private lateinit var viewModelForHeaders: ArticleContentViewModelForHeaders
    private lateinit var viewModelForParagraphs: ArticleContentViewModelForParagraphs
    private lateinit var viewModelForCodeSnippets: ArticleContentViewModelForCodeSnippets
    private lateinit var viewModelForImages: ArticleContentViewModelForImages
    private lateinit var adapter: ArticleContentAdapter
    private lateinit var gottenArticle: Article
    private lateinit var recyclerView: RecyclerView

    private var articleContentMediator = MediatorLiveData<List<Any>>()
    /**TODO: temp name*/
    private val webViewsHashMapArrayList: ArrayList<HashMap<String, Int>> = ArrayList()

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

        viewModelForHeaders = ViewModelProvider(this, viewModelProviderFactory)
            .get(ArticleContentViewModelForHeaders::class.java)

        viewModelForParagraphs = ViewModelProvider(this, viewModelProviderFactory)
            .get(ArticleContentViewModelForParagraphs::class.java)

        viewModelForCodeSnippets = ViewModelProvider(this, viewModelProviderFactory)
            .get(ArticleContentViewModelForCodeSnippets::class.java)

        viewModelForImages = ViewModelProvider(this, viewModelProviderFactory)
            .get(ArticleContentViewModelForImages::class.java)

        adapter = ArticleContentAdapter(requireContext())

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setItemViewCacheSize(RECYCLER_VIEW_CACHE_SIZE)

        // Запрос на сервер содержимого данной статьи.
        viewModelForHeaders.makeRequestForElements(gottenArticle.idFromServer)
        viewModelForParagraphs.makeRequestForElements(gottenArticle.idFromServer)
        viewModelForCodeSnippets.makeRequestForElements(gottenArticle.idFromServer)
        viewModelForImages.makeRequestForElements(gottenArticle.idFromServer)

        addSourcesToMediator()
        observeMediator()
        observeWebViewsHeight()
    }

    /**
     * Добавляет в медиатор источники данных - элементы статьи.
     * Данные фильтруются из списка всех элементов по id статьи.
     * */
    private fun addSourcesToMediator() {
        articleContentMediator.addSource(viewModelForHeaders.allArticlesHeaders, Observer { allHeaders ->
            val filteredHeaders = ArrayList<ArticleHeader>()
            for (header in allHeaders) {
                if (header.articleIdFromServer == gottenArticle.idFromServer) {
                    filteredHeaders.add(header)
                }
            }
            articleContentMediator.value = filteredHeaders
        })

        articleContentMediator.addSource(viewModelForParagraphs.allArticlesParagraphs, Observer { allParagraphs ->
            val filteredParagraphs = ArrayList<ArticleParagraph>()
            for (paragraph in allParagraphs) {
                if (paragraph.articleIdFromServer == gottenArticle.idFromServer) {
                    filteredParagraphs.add(paragraph)
                }
            }
            articleContentMediator.value = filteredParagraphs
        })

        articleContentMediator.addSource(viewModelForCodeSnippets.allArticlesCodeSnippets, Observer { allCodeSnippets ->
            val filteredCodeSnippets = ArrayList<ArticleCodeSnippet>()
            for (codeSnippet in allCodeSnippets) {
                if (codeSnippet.articleIdFromServer == gottenArticle.idFromServer) {
                    filteredCodeSnippets.add(codeSnippet)
                }
            }
            articleContentMediator.value = filteredCodeSnippets
        })

        articleContentMediator.addSource(viewModelForImages.allArticlesImages, Observer { allImages ->
            val filteredImages = ArrayList<ArticleImage>()
            for (image in allImages) {
                if (image.articleIdFromServer == gottenArticle.idFromServer) {
                    filteredImages.add(image)
                }
            }
            articleContentMediator.value = filteredImages
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

    private fun observeWebViewsHeight() {
        CodeSnippetController.webViewHeightIdHashMap.observe(viewLifecycleOwner, Observer {
            if (!webViewsHashMapArrayList.contains(it)) {
                webViewsHashMapArrayList.add(it)
            }
        })
    }

    /**TODO: refactor*/
    override fun onPause() {
        super.onPause()
        for (hashMap in webViewsHashMapArrayList) {
            viewModelForCodeSnippets.updateWebViewHeight(
                hashMap[CodeSnippetController.KEY_HEIGHT]!!,
                hashMap[CodeSnippetController.KEY_ID]!!
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        wasFragmentAttached.postValue(true)
    }

    override fun onDetach() {
        super.onDetach()
        wasFragmentAttached.postValue(false)
    }
}
