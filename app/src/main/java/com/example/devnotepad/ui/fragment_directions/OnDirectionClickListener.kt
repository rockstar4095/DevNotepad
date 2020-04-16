package com.example.devnotepad.ui.fragment_directions

import com.example.devnotepad.DirectionOfStudy

/**
 * Слушатель кликов по элементам списка.
 * */
interface OnDirectionClickListener {
    fun onDirectionClick(directionOfStudy: DirectionOfStudy)
}