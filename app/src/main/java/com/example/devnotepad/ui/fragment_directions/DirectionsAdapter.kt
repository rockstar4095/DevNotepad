package com.example.devnotepad.ui.fragment_directions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.devnotepad.DirectionOfStudy
import com.example.devnotepad.R

class DirectionsAdapter internal constructor(
    context: Context,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<DirectionsAdapter.DirectionsViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    // cached copy of directions
    private var directions = emptyList<DirectionOfStudy>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectionsViewHolder {
        val itemView = inflater.inflate(R.layout.directions_item, parent, false)
        return DirectionsViewHolder(itemView)
    }

    override fun getItemCount() = directions.size

    override fun onBindViewHolder(holder: DirectionsViewHolder, position: Int) {
        val current = directions[position]
        holder.directionItemView.text = current.name
        holder.bind(current, onItemClickListener)
    }

    internal fun setArticles(directions: List<DirectionOfStudy>) {
        this.directions = directions
        notifyDataSetChanged()
    }

    inner class DirectionsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val directionItemView: TextView = itemView.findViewById(R.id.textView)

        fun bind(directionOfStudy: DirectionOfStudy, onItemClickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(directionOfStudy)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(directionOfStudy: DirectionOfStudy)
    }
}