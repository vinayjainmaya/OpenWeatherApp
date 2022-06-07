package com.example.openweatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.openweatherapp.R
import com.example.openweatherapp.databinding.BookmarkedLocationItemLayoutBinding

/**
 * Adapter class to show bookmarked location
 *
 * @param listener on item click listener
 *
 */
class BookmarkedLocationAdapter(private val listener: OnItemClickListener)
: RecyclerView.Adapter<BookmarkedLocationAdapter.BookmarkViewHolder>() {

    private var items: List<String> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookmarkedLocationAdapter.BookmarkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookmarked_location_item_layout, parent, false)
        val binding by lazy { BookmarkedLocationItemLayoutBinding.bind(view) }

        return BookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val item = items[position]
        holder.binding.text.text = item
    }

    override fun getItemCount() = items.size

    fun setItem(i: List<String>) {
        items = i
    }

    inner class BookmarkViewHolder(val binding: BookmarkedLocationItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.rootLayout.setOnClickListener {
                listener.onItemClick(items[adapterPosition])
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(s: String)
    }

}