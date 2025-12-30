package space.carlosrdgz.test.vepormas.ui.screens.photos

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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import space.carlosrdgz.test.vepormas.domain.model.Photo
import space.carlosrdgz.test.vepormas.ui.theme.TestBXTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: PhotosViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is PhotosUiEffect.ShowError -> {
                    // Handle error effect if needed
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
                        text = "Photos",
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
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is PhotosUiState.Error -> {
                    val state = uiState as PhotosUiState.Error
                    ErrorScreen(
                        message = state.message,
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
    loadMoreError: String?,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= photos.size - 3 &&
                    hasMorePages &&
                    !isLoadingMore &&
                    loadMoreError == null) {
                    onLoadMore()
                }
            }
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
                onPhotoClick = {}
            )
        }

        if (isLoadingMore) {
            items(3) {
                SkeletonPhotoItem()
            }
        }

        if (loadMoreError != null) {
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
                            text = loadMoreError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Button(
                            onClick = onLoadMore,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorScreen(
    message: String,
    canRetry: Boolean,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Error: $message",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            if (canRetry) {
                Button(
                    onClick = onRetry,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Retry")
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
                loadMoreError = null,
                onLoadMore = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

