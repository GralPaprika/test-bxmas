package space.carlosrdgz.test.vepormas.ui.navigation

import kotlinx.serialization.Serializable

sealed class NavigationRoutes() {
    @Serializable
    data object Home : NavigationRoutes()

    @Serializable
    data class PhotoDetail(
        val photoId: Int,
        val photoTitle: String,
        val photoUrl: String
    ) : NavigationRoutes()
}
