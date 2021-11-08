package com.alexruskovski.falcon.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//private val LightColorPalette = lightColors(
//    primary = primaryColour,
//    primaryVariant = primaryColour,
//    secondary = secondary,
//
//    surface = colourSurface,
//    onSurface = colourOnSurface,
//
//
//    background = colourBackground,
//    onBackground = colourOnBackground,
//
//    onPrimary = Color.White,
//    onSecondary = primaryColour,
//)
//
//private val DarkColorPalette = darkColors(
//    primary = primaryColourDark,
//    onPrimary = colourOnBackgroundDark,
//
//    primaryVariant = primaryColourDark,
//    secondary = secondaryDark,
//
//    surface = colourSurfaceDark,
//    onSurface = colourOnSurfaceDark,
//
//    background = colourBackgroundDark,
//    onBackground = colourOnBackgroundDark,
//
//    onSecondary = primaryColourDark,
//)


private val DarkColorPalette = darkColors(
    primary = Yellow800,
    primaryVariant = Yellow800,
    secondary = secondary,
    surface = colorPrimaryDark,
    background = colorPrimaryDark,
    onPrimary = Color.White,
    onSecondary = colorDarkText,
    onBackground = colorDarkText,
    onSurface = colorDarkText,
)

private val LightColorPalette = lightColors(
    primary = purple_700,
    primaryVariant = Yellow800,
    secondary = secondary,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = colorTextHeading,
    onBackground = colorTextHeading,
    onSurface = colorTextHeading,
)




@Composable
fun FalconAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
