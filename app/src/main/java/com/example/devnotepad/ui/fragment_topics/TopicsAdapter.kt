package com.example.devnotepad.ui.fragment_topics

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.devnotepad.R
import com.example.devnotepad.Topic
import kotlinx.android.synthetic.main.topic_item.view.*

class TopicsAdapter internal constructor(
    context: Context,
    private val onTopicClickListener: OnTopicClickListener
) : RecyclerView.Adapter<TopicsAdapter.TopicsViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var topics = emptyList<Topic>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicsViewHolder {
        val itemView = inflater.inflate(R.layout.topic_item, parent, false)
        return TopicsViewHolder(itemView)
    }

    override fun getItemCount() = topics.size

    override fun onBindViewHolder(holder: TopicsViewHolder, position: Int) {
        val current = topics[position]
        holder.topicItemView.text = current.name
        holder.bind(current, onTopicClickListener)
    }

    internal fun setTopics(topics: List<Topic>) {
        this.topics = topics
        notifyDataSetChanged()
    }

    inner class TopicsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val topicItemView: TextView = itemView.textView

        fun bind(
            topic: Topic,
            onTopicClickListener: OnTopicClickListener
        ) {
            itemView.setOnClickListener {
                onTopicClickListener.onTopicClick(topic)
            }
        }
    }
}