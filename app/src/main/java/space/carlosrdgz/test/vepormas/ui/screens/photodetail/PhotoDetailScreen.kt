package space.carlosrdgz.test.vepormas.ui.screens.photodetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import space.carlosrdgz.test.vepormas.domain.model.Photo
import space.carlosrdgz.test.vepormas.ui.theme.TestBXTheme

@Composable
fun PhotoDetailScreen(
    viewModel: PhotoDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is PhotoDetailUiEffect.NavigateBack -> onNavigateBack()
                is PhotoDetailUiEffect.ShowError -> {
                    // Handle error effect if needed
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (uiState) {
            is PhotoDetailUiState.Loading -> {
                PhotoDetailSkeletonLoading()
            }

            is PhotoDetailUiState.Success -> {
                val photo = (uiState as PhotoDetailUiState.Success).photo
                PhotoDetailContent(
                    photo = photo,
                    onBackClick = { viewModel.handleIntent(PhotoDetailIntent.GoBack) }
                )
            }

            is PhotoDetailUiState.Error -> {
                val errorMessage = (uiState as PhotoDetailUiState.Error).message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: $errorMessage",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun PhotoDetailContent(
    photo: PhotoDetailedInfo,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photo.url)
                .crossfade(true)
                .build(),
            contentDescription = photo.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .height(300.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                ),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = photo.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = photo.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotoDetailScreenPreview() {
    TestBXTheme {
        PhotoDetailContent(
            photo = PhotoDetailedInfo(
                id = 1,
                title = "Sample Photo Title",
                url = "https://picsum.photos/600/400",
                description = "This is a detailed description of the sample photo."
            )
        )
    }
}

