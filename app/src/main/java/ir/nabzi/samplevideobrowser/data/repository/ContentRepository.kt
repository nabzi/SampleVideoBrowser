package ir.nabzi.samplevideobrowser.data.repository

import ir.nabzi.samplevideobrowser.data.repository.NetworkCall
import ir.nabzi.samplevideobrowser.data.repository.RemoteResource
import ir.nabzi.samplevideobrowser.model.SearchResponse
import ir.nabzi.samplevideobrowser.data.db.ContentDao
import ir.nabzi.samplevideobrowser.data.remote.ApiService
import ir.nabzi.samplevideobrowser.data.repository.ContentRepositoryImpl.Companion.PAGE_SIZE
import ir.nabzi.samplevideobrowser.model.Content
import ir.nabzi.samplevideobrowser.model.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response

interface ContentRepository {
    fun getContentsNearLocation(
        search: String,
        coroutineScope: CoroutineScope,
        page: Int,
        shouldFetch: Boolean
    ): StateFlow<Resource<List<Content>>?>
}

class ContentRepositoryImpl(
    private val dbDataSource: ContentDBDataSource,
    private val remoteDataSource: ContentRemoteDataSource
) : ContentRepository {
    companion object {
        val PAGE_SIZE = 20
    }

    override fun getContentsNearLocation(
        search: String,
        coroutineScope: CoroutineScope,
        page: Int,
        shouldFetch: Boolean
    )
            : StateFlow<Resource<List<Content>>?> {
        return object : RemoteResource<List<Content>>() {
            override suspend fun updateDB(result: List<Content>) {
                if (page == 1)
                    dbDataSource.clear()
                dbDataSource.update(result)
            }

            override fun getFromDB(): Flow<List<Content>> {
                return dbDataSource.getContents()
            }

            override suspend fun pullFromServer(): Resource<List<Content>> {
                return remoteDataSource.getContents(search)
            }
        }.get(coroutineScope, shouldFetch)
    }
}

class ContentDBDataSource(private val ContentDao: ContentDao) {
    suspend fun update(result: List<Content>) {
        ContentDao.addList(result)
    }

    suspend fun clear() {
        ContentDao.removeAll()
    }

    fun getContents(): Flow<List<Content>> {
        return ContentDao.getContentsFlow()
    }
}

class ContentRemoteDataSource(val apiServices: ApiService) {
    suspend fun getContents(search : String): Resource<List<Content>> {
        val res = object : NetworkCall<SearchResponse>() {
            override suspend fun createCall(): Response<SearchResponse> {
                return apiServices.getContentList(search)
            }
        }.fetch()
        var result = res.data?.let { searchResponseToContents(it) }
        return Resource( res.status, result?.first, res.message, res.errorCode , result?.second )
    }

    private fun searchResponseToContents(searchResponse: SearchResponse): Pair<List<Content>, Boolean> {
        return Pair(arrayListOf(), false)

    }

    private fun getImageUrl(code: String): String? {
        return when (code) {
            "SCHOOL" -> "http://riyainfosystems.com/wp-content/uploads/2019/06/5b76b45cbb3a6.jpg"
            "PARK_RECREATION_AREA" -> "https://www.hamburg.de/image/3242800/16x9/990/557/19bcf03f011e6d1477b4e4f17be8a7a5/Cd/bild-fuer-einleitungstext.jpg"
            "HOTEL_MOTEL" -> "https://www.epersianhotel.com/images/other-hotel/kish/toranj/dbl-room1.jpg"
            "MARKET" -> "https://www.epersianhotel.com/images/other-hotel/kish/toranj/view7.jpg"
            "HEALTH_CARE_SERVICE" -> "https://www.healthcareitnews.com/sites/hitn/files/120319%20CaroMont%20Regional%20Medical%20Center%20712.jpg"
            else -> "https://cdn.britannica.com/s:690x388,c:crop/12/130512-004-AD0A7CA4/campus-Riverside-Ottawa-The-Hospital-Ont.jpg"
        }
    }
}