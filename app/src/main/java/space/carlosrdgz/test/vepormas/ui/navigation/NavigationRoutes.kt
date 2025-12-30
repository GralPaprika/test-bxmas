package space.carlosrdgz.test.vepormas.ui.navigation

import kotlinx.serialization.Serializable

sealed class NavigationRoutes() {
    @Serializable
    data object Home : NavigationRoutes()
}


