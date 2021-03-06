package ru.app.pharmacy.repositories.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import kotlinx.coroutines.CoroutineScope
import ru.app.pharmacy.models.Filter
import ru.app.pharmacy.models.Medicine
import ru.app.pharmacy.repositories.MedicineRepository

class MedicineDataSourceFactory(
    private val scope: CoroutineScope,
    private val repository: MedicineRepository,
    private var categoryId: Int = 0,
    private var query: String = "",
    private var filter: Filter? = null
) : DataSource.Factory<Int, Medicine>() {

    val source = MutableLiveData<MedicineDataSource>()

    override fun create(): DataSource<Int, Medicine> {
        val src = MedicineDataSource(scope, repository, query, categoryId, filter)
        source.postValue(src)
        return src
    }

    fun getQuery() = query

    fun getSource() = source.value

    fun updateQuery(query: String) {
        this.query = query
        getSource()?.refresh()
    }

    fun updateQuery(id: Int, filter: Filter?) {
        this.categoryId = id
        this.filter = filter
        getSource()?.refresh()
    }
}