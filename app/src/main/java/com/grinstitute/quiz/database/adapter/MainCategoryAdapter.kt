package com.grinstitute.quiz.database.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.grinstitute.quiz.R
import com.grinstitute.quiz.database.model.Config_Category
import com.grinstitute.quiz.databinding.CategoryItemBinding
import com.grinstitute.quiz.frag.SelectionHome

class MainCategoryAdapter(private val fragment: Fragment,private val mainCategories: ArrayList<Config_Category>) : RecyclerView.Adapter<MainCategoryAdapter.ViewHolder>() {

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
                val fragment = SelectionHome.newInstance(mainCategories[position].name,mainCategories[position].db)
                this.fragment.parentFragmentManager.beginTransaction().replace(R.id.fragmentMain,fragment).addToBackStack(null).commit()
            }
        }
    }
}