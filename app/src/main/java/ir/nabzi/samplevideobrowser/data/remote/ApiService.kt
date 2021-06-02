package ir.nabzi.samplevideobrowser.data.remote

import ir.nabzi.aroundme.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/search/2/nearbySearch/.json")
    suspend  fun getContentList(@Query("search")search : String ) :Response<SearchResponse>
}