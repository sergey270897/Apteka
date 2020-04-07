package ru.app.apteka.ui.adapters

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.app.apteka.R
import ru.app.apteka.databinding.CardLoadingBinding
import ru.app.apteka.databinding.CardMedicineBinding
import ru.app.apteka.models.Medicine
import ru.app.apteka.network.NetworkState

class MedicineAdapter(private val callback: OnClickListener) :
    PagedListAdapter<Medicine, RecyclerView.ViewHolder>(diffCallback) {

    private var networkState: NetworkState? = null

    interface OnClickListener {
        fun onClickBuy()
        fun onClickCount()
        fun onClickRefresh()
        fun listUpdated(size: Int, networkState: NetworkState?)
    }

    enum class ViewType {
        MEDICINE,
        LOADING
    }

    inner class MedicineItemHolder(private val binding: CardMedicineBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Medicine?) {
            item?.let {
                binding.medicine = item
            }
        }
    }

    inner class MedicineLoadingHolder(private val binding: CardLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(networkState: NetworkState?) {
            binding.state = networkState
            binding.btnRefreshMedicineCard.setOnClickListener { callback.onClickRefresh() }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount-1) {
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
                MedicineItemHolder(binding)
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
        return super.getItemCount() + if(hasExtraRow() && super.getItemCount() != 0) 1 else 0
    }


    private fun hasExtraRow() = networkState != null && networkState != NetworkState.SUCCESS

    fun updateNetworkState(newNetworkState: NetworkState?) {
        val lastState = networkState
        val hadExtraRow = hasExtraRow()

        networkState = newNetworkState
        val currentExtraRow = hasExtraRow()

        if (hadExtraRow != currentExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(itemCount-1)
            } else {
                notifyItemInserted(itemCount-1)
            }
        } else if (currentExtraRow && lastState != newNetworkState) {
            notifyItemChanged(itemCount-1)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Medicine>() {
            override fun areItemsTheSame(oldItem: Medicine, newItem: Medicine): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Medicine, newItem: Medicine): Boolean =
                oldItem.hashCode() == newItem.hashCode()
        }
    }
}

@BindingAdapter("app:url", "app:errorImage")
fun loadImage(image: ImageView, url: String, errorImage: Drawable) {
    Picasso.get().load(url).error(errorImage).into(image)
}

