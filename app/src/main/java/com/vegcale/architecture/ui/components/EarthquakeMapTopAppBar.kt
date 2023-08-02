package com.vegcale.architecture.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.vegcale.architecture.navigation.TopLevelDestination

private val TabHeight = 56.dp
private const val InactiveTabOpacity = 0.60f
private const val TabFadeInAnimationDuration = 150
private const val TabFadeInAnimationDelay = 100
private const val TabFadeOutAnimationDuration = 100

@Composable
fun EarthquakeMapTopAppBar(
    allScreens: List<TopLevelDestination>,
    onTabSelected: (TopLevelDestination) -> Unit,
    currentScreen: TopLevelDestination,
    modifier: Modifier = Modifier
) {
    val color = MaterialTheme.colorScheme.onPrimary

    Surface(
        modifier = modifier
            .height(TabHeight)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            allScreens.forEach { screen ->
                Row(
                    modifier = Modifier
                        .selectable(
                            selected = currentScreen == screen,
                            role = Role.Tab,
                            onClick = { onTabSelected(screen) },
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val durationMillis =
                        if (currentScreen == screen) {
                            TabFadeInAnimationDuration
                        } else {
                            TabFadeOutAnimationDuration
                        }
                    val animSpec = remember {
                        tween<Color>(
                            durationMillis = durationMillis,
                            easing = LinearEasing,
                            delayMillis = TabFadeInAnimationDelay
                        )
                    }
                    val tabTintColor by animateColorAsState(
                        targetValue = if (currentScreen == screen) color else color.copy(alpha = InactiveTabOpacity),
                        animationSpec = animSpec
                    )
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.description,
                        tint = tabTintColor
                    )
                    if (currentScreen == screen) {
                        Text(
                            text = screen.description,
                            color = tabTintColor
                        )
                        Spacer(Modifier.width(12.dp))
                    }
                }
            }
        }
    }
}