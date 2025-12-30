package space.carlosrdgz.test.vepormas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import space.carlosrdgz.test.vepormas.ui.screens.photodetail.PhotoDetailScreen
import space.carlosrdgz.test.vepormas.ui.screens.photos.HomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.Home
    ) {
        composable<NavigationRoutes.Home> {
            HomeScreen(
                onPhotoClick = { photo ->
                    navController.navigate(
                        NavigationRoutes.PhotoDetail(
                            photoId = photo.id,
                            photoTitle = photo.title,
                            photoUrl = photo.url
                        )
                    )
                }
            )
        }

        composable<NavigationRoutes.PhotoDetail> {
            PhotoDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
