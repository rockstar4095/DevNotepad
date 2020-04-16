package com.example.devnotepad.ui.fragment_topics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.devnotepad.R
import com.example.devnotepad.Topic
import com.example.devnotepad.ui.fragment_articles.ArticlesFragment
import com.example.devnotepad.ui.fragment_directions.DirectionsFragment
import kotlinx.android.synthetic.main.topics_fragment.*

class TopicsFragment : Fragment(),
    OnTopicClickListener {

    companion object {
        fun newInstance() = TopicsFragment()

        const val topicKey = "topic"
    }

    private lateinit var viewModel: TopicsViewModel
    private lateinit var gottenDirection: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gottenDirection = arguments!!.getString(DirectionsFragment.directionKey)!!

        return inflater.inflate(R.layout.topics_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(TopicsViewModel::class.java)
        val adapter = TopicsAdapter(requireContext(), this)

        topicsRecyclerView.adapter = adapter
        topicsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.makeRequestForTopics()
        viewModel.allTopics.observe(viewLifecycleOwner, Observer { topics ->
            topics?.let { adapter.setTopics(sortTopicsByDirection(it)) }
        })
    }

    /**
     * Сортирует и возвращает темы по направлению.
     * */
    private fun sortTopicsByDirection(topics: List<Topic>): List<Topic> {
        val sortedTopics: ArrayList<Topic> = ArrayList()

        for (topic in topics) {
            if (topic.directionOfStudy == gottenDirection) {
                sortedTopics.add(topic)
            }
        }

        return sortedTopics
    }

    private fun openArticlesFragment(topic: Topic) {
        val articlesFragment: Fragment = ArticlesFragment()

        val bundle = Bundle()
        bundle.putString(topicKey, topic.name)
        articlesFragment.arguments = bundle

        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragmentContainer, articlesFragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun onTopicClick(topic: Topic) {
        openArticlesFragment(topic)
    }
}