package ru.app.apteka.repositories.manager

import android.content.Context
import android.content.SharedPreferences
import ru.app.apteka.models.Filter
import ru.app.apteka.utils.extensions.setValue

class SharedPrefsManager(private val context: Context) {

    companion object{
        private const val SP_NAME = "AptekaSP"
        private const val SP_SORT_INDEX = "SortIndex"
        private const val SP_PRICE_FROM = "PriceFrom"
        private const val SP_PRICE_TO = "PriceTo"
        private const val SP_AVAILABLE = "Available"
    }

    private fun get(): SharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    fun getFilterSearchingMedicines(): Filter {
        val sortIndex = get().getInt(SP_SORT_INDEX, 0)
        val priceFrom = get().getInt(SP_PRICE_FROM, 0)
        val priceTo = get().getInt(SP_PRICE_TO, 20000)
        val available = get().getBoolean(SP_AVAILABLE, false)
        return Filter(sortIndex, priceFrom, priceTo, available)
    }

    fun saveFilterSearchingMedicines(filter:Filter){
        get().setValue(SP_SORT_INDEX, filter.sortIndex)
        get().setValue(SP_PRICE_FROM, filter.priceFrom)
        get().setValue(SP_PRICE_TO, filter.priceTo)
        get().setValue(SP_AVAILABLE, filter.available)
    }
}