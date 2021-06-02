package ir.nabzi.aroundme.data.repository

import ir.nabzi.samplevideobrowser.model.Resource
import ir.nabzi.samplevideobrowser.model.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
*   RemoteResource<ResultType> is an abstract class that can be used for resources that can be fetched
 *   from remote server or database.
 *   If parameter "shouldFetch" is set to true, we always fetch data from server and update database,
 *   and we can use it for  fetching data conditionally
* */
abstract class RemoteResource<ResultType> {
    fun get(coroutineScope: CoroutineScope, shouldFetch : Boolean): StateFlow<Resource<ResultType>> {
        var stateFlow: MutableStateFlow<Resource<ResultType>> = MutableStateFlow(Resource.loading(null))
        coroutineScope.launch {
            if (shouldFetch) {
                val resource = pullFromServer()

                if (resource.status == Status.ERROR) {
                    getFromDB().collect {
                        stateFlow.emit(
                            Resource.error<ResultType>(
                                resource.message ?: "error loading from server", it , resource.errorCode
                            )
                        )
                    }

                } else {
                    resource.data?.let {
                        updateDB(it)
                    }
                    getFromDB().collect {
                        stateFlow.emit(
                            Resource(Status.SUCCESS , it,null,0 , resource.hasMore)
                        )
                    }
                }

            } else {
                getFromDB().collect {
                    stateFlow.emit(
                        Resource(Status.SUCCESS , it,null,0 , true)
                    )
                }
            }
        }
        return stateFlow
    }

    abstract suspend fun updateDB(result : ResultType)

    abstract fun getFromDB(): Flow<ResultType>

    abstract suspend fun pullFromServer(): Resource<ResultType>
}