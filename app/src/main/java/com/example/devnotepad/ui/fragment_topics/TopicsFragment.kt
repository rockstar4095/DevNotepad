package com.example.devnotepad.ui.fragment_topics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.devnotepad.R
import com.example.devnotepad.StructureData
import com.example.devnotepad.Topic
import com.example.devnotepad.ui.OnItemClickListener
import com.example.devnotepad.ui.ViewModelProviderFactory
import com.example.devnotepad.ui.fragment_articles.ArticlesFragment
import com.example.devnotepad.ui.fragment_directions.DirectionsFragment
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.topics_fragment.*
import javax.inject.Inject

class TopicsFragment : DaggerFragment(),
    OnItemClickListener {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    companion object {
        fun newInstance() = TopicsFragment()

        const val topicKey = "topic"
    }

    private lateinit var viewModel: TopicsViewModel
    private var gottenDirectionId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gottenDirectionId = arguments!!.getInt(DirectionsFragment.directionKey)

        return inflater.inflate(R.layout.topics_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(TopicsViewModel::class.java)
        val adapter = TopicsAdapter(requireContext(), this)

        topicsRecyclerView.adapter = adapter
        topicsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.repositoryForStructureData.makeRequestForElements()
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
            if (topic.directionIdFromServer == gottenDirectionId) {
                sortedTopics.add(topic)
            }
        }

        return sortedTopics
    }

    private fun openArticlesFragment(topic: Topic) {
        val articlesFragment: Fragment = ArticlesFragment()

        val bundle = Bundle()
        bundle.putInt(topicKey, topic.idFromServer)
        articlesFragment.arguments = bundle

        activity?.supportFragmentManager?.beginTransaction()
            ?.setCustomAnimations(
                R.anim.slide_in_from_right,
                R.anim.slide_out_to_left,
                R.anim.slide_in_from_left,
                R.anim.slide_out_to_right)

            ?.replace(R.id.fragmentContainer, articlesFragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun onItemClick(structureData: StructureData) {
        openArticlesFragment(structureData as Topic)
    }
}