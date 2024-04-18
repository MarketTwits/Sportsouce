package com.markettwits.core_ui.items.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.font.FontFamily
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import sportsouce.core.ui.generated.resources.Res
import sportsouce.core.ui.generated.resources.nunito_bold
import sportsouce.core.ui.generated.resources.nunito_extra_bold
import sportsouce.core.ui.generated.resources.nunito_light
import sportsouce.core.ui.generated.resources.nunito_medium
import sportsouce.core.ui.generated.resources.nunito_regular
import sportsouce.core.ui.generated.resources.nunito_semi_bold

@Immutable
@Stable
object FontNunito {
    @Composable
    fun light() = FontFamily(Font(Res.font.nunito_light))

    @Composable
    fun regular() = FontFamily(Font(Res.font.nunito_regular))

    @Composable
    fun medium() = FontFamily(Font(Res.font.nunito_medium))

    @Composable
    fun bold() = FontFamily(Font(Res.font.nunito_bold))

    @Composable
    fun semiBoldBold() = FontFamily(Font(Res.font.nunito_semi_bold))

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun extraBold() = FontFamily(Font(Res.font.nunito_extra_bold))
}
