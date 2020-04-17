package com.example.devnotepad.ui.fragment_article_content

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.devnotepad.Article

import com.example.devnotepad.R
import com.example.devnotepad.ui.fragment_articles.ArticlesFragment
import kotlinx.android.synthetic.main.article_content_fragment.view.*

class ArticleContentFragment : Fragment() {

    companion object {
        fun newInstance() = ArticleContentFragment()
    }

    private lateinit var viewModel: ArticleContentViewModel
    private lateinit var gottenArticle: Article

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gottenArticle = arguments!!.getParcelable(ArticlesFragment.articleKey)!!

        val v: View = inflater.inflate(R.layout.article_content_fragment, container, false)

        v.txtArticleContent.text = gottenArticle.text

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ArticleContentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
