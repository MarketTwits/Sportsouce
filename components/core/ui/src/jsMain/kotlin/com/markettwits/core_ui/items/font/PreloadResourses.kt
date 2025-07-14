package com.markettwits.core_ui.items.font

import androidx.compose.runtime.*
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import org.jetbrains.compose.resources.preloadFont
import sportsouce.components.core.ui.generated.resources.*

@Composable
fun PreloadFontResources(onFontReady: @Composable () ->  Unit ) {
    var areFontsReady by remember { mutableStateOf(false) }

    val font1 by preloadFont(Res.font.nunito_light)
    val font2 by preloadFont(Res.font.nunito_light)
    val font3 by preloadFont(Res.font.nunito_regular)
    val font4 by preloadFont(Res.font.nunito_medium)
    val font5 by preloadFont(Res.font.nunito_bold)
    val font6 by preloadFont(Res.font.nunito_semi_bold)
    val font7 by preloadFont(Res.font.nunito_extra_bold)

    LaunchedEffect(font1, font2, font3, font4, font5, font6, font7) {
        if (font1 != null && font2 != null && font3
            != null && font4 != null && font5
            != null && font6 != null && font7 != null
        ) {
            areFontsReady = true
        }
    }

    if (!areFontsReady) {
        LoadingFullScreen()
    } else {
        onFontReady()
    }
}