package space.carlosrdgz.test.vepormas.ui.screens.photos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import space.carlosrdgz.test.vepormas.domain.PaginationConstants
import space.carlosrdgz.test.vepormas.domain.model.Photo
import space.carlosrdgz.test.vepormas.domain.usecase.GetPhotosUseCase
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PhotosUiState>(PhotosUiState.Loading)
    val uiState: StateFlow<PhotosUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<PhotosUiEffect>()
    val uiEffect = _uiEffect

    private var currentPage = PaginationConstants.PAGINATION_START_INDEX
    private var allPhotos = mutableListOf<Photo>()
    private var isLoadingMore = false

    init {
        handleIntent(PhotosIntent.LoadPhotos)
    }

    fun handleIntent(intent: PhotosIntent) {
        viewModelScope.launch {
            when (intent) {
                is PhotosIntent.LoadPhotos -> loadPhotos()
                is PhotosIntent.LoadMorePhotos -> loadMorePhotos()
                is PhotosIntent.RetryLoadPhotos -> retryLoadPhotos()
                is PhotosIntent.DeletePhoto -> deletePhoto(intent.photoId)
            }
        }
    }

    private suspend fun loadPhotos() {
        _uiState.value = PhotosUiState.Loading
        getPhotosUseCase(
            limit = PaginationConstants.PHOTO_PAGE_SIZE,
            start = PaginationConstants.PAGINATION_START_INDEX
        ).collect { result ->
            result
                .onSuccess { photos ->
                    currentPage = PaginationConstants.PAGINATION_START_INDEX
                    allPhotos = photos.toMutableList()
                    _uiState.value = PhotosUiState.Success(
                        photos = allPhotos,
                        hasMorePages = photos.size >= PaginationConstants.PHOTO_PAGE_SIZE
                    )
                }
                .onFailure { error ->
                    Log.e("PhotosList", "Error loading photos", error)
                    _uiState.value = PhotosUiState.Error(canRetry = true)
                }
        }
    }

    private suspend fun loadMorePhotos() {
        if (isLoadingMore) return

        val currentState = _uiState.value
        if (currentState !is PhotosUiState.Success || !currentState.hasMorePages) return

        isLoadingMore = true
        _uiState.value = currentState.copy(isLoadingMore = true, loadMoreError = false)

        currentPage += PaginationConstants.PHOTO_PAGE_SIZE
        getPhotosUseCase(
            limit = PaginationConstants.PHOTO_PAGE_SIZE,
            start = currentPage
        ).collect { result ->
            result
                .onSuccess { newPhotos ->
                    allPhotos.addAll(newPhotos)
                    _uiState.value = PhotosUiState.Success(
                        photos = allPhotos,
                        hasMorePages = newPhotos.size >= PaginationConstants.PHOTO_PAGE_SIZE,
                        isLoadingMore = false,
                        loadMoreError = false,
                    )
                    isLoadingMore = false
                }
                .onFailure { error ->
                    Log.e("PhotosList", "Error loading more photos", error)
                    currentPage -= PaginationConstants.PHOTO_PAGE_SIZE
                    isLoadingMore = false
                    _uiState.value = currentState.copy(
                        isLoadingMore = false,
                        loadMoreError = true,
                    )
                }
        }
    }

    private suspend fun retryLoadPhotos() {
        loadPhotos()
    }

    private suspend fun deletePhoto(photoId: Int) {
        val currentState = _uiState.value
        if (currentState !is PhotosUiState.Success) return

        try {
            val updatedPhotos = currentState.photos.filter { it.id != photoId }
            _uiState.value = currentState.copy(photos = updatedPhotos)
            _uiEffect.emit(PhotosUiEffect.DeleteSuccess)
        } catch (e: Exception) {
            Log.e("PhotosList", "Error deleting photo", e)
        }
    }
}

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

sealed class PhotosIntent {
    data object LoadPhotos : PhotosIntent()
    data object LoadMorePhotos : PhotosIntent()
    data object RetryLoadPhotos : PhotosIntent()
    data class DeletePhoto(val photoId: Int) : PhotosIntent()
}

sealed class PhotosUiEffect {
    data object DeleteSuccess : PhotosUiEffect()
}

