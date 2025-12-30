package space.carlosrdgz.test.vepormas.ui.screens.photos

import space.carlosrdgz.test.vepormas.domain.model.Photo

sealed class PhotosUiState {
    data object Loading : PhotosUiState()
    data class Success(
        val photos: List<Photo> = emptyList(),
        val hasMorePages: Boolean = false,
        val isLoadingMore: Boolean = false,
        val loadMoreError: Boolean = false
    ) : PhotosUiState()
    data class Error(val canRetry: Boolean = true) : PhotosUiState()
}
