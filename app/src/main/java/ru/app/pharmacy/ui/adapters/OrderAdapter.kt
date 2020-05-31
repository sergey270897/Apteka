package ru.app.pharmacy.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_order.view.*
import ru.app.pharmacy.R
import ru.app.pharmacy.models.Order

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Order) {
            itemView.order_date.text = item.getDateString()
            with(itemView.order_status) {
                text = item.getStatusString()
                val color = when (item.status) {
                    10 -> resources.getColor(R.color.colorAccent)
                    20 -> resources.getColor(R.color.colorGreen)
                    30 -> resources.getColor(R.color.colorRed)
                    else -> resources.getColor(R.color.colorYellow)
                }
                setTextColor(color)
            }
            val productAdapter = OrderItemAdapter()
            with(itemView.order_list_products) {
                adapter = productAdapter
                layoutManager = LinearLayoutManager(context)
            }
            productAdapter.updateData(item.products)
        }
    }

    private var listOrders: List<Order> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_order, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listOrders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(listOrders[position])

    fun updateData(data: List<Order>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
                listOrders[oldPos].id == data[newPos].id

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
                listOrders[oldPos].hashCode() == data[newPos].hashCode()

            override fun getNewListSize(): Int = data.size

            override fun getOldListSize(): Int = listOrders.size
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listOrders = data
        diffResult.dispatchUpdatesTo(this)
    }
}