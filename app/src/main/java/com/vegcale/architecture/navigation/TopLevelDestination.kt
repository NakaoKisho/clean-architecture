package com.vegcale.architecture.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.vegcale.architecture.ui.MainScreen

interface TopLevelDestination {
    val description: String
    val icon: ImageVector
    val route: String
    val screen: @Composable () -> Unit
}

/**
 * Rally app navigation destinations
 */
object Map: TopLevelDestination {
    override val description = "Home"
    override val icon = Icons.Filled.Home
    override val route = "home"
    override val screen: @Composable () -> Unit = { MainScreen() }
}

object List: TopLevelDestination {
    override val description = "Lists"
    override val icon = Icons.Filled.List
    override val route = "lists"
    override val screen: @Composable () -> Unit = { MainScreen() }
}

object Settings: TopLevelDestination {
    override val description = "Settings"
    override val icon = Icons.Filled.Settings
    override val route = "settings"
    override val screen: @Composable () -> Unit = { MainScreen() }
}

// Screens to be displayed in the top RallyTabRow
val tabRowScreens = listOf(Map, List, Settings)