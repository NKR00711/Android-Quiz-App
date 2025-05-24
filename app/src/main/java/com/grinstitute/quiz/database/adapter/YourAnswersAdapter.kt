package com.grinstitute.quiz.database.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.grinstitute.quiz.R
import com.grinstitute.quiz.database.model.Question
import com.grinstitute.quiz.databinding.PracticeItemQuestionBinding
import com.grinstitute.quiz.util.*

class YourAnswersAdapter(
    private val questionList: ArrayList<Question>
) : RecyclerView.Adapter<YourAnswersAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var question: TextView = itemView.findViewById(R.id.question)
        var binding: PracticeItemQuestionBinding = PracticeItemQuestionBinding.bind(itemView)

        fun bind(question: Question,bindingPosition:Int){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PracticeItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false).root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            val question = questionList[position]
            holder.question.text = "Q${position + 1}.${question.question}"

            val options = listOf(
                holder.binding.option1,
                holder.binding.option2,
                holder.binding.option3,
                holder.binding.option4
            )

            options.forEachIndexed { i, option ->
                option.isEnabled = true
                option.setCheckable(false)
                option.visibility = if (i < question.options.size) View.VISIBLE else View.GONE
                option.isChecked = false
                option.background = AppCompatResources.getDrawable(
                    holder.binding.root.context,
                    R.drawable.answer_selector
                )
                option.setCheckMarkDrawable(null)
                if (i < question.options.size) {
                    option.text = "${'A' + i}. ${question.options[i]}"
                }
                if (question.selectedOption == null) {
                    option.isEnabled = false
                } else {
                    checkAt(holder, question.selectedOption!!, question.answer.toInt(), position)
                }
            }

            options[question.answer.toInt()].setCheckMarkDrawable(R.drawable.ischeck)
            options[question.answer.toInt()].isChecked = true
            options[question.answer.toInt()].background = AppCompatResources.getDrawable(holder.itemView.rootView.context,R.drawable.option_bg)

            holder.bind(question, position)
        }
    }

    override fun getItemCount(): Int = questionList.size

    private fun checkAt(holder: ViewHolder, position: Int, correctAnswer: Int, bindingPosition: Int){
        val options = listOf(
            holder.binding.option1,
            holder.binding.option2,
            holder.binding.option3,
            holder.binding.option4
        )

        options.forEach { it.setCheckable(false)  }
        options[position].setCheckMarkDrawable(R.drawable.ischeck)
        options[position].isChecked = true
        options[correctAnswer].setCheckMarkDrawable(R.drawable.ischeck)
        options[correctAnswer].background = AppCompatResources.getDrawable(holder.itemView.rootView.context,R.drawable.option_bg)
        options[correctAnswer].isChecked = true
        if(position != correctAnswer){
            options[position].isChecked = false
            options[position].background = AppCompatResources.getDrawable(holder.itemView.rootView.context,R.drawable.wrong_option_bg)
        }

//        if (bindingPosition != RecyclerView.NO_POSITION) {
            questionList[bindingPosition].selectedOption = position
//        }

//        Log.d("question","${questionList[bindingPosition]} and ${options[position].isActivated}")
    }

}
