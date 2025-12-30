package space.carlosrdgz.test.vepormas.domain.repository

import kotlinx.coroutines.flow.Flow
import space.carlosrdgz.test.vepormas.domain.model.Photo

interface PhotoRepository {
    fun getPhotos(
        limit: Int,
        start: Int,
    ): Flow<Result<List<Photo>>>

    fun getPhotoText(): Flow<Result<String>>
}

