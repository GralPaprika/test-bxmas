package space.carlosrdgz.test.vepormas.domain.repository

import kotlinx.coroutines.flow.Flow
import space.carlosrdgz.test.vepormas.domain.model.Photo

interface PhotoRepository {
    fun getPhotos(): Flow<List<Photo>>
}

