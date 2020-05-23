package com.example.devnotepad.ui.fragment_article_content

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devnotepad.*
import com.example.devnotepad.R
import com.example.devnotepad.data.data_handlers.HandlerForContentData
import com.example.devnotepad.ui.ViewModelProviderFactory

import com.example.devnotepad.ui.fragment_articles.ArticlesFragment
import com.example.devnotepad.utils.InternetConnectionChecker
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.article_content_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.schedule

class ArticleContentFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    companion object {
        private const val RECYCLER_VIEW_CACHE_SIZE = 64

        var wasFragmentAttached: MutableLiveData<Boolean> = MutableLiveData()
        const val KEY_HEIGHT_FOR_DYNAMIC_VIEWS = "height"
        const val KEY_ID_FOR_DYNAMIC_VIEWS = "id"
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
        viewModelForHeaders.repositoryForArticlesContent.makeRequestForElements(gottenArticle.idFromServer)
        viewModelForParagraphs.repositoryForArticlesContent.makeRequestForElements(gottenArticle.idFromServer)
        viewModelForCodeSnippets.repositoryForArticlesContent.makeRequestForElements(gottenArticle.idFromServer)
        viewModelForImages.repositoryForArticlesContent.makeRequestForElements(gottenArticle.idFromServer)

        addSourcesToMediator()
        observeDynamicViewsHeight()
        observeMediator()
        observeBasicContentLoadingStatus()
        observeInternetConnection()
    }

    private fun observeBasicContentLoadingStatus() {
        HandlerForContentData.wasBasicContentLoadedLiveData.observe(
            viewLifecycleOwner,
            Observer { wasBasicContentLoaded ->
                if (wasBasicContentLoaded) {
                    hideProgressBar()
                    showRecyclerView()
                }
            })
    }

    /**TODO: create Interface for this method*/
    private fun observeInternetConnection() {
        InternetConnectionChecker.isInternetConnectedLiveData.observe(
            viewLifecycleOwner, Observer {
                if (!it) {
                    Timer().schedule(300) {
                        CoroutineScope(Dispatchers.Main).launch {
                            hideProgressBar()
                            showRecyclerView()
                        }
                    }
                }
            })
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showRecyclerView() {
        recyclerView.animate().alpha(1f).duration = 300
    }

    private fun removeObservation() {
        HandlerForContentData.wasBasicContentLoadedLiveData = MutableLiveData()
        articleContentMediator.removeObservers(this)
        articleContentMediator.removeSource(viewModelForHeaders.allArticlesHeaders)
        articleContentMediator.removeSource(viewModelForParagraphs.allArticlesParagraphs)
        articleContentMediator.removeSource(viewModelForCodeSnippets.allArticlesCodeSnippets)
        articleContentMediator.removeSource(viewModelForImages.allArticlesImages)
        CodeSnippetController.webViewHeightIdHashMap.removeObservers(this)
    }

    /**
     * Добавляет в медиатор источники данных - элементы статьи.
     * Данные фильтруются из списка всех элементов по id статьи.
     * */
    private fun addSourcesToMediator() {
        articleContentMediator.addSource(viewModelForHeaders.allArticlesHeaders) { allHeaders ->
            val filteredHeaders = ArrayList<ArticleHeader>()
            for (header in allHeaders) {
                if (header.articleIdFromServer == gottenArticle.idFromServer) {
                    filteredHeaders.add(header)
                }
            }
            articleContentMediator.value = filteredHeaders
        }

        articleContentMediator.addSource(viewModelForParagraphs.allArticlesParagraphs) { allParagraphs ->
            val filteredParagraphs = ArrayList<ArticleParagraph>()
            for (paragraph in allParagraphs) {
                if (paragraph.articleIdFromServer == gottenArticle.idFromServer) {
                    filteredParagraphs.add(paragraph)
                }
            }
            articleContentMediator.value = filteredParagraphs
        }

        articleContentMediator.addSource(viewModelForCodeSnippets.allArticlesCodeSnippets) { allCodeSnippets ->
            val filteredCodeSnippets = ArrayList<ArticleCodeSnippet>()
            for (codeSnippet in allCodeSnippets) {
                if (codeSnippet.articleIdFromServer == gottenArticle.idFromServer) {
                    filteredCodeSnippets.add(codeSnippet)
                }
            }
            articleContentMediator.value = filteredCodeSnippets
        }

        articleContentMediator.addSource(viewModelForImages.allArticlesImages) { allImages ->
            val filteredImages = ArrayList<ArticleImage>()
            for (image in allImages) {
                if (image.articleIdFromServer == gottenArticle.idFromServer) {
                    filteredImages.add(image)
                }
            }
            articleContentMediator.value = filteredImages
        }
    }

    /**
     * Устанавливает наблюдатель за медиатором.
     * */
    private fun observeMediator() {
        articleContentMediator.observe(viewLifecycleOwner, Observer { articlePieces ->
            val filteredPieces = ArrayList<ArticlePiece>()

            if (articlePieces.isNotEmpty()) {
                for (piece in articlePieces) {
                    if (piece is ArticlePiece) {
                        filteredPieces.add(piece)
                    }
                }
            }

            /**TODO: probably, not to sort here.*/
            val piecesSortedByPosition =
                filteredPieces.sortedBy { piece -> piece.positionInArticle }
            adapter.addArticlePieces(piecesSortedByPosition)
        })
    }

    private fun observeDynamicViewsHeight() {
        CodeSnippetController.webViewHeightIdHashMap.observe(viewLifecycleOwner, Observer {
            if (!webViewsHashMapArrayList.contains(it)) {
                println("debug: observeDynamicViewsHeight: height was added")
                webViewsHashMapArrayList.add(it)
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        wasFragmentAttached.postValue(true)
    }

    override fun onDetach() {
        super.onDetach()
        wasFragmentAttached.postValue(false)
        updateDynamicViewsHeight()

        removeObservation()
        HandlerForContentData.resetWasBasicContentLoadedStatus()
        HandlerForContentData.resetWereCodeSnippetsLoadedStatus()
    }

    override fun onDestroy() {
        super.onDestroy()

        recyclerView.adapter = null
    }

    /**TODO: relocate to viewModel and make suitable for another dynamic types*/
    private fun updateDynamicViewsHeight() {
        for (hashMap in webViewsHashMapArrayList) {
            viewModelForCodeSnippets.updateViewHeight(
                hashMap[KEY_HEIGHT_FOR_DYNAMIC_VIEWS]!!,
                hashMap[KEY_ID_FOR_DYNAMIC_VIEWS]!!
            )

            println("debug: webViewMeasuredHeight = ${hashMap[KEY_HEIGHT_FOR_DYNAMIC_VIEWS]!!} from fragment")
        }
    }
}
