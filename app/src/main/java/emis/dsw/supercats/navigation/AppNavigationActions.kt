package emis.dsw.supercats.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object AppRoute {
    const val INTERNET_CATS = "1"
    const val SAVED_CATS = "2"
}

data class AppTopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val label: String,
)

class AppNavigationActions(private val navController: NavHostController) {

    fun navigateTo(destination: AppTopLevelDestination) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

val TOP_LEVEL_DESTINATIONS = listOf(
    AppTopLevelDestination(
        route = AppRoute.INTERNET_CATS,
        selectedIcon = Icons.Default.Inbox,
        unselectedIcon = Icons.Default.Inbox,
        label = "Losuj Koty"
    ),
    AppTopLevelDestination(
        route = AppRoute.SAVED_CATS,
        selectedIcon = Icons.Default.Article,
        unselectedIcon = Icons.Default.Article,
        label = "Zapisane Koty"
    ),

)
