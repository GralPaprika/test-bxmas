package space.carlosrdgz.test.vepormas.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query
import space.carlosrdgz.test.vepormas.data.remote.dto.PhotoDto

interface PhotoApiService {
    @GET("photos")
    suspend fun getPhotos(
        @Query("_limit") limit: Int,
        @Query("_start") start: Int,
    ): List<PhotoDto>
}

