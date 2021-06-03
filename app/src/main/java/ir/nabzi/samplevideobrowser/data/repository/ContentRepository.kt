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
                Content("id1" , "Red color", "Red is the color at the long wavelength end of the visible spectrum of light, next to orange and opposite violet. It has a dominant wavelength of approximately 625–740 nanometres.[1] It is a primary color in the RGB color model and the CMYK color model" ,
                "https://www.healthcareitnews.com/sites/hitn/files/120319%20CaroMont%20Regional%20Medical%20Center%20712.jpg",
                "https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_5mb.mp4",
                        70 , 2,0,3
                        ),
                Content("id2" , "Blue" , "Blue is one of the three primary colours of pigments in painting and traditional colour theory, as well as in the RGB colour model. It lies between violet and green on the spectrum of visible light. The eye perceives blue when observing light with a dominant wavelength between approximately 450 and 495 nanometres." ,
                        "https://cdn.britannica.com/s:690x388,c:crop/12/130512-004-AD0A7CA4/campus-Riverside-Ottawa-The-Hospital-Ont.jpg",
                        "https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_5mb.mp4",
                        20 , 5,4,2
                ),
                Content("id3" , "Black White Black White Black White Black White" , "Black is a color which results from the absence or complete absorption of visible light. It is an achromatic color, without hue, like white and gray." ,
                        "http://riyainfosystems.com/wp-content/uploads/2019/06/5b76b45cbb3a6.jpg",
                        "https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_5mb.mp4",
                        21 , 5,1,10
                )
        ).filter {
            it.title.startsWith(search)
        }
    }

}