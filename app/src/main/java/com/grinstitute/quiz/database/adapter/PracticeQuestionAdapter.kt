package com.grinstitute.quiz.database.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grinstitute.quiz.database.model.Question
import com.grinstitute.quiz.databinding.PracticeItemQuestionBinding

class PracticeQuestionAdapter(
    private val questionList: ArrayList<Question>
) : RecyclerView.Adapter<PracticeQuestionAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(val binding: PracticeItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = PracticeItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questionList[position]
        holder.binding.question.text = "Q${position + 1}.${question.question}"
        if(question.options.isNotEmpty()) {
            holder.binding.option1.visibility = View.VISIBLE
            holder.binding.option1.text = "A. ${question.options[0]}"
        }
        if(question.options.size > 1) {
            holder.binding.option2.visibility = View.VISIBLE
            holder.binding.option2.text = "B. ${question.options[1]}"
        }
        if(question.options.size > 2) {
            holder.binding.option3.visibility = View.VISIBLE
            holder.binding.option3.text = "C. ${question.options[2]}"
        }
        if(question.options.size > 3) {
            holder.binding.option4.visibility = View.VISIBLE
            holder.binding.option4.text = "D. ${question.options[3]}"
        }
        checkAt(holder,question.answer.toInt())
        // Add logic to handle answer selection, explanation, etc.
    }

    override fun getItemCount(): Int = questionList.size

    private fun checkAt(holder: QuestionViewHolder, position: Int){
        val options = listOf(
            holder.binding.option1,
            holder.binding.option2,
            holder.binding.option3,
            holder.binding.option4
        )

        options.forEach { it.isChecked = false }

        if (position in options.indices) {
            options[position].isChecked = true
        }
    }
}
