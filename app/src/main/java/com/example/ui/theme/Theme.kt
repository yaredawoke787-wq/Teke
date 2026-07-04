package com.example.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    secondary = GoldSecondary,
    tertiary = GoldDark,
    background = ObsidianBackground,
    surface = ObsidianSurface,
    onPrimary = ObsidianBackground,
    onSecondary = ObsidianBackground,
    onBackground = ObsidianTextPrimary,
    onSurface = ObsidianTextPrimary,
    outline = ObsidianBorder
)

private val LightColorScheme = lightColorScheme(
    primary = GoldPrimary,
    secondary = GoldDark,
    tertiary = GoldSecondary,
    background = IvoryBackground,
    surface = IvorySurface,
    onPrimary = IvorySurface,
    onSecondary = IvorySurface,
    onBackground = IvoryTextPrimary,
    onSurface = IvoryTextPrimary,
    outline = IvoryBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    // Determine the baseline target scheme
    val targetColorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // Animate every color to guarantee an incredibly smooth, animated theme transition
    val animatedColorScheme = targetColorScheme.copy(
        primary = animateColorAsState(targetColorScheme.primary, animationSpec = tween(500), label = "primary").value,
        secondary = animateColorAsState(targetColorScheme.secondary, animationSpec = tween(500), label = "secondary").value,
        tertiary = animateColorAsState(targetColorScheme.tertiary, animationSpec = tween(500), label = "tertiary").value,
        background = animateColorAsState(targetColorScheme.background, animationSpec = tween(500), label = "background").value,
        surface = animateColorAsState(targetColorScheme.surface, animationSpec = tween(500), label = "surface").value,
        onPrimary = animateColorAsState(targetColorScheme.onPrimary, animationSpec = tween(500), label = "onPrimary").value,
        onSecondary = animateColorAsState(targetColorScheme.onSecondary, animationSpec = tween(500), label = "onSecondary").value,
        onBackground = animateColorAsState(targetColorScheme.onBackground, animationSpec = tween(500), label = "onBackground").value,
        onSurface = animateColorAsState(targetColorScheme.onSurface, animationSpec = tween(500), label = "onSurface").value,
        outline = animateColorAsState(targetColorScheme.outline, animationSpec = tween(500), label = "outline").value
    )

    MaterialTheme(
        colorScheme = animatedColorScheme,
        typography = Typography,
        content = content
    )
}
