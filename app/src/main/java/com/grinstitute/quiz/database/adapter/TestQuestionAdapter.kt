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
import com.grinstitute.quiz.databinding.TestItemQuestionBinding
import com.grinstitute.quiz.util.*

class TestQuestionAdapter(
    private val questionList: ArrayList<Question>
) : RecyclerView.Adapter<TestQuestionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var question: TextView = itemView.findViewById(R.id.question)
//        var option1: CheckedTextView = itemView.findViewById(R.id.option1)
//        var option2: CheckedTextView = itemView.findViewById(R.id.option2)
//        var option3: CheckedTextView = itemView.findViewById(R.id.option3)
//        var option4: CheckedTextView = itemView.findViewById(R.id.option4)
        var binding = TestItemQuestionBinding.bind(itemView)

        fun bind(question: Question,bindingPosition:Int){
            binding.option1.setOnClickListener {
                checkAt(this,0, bindingPosition)
            }
            binding.option2.setOnClickListener {
                checkAt(this,1, bindingPosition)
            }
            binding.option3.setOnClickListener {
                checkAt(this,2, bindingPosition)
            }
            binding.option4.setOnClickListener {
                checkAt(this, 3, bindingPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TestItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false).root)
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
                option.visibility = if (i < question.options.size) View.VISIBLE else View.GONE
                if (i < question.options.size) {
                    option.text = "${'A' + i}. ${question.options[i]}"
                }
                if (question.selectedOption == null) {
                    option.isChecked = false
//                    option.setCheckable(true)
                    option.background = AppCompatResources.getDrawable(
                        holder.itemView.rootView.context,
                        R.drawable.option_selector
                    )
                    option.setCheckMarkDrawable(null)
                }// else option.setCheckable(false)
            }

            question.selectedOption?.let { selected ->
                if (!options.any(CheckedTextView::isChecked)) {
                    checkAt(holder, selected, position)
                }
            }


            holder.bind(question, position)
        }
    }

    override fun getItemCount(): Int = questionList.size

    private fun checkAt(holder: ViewHolder, position: Int, bindingPosition: Int){
        val options = listOf(
            holder.binding.option1,
            holder.binding.option2,
            holder.binding.option3,
            holder.binding.option4
        )

        options.forEach { option ->
            option.isChecked = false
            option.background = AppCompatResources.getDrawable(
                holder.itemView.rootView.context,
                R.drawable.option_selector
            )
            option.setCheckMarkDrawable(null)
        }
//        options[position].setCheckMarkDrawable(R.drawable.ischeck)
        options[position].isChecked = true
//        options[correctAnswer].setCheckMarkDrawable(R.drawable.ischeck)
//        options[correctAnswer].isChecked = true
//        if(position != correctAnswer){
//            options[position].isChecked = false
//            options[position].background = AppCompatResources.getDrawable(holder.itemView.rootView.context,R.drawable.wrong_option_bg)
//        }

//        if (bindingPosition != RecyclerView.NO_POSITION) {
            questionList[bindingPosition].selectedOption = position
//        }

//        Log.d("question","${questionList[bindingPosition]} and ${options[position].isActivated}")
    }

}
