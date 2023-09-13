package com.vegcale.architecture.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vegcale.architecture.feature.search.SearchScreen
import com.vegcale.architecture.feature.settings.SettingsScreen

@Composable
fun EmpNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Map.route,
        modifier = modifier
    ) {
        composable(route = Map.route) {
            SearchScreen()
        }
//        composable(route = Lists.route) {
//            SearchScreen()
//        }
        composable(route = Settings.route) {
            SettingsScreen()
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }