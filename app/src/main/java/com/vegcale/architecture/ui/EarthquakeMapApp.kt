package com.vegcale.architecture.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vegcale.architecture.navigation.List
import com.vegcale.architecture.navigation.Map
import com.vegcale.architecture.navigation.Settings
import com.vegcale.architecture.navigation.TopLevelDestination
import com.vegcale.architecture.navigation.tabRowScreens
import com.vegcale.architecture.ui.components.EarthquakeMapTopAppBar
import com.vegcale.architecture.ui.theme.ArchitectureTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EarthquakeMapApp() {
    ArchitectureTheme {
        var currentScreen: TopLevelDestination by remember { mutableStateOf(Map) }
        val navController = rememberNavController()

        Scaffold(
            topBar = {
                EarthquakeMapTopAppBar(
                    allScreens = tabRowScreens,
                    onTabSelected = { screen -> currentScreen = screen },
                    currentScreen = currentScreen
                )
            }
        ){ innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Map.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = Map.route) {
                    Map.screen()
                }
                composable(route = Map.route) {
                    List.screen()
                }
                composable(route = Map.route) {
                    Settings.screen()
                }
            }
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