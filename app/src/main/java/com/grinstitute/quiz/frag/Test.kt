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
import com.grinstitute.quiz.database.model.Category
import com.grinstitute.quiz.database.model.Question
import com.grinstitute.quiz.databinding.FragmentTestBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
/**
 * A simple [Fragment] subclass.
 * Use the [Test.newInstance] factory method to
 * create an instance of this fragment.
 */
class Test : Fragment() {
    private lateinit var name: String
    private lateinit var binding: FragmentTestBinding
    private lateinit var questions: ArrayList<Question>
    private lateinit var topicList: ArrayList<Category>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(ARG_PARAM1)!!
            questions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableArrayList(ARG_PARAM2, Question::class.java)!!
            } else {
                @Suppress("DEPRECATION")
                it.getParcelableArrayList(ARG_PARAM2)!!
            }
            topicList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableArrayList(ARG_PARAM3, Category::class.java)!!
            } else {
                @Suppress("DEPRECATION")
                it.getParcelableArrayList(ARG_PARAM3)!!
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
        val activity = requireActivity() as MainActivity
        activity.binding.toolbarTitle.text = name
        activity.binding.toolbarTitle.isSelected = true
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: ArrayList<Question>, param3: ArrayList<Category>) =
            Test().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1,param1)
                    putParcelableArrayList(ARG_PARAM2, param2)
                    putParcelableArrayList(ARG_PARAM3, param3)
                }
            }
    }
}