package com.grinstitute.quiz.frag

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.grinstitute.quiz.MainActivity
import com.grinstitute.quiz.R
import com.grinstitute.quiz.database.adapter.TabAdapter
import com.grinstitute.quiz.database.adapter.TestQuestionAdapter
import com.grinstitute.quiz.database.model.Category
import com.grinstitute.quiz.database.model.Question
import com.grinstitute.quiz.databinding.FragmentTestBinding
import com.grinstitute.quiz.util.copyToClipboard
import com.grinstitute.quiz.util.parseQnA
import com.grinstitute.quiz.util.shareText
import com.grinstitute.quiz.util.showIssueReportDialog

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
        binding.tabRecycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        val tabAdapter = TabAdapter(questions) { position ->
            binding.questionPager.scrollToPosition(position)
        }
        binding.tabRecycler.adapter = tabAdapter

        binding.questionPager.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.questionPager.adapter = TestQuestionAdapter(questions)
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
        val layoutManager = binding.questionPager.layoutManager as LinearLayoutManager

        binding.previous.setOnClickListener {
            val currentPosition = layoutManager.findFirstVisibleItemPosition()

            if (currentPosition > 0) {
                binding.questionPager.smoothScrollToPosition(currentPosition - 1)
            }
        }

        binding.next.setOnClickListener {
            val currentPosition = layoutManager.findFirstVisibleItemPosition()
            val totalItems = binding.questionPager.adapter?.itemCount ?: 0
            if (currentPosition < totalItems) {
                binding.questionPager.smoothScrollToPosition(currentPosition + 1)
            }
        }

        var isMenuOpen = false

        binding.moreButton.setOnClickListener {
            val popup = PopupMenu(requireContext(), it)
            popup.menuInflater.inflate(R.menu.more_options_menu, popup.menu)

            try {
                val fields = popup.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field.get(popup)
                        val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.javaPrimitiveType)
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Update icon to selected (arrow up)
            binding.moreButton.isSelected = true
            isMenuOpen = true

            popup.setOnDismissListener {
                // Revert icon back when menu closes
                binding.moreButton.isSelected = false
                isMenuOpen = false
            }

            popup.setOnMenuItemClickListener { item ->
                val myPosition:Int = (binding.questionPager.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                when (item.itemId) {
                    R.id.action_copy -> {
                        copyToClipboard(requireContext(), parseQnA(requireContext(),questions[myPosition]))
                        true
                    }
                    R.id.action_share -> {
                        shareText(requireContext(),parseQnA(requireContext(),questions[myPosition]))
                        true
                    }
                    R.id.action_report -> {
                        showIssueReportDialog(requireContext(),myPosition,questions[myPosition],topicList)
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }
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