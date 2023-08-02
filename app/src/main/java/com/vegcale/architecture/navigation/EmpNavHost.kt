package com.vegcale.architecture.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vegcale.architecture.ui.ListsScreen
import com.vegcale.architecture.ui.MainScreen
import com.vegcale.architecture.ui.SettingsScreen

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
            MainScreen()
        }
        composable(route = Lists.route) {
            ListsScreen()
        }
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