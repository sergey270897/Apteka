package ru.app.pharmacy.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.app.pharmacy.R
import ru.app.pharmacy.databinding.CardOrderItemBinding
import ru.app.pharmacy.models.Product

class OrderItemAdapter : RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CardOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.product = product
        }
    }

    private var list: List<Product> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_order_item, parent, false)
        val binding = CardOrderItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])

    fun updateData(data: List<Product>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
                list[oldPos].id == data[newPos].id

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
                list[oldPos].hashCode() == data[newPos].hashCode()

            override fun getNewListSize(): Int = data.size

            override fun getOldListSize(): Int = list.size
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list = data
        diffResult.dispatchUpdatesTo(this)
    }
}