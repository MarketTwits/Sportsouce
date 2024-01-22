package com.markettwits.core_ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.markettwits.core_ui.R

@Immutable
@Stable
object FontNunito{
    val light = FontFamily(Font(R.font.nunito_light))
    val regular = FontFamily(Font(R.font.nunito_regular))
    val medium = FontFamily(Font(R.font.nunito_medium))
    val bold = FontFamily(Font(R.font.nunito_bold))
    val semiBoldBold = FontFamily(Font(R.font.nunito_semi_bold))
    val extraBold = FontFamily(Font(R.font.nunito_extra_bold))
}
