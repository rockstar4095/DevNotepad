package com.example.devnotepad.ui

import com.example.devnotepad.StructureData

/**
 * Слушатель кликов по структурным элементам списка.
 * */
interface OnItemClickListener {
    fun onItemClick(structureData: StructureData)
}