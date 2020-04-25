package ru.app.apteka.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.launch
import ru.app.apteka.models.Filter
import ru.app.apteka.models.MedicineCart
import ru.app.apteka.network.NetworkState
import ru.app.apteka.repositories.MedicineRepository
import ru.app.apteka.repositories.datasource.MedicineDataSourceFactory
import ru.app.apteka.repositories.manager.SharedPrefsManager
import ru.app.apteka.ui.base.BaseViewModel

class MedicineModel(
    val categoryId: Int,
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
        if (categoryId != 0) {
            getMedicinesByCategoriesID(categoryId, filter.value)
        }
        filter.value = getFilter()
    }

    fun getFilter(): Filter = sharedPrefsManager.getFilterSearchingMedicines()

    fun saveFilter(filter: Filter) {
        sharedPrefsManager.saveFilterSearchingMedicines(filter)
    }

    private fun getConfig() = PagedList.Config.Builder()
        .setPageSize(20)
        .setInitialLoadSizeHint(20)
        .setEnablePlaceholders(false)
        .build()

    fun getMedicinesByName(query: String) {
        val search = query.trim()
        if (medicineDataSource.getQuery() == search) return
        medicineDataSource.updateQuery(query)
    }

    private fun getMedicinesByCategoriesID(id: Int, filter: Filter?) {
        medicineDataSource.updateQuery(id, filter)
    }

    fun refreshFailedRequest() = medicineDataSource.getSource()?.retryFailedQuery()

    fun refreshAll() = medicineDataSource.getSource()?.refresh()

    fun getCurrentQuery() = medicineDataSource.getQuery()

    fun applyFilter(){
        medicineDataSource.updateQuery(categoryId, filter.value)
        closeFilterDialog.value = !closeFilterDialog.value!!
    }

    fun setSortIndex(value:Int){
        filter.value?.sortIndex = value
    }

    fun closeFilter(){
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