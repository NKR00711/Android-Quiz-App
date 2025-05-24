package com.grinstitute.quiz.frag

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.radiobutton.MaterialRadioButton
import com.grinstitute.quiz.MainActivity
import com.grinstitute.quiz.MainActivity.Companion.dataBaseManager
import com.grinstitute.quiz.R
import com.grinstitute.quiz.database.Constants.CATEGORY
import com.grinstitute.quiz.database.Constants.QUESTION
import com.grinstitute.quiz.database.adapter.TopicAdapter
import com.grinstitute.quiz.database.model.Category
import com.grinstitute.quiz.database.model.Question
import com.grinstitute.quiz.databinding.FragmentTopicBinding
import com.grinstitute.quiz.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class Topic : Fragment() {
    // TODO: Rename and change types of parameters
    private var name: String? = null
    private var dbName: String? = null
    var type: Int? = null
    private lateinit var binding: FragmentTopicBinding
    private lateinit var fragment: Fragment
    private var topicList = ArrayList<Category>()
    private lateinit var adapter: TopicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(ARG_PARAM1)
            dbName = it.getString(ARG_PARAM2)
            type = it.getInt(ARG_PARAM3)
        }
        adapter = TopicAdapter(this, topicList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopicBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as MainActivity
        activity.setGoBackButton()
//        activity.supportActionBar?.title = name
        activity.binding.toolbarTitle.text = name
        activity.binding.toolbarTitle.isSelected = true
        binding.topicRecycler.visibility = View.GONE
        binding.shimmerView.visibility = View.VISIBLE
        binding.topicRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.topicRecycler.adapter = adapter
        binding.testLayout.visibility = when (type) {
            3 -> View.VISIBLE
            else -> View.GONE
        }
        val questionMap = mutableMapOf<Long, ArrayList<Question>>()
        adapter.setQuestionMap(questionMap)
        dataBaseManager.getSnapShot(requireContext(), dbName!!){
            if (it != null) {
                for (i in it.child(CATEGORY).children) {
                    val category = i.getValue(Category::class.java)
                    category?.let {
                        val position = topicList.indexOfFirst { it.id == category.id }
                        if (position != -1) {
                            topicList[position] = category
//                            adapter.notifyItemChanged(position)
                        } else {
                            topicList.add(category)
//                            adapter.notifyItemInserted(topicList.size - 1)
                        }
                    }
                }
                topicList.sortBy { it.name }
                @SuppressLint("NotifyDataSetChanged")
                adapter.notifyDataSetChanged()
                for (i in it.child(QUESTION).children) {
                    val question = i.getValue(Question::class.java)
                    question?.let {
                        val list = questionMap.getOrPut(question.cid) { ArrayList() }
                        list.add(question)
                    }
                }
                binding.shimmerView.visibility = View.GONE
                binding.topicRecycler.visibility = View.VISIBLE
            }
        }

        binding.startTest.setOnClickListener {
            val selectedTopics = adapter.getSelectedTopics()
            if (selectedTopics.isNotEmpty()) {
                val numberOfQuestions = binding.root.findViewById<MaterialRadioButton>(binding.numberOfQuestions.checkedRadioButtonId)!!.text.toString().toInt()
                val questions = generateQuestions(questionMap, selectedTopics, numberOfQuestions)
                if (questions.isNotEmpty()) {
                    val topicsString: String = selectedTopics.joinToString(", ") { it.name }
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentMain, Test.newInstance(topicsString, questions, selectedTopics))
                        .addToBackStack(null).commit()
                }
            } else showToast(requireContext(), "Please select at least one topic")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String, type:Int) =
            Topic().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putInt(ARG_PARAM3, type)
                }
            }
    }
}