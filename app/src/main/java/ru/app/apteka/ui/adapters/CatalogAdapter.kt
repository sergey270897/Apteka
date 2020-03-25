package ru.app.apteka.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.app.apteka.R
import ru.app.apteka.databinding.CardCategoryBinding
import ru.app.apteka.models.Category
import ru.app.apteka.viewmodels.CatalogModel

class CatalogAdapter(private val viewModel: CatalogModel) :
    RecyclerView.Adapter<CatalogAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: CardCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category) {
            with(binding) {
                category = item
                vm = viewModel
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