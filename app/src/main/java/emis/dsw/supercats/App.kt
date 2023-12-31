package emis.dsw.supercats

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import emis.dsw.supercats.navigation.AppBottomNavigationBar
import emis.dsw.supercats.navigation.AppNavigationActions
import emis.dsw.supercats.navigation.AppRoute
import emis.dsw.supercats.navigation.AppTopLevelDestination
import emis.dsw.supercats.screens.InternetCatsScreen
import emis.dsw.supercats.screens.SavedCatsScreen
import emis.dsw.supercats.viewmodel.AppUiState
import emis.dsw.supercats.viewmodel.AppViewModel


@Composable
fun App(
    viewModel: AppViewModel,
    appUiState: AppUiState,
    getRndCats: () -> Unit,
    createFolder: (String) -> Unit,
    saveCatImage: (String) -> Unit,
) {
    NavigationWrapper(
        viewModel = viewModel,
        appUiState = appUiState,
        getRndCats = getRndCats,
        createFolder = createFolder,
        saveCatImage = saveCatImage,
    )
}

@Composable
fun NavigationWrapper(
    viewModel: AppViewModel,
    appUiState: AppUiState,
    getRndCats: () -> Unit,
    createFolder: (String) -> Unit,
    saveCatImage: (String) -> Unit,
) {
    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        AppNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: AppRoute.INTERNET_CATS

    AppContent(
        viewModel = viewModel,
        appUiState = appUiState,
        navController = navController,
        selectedDestination = selectedDestination,
        navigateToTopLevelDestination = navigationActions::navigateTo,
        getRndCats = getRndCats,
        createFolder = createFolder,
        saveCatImage = saveCatImage,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier,
    appUiState: AppUiState,
    navController: NavHostController,
    selectedDestination: String,
    navigateToTopLevelDestination: (AppTopLevelDestination) -> Unit,
    getRndCats: () -> Unit,
    createFolder: (String) -> Unit,
    saveCatImage: (String) -> Unit,
) {
    Row {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            TopAppBar(
                title = {
                    Text(text = "SuperCats")
                },
                navigationIcon = {
                    IconButton(onClick = { /* Obsługa kliknięcia ikony nawigacji */ }) {
                        Image(
                            painter = painterResource(id = R.drawable.cat),
                            contentDescription = null
                        )
                    }
                },
            )
            AppNavHost(
                viewModel = viewModel,
                navController = navController,
                appUiState = appUiState,
                modifier = Modifier.weight(1f),
                getRndCats = getRndCats,
                createFolder = createFolder,
                saveCatImage = saveCatImage,
            )
            AnimatedVisibility(visible = true) {
                AppBottomNavigationBar(
                    viewModel = viewModel,
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination
                )
            }
        }
    }
}

@Composable
private fun AppNavHost(
    viewModel: AppViewModel,
    navController: NavHostController,
    appUiState: AppUiState,
    modifier: Modifier = Modifier,
    getRndCats: () -> Unit,
    createFolder: (String) -> Unit,
    saveCatImage: (String) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppRoute.INTERNET_CATS,
    ) {
        composable(AppRoute.INTERNET_CATS) {
            InternetCatsScreen(
                cats = appUiState.cats,
                getRndCats = getRndCats,
                createFolder = createFolder,
                saveCatImage = saveCatImage,
                viewModel = viewModel,
            )
        }
        composable(AppRoute.SAVED_CATS) {
            SavedCatsScreen(
                appUiState = appUiState,
                viewModel = viewModel,
            )
        }
    }
}