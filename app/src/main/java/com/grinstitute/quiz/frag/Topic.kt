package com.grinstitute.quiz.frag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.grinstitute.quiz.MainActivity
import com.grinstitute.quiz.MainActivity.Companion.dataBaseManager
import com.grinstitute.quiz.R
import com.grinstitute.quiz.database.Constants.CATEGORY
import com.grinstitute.quiz.database.Constants.QUESTION
import com.grinstitute.quiz.database.adapter.TopicAdapter
import com.grinstitute.quiz.database.model.Category
import com.grinstitute.quiz.databinding.FragmentTopicBinding

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
            fragment = when (type) {
                1 -> {//Study
                    Study.newInstance(name!!, dbName!!)
                }

                2 -> {//Practice
                    Practice.newInstance(name!!, dbName!!)
                }

                else -> {//Test
                    Test.newInstance(name!!, dbName!!)
                }
            }
        }
        adapter = TopicAdapter(this, topicList)
        dataBaseManager.getSnapShot(requireContext(), dbName!!){
            if (it != null) {
//                for (i in it.child(QUESTION).children) {
//                    val category = i.getValue(Category::class.java)
//                    category?.let {
//                        val position = topicList.indexOfFirst { it.id == category.id }
//                        if (position != -1) {
//                            topicList[position] = category
//                            adapter.notifyItemChanged(position)
//                        } else {
//                            topicList.add(category)
//                            adapter.notifyItemInserted(topicList.size - 1)
//                        }
//                    }
//                }
                for (i in it.child(CATEGORY).children) {
                    val category = i.getValue(Category::class.java)
                    category?.let {
                        val position = topicList.indexOfFirst { it.id == category.id }
                        if (position != -1) {
                            topicList[position] = category
                            adapter.notifyItemChanged(position)
                        } else {
                            topicList.add(category)
                            adapter.notifyItemInserted(topicList.size - 1)
                        }
                    }
                }
            }
        }
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
        binding.topicRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.topicRecycler.adapter = adapter
        val activity = activity as MainActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.actionBarToggle.isDrawerIndicatorEnabled = true
        activity.binding.toolbar.navigationIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.white))
        activity.binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack() // Go back
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