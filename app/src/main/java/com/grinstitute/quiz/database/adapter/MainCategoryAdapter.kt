package com.grinstitute.quiz.database.adapter

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.grinstitute.quiz.R
import com.grinstitute.quiz.database.model.Config_Category
import com.grinstitute.quiz.databinding.CategoryItemBinding
import com.grinstitute.quiz.frag.SelectionHome

class MainCategoryAdapter(
    private val fragment: Fragment,
//    private val navController: NavController,
    private val mainCategories: ArrayList<Config_Category>
) : RecyclerView.Adapter<MainCategoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = CategoryItemBinding.bind(itemView)
        var itemCard = binding.category
        private var name = binding.categoryName
        private var image = binding.categoryImage

        fun bind(document: Config_Category){
            name.text = document.name
            image.load(document.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CategoryItemBinding.inflate(fragment.layoutInflater, parent, false).root)
    }

    override fun getItemCount(): Int {
        return mainCategories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(mainCategories.isNotEmpty()){
            holder.bind(mainCategories[position])
            holder.itemCard.setOnClickListener {
//                val action = HomeDirections.actionHomeToSelection(
//                    param1 = mainCategories[position].name,
//                    param2 = mainCategories[position].db
//                )
//                navController.navigate(R.id.selectionFragment)
                val fragment = SelectionHome.newInstance(mainCategories[position].name,mainCategories[position].db)
                this.fragment.parentFragmentManager.beginTransaction().replace(R.id.fragmentMain,fragment).addToBackStack(null).commit()
            }
        }
    }
}