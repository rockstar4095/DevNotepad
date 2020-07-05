package ru.devnotepad.librarynavigation.impl.adapters

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.devnotepad.librarynavigation.impl.databinding.MenuItemBinding
import ru.devnotepad.librarynavigation.impl.entities.NestedMenuItem

class MenuItemViewHolder(
    binding: MenuItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val menuItemTextView: TextView = binding.menuItemTextView

    fun bindMenuItem(menuItem: NestedMenuItem) {
        menuItemTextView.text = menuItem.name
    }
}