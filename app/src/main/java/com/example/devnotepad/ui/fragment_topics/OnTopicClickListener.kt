package com.example.devnotepad.ui.fragment_topics

import com.example.devnotepad.Topic

/**
 * Слушатель кликов по элементам списка.
 * */
interface OnTopicClickListener {
    fun onTopicClick(topic: Topic)
}