package com.grinstitute.quiz.frag

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.grinstitute.quiz.MainActivity
import com.grinstitute.quiz.R
import com.grinstitute.quiz.database.model.Question
import com.grinstitute.quiz.databinding.FragmentPracticeBinding
import com.grinstitute.quiz.databinding.FragmentTestBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [Practice.newInstance] factory method to
 * create an instance of this fragment.
 */
class Practice : Fragment() {
    private lateinit var name: String
    private lateinit var binding: FragmentPracticeBinding
    private lateinit var questions: ArrayList<Question>

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
        binding = FragmentPracticeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (requireActivity() as AppCompatActivity).supportActionBar?.title = name
        val activity = requireActivity() as MainActivity
        activity.binding.toolbarTitle.text = name
        activity.binding.toolbarTitle.isSelected = true
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Practice.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: ArrayList<Question>) =
            Practice().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1,param1)
                    putParcelableArrayList(ARG_PARAM2, param2)
                }
            }
    }
}