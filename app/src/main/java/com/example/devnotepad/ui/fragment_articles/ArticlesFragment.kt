package com.example.devnotepad.ui.fragment_articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devnotepad.Article
import com.example.devnotepad.R
import com.example.devnotepad.StructureData
import com.example.devnotepad.ui.OnItemClickListener
import com.example.devnotepad.ui.ViewModelProviderFactory
import com.example.devnotepad.ui.fragment_topics.TopicsFragment
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.articles_fragment.*
import javax.inject.Inject

class ArticlesFragment : DaggerFragment(),
    OnItemClickListener {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    companion object {
        fun newInstance() = ArticlesFragment()

        const val articleKey = "article"
    }

    private lateinit var viewModel: ArticlesViewModel
    private var gottenTopicId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gottenTopicId = arguments!!.getInt(TopicsFragment.topicKey)

        return inflater.inflate(R.layout.articles_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(ArticlesViewModel::class.java)
        val adapter = ArticlesAdapter(requireContext(), this)

        articlesRecyclerView.adapter = adapter
        articlesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.repositoryForStructureData.makeRequestForElements()
        viewModel.allArticles.observe(viewLifecycleOwner, Observer { articles ->
            articles?.let { adapter.setArticles(sortArticlesByTopic(it)) }
        })
    }

    /**
     * Сортирует и возвращает статьи по направлению.
     * */
    private fun sortArticlesByTopic(articles: List<Article>): List<Article> {
        val sortedArticles: ArrayList<Article> = ArrayList()

        for (article in articles) {
            if (article.topicIdFromServer == gottenTopicId) {
                sortedArticles.add(article)
            }
        }

        return sortedArticles
    }

//    private fun openArticleContentFragment(article: Article) {
//        val articleContentFragment: Fragment = ArticleContentFragment()
//
//        val bundle = Bundle()
//        bundle.putParcelable(articleKey, article)
//        articleContentFragment.arguments = bundle
//
//        activity?.supportFragmentManager?.beginTransaction()
//            ?.setCustomAnimations(
////                R.anim.slide_in_from_right,
//                0,
//                R.anim.slide_out_to_left,
//                R.anim.slide_in_from_left,
//                R.anim.slide_out_to_right)
//
//            ?.replace(R.id.fragmentContainer, articleContentFragment)
//            ?.addToBackStack(null)
//            ?.commit()
//    }

    override fun onItemClick(structureData: StructureData) {
//        openArticleContentFragment(structureData as Article)
    }
}