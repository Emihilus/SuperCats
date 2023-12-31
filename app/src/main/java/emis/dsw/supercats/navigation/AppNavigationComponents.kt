package emis.dsw.supercats.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import emis.dsw.supercats.viewmodel.AppViewModel

@Composable
fun AppBottomNavigationBar(
    selectedDestination: String,
    navigateToTopLevelDestination: (AppTopLevelDestination) -> Unit,
    viewModel: AppViewModel,
) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        TOP_LEVEL_DESTINATIONS.forEach { destination ->
            NavigationBarItem(
                selected = selectedDestination == destination.route,
                onClick = {
                    navigateToTopLevelDestination(destination)
                    if (destination.route == AppRoute.INTERNET_CATS) {
                        viewModel.getRandomCats()
                    }
                },
                icon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = ""
                    )
                },
                label = { Text(text = destination.label) }
            )
        }
    }
}

