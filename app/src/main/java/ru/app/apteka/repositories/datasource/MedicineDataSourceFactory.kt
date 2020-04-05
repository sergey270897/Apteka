package ru.app.apteka.repositories.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import kotlinx.coroutines.CoroutineScope
import ru.app.apteka.models.Medicine
import ru.app.apteka.repositories.MedicineRepository

class MedicineDataSourceFactory(
    private val scope: CoroutineScope,
    private val repository: MedicineRepository,
    private var categoryId: Int = 0,
    private var query: String = ""
): DataSource.Factory<Int, Medicine>() {

    val source = MutableLiveData<MedicineDataSource>()

    override fun create(): DataSource<Int, Medicine> {
        val src  =MedicineDataSource(scope, repository, query, categoryId)
        source.postValue(src)
        return src
    }

    fun getQuery() = query

    fun getSource() = source.value

    fun updateQuery(query: String){
        this.query = query
        getSource()?.refresh()
    }

    fun updateQuery(id:Int){
        this.categoryId = id
        getSource()?.refresh()
    }
}