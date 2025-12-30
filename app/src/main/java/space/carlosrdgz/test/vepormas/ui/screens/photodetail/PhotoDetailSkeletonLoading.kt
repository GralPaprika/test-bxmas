package space.carlosrdgz.test.vepormas.ui.screens.photodetail

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import space.carlosrdgz.test.vepormas.ui.theme.TestBXTheme

@Composable
fun PhotoDetailSkeletonLoading(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "photoDetailSkeleton")
    val alpha = infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(1000, easing = LinearEasing)
        ),
        label = "skeletonAlpha"
    )

    val skeletonColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha.value)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { },
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Image skeleton
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .height(300.dp)
                .background(
                    color = skeletonColor,
                    shape = RoundedCornerShape(12.dp)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(12.dp)
                    .background(
                        color = skeletonColor,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(bottom = 8.dp)
            )
            repeat(2) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp)
                        .background(
                            color = skeletonColor,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(bottom = 6.dp)
                )
            }

            Box(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .height(12.dp)
                    .background(
                        color = skeletonColor,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(bottom = 8.dp)
            )

            repeat(3) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .background(
                            color = skeletonColor,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(bottom = 6.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotoDetailSkeletonLoadingPreview() {
    TestBXTheme {
        PhotoDetailSkeletonLoading()
    }
}

