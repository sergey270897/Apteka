package ru.app.apteka.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import ru.app.apteka.models.Medicine
import ru.app.apteka.repositories.MedicineDataSource

class MedicineModel : ViewModel() {
    val medicines: LiveData<PagedList<Medicine>>

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .build()

        medicines = initPagedListBuilder(config).build()
    }

    private fun initPagedListBuilder(config: PagedList.Config): LivePagedListBuilder<Int, Medicine> {

        val dataSourceFactory = object : DataSource.Factory<Int, Medicine>() {
            override fun create(): DataSource<Int, Medicine> {
                return MedicineDataSource(viewModelScope)
            }
        }

        return LivePagedListBuilder(dataSourceFactory, config)
    }
}