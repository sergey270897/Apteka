package ru.app.pharmacy.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.app.pharmacy.R
import ru.app.pharmacy.databinding.CardLoadingBinding
import ru.app.pharmacy.databinding.CardMedicineBinding
import ru.app.pharmacy.models.Medicine
import ru.app.pharmacy.network.NetworkState

class MedicineAdapter(private val callback: OnClickListener) :
    PagedListAdapter<Medicine, RecyclerView.ViewHolder>(diffCallback) {

    interface OnClickListener {

        fun onClickBuy(item: Medicine)
        fun onClickRefresh()
        fun listUpdated(size: Int, networkState: NetworkState?)
        fun onClickItem(item: Medicine)
    }

    enum class ViewType {

        MEDICINE,
        LOADING
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Medicine>() {
            override fun areItemsTheSame(oldItem: Medicine, newItem: Medicine): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Medicine, newItem: Medicine): Boolean =
                oldItem.hashCode() == newItem.hashCode()
        }
    }

    inner class MedicineItemHolder(private val binding: CardMedicineBinding) :
        RecyclerView.ViewHolder(binding.root), LifecycleOwner {

        fun bind(item: Medicine?) {
            item?.let {
                binding.medicine = item
                binding.btnBuyMedicineCard.setOnClickListener {
                    item.count.value = item.count.value?.plus(1)
                    callback.onClickBuy(item)
                }
                binding.btnDecMedicineCard.setOnClickListener {
                    item.count.value = item.count.value?.minus(1)
                    callback.onClickBuy(item)
                }
                binding.btnIncMedicineCard.setOnClickListener {
                    item.count.value = item.count.value?.plus(1)
                    callback.onClickBuy(item)
                }

                binding.medicineCard.setOnClickListener {
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

    inner class MedicineLoadingHolder(private val binding: CardLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(networkState: NetworkState?) {
            binding.state = networkState
            binding.btnRefreshMedicineCard.setOnClickListener { callback.onClickRefresh() }
        }
    }

    private var networkState: NetworkState? = null

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is MedicineItemHolder) holder.markAttach()
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is MedicineItemHolder) holder.markDetach()
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            ViewType.LOADING.ordinal
        } else {
            ViewType.MEDICINE.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.MEDICINE.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_medicine, parent, false)
                val binding = CardMedicineBinding.bind(view)
                val viewHolder = MedicineItemHolder(binding)
                binding.lifecycleOwner = viewHolder
                viewHolder
            }

            ViewType.LOADING.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_loading, parent, false)
                val binding = CardLoadingBinding.bind(view)
                MedicineLoadingHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ViewType.MEDICINE.ordinal -> (holder as MedicineItemHolder).bind(getItem(position))
            ViewType.LOADING.ordinal -> (holder as MedicineLoadingHolder).bind(networkState)
        }
    }

    override fun getItemCount(): Int {
        this.callback.listUpdated(super.getItemCount(), networkState)
        return super.getItemCount() + if (hasExtraRow() && super.getItemCount() != 0) 1 else 0
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.SUCCESS

    fun updateNetworkState(newNetworkState: NetworkState?) {
        val lastState = networkState
        val hadExtraRow = hasExtraRow()

        networkState = newNetworkState
        val currentExtraRow = hasExtraRow()

        if (hadExtraRow != currentExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(itemCount - 1)
            } else {
                notifyItemInserted(itemCount - 1)
            }
        } else if (currentExtraRow && lastState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}



