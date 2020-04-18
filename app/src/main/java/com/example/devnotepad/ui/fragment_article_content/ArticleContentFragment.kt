package com.example.devnotepad.ui.fragment_article_content

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.devnotepad.Article
import com.example.devnotepad.ArticleHeader

import com.example.devnotepad.R
import com.example.devnotepad.ui.fragment_articles.ArticlesFragment
import kotlinx.android.synthetic.main.article_content_fragment.view.*

class ArticleContentFragment : Fragment() {

    companion object {
        fun newInstance() = ArticleContentFragment()
    }

    private lateinit var viewModel: ArticleContentViewModel
    private lateinit var gottenArticle: Article
    private val articlePieces: MediatorLiveData<List<Any>> = MediatorLiveData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gottenArticle = arguments!!.getParcelable(ArticlesFragment.articleKey)!!

        val v: View = inflater.inflate(R.layout.article_content_fragment, container, false)
        v.txtArticleContent.text = gottenArticle.text

        var i = 0;
        v.button.setOnClickListener(View.OnClickListener {
            viewModel.insertHeader(ArticleHeader(i, i, "attempt $i"))
            i++
        })

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val myViewModelFactory = MyViewModelFactory(activity!!.application, gottenArticle)
        viewModel = ViewModelProvider(this, myViewModelFactory).get(ArticleContentViewModel::class.java)

        viewModel.makeRequestForContent()
        viewModel.allArticleHeaders.observe(viewLifecycleOwner, Observer {
            println("debug: size: ${it.size}")
        })

//        articlePieces.addSource(viewModel.allArticleHeaders, Observer {
//            articlePieces.value = it
//            for (any in it){
//                println("debug: $any")
//            }
//        })
//
//        articlePieces.addSource(viewModel.allArticleParagraphs, Observer {
//            articlePieces.value = it
//            for (any in it){
//                println("debug: $any")
//            }
//        })
//
//        articlePieces.observe(viewLifecycleOwner, Observer {
//            for (any in it){
//                println("debug: $any")
//            }
//        })
    }
}
