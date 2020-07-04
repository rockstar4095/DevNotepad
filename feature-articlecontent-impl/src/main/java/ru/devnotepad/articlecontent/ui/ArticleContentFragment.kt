package ru.devnotepad.articlecontent.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import ru.devnotepad.articlecontent.adapters.ArticlePiecesAdapter
import ru.devnotepad.articlecontent.databinding.ArticleContentFragmentBinding
import ru.devnotepad.articlecontent.di.ComponentHolder
import ru.devnotepad.articlecontent.entities.ArticlePiece
import javax.inject.Inject

class ArticleContentFragment : Fragment() {

    companion object {
        private const val RECYCLER_VIEW_CACHE_SIZE = 64
        const val TEMP_ARTICLE_ID = 13
    }

    // TODO: create factory for viewModel.
    // TODO: viewModel should be initialized with articleId as a parameter.

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ArticleContentViewModel by viewModels { viewModelFactory }
    private lateinit var binding: ArticleContentFragmentBinding
    private val articleContentAdapter by lazy {
        ArticlePiecesAdapter(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearComponent()
    }

    private fun initComponent(): Unit = ComponentHolder.initComponent(this)
    private fun clearComponent(): Unit = ComponentHolder.clearComponent()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ArticleContentFragmentBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        observeViewModel()
    }

    private fun initRecycler() = with(binding.recycler) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = articleContentAdapter
    }

    private fun observeViewModel() = with(viewModel) {
        articlePiecesLiveData(TEMP_ARTICLE_ID).observe(viewLifecycleOwner) {
            populateArticleContentAdapter(it)
        }
    }

    private fun populateArticleContentAdapter(articlePieces: List<ArticlePiece>) {
        articleContentAdapter.setArticlePieces(articlePieces)
    }
}