package com.grinstitute.quiz.frag

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grinstitute.quiz.MainActivity
import com.grinstitute.quiz.R
import com.grinstitute.quiz.database.model.Category
import com.grinstitute.quiz.database.model.Question
import com.grinstitute.quiz.database.model.QuizResult
import com.grinstitute.quiz.databinding.FragmentResultBinding
import com.grinstitute.quiz.util.*
import ir.mahozad.android.PieChart
import ir.mahozad.android.component.Alignment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Result.newInstance] factory method to
 * create an instance of this fragment.
 */
class Result : Fragment() {
    private lateinit var binding: FragmentResultBinding
    private lateinit var questions: ArrayList<Question>
    private lateinit var topicList: ArrayList<Category>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            topicList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableArrayList(ARG_PARAM1, Category::class.java)!!
            } else {
                @Suppress("DEPRECATION")
                it.getParcelableArrayList(ARG_PARAM1)!!
            }
            questions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableArrayList(ARG_PARAM2, Question::class.java)!!
            } else {
                @Suppress("DEPRECATION")
                it.getParcelableArrayList(ARG_PARAM2)!!
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as MainActivity
        activity.binding.toolbarTitle.text = "Result"
        activity.binding.toolbarTitle.isSelected = true

        setPieChartData(questions, binding.pieChart)

        binding.viewAnswers.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentMain, YourAnswer.newInstance(questions,topicList))
                .addToBackStack(null).commit()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Result.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: ArrayList<Category>, param2: ArrayList<Question>) =
            Result().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_PARAM1, param1)
                    putParcelableArrayList(ARG_PARAM2, param2)
                }
            }
    }
}