package space.carlosrdgz.test.vepormas.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query

interface LoremApiService {
    @GET("api/lorem")
    suspend fun getText(@Query("count") count: Int = 1): String
}