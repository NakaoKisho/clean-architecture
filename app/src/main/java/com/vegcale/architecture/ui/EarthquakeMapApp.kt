package com.vegcale.architecture.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vegcale.architecture.navigation.EmpNavHost
import com.vegcale.architecture.navigation.Map
import com.vegcale.architecture.navigation.navigateSingleTopTo
import com.vegcale.architecture.navigation.tabRowScreens
import com.vegcale.architecture.ui.components.EmpTopAppBar
import com.vegcale.architecture.ui.theme.ArchitectureTheme

@Composable
fun EarthquakeMapApp() {
    ArchitectureTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = tabRowScreens.find { it.route == currentDestination?.route } ?: Map

        Scaffold(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primary)
                .safeDrawingPadding(),
            topBar = {
                EmpTopAppBar(
                    allScreens = tabRowScreens,
                    onTabSelected = { screen -> navController.navigateSingleTopTo(screen.route) },
                    currentScreen = currentScreen,
                )
            }
        ){ innerPadding ->
            EmpNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun EarthquakeMapAppPreview() {
    EarthquakeMapApp()
}