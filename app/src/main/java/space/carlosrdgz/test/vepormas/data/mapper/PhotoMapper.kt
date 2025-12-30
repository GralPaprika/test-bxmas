package space.carlosrdgz.test.vepormas.data.mapper

import space.carlosrdgz.test.vepormas.data.remote.dto.PhotoDto
import space.carlosrdgz.test.vepormas.domain.model.Photo

object PhotoMapper {
    fun PhotoDto.toPhotoList(): Photo = Photo(
        id = id,
        title = title,
        url = "https://picsum.photos/300/200",
    )

    fun List<PhotoDto>.toPhotoList(): List<Photo> = map { it.toPhotoList() }
}

