package com.example.devnotepad.ui.fragment_articles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devnotepad.Article

import com.example.devnotepad.R
import com.example.devnotepad.ui.fragment_topics.TopicsFragment
import kotlinx.android.synthetic.main.articles_fragment.*

class ArticlesFragment : Fragment(),
    OnArticleClickListener {

    companion object {
        fun newInstance() = ArticlesFragment()
    }

    private lateinit var viewModel: ArticlesViewModel
    private lateinit var gottenTopic: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gottenTopic = arguments!!.getString(TopicsFragment.topicKey)!!

        return inflater.inflate(R.layout.articles_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ArticlesViewModel::class.java)

        viewModel = ViewModelProvider(this).get(ArticlesViewModel::class.java)
        val adapter = ArticlesAdapter(requireContext(), this)

        articlesRecyclerView.adapter = adapter
        articlesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.makeRequestForArticles()
        viewModel.allArticles.observe(viewLifecycleOwner, Observer { articles ->
            articles?.let { adapter.setArticles(sortArticlesByTopic(it)) }
        })
    }

    /**
     * Сортирует и возвращает темы по направлению.
     * */
    private fun sortArticlesByTopic(articles: List<Article>): List<Article> {
        val sortedArticles: ArrayList<Article> = ArrayList()

        for (article in articles) {
            if (article.topic == gottenTopic) {
                sortedArticles.add(article)
            }
        }

        return sortedArticles
    }

    override fun onArticleClick(article: Article) {
        Toast.makeText(requireContext(), article.name, Toast.LENGTH_SHORT).show()
    }
}