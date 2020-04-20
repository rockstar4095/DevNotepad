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

import com.example.devnotepad.ui.fragment_articles.ArticlesFragment

class ArticleContentFragment : Fragment() {

    companion object {
        fun newInstance() = ArticleContentFragment()
    }

    private lateinit var viewModel: ArticleContentViewModel
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

        viewModel = ViewModelProvider(this).get(ArticleContentViewModel::class.java)
        adapter = ArticleContentAdapter(requireContext())

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Запрос на сервер содержимого данной статьи.
        viewModel.makeRequestForContent(gottenArticle.idFromServer)

        addSourcesToMediator()
        observeMediator()
    }

    /**
     * Добавляет в медиатор источники данных - элементы статьи.
     * Данные фильтруются из списка всех элементов по id статьи.
     * */
    private fun addSourcesToMediator() {
        articleContentMediator.addSource(viewModel.allArticlesHeaders, Observer { allHeaders ->
            val filteredHeaders = ArrayList<ArticleHeader>()
            for (header in allHeaders) {
                if (header.articleIdFromServer == gottenArticle.idFromServer) {
                    filteredHeaders.add(header)
                }
            }
            articleContentMediator.value = filteredHeaders
        })

        articleContentMediator.addSource(viewModel.allArticlesParagraphs, Observer { allParagraphs ->
            val filteredParagraphs = ArrayList<ArticleParagraph>()
            for (paragraph in allParagraphs) {
                if (paragraph.articleIdFromServer == gottenArticle.idFromServer) {
                    filteredParagraphs.add(paragraph)
                }
            }
            articleContentMediator.value = filteredParagraphs
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
