package com.example.devnotepad.ui.fragment_directions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.devnotepad.DirectionOfStudy
import com.example.devnotepad.R
import kotlinx.android.synthetic.main.direction_item.view.*

class DirectionsAdapter internal constructor(
    context: Context,
    private val onDirectionClickListener: OnDirectionClickListener
) : RecyclerView.Adapter<DirectionsAdapter.DirectionsViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var directions = emptyList<DirectionOfStudy>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectionsViewHolder {
        val itemView = inflater.inflate(R.layout.direction_item, parent, false)
        return DirectionsViewHolder(itemView)
    }

    override fun getItemCount() = directions.size

    override fun onBindViewHolder(holder: DirectionsViewHolder, position: Int) {
        val currentItem = directions[position]
        holder.txtDirection.text = currentItem.name

        setDirectionLogo(currentItem, holder)

        holder.bind(currentItem, onDirectionClickListener)
    }

    private fun setDirectionLogo(currentItem: DirectionOfStudy, holder: DirectionsViewHolder) {
        when(currentItem.idFromServer) {
            6 -> holder.imgDirection.setImageResource(R.mipmap.android_logo)
            2 -> holder.imgDirection.setImageResource(R.mipmap.java_logo)
            3 -> holder.imgDirection.setImageResource(R.mipmap.kotlin_logo)
        }
    }

    internal fun setDirections(directions: List<DirectionOfStudy>) {
        this.directions = directions
        notifyDataSetChanged()
    }

    inner class DirectionsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val txtDirection: TextView = itemView.txtDirection
        val imgDirection: ImageView = itemView.imgDirection

        fun bind(
            directionOfStudy: DirectionOfStudy,
            onDirectionClickListener: OnDirectionClickListener
        ) {
            itemView.setOnClickListener {
                onDirectionClickListener.onDirectionClick(directionOfStudy)
            }
        }
    }
}