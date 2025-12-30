package space.carlosrdgz.test.vepormas.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import space.carlosrdgz.test.vepormas.data.mapper.PhotoMapper.toPhotoList
import space.carlosrdgz.test.vepormas.data.remote.api.PhotoApiService
import space.carlosrdgz.test.vepormas.domain.model.Photo
import space.carlosrdgz.test.vepormas.domain.repository.PhotoRepository

class PhotoRepositoryImpl(
    private val photoApiService: PhotoApiService
) : PhotoRepository {
    override fun getPhotos(limit: Int, start: Int): Flow<List<Photo>> = flow {
        val photos = photoApiService.getPhotos(limit, start).toPhotoList()
        emit(photos)
    }
}

