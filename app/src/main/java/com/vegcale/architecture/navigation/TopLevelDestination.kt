package com.vegcale.architecture.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

interface TopLevelDestination {
    val description: String
    val icon: ImageVector
    val route: String
}

object Map: TopLevelDestination {
    override val description = "Home"
    override val icon = Icons.Filled.Home
    override val route = "home"
}

//object Lists: TopLevelDestination {
//    override val description = "Lists"
//    override val icon = Icons.Filled.List
//    override val route = "lists"
//}

object Settings: TopLevelDestination {
    override val description = "Settings"
    override val icon = Icons.Filled.Settings
    override val route = "settings"
}

//val tabRowScreens = listOf(Map, Lists, Settings)
val tabRowScreens = listOf(Map, Settings)
