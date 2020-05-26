package ru.app.pharmacy.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.app.pharmacy.R
import ru.app.pharmacy.databinding.CardCartBinding
import ru.app.pharmacy.models.MedicineCart

class CartAdapter(private val callback: OnClickListener) :
    RecyclerView.Adapter<CartAdapter.CartHolder>() {

    interface OnClickListener {
        fun onClickCount(item: MedicineCart)
        fun onClickItem(item: MedicineCart)
    }

    inner class CartHolder(private val binding: CardCartBinding) :
        RecyclerView.ViewHolder(binding.root), LifecycleOwner {

        fun bind(item: MedicineCart) {
            binding.let {
                binding.medicine = item
                binding.btnDecCartCard.setOnClickListener {
                    item.count.value = item.count.value?.minus(1)
                    callback.onClickCount(item)
                }

                binding.btnIncCartCard.setOnClickListener {
                    item.count.value = item.count.value?.plus(1)
                    callback.onClickCount(item)
                }

                binding.cardCart.setOnClickListener {
                    callback.onClickItem(item)
                }
            }
        }

        private val lifecycle = LifecycleRegistry(this)

        init {
            lifecycle.currentState = Lifecycle.State.INITIALIZED
        }

        override fun getLifecycle(): Lifecycle = lifecycle

        fun markAttach() {
            lifecycle.currentState = Lifecycle.State.STARTED
        }

        fun markDetach() {
            lifecycle.currentState = Lifecycle.State.DESTROYED
        }
    }

    private var items: List<MedicineCart> = listOf()

    fun updateData(data: List<MedicineCart>) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_cart, parent, false)
        val binding = CardCartBinding.bind(view)
        val viewHolder = CartHolder(binding)
        binding.lifecycleOwner = viewHolder
        return viewHolder
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CartHolder, position: Int) = holder.bind(items[position])

    override fun onViewAttachedToWindow(holder: CartHolder) {
        super.onViewAttachedToWindow(holder)
        holder.markAttach()
    }

    override fun onViewDetachedFromWindow(holder: CartHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.markDetach()
    }
}