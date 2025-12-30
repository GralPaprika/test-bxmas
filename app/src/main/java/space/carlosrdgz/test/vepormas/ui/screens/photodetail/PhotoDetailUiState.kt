package space.carlosrdgz.test.vepormas.ui.screens.photodetail

sealed class PhotoDetailUiState {
    data object Loading : PhotoDetailUiState()
    data class Success(val photo: PhotoDetailedInfo) : PhotoDetailUiState()
    data object Error : PhotoDetailUiState()
}
