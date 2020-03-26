package ru.app.apteka.ui.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.app.apteka.R
import ru.app.apteka.databinding.CardMedicineBinding
import ru.app.apteka.models.Medicine

class MedicineAdapter: PagedListAdapter<Medicine, MedicineAdapter.MedicineViewHolder>(DiffUtilCallback()) {

    inner class MedicineViewHolder(private val binding: CardMedicineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Medicine) {
            binding.medicine = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.card_medicine, parent, false)
        val binding = CardMedicineBinding.bind(inflater)
        return MedicineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<Medicine>() {
    override fun areItemsTheSame(oldItem: Medicine, newItem: Medicine): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Medicine, newItem: Medicine): Boolean =
        oldItem.hashCode() == newItem.hashCode()
}

@BindingAdapter("app:url","app:errorImage")
fun loadImage(image:ImageView, url:String, errorImage:Drawable){
    Picasso.get().load(url).error(errorImage).into(image)
}