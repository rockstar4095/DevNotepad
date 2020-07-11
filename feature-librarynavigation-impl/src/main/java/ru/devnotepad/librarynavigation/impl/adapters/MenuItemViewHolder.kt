package ru.devnotepad.librarynavigation.impl.adapters

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.devnotepad.librarynavigation.impl.databinding.MenuItemBinding
import ru.devnotepad.librarynavigation.impl.entities.NestedMenuItem
import ru.devnotepad.librarynavigation.impl.ui.OnMenuItemClickListener

class MenuItemViewHolder(
    binding: MenuItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val menuItemTextView: TextView = binding.menuItemTextView

    fun bindMenuItem(menuItem: NestedMenuItem, onMenuItemClickListener: OnMenuItemClickListener) {
        menuItemTextView.text = menuItem.name
        menuItemTextView.setOnClickListener { onMenuItemClickListener.onMenuItemClick(menuItem) }
    }
}