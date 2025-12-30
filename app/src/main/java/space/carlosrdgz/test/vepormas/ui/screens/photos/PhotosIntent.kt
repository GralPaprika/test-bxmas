package space.carlosrdgz.test.vepormas.ui.screens.photos

sealed class PhotosIntent {
    data object LoadPhotos : PhotosIntent()
    data object LoadMorePhotos : PhotosIntent()
    data object RetryLoadPhotos : PhotosIntent()
    data class DeletePhoto(val photoId: Int) : PhotosIntent()
}
