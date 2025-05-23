package com.grinstitute.quiz.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.grinstitute.quiz.MainActivity
import com.grinstitute.quiz.MainActivity.Companion.dataBaseManager
import com.grinstitute.quiz.R
import com.grinstitute.quiz.database.Constants.CONFIG
import com.grinstitute.quiz.database.adapter.MainCategoryAdapter
import com.grinstitute.quiz.database.model.Config_Category
import com.grinstitute.quiz.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mainCategories = ArrayList<Config_Category>()
    private lateinit var adapter: MainCategoryAdapter
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        adapter = MainCategoryAdapter(this, mainCategories)
        dataBaseManager.getSnapShot(requireContext(),CONFIG) { snapshot ->
            if(snapshot != null) {
                for (mainCategory in snapshot.children) {
                    val mainCategoryTmp = mainCategory.getValue(Config_Category::class.java)
                    mainCategoryTmp?.let {
                        val position = mainCategories.indexOfFirst { it.db == mainCategoryTmp.db }
                        if (position != -1) {
                            mainCategories[position] = mainCategoryTmp
                            adapter.notifyItemChanged(position)
                        } else {
                            mainCategories.add(mainCategoryTmp)
                            adapter.notifyItemInserted(mainCategories.size - 1)
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
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)
        binding.homeRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.homeRecycler.adapter = adapter
        val activity = requireActivity() as MainActivity
        activity.binding.toolbarTitle.text = getString(R.string.app_name)
        activity.binding.toolbarTitle.isSelected = true
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.actionBarToggle.isDrawerIndicatorEnabled = true
        activity.actionBarToggle.syncState()
        activity.binding.toolbar.setNavigationOnClickListener {
            activity.binding.main.openDrawer(GravityCompat.START)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}