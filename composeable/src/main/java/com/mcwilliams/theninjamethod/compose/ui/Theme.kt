package com.mcwilliams.theninjamethod.compose.ui

import androidx.compose.Composable
import androidx.ui.foundation.isSystemInDarkTheme
import androidx.ui.graphics.Color
import androidx.ui.material.MaterialTheme
import androidx.ui.material.darkColorPalette
import androidx.ui.material.lightColorPalette

private val DarkColorPalette = darkColorPalette(
//        primary = purple200,
//        primaryVariant = purple700,
//        secondary = teal200,
//        background = Color.Black,
//        onPrimary = grey400
    primary = Color.White,
    primaryVariant = Color.Red,
    onPrimary = Color.Black,
    secondary = Color.Red,
    onSecondary = Color.White,
    error = Color.Red
//    surface = Color.DarkGray
)


private val LightColorPalette = lightColorPalette(
        primary = purple500,
        primaryVariant = purple700,
        secondary = teal200

        /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ComposeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    MaterialTheme(
            colors = if (darkTheme) DarkColorPalette else LightColorPalette,
            typography = typography,
            shapes = shapes,
            content = content
    )
}