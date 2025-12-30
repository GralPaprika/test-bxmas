package space.carlosrdgz.test.vepormas.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    secondary = DarkSecondary,
    onSecondary = DarkOnPrimary,
    tertiary = DarkSuccess,
    onTertiary = DarkOnPrimary,
    background = DarkBackground,
    onBackground = DarkOnPrimary,
    surface = DarkSurface,
    onSurface = DarkOnPrimary,
    surfaceVariant = DarkBorder,
    onSurfaceVariant = DarkOnPrimary,
    outline = DarkBorder,
    outlineVariant = DarkBorder,
    error = DarkError,
    onError = DarkOnPrimary,
    errorContainer = DarkError,
    onErrorContainer = DarkOnError,
    scrim = DarkBackground
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    secondary = LightSecondary,
    onSecondary = LightOnPrimary,
    tertiary = LightSuccess,
    onTertiary = LightOnPrimary,
    background = LightBackground,
    onBackground = LightOnPrimary,
    surface = LightSurface,
    onSurface = LightOnPrimary,
    surfaceVariant = LightBorder,
    onSurfaceVariant = LightOnPrimary,
    outline = LightBorder,
    outlineVariant = LightBorder,
    error = LightError,
    onError = LightOnError,
    errorContainer = LightError,
    onErrorContainer = LightOnError,
    scrim = LightBackground
)

@Composable
fun TestBXTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}