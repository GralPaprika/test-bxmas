package space.carlosrdgz.test.vepormas.ui.screens.photos

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import space.carlosrdgz.test.vepormas.R
import space.carlosrdgz.test.vepormas.domain.model.Photo
import space.carlosrdgz.test.vepormas.ui.common.components.DeleteConfirmationDialog
import space.carlosrdgz.test.vepormas.ui.screens.photos.components.PhotoItem
import space.carlosrdgz.test.vepormas.ui.screens.photos.components.SkeletonPhotoItem
import space.carlosrdgz.test.vepormas.ui.theme.TestBXTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: PhotosViewModel = hiltViewModel(),
    onPhotoClick: (Photo) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is PhotosUiEffect.DeleteSuccess -> {
                    Toast.makeText(context,
                        context.getString(R.string.toast_deleted_text), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.home_screen_title),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            when (uiState) {
                is PhotosUiState.Loading -> {
                    LoadingScreen()
                }

                is PhotosUiState.Success -> {
                    val state = uiState as PhotosUiState.Success
                    PhotosList(
                        photos = state.photos,
                        isLoadingMore = state.isLoadingMore,
                        hasMorePages = state.hasMorePages,
                        loadMoreError = state.loadMoreError,
                        onLoadMore = {
                            viewModel.handleIntent(PhotosIntent.LoadMorePhotos)
                        },
                        onPhotoClick = onPhotoClick,
                        onDeletePhoto = { photoId ->
                            viewModel.handleIntent(PhotosIntent.DeletePhoto(photoId))
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is PhotosUiState.Error -> {
                    val state = uiState as PhotosUiState.Error
                    HomeErrorScreen(
                        canRetry = state.canRetry,
                        onRetry = {
                            viewModel.handleIntent(PhotosIntent.RetryLoadPhotos)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PhotosList(
    photos: List<Photo>,
    isLoadingMore: Boolean,
    hasMorePages: Boolean,
    loadMoreError: Boolean,
    onLoadMore: () -> Unit,
    onPhotoClick: (Photo) -> Unit = {},
    onDeletePhoto: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    val selectedPhotoForDelete = remember { mutableStateOf<Photo?>(null) }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= photos.size - 3 &&
                    hasMorePages &&
                    !isLoadingMore &&
                    !loadMoreError) {
                    onLoadMore()
                }
            }
    }

    if (selectedPhotoForDelete.value != null) {
        DeleteConfirmationDialog(
            title = stringResource(R.string.delete_confirmation_dialog_title),
            message = stringResource(R.string.delete_confirmation_dialog_message),
            onConfirm = {
                selectedPhotoForDelete.value?.let { photo ->
                    onDeletePhoto(photo.id)
                    selectedPhotoForDelete.value = null
                }
            },
            onDismiss = {
                selectedPhotoForDelete.value = null
            }
        )
    }

    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        items(
            items = photos,
            key = { photo -> photo.id }
        ) { photo ->
            PhotoItem(
                photo = photo,
                onPhotoClick = onPhotoClick,
                onLongPress = { selectedPhotoForDelete.value = it }
            )
        }

        if (isLoadingMore) {
            items(3) {
                SkeletonPhotoItem()
            }
        }

        if (loadMoreError) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.home_screen_loading_photos_error_title),
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                        Text(
                            text = stringResource(R.string.home_screen_loading_photos_error_subtitle),
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 10.sp
                        )
                        Button(
                            onClick = onLoadMore,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(stringResource(R.string.btn_retry))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TestBXTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            PhotosList(
                photos = listOf(
                    Photo(1, "Sample Photo 1", ""),
                    Photo(2, "Sample Photo 2", ""),
                    Photo(3, "Sample Photo 3", ""),
                ),
                isLoadingMore = false,
                hasMorePages = true,
                loadMoreError = true,
                onLoadMore = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

