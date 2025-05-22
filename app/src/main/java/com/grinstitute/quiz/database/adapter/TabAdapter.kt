package com.grinstitute.quiz.database.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.grinstitute.quiz.R
import com.grinstitute.quiz.database.model.Question
import com.grinstitute.quiz.databinding.ItemTabBinding

class TabAdapter(
    private val tabList: ArrayList<Question>, // can be numbers or category names
    private val onTabClick: (Int) -> Unit
) : RecyclerView.Adapter<TabAdapter.TabViewHolder>() {

    private var selectedPosition = 0

    inner class TabViewHolder(private val binding: ItemTabBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tabText: Question, position: Int) {
            binding.tabTitle.text = (position+1).toString()

            // Highlight selected tab
            binding.tabCard.strokeWidth = if (position == selectedPosition) 4 else 0
            binding.tabCard.strokeColor = if (position == selectedPosition)
                ContextCompat.getColor(binding.root.context, R.color.quiz_primary)
            else
                Color.TRANSPARENT

            binding.root.setOnClickListener {
                val previousSelected = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)
                onTabClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        val binding = ItemTabBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TabViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        holder.bind(tabList[position], position)
    }

    override fun getItemCount(): Int = tabList.size

    fun setSelected(position: Int) {
        val previous = selectedPosition
        selectedPosition = position
        notifyItemChanged(previous)
        notifyItemChanged(position)
    }
}
