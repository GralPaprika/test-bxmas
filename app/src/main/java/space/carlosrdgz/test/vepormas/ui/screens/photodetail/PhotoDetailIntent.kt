package space.carlosrdgz.test.vepormas.ui.screens.photodetail

import androidx.lifecycle.SavedStateHandle

sealed class PhotoDetailIntent {
    data class LoadPhotoDetail(val savedStateHandle: SavedStateHandle) : PhotoDetailIntent()
    data object GoBack : PhotoDetailIntent()
}
