package space.carlosrdgz.test.vepormas.ui.screens.photodetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import space.carlosrdgz.test.vepormas.domain.usecase.GetPhotoDetailsUseCase
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPhotoDetailsUseCase: GetPhotoDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PhotoDetailUiState>(PhotoDetailUiState.Loading)
    val uiState: StateFlow<PhotoDetailUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<PhotoDetailUiEffect>()
    val uiEffect = _uiEffect

    init {
        handleIntent(PhotoDetailIntent.LoadPhotoDetail(savedStateHandle))
    }

    fun handleIntent(intent: PhotoDetailIntent) {
        viewModelScope.launch {
            when (intent) {
                is PhotoDetailIntent.LoadPhotoDetail -> loadPhotoDetail(intent.savedStateHandle)
                is PhotoDetailIntent.GoBack -> _uiEffect.emit(PhotoDetailUiEffect.NavigateBack)
            }
        }
    }

    private suspend fun loadPhotoDetail(savedStateHandle: SavedStateHandle) {
        val photoId = savedStateHandle.get<Int>("photoId") ?: throw Exception("Photo ID not found")
        val photoTitle = savedStateHandle.get<String>("photoTitle") ?: "Unknown"
        val photoUrl = savedStateHandle.get<String>("photoUrl") ?: ""

        getPhotoDetailsUseCase().collect { result ->
            result.onSuccess {
                val photo = PhotoDetailedInfo(
                    id = photoId,
                    title = photoTitle,
                    url = photoUrl,
                    description = it
                )

                _uiState.value = PhotoDetailUiState.Success(photo)
            }.onFailure {
                val errorMessage = it.message ?: "Unknown error occurred"
                _uiState.value = PhotoDetailUiState.Error(errorMessage)
                _uiEffect.emit(PhotoDetailUiEffect.ShowError(errorMessage))
            }
        }
    }
}

sealed class PhotoDetailUiState {
    data object Loading : PhotoDetailUiState()
    data class Success(val photo: PhotoDetailedInfo) : PhotoDetailUiState()
    data class Error(val message: String) : PhotoDetailUiState()
}

sealed class PhotoDetailIntent {
    data class LoadPhotoDetail(val savedStateHandle: SavedStateHandle) : PhotoDetailIntent()
    data object GoBack : PhotoDetailIntent()
}

sealed class PhotoDetailUiEffect {
    data object NavigateBack : PhotoDetailUiEffect()
    data class ShowError(val message: String) : PhotoDetailUiEffect()
}

data class PhotoDetailedInfo(
    val id: Int,
    val url: String,
    val title: String,
    val description: String,
)

