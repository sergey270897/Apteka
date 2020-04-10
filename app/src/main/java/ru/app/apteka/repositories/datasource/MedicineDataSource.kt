package ru.app.apteka.repositories.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.app.apteka.models.Medicine
import ru.app.apteka.network.NetworkState
import ru.app.apteka.repositories.MedicineRepository

class MedicineDataSource(
    private val scope: CoroutineScope,
    private val repository: MedicineRepository,
    private val query: String,
    private val categoryId: Int
) : PositionalDataSource<Medicine>() {

    private var supervisorJob = SupervisorJob()
    private val networkState = MutableLiveData<NetworkState>()
    private var retryQuery: (() -> Any)? = null


    private var lastCount = 0

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Medicine>) {
        if(lastCount == 20){
            retryQuery = { loadRange(params, callback) }
            executeQuery(
                offset = params.startPosition,
                count = params.loadSize
            ) {
                callback.onResult(it)
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Medicine>) {
        retryQuery = {loadInitial(params, callback)}
        executeQuery(
            offset = params.requestedStartPosition,
            count = params.requestedLoadSize
        ){
            lastCount = it.size
            callback.onResult(it, 0)
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
                categoryId = categoryId
            )
            retryQuery = null
            networkState.postValue(NetworkState.SUCCESS)
            callback(medicines)
        }
    }

    private fun getErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.d("M__MedicineDataSource", "An error happened: $e")
        networkState.postValue(NetworkState.FAILED)
    }

    fun getNetworkState():LiveData<NetworkState> = networkState

    fun refresh() = this.invalidate()

    fun retryFailedQuery(){
        val lastQuery = retryQuery
        retryQuery = null
        lastQuery?.invoke()
    }
}