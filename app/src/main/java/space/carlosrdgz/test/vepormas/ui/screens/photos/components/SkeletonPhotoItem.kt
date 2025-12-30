package space.carlosrdgz.test.vepormas.ui.screens.photos.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import space.carlosrdgz.test.vepormas.ui.theme.TestBXTheme

@Composable
fun SkeletonPhotoItem(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton")
    val alpha = infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing)
        ),
        label = "skeletonAlpha"
    )

    val skeletonColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha.value)

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .weight(2f)
                        .padding(horizontal = 16.dp)
                        .background(
                            color = skeletonColor,
                            shape = MaterialTheme.shapes.small
                        )
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                        .requiredSize(60.dp, 45.dp)
                        .background(
                            color = skeletonColor,
                            shape = MaterialTheme.shapes.small
                        )
                    )
                }
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SkeletonPhotoItemPreview() {
    TestBXTheme {
        Column {
            repeat(3) {
                SkeletonPhotoItem()
            }
        }
    }
}

