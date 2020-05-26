package ru.app.pharmacy.repositories.manager

import android.content.Context
import android.content.SharedPreferences
import ru.app.pharmacy.models.Filter
import ru.app.pharmacy.models.Profile
import ru.app.pharmacy.utils.extensions.setValue

class SharedPrefsManager(private val context: Context) {

    companion object{
        private const val SP_TITLE = "PharmacySP"
        private const val SP_SORT_INDEX = "SortIndex"
        private const val SP_PRICE_FROM = "PriceFrom"
        private const val SP_PRICE_TO = "PriceTo"
        private const val SP_AVAILABLE = "Available"

        private const val SP_NAME = "Name"
        private const val SP_TOKEN = "Token"
        private const val SP_REFRESH = "Refresh"
        private const val SP_EMAIL = "Email"
        private const val SP_PHARMACY_ID = "PharmacyID"
    }

    private fun get(): SharedPreferences = context.getSharedPreferences(SP_TITLE, Context.MODE_PRIVATE)

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

    fun saveProfile(profile: Profile){
        get().setValue(SP_NAME, profile.name)
        get().setValue(SP_TOKEN, profile.token)
        get().setValue(SP_REFRESH, profile.refresh)
        get().setValue(SP_EMAIL, profile.email)
        get().setValue(SP_PHARMACY_ID, profile.pharmacyId)
    }

    fun getProfile():Profile{
        val token = get().getString(SP_TOKEN, null)
        val refresh = get().getString(SP_REFRESH, null)
        val name = get().getString(SP_NAME, null)
        val email = get().getString(SP_EMAIL, null)
        val id = get().getInt(SP_PHARMACY_ID, -1)
        val profile = Profile(token, refresh, name, email)
        profile.pharmacyId = id
        return profile
    }
}