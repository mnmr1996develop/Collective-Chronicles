package com.michaelrichards.collectivechronicles.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Primary80,
    onPrimary = Primary20,
    primaryContainer = Primary30,
    onPrimaryContainer = Primary90,
    inversePrimary = Primary40,
    secondary = DarkenedPrimary80,
    onSecondary = DarkenedPrimary20,
    secondaryContainer = DarkenedPrimary30,
    onSecondaryContainer = DarkenedPrimary90,
    tertiary = Secondary80,
    onTertiary = Secondary20,
    tertiaryContainer = Secondary30,
    onTertiaryContainer = Secondary90,
    error = Red80,
    onError = Red20,
    onErrorContainer = Red90,
    errorContainer = Red30,
    background = Grey10,
    onBackground = Grey90,
    surface = PrimaryGrey30,
    onSurface = PrimaryGrey80,
    inverseSurface = Grey90,
    inverseOnSurface = Grey10,
    surfaceVariant = PrimaryGrey30,
    onSurfaceVariant = PrimaryGrey80,
    outline = PrimaryGrey80
)

private val LightColorScheme = lightColorScheme(
    primary = Primary40,
    onPrimary = Color.White,
    primaryContainer = Primary90,
    onPrimaryContainer = Primary10,
    inversePrimary = Primary80,
    secondary = DarkenedPrimary80,
    onSecondary = Color.White,
    secondaryContainer = DarkenedPrimary90,
    onSecondaryContainer = DarkenedPrimary10,
    tertiary = Secondary40,
    onTertiary = Color.White,
    tertiaryContainer = Secondary90,
    onTertiaryContainer = Secondary10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = Grey99,
    onBackground = Grey10,
    surface = PrimaryGrey90,
    onSurface = PrimaryGrey30,
    inverseSurface = Grey20,
    inverseOnSurface = Grey95,
    surfaceVariant = PrimaryGrey90,
    onSurfaceVariant = PrimaryGrey30,
    outline = PrimaryGrey50
)

@Composable
fun CollectiveChroniclesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
       /* dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }*/

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}