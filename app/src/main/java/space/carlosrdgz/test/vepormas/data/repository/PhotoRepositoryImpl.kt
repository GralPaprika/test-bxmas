package space.carlosrdgz.test.vepormas.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import space.carlosrdgz.test.vepormas.data.mapper.PhotoMapper.toPhotoList
import space.carlosrdgz.test.vepormas.data.remote.api.LoremApiService
import space.carlosrdgz.test.vepormas.data.remote.api.PhotoApiService
import space.carlosrdgz.test.vepormas.domain.model.Photo
import space.carlosrdgz.test.vepormas.domain.repository.PhotoRepository

class PhotoRepositoryImpl(
    private val photoApiService: PhotoApiService,
    private val loremApiService: LoremApiService,
) : PhotoRepository {
    override fun getPhotos(limit: Int, start: Int): Flow<Result<List<Photo>>> = flow {
        try {
            val photos = photoApiService.getPhotos(limit, start).toPhotoList()
            emit(Result.success(photos))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getPhotoText(): Flow<Result<String>> = flow {
        try {
            val text = loremApiService.getText(count = 2)
            emit(Result.success(text))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}

