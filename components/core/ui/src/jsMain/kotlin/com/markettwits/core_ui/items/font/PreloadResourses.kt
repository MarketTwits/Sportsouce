package com.markettwits.core_ui.items.font

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.preloadFont
import sportsouce.components.core.ui.generated.resources.Res
import sportsouce.components.core.ui.generated.resources.nunito_bold
import sportsouce.components.core.ui.generated.resources.nunito_extra_bold
import sportsouce.components.core.ui.generated.resources.nunito_light
import sportsouce.components.core.ui.generated.resources.nunito_medium
import sportsouce.components.core.ui.generated.resources.nunito_regular
import sportsouce.components.core.ui.generated.resources.nunito_semi_bold

@Composable
fun preloadFontResources(){
    preloadFont(Res.font.nunito_light)
    preloadFont(Res.font.nunito_regular)
    preloadFont(Res.font.nunito_medium)
    preloadFont(Res.font.nunito_bold)
    preloadFont(Res.font.nunito_semi_bold)
    preloadFont(Res.font.nunito_extra_bold)
}