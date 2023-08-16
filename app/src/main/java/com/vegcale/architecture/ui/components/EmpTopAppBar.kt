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

private const val INACTIVE_TAB_OPACITY = 0.60f
private const val TAB_FADE_IN_ANIMATION_DURATION = 150
private const val TAB_FADE_IN_ANIMATION_DELAY = 100
private const val TAB_FADE_OUT_ANIMATION_DURATION = 100

@Composable
fun EmpTopAppBar(
    allScreens: List<TopLevelDestination>,
    onTabSelected: (TopLevelDestination) -> Unit,
    currentScreen: TopLevelDestination,
    modifier: Modifier = Modifier
) {
    val tabHeight = 56.dp
    val color = MaterialTheme.colorScheme.onPrimary

    Surface(
        modifier = modifier
            .height(tabHeight)
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
                            TAB_FADE_IN_ANIMATION_DURATION
                        } else {
                            TAB_FADE_OUT_ANIMATION_DURATION
                        }
                    val animSpec = remember {
                        tween<Color>(
                            durationMillis = durationMillis,
                            easing = LinearEasing,
                            delayMillis = TAB_FADE_IN_ANIMATION_DELAY
                        )
                    }
                    val tabTintColor by animateColorAsState(
                        targetValue = if (currentScreen == screen) color else color.copy(alpha = INACTIVE_TAB_OPACITY),
                        animationSpec = animSpec,
                        label = "Tab tint color change"
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