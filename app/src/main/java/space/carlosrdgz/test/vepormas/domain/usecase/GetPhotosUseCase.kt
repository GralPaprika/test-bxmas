package space.carlosrdgz.test.vepormas.domain.usecase

import kotlinx.coroutines.flow.Flow
import space.carlosrdgz.test.vepormas.domain.PaginationConstants
import space.carlosrdgz.test.vepormas.domain.model.Photo
import space.carlosrdgz.test.vepormas.domain.repository.PhotoRepository
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {
    operator fun invoke(
        limit: Int = PaginationConstants.PHOTO_PAGE_SIZE,
        start: Int = PaginationConstants.PAGINATION_START_INDEX,
    ): Flow<Result<List<Photo>>> = photoRepository.getPhotos(limit, start)
}

