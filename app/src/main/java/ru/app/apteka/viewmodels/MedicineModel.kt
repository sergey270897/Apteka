package ru.app.apteka.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import ru.app.apteka.network.NetworkState
import ru.app.apteka.repositories.MedicineRepository
import ru.app.apteka.repositories.datasource.MedicineDataSourceFactory
import ru.app.apteka.repositories.manager.SharedPrefsManager
import ru.app.apteka.ui.base.BaseViewModel

class MedicineModel(val categoryId:Int, repository: MedicineRepository): BaseViewModel() {

    private val medicineDataSource = MedicineDataSourceFactory(
        repository = repository,
        scope = ioScope
    )

    val networkState: LiveData<NetworkState>? = Transformations.switchMap(medicineDataSource.source){
        it.getNetworkState()
    }

    val medicines = LivePagedListBuilder(medicineDataSource, getConfig()).build()

    init {
        if(categoryId != 0){
            getMedicinesByCategoriesID(categoryId)
        }
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

    private fun getMedicinesByCategoriesID(id:Int){
        medicineDataSource.updateQuery(id)
    }

    fun refreshFailedRequest() = medicineDataSource.getSource()?.retryFailedQuery()

    fun refreshAll() = medicineDataSource.getSource()?.refresh()

    fun getCurrentQuery() = medicineDataSource.getQuery()
}