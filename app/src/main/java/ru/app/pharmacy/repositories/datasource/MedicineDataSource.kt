package ru.app.pharmacy.repositories.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.app.pharmacy.models.Filter
import ru.app.pharmacy.models.Medicine
import ru.app.pharmacy.network.NetworkState
import ru.app.pharmacy.repositories.MedicineRepository

class MedicineDataSource(
    private val scope: CoroutineScope,
    private val repository: MedicineRepository,
    private val query: String,
    private val categoryId: Int,
    private val filter: Filter?
) : PositionalDataSource<Medicine>() {

    private var supervisorJob = SupervisorJob()
    private val networkState = MutableLiveData<NetworkState>()
    private var retryQuery: (() -> Any)? = null
    private var lastCount = 0

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Medicine>) {
        retryQuery = { loadInitial(params, callback) }
        executeQuery(
            offset = 0,
            count = params.requestedLoadSize
        ) {
            lastCount = it.size
            callback.onResult(it, 0)
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Medicine>) {
        if (lastCount == 20) {
            retryQuery = { loadRange(params, callback) }
            executeQuery(
                offset = params.startPosition,
                count = params.loadSize
            ) {
                lastCount = it.size
                callback.onResult(it)
            }
        }
    }

    override fun invalidate() {
        super.invalidate()
        supervisorJob.cancelChildren()
    }

    private fun executeQuery(offset: Int, count: Int, callback: (List<Medicine>) -> Unit) {
        networkState.postValue(NetworkState.RUNNING)
        scope.launch(getErrorHandler() + supervisorJob) {
            delay(500)
            val medicines = repository.getMedicine(
                offset = offset,
                count = count,
                q = query,
                categoryId = categoryId,
                order = if (filter?.sortIndex == 0) "asc" else "desc",
                priceTo = filter?.priceTo ?: 20000,
                priceFrom = filter?.priceFrom ?: 0,
                available = filter?.available ?: false
            )

            retryQuery = null
            networkState.postValue(NetworkState.SUCCESS)
            callback(medicines)
        }
    }

    private fun getErrorHandler() = CoroutineExceptionHandler { _, e ->
        networkState.postValue(NetworkState.FAILED)
        supervisorJob.cancelChildren()
    }

    fun getNetworkState(): LiveData<NetworkState> = networkState

    fun refresh() = this.invalidate()

    fun retryFailedQuery() {
        val lastQuery = retryQuery
        retryQuery = null
        lastQuery?.invoke()
    }
}