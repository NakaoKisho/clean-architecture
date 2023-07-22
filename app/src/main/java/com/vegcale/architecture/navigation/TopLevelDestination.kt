package com.vegcale.architecture.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.vegcale.architecture.ui.MainScreen

interface TopLevelDestination {
    val icon: ImageVector
    val route: String
    val screen: @Composable () -> Unit
}

/**
 * Rally app navigation destinations
 */
object Map : TopLevelDestination {
    override val icon = Icons.Filled.Home
    override val route = "overview"
    override val screen: @Composable () -> Unit = { MainScreen() }
}

object List : TopLevelDestination {
    override val icon = Icons.Filled.Home
    override val route = "accounts"
    override val screen: @Composable () -> Unit = { MainScreen() }
}

object Settings : TopLevelDestination {
    override val icon = Icons.Filled.Home
    override val route = "bills"
    override val screen: @Composable () -> Unit = { MainScreen() }
}

// Screens to be displayed in the top RallyTabRow
val tabRowScreens = listOf(Map, List, Settings)