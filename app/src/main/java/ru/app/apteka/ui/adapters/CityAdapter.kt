package ru.app.apteka.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import ru.app.apteka.R
import ru.app.apteka.models.Pharmacy

class CityAdapter(context: Context, private val resource: Int, tvResourceId: Int) :
    ArrayAdapter<Pharmacy>(context, resource, tvResourceId) {

    private var list: MutableList<Pharmacy> = mutableListOf(Pharmacy(-1, "Не выбрано", ""))

    private var showProgress = true

    fun setShowProgress(value: Boolean) {
        showProgress = value
        notifyDataSetChanged()
    }

    fun addAll(values: List<Pharmacy>) {
        list.addAll(values)
        notifyDataSetChanged()
    }

    fun getPositionById(id: Int): Int {
        for (i in list.indices) {
            if (list[i].id == id) return i
        }
        return 0
    }

    override fun getItem(position: Int): Pharmacy {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getViewCity(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getViewCity(position, convertView, parent)
    }

    private fun getViewCity(position: Int, convertView: View?, parent: ViewGroup): View {
        val pharmacy = getItem(position)

        val viewResult: View =
            convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        viewResult.findViewById<TextView>(R.id.list_item_tv).text =
            if (pharmacy.address.isEmpty()) pharmacy.name else "${pharmacy.name}\n${pharmacy.address}"

        val progress = viewResult.findViewById<ProgressBar>(R.id.list_item_progress)
        if(showProgress){
            progress.visibility = View.VISIBLE
            progress.isIndeterminate = true
        }else{
            progress.visibility = View.GONE
            progress.isIndeterminate = false
        }
        return viewResult
    }
}