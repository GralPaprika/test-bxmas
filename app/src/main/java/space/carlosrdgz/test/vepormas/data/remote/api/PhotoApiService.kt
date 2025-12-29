package space.carlosrdgz.test.vepormas.data.remote.api

import retrofit2.http.GET
import space.carlosrdgz.test.vepormas.data.remote.dto.PhotoDto

interface PhotoApiService {
    @GET("photos")
    suspend fun getPhotos(): List<PhotoDto>
}

