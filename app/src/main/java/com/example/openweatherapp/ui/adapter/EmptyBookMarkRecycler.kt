package com.example.openweatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.example.openweatherapp.R
import com.example.openweatherapp.databinding.EmptyBookmarkedLayoutBinding

class EmptyBookMarkRecycler: RecyclerView.Adapter<EmptyBookMarkRecycler.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = Unit
    override fun getItemCount() = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EmptyBookmarkedLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding.root)
    }

}