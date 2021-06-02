package ir.nabzi.samplevideobrowser.data.repository

import ir.nabzi.samplevideobrowser.data.repository.NetworkCall
import ir.nabzi.samplevideobrowser.data.repository.RemoteResource
import ir.nabzi.samplevideobrowser.model.SearchResponse
import ir.nabzi.samplevideobrowser.data.db.ContentDao
import ir.nabzi.samplevideobrowser.data.remote.ApiService
import ir.nabzi.samplevideobrowser.data.repository.ContentRepositoryImpl.Companion.PAGE_SIZE
import ir.nabzi.samplevideobrowser.model.Content
import ir.nabzi.samplevideobrowser.model.Resource
import ir.nabzi.samplevideobrowser.model.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response

interface ContentRepository {
    fun getContents(
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

    override fun getContents(
        search: String,
        coroutineScope: CoroutineScope,
        page: Int,
        shouldFetch: Boolean
    )
            : StateFlow<Resource<List<Content>>?> {
        return object : RemoteResource<List<Content>>() {
            override suspend fun updateDB(result: List<Content>) {
                dbDataSource.clear()
                dbDataSource.update(result)
            }

            override fun getFromDB(): Flow<List<Content>> {
                return dbDataSource.getContents(search)
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

    fun getContents(search: String): Flow<List<Content>> {
        return ContentDao.getContentsFlow("$search%")
    }
}

class ContentRemoteDataSource(val apiServices: ApiService) {
    suspend fun getContents(search : String = ""): Resource<List<Content>> {
        val res = object : NetworkCall<SearchResponse>() {
            override suspend fun createCall(): Response<SearchResponse> {
                return apiServices.getContentList(search)
            }
        }.fetch()
        //var result = res.data?.let { searchResponseToContents(it , search) }
        var result = searchResponseToContents(null , search)
        //return Resource( res.status, result?.first, res.message, res.errorCode , result?.second )
        return Resource( Status.SUCCESS, result?.first, res.message, res.errorCode , result?.second )
    }

    private fun searchResponseToContents(searchResponse: SearchResponse? , search: String): Pair<List<Content>, Boolean> {
        return Pair(getMockData(search), false)

    }

    private fun getMockData(search: String = ""): List<Content> {
        return arrayListOf(
                Content("id1" , "avideo 1" , "Video 1 Video 1 Video 1 Video 1" ,
                "https://www.healthcareitnews.com/sites/hitn/files/120319%20CaroMont%20Regional%20Medical%20Center%20712.jpg",
                "https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_5mb.mp4",
                        10 , 2,0
                        ),
                Content("id2" , "avadeo 2" , "Video 2 Video 1 Video 1 Video 1" ,
                        "https://cdn.britannica.com/s:690x388,c:crop/12/130512-004-AD0A7CA4/campus-Riverside-Ottawa-The-Hospital-Ont.jpg",
                        "https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_5mb.mp4",
                        20 , 5,0
                ),
                Content("id3" , "bvideo 3" , "Video3  Video 1 Video 1 Video 1" ,
                        "http://riyainfosystems.com/wp-content/uploads/2019/06/5b76b45cbb3a6.jpg",
                        "https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_5mb.mp4",
                        21 , 5,1
                )
        ).filter {
            it.title.startsWith(search)
        }
    }

}