package com.grinstitute.quiz.frag

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.grinstitute.quiz.MainActivity
import com.grinstitute.quiz.database.adapter.StudyQuestionAdapter
import com.grinstitute.quiz.database.adapter.TabAdapter
import com.grinstitute.quiz.database.model.Question
import com.grinstitute.quiz.databinding.FragmentStudyBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Study : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var name: String
    private lateinit var questions: ArrayList<Question>
    private lateinit var binding: FragmentStudyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(ARG_PARAM1)!!
            questions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableArrayList(ARG_PARAM2, Question::class.java)!!
            } else {
                it.getParcelableArrayList(ARG_PARAM2)!!
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (requireActivity() as AppCompatActivity).supportActionBar?.title = name
        val activity = requireActivity() as MainActivity
        activity.binding.toolbarTitle.text = name
        activity.binding.toolbarTitle.isSelected = true
        binding.tabRecycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        val tabAdapter = TabAdapter(questions) { position ->
            binding.questionPager.scrollToPosition(position)
        }
        binding.tabRecycler.adapter = tabAdapter

        binding.questionPager.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.questionPager.adapter = StudyQuestionAdapter(questions)
        binding.questionPager.setHasFixedSize(true)

        PagerSnapHelper().attachToRecyclerView(binding.questionPager)

        binding.questionPager.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val position = (recyclerView.layoutManager as LinearLayoutManager)
                        .findFirstCompletelyVisibleItemPosition()
                    tabAdapter.setSelected(position)
                    binding.tabRecycler.scrollToPosition(position)
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: ArrayList<Question>) =
            Study().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1,param1)
                    putParcelableArrayList(ARG_PARAM2, param2)
                }
            }
    }
}