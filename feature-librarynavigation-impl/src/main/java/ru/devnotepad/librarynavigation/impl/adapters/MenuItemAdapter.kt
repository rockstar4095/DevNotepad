package ru.devnotepad.librarynavigation.impl.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.devnotepad.librarynavigation.impl.databinding.MenuItemBinding
import ru.devnotepad.librarynavigation.impl.entities.NestedMenuItem

class MenuItemAdapter(context: Context) : RecyclerView.Adapter<MenuItemViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)
    private val menuItems = ArrayList<NestedMenuItem>()

    fun setMenuItems(menuItems: List<NestedMenuItem>) {
        this.menuItems.addAll(menuItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder =
        getMenuItemViewHolder(parent)

    private fun getMenuItemViewHolder(parent: ViewGroup): MenuItemViewHolder =
        MenuItemViewHolder(MenuItemBinding.inflate(layoutInflater, parent, false))

    override fun getItemCount(): Int = menuItems.size

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        holder.bindMenuItem(menuItems[position])
    }
}