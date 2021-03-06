package ru.app.pharmacy.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.launch
import ru.app.pharmacy.models.Filter
import ru.app.pharmacy.models.MedicineCart
import ru.app.pharmacy.network.NetworkState
import ru.app.pharmacy.repositories.MedicineRepository
import ru.app.pharmacy.repositories.datasource.MedicineDataSourceFactory
import ru.app.pharmacy.repositories.manager.SharedPrefsManager
import ru.app.pharmacy.ui.base.BaseViewModel

class MedicineModel(
    private val categoryId: Int,
    private val repository: MedicineRepository,
    private val sharedPrefsManager: SharedPrefsManager
) : BaseViewModel() {

    private val medicineDataSource = MedicineDataSourceFactory(
        repository = repository,
        scope = ioScope
    )
    val networkState: LiveData<NetworkState>? =
        Transformations.switchMap(medicineDataSource.source) {
            it.getNetworkState()
        }
    val closeFilterDialog = MutableLiveData(false)
    val medicines = LivePagedListBuilder(medicineDataSource, getConfig()).build()
    val filter = MutableLiveData<Filter>()

    init {
        filter.value = getFilter()
        if (categoryId != 0) {
            getMedicinesByCategoriesID(categoryId, filter.value)
        }
    }

    private fun getFilter(): Filter = sharedPrefsManager.getFilterSearchingMedicines()

    private fun saveFilter(filter: Filter) {
        sharedPrefsManager.saveFilterSearchingMedicines(filter)
    }

    private fun getConfig() = PagedList.Config.Builder()
        .setPageSize(20)
        .setInitialLoadSizeHint(20)
        .setEnablePlaceholders(false)
        .build()

    private fun getMedicinesByCategoriesID(id: Int, filter: Filter?) {
        medicineDataSource.updateQuery(id, filter)
    }

    fun getMedicinesByName(query: String) {
        val search = query.trim()
        if (medicineDataSource.getQuery() == search) return
        medicineDataSource.updateQuery(query)
    }

    fun refreshFailedRequest() = medicineDataSource.getSource()?.retryFailedQuery()

    fun refreshAll() = medicineDataSource.getSource()?.refresh()

    fun getCurrentQuery() = medicineDataSource.getQuery()

    fun applyFilter() {
        saveFilter(filter.value!!)
        medicineDataSource.updateQuery(categoryId, filter.value)
        closeFilterDialog.value = !closeFilterDialog.value!!
    }

    fun setSortIndex(value: Int) {
        filter.value?.sortIndex = value
    }

    fun closeFilter() {
        closeFilterDialog.value = !closeFilterDialog.value!!
    }

    fun deleteCartItem(item: MedicineCart) {
        ioScope.launch {
            repository.deleteCartItem(item)
        }
    }

    fun addCartItem(item: MedicineCart) {
        ioScope.launch {
            repository.addCartItem(item)
        }
    }
}