package com.nocturne.store.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary           = Color(0xFF2563EB),
    onPrimary         = Color(0xFFFFFFFF),
    primaryContainer  = Color(0xFFDBEAFE),
    onPrimaryContainer= Color(0xFF1E3A5F),
    secondary         = Color(0xFF1E3A5F),
    onSecondary       = Color(0xFFFFFFFF),
    background        = Color(0xFFF8FAFC),
    onBackground      = Color(0xFF0F172A),
    surface           = Color(0xFFFFFFFF),
    onSurface         = Color(0xFF0F172A),
    surfaceVariant    = Color(0xFFF1F5F9),
    onSurfaceVariant  = Color(0xFF64748B),
    outline           = Color(0xFFE2E8F0),
    outlineVariant    = Color(0xFFCBD5E1),
    error             = Color(0xFFEF4444),
    onError           = Color(0xFFFFFFFF),
    errorContainer    = Color(0xFFFEE2E2),
    onErrorContainer  = Color(0xFF991B1B),
)

@Composable
fun NocturneStoreTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        shapes      = NocturneShapes,
        content     = content
    )
}
