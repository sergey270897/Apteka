package ru.app.pharmacy.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.app.pharmacy.R
import ru.app.pharmacy.databinding.CardCategoryBinding
import ru.app.pharmacy.models.Category

class CatalogAdapter(val callback: OnClickListener) :
    RecyclerView.Adapter<CatalogAdapter.ViewHolder>() {

    interface OnClickListener {
        fun onClickItem(category: Category)
    }

    inner class ViewHolder(private val binding: CardCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category) {
            with(binding) {
                category = item
                cardCategory.setOnClickListener { callback.onClickItem(item) }
            }
        }
    }

    private var items: List<Category> = listOf()

    fun updateData(data: List<Category>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].id == data[newPos].id

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].hashCode() == data[newPos].hashCode()

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.card_category, parent, false)
        val binding = CardCategoryBinding.bind(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
}