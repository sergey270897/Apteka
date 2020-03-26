package ru.app.apteka.repositories

import androidx.paging.PositionalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.app.apteka.models.Medicine

class MedicineDataSource(private val scope:CoroutineScope) : PositionalDataSource<Medicine>() {

    private val repository = MedicineRepository()

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Medicine>) {
        scope.launch {
            val result = repository.getMedicine(offset = params.startPosition,count = params.loadSize)
            callback.onResult(result)
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Medicine>) {
        scope.launch {
            val result = repository.getMedicine(offset = params.requestedStartPosition,count = params.requestedLoadSize)
            callback.onResult(result, 0)
        }
    }
}