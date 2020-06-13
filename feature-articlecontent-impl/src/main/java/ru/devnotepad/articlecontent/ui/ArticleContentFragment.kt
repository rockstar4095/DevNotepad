package ru.devnotepad.articlecontent.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.devnotepad.articlecontent.adapters.ArticleContentAdapter
import ru.devnotepad.articlecontent.databinding.ArticleContentFragmentBinding

class ArticleContentFragment : Fragment() {

    companion object {
        private const val RECYCLER_VIEW_CACHE_SIZE = 64
    }

    // TODO: create factory for viewModel.
    // TODO: viewModel should be initialized with articleId as a parameter.
    private val viewModel: ArticleContentViewModel by viewModels()
    private lateinit var binding: ArticleContentFragmentBinding
    private val articleContentAdapter by lazy {
        ArticleContentAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ArticleContentFragmentBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}