package space.carlosrdgz.test.vepormas.ui.screens.photos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import space.carlosrdgz.test.vepormas.domain.model.Photo
import space.carlosrdgz.test.vepormas.ui.theme.TestBXTheme

@Composable
fun PhotoItem(
    modifier: Modifier = Modifier,
    photo: Photo,
    onPhotoClick: (Photo) -> Unit = {},
    onLongPress: (Photo) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .combinedClickable(
                    onClick = { onPhotoClick(photo) },
                    onLongClick = { onLongPress(photo) }
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = photo.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(2f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(photo.url)
                        .crossfade(true)
                        .build(),
                    contentDescription = photo.title,
                    modifier = Modifier
                        .requiredSize(60.dp, 45.dp)
                        .weight(1f)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.small
                        ),
                    contentScale = ContentScale.Crop
                )
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PhotoItemListPreview() {
    TestBXTheme {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            PhotoItem(
                photo = Photo(
                    id = 1,
                    title = "Sample Photo 1",
                    url = "https://picsum.photos/300/200"
                ),
                onPhotoClick = {}
            )
            PhotoItem(
                photo = Photo(
                    id = 2,
                    title = "Sample Photo 2",
                    url = "https://picsum.photos/300/200"
                ),
                onPhotoClick = {}
            )
            PhotoItem(
                photo = Photo(
                    id = 3,
                    title = "Sample Photo 3",
                    url = "https://picsum.photos/300/200"
                ),
                onPhotoClick = {}
            )
        }
    }
}
