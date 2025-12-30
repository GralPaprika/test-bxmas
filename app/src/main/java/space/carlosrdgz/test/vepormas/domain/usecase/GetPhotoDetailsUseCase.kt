package space.carlosrdgz.test.vepormas.domain.usecase

import space.carlosrdgz.test.vepormas.domain.repository.PhotoRepository
import javax.inject.Inject

class GetPhotoDetailsUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {
    operator fun invoke() = photoRepository.getPhotoText()
}