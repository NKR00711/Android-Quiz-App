package com.grinstitute.quiz.frag

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grinstitute.quiz.R
import com.grinstitute.quiz.database.model.Question
import com.grinstitute.quiz.databinding.FragmentStudyBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Study : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var questions: ArrayList<Question>
    private lateinit var binding: FragmentStudyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                questions = it.getParcelableArrayList(ARG_PARAM1, Question::class.java)!!
            } else {
                questions = it.getParcelableArrayList(ARG_PARAM1)!!
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
        binding.tabRecycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        val tabAdapter = TabAdapter(tabList) { position ->
            questionPager.smoothScrollToPosition(position)
        }
        tabRecycler.adapter = tabAdapter

//        (requireActivity() as AppCompatActivity).supportActionBar?.title = questions.size.toString()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: ArrayList<Question>) =
            Study().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_PARAM1, param1)
                }
            }
    }
}