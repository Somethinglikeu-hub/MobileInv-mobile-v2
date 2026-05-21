package com.bistpicker.mobile.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.bistpicker.mobile.R
import com.bistpicker.mobile.ui.screens.home.HomeScreen
import com.bistpicker.mobile.ui.screens.scoring.ScoringScreen
import com.bistpicker.mobile.ui.screens.detail.DetailScreen
import com.bistpicker.mobile.ui.screens.about.AboutScreen
import com.bistpicker.mobile.ui.screens.macro.MacroScreen
import com.bistpicker.mobile.ui.screens.history.HistoryScreen
import com.bistpicker.mobile.ui.theme.BistPickerTheme

sealed class Screen(val route: String, val labelId: Int, val icon: ImageVector) {
    object Home : Screen("home", R.string.nav_home, Icons.Default.Home)
    object Scoring : Screen("scoring", R.string.nav_scoring, Icons.Default.Search)
    object Market : Screen("market", R.string.nav_macro, Icons.Default.Public)
    object History : Screen("history", R.string.nav_history, Icons.Default.History)
    object About : Screen("about", R.string.nav_about, Icons.Default.Info)
    object Detail : Screen("detail/{ticker}", 0, Icons.Default.Home)
}

@Composable
fun BistPickerApp() {
    val navController = rememberNavController()

    BistPickerTheme {
        val screens = listOf(Screen.Home, Screen.Scoring, Screen.Market, Screen.History, Screen.About)
        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    screens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(stringResource(screen.labelId), softWrap = false) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController,
                startDestination = Screen.Home.route,
                Modifier.padding(innerPadding)
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        onNavigateToDetail = { ticker ->
                            navController.navigate("detail/$ticker")
                        }
                    )
                }
                composable(Screen.Scoring.route) {
                    ScoringScreen(
                        onNavigateToDetail = { ticker ->
                            navController.navigate("detail/$ticker")
                        }
                    )
                }
                composable(Screen.Market.route) {
                    MacroScreen()
                }
                composable(Screen.History.route) {
                    HistoryScreen()
                }
                composable(Screen.About.route) {
                    AboutScreen()
                }
                composable(Screen.Detail.route) { backStackEntry ->
                    val ticker = backStackEntry.arguments?.getString("ticker") ?: ""
                    DetailScreen(
                        ticker = ticker,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
