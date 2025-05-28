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
import com.grinstitute.quiz.database.model.Question
import com.grinstitute.quiz.databinding.CategoryItemBinding
import com.grinstitute.quiz.databinding.CategoryListBinding
import com.grinstitute.quiz.frag.Practice
import com.grinstitute.quiz.frag.SelectionHome
import com.grinstitute.quiz.frag.Study
import com.grinstitute.quiz.frag.Test
import com.grinstitute.quiz.frag.Topic
import com.grinstitute.quiz.util.*

class TopicAdapter(private val fragment: Topic, private val topicList: ArrayList<Category>) : RecyclerView.Adapter<TopicAdapter.ViewHolder>() {
    private var questionMap: MutableMap<Long, ArrayList<Question>> = mutableMapOf()
    private var selectedTopicList: ArrayList<Category> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CategoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return topicList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(topicList.isNotEmpty()){
            holder.bind(topicList[position])
            holder.binding.category.setOnClickListener {
                if(questionMap[topicList[position].id]?.isNotEmpty() == true) {
                    if (fragment.type == 3) {
                        holder.binding.testSelect.toggle()
                        return@setOnClickListener
                    }
                    val fragment = when (fragment.type) {
                        1 -> {//Study
                            Study.newInstance(
                                topicList[position].name,
                                questionMap[topicList[position].id]!!,
                                topicList
                            )
                        }

                        2 -> {//Practice
                            Practice.newInstance(
                                topicList[position].name,
                                questionMap[topicList[position].id]!!,
                                topicList
                            )
                        }

                        else -> {//Test
                            Test.newInstance(
                                topicList[position].name,
                                questionMap[topicList[position].id]!!,
                                topicList
                            )
                        }
                    }
                    this.fragment.parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentMain, fragment)
                        .addToBackStack(null)
                        .commit()
                } else {
                    showToast(fragment.requireContext(), "No Questions Available")
                }
            }
        }
    }

    fun setQuestionMap(questionMap: MutableMap<Long, ArrayList<Question>>) {
        this.questionMap = questionMap
    }

    inner class ViewHolder(var binding: CategoryListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(document: Category){
            binding.categoryName.text = document.name
            binding.categoryName.isSelected = true
            binding.categoryImage.load(document.image)
            if(fragment.type == 3){
                binding.testSelect.visibility = View.VISIBLE
            }
            binding.questionCount.text = "${questionMap[document.id]?.size.toString()} Questions"

            binding.testSelect.isChecked = selectedTopicList.any { it.id == document.id }

            binding.testSelect.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedTopicList.add(document)
                } else {
                    selectedTopicList.find { it.id == document.id }?.let {
                        selectedTopicList.remove(it)
                    }
                }
            }
        }
    }

    fun getSelectedTopics(): ArrayList<Category> {
        return selectedTopicList
    }

}
