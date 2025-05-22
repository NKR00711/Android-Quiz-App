package com.grinstitute.quiz.frag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.grinstitute.quiz.R
import com.grinstitute.quiz.databinding.FragmentSelectionHomeBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SelectionHome : Fragment() {
    // TODO: Rename and change types of parameters
    private var name: String? = null
    private var dbName: String? = null
    private lateinit var binding: FragmentSelectionHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(ARG_PARAM1)
            dbName = it.getString(ARG_PARAM2)
        }
//        dataBaseManager.getSnapShot(requireContext(), dbName+CATEGORY){
//            if (it != null) {
//                for (i in it.children) {
//                    val category = i.getValue(Category::class.java)
//                }
//            }
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectionHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = name
        binding.study.setOnClickListener { view ->
            val fragment = Study.newInstance(name!!,dbName!!)
            parentFragmentManager.beginTransaction().replace(R.id.fragmentMain,fragment).addToBackStack(null).commit()
        }
        binding.practice.setOnClickListener { view ->
            val fragment = Practice.newInstance(name!!,dbName!!)
            parentFragmentManager.beginTransaction().replace(R.id.fragmentMain,fragment).addToBackStack(null).commit()
        }
        binding.test.setOnClickListener { view ->
            val fragment = Test.newInstance(name!!,dbName!!)
            parentFragmentManager.beginTransaction().replace(R.id.fragmentMain,fragment).addToBackStack(null).commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SelectionHome().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}