package com.grinstitute.quiz.frag

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.grinstitute.quiz.R
import com.grinstitute.quiz.database.model.Question
import com.grinstitute.quiz.databinding.FragmentTestBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [Test.newInstance] factory method to
 * create an instance of this fragment.
 */
class Test : Fragment() {
    private lateinit var binding: FragmentTestBinding
    private lateinit var questions: ArrayList<Question>

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
        binding = FragmentTestBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (requireActivity() as AppCompatActivity).supportActionBar?.title = name
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: ArrayList<Question>) =
            Test().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_PARAM1, param1)
                }
            }
    }
}