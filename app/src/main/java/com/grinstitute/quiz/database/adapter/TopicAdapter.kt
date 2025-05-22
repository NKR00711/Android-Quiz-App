package com.grinstitute.quiz.database.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.grinstitute.quiz.R
import com.grinstitute.quiz.database.model.Category
import com.grinstitute.quiz.databinding.CategoryItemBinding
import com.grinstitute.quiz.databinding.CategoryListBinding
import com.grinstitute.quiz.frag.SelectionHome
import com.grinstitute.quiz.frag.Topic

class TopicAdapter(private val fragment: Topic, private val topicList: ArrayList<Category>) : RecyclerView.Adapter<TopicAdapter.ViewHolder>() {
    private lateinit var binding: CategoryListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CategoryListBinding.inflate(fragment.layoutInflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return topicList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(topicList.isNotEmpty()){
            holder.bind(topicList[position])
            binding.category.setOnClickListener {
                if(fragment.type == 3){
                    holder.testSelect.toggle()
                    return@setOnClickListener
                }
                val fragment = SelectionHome.newInstance(topicList[position].name,topicList[position].image)
                this.fragment.parentFragmentManager.beginTransaction().replace(R.id.fragmentMain,fragment).addToBackStack(null).commit()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var testSelect: CheckBox = binding.testSelect

        fun bind(document: Category){
            binding.categoryName.text = document.name
            binding.categoryImage.load(document.image)
            if(fragment.type == 3){
                testSelect.visibility = View.VISIBLE
            }
        }
    }

}
