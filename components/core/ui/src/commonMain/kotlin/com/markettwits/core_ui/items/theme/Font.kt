package com.markettwits.core_ui.items.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.FontResource
import sportsouce.components.core.ui.generated.resources.*

@Immutable
object FontRoboto {
    @Composable
    fun family() = FontFamily(
        Font(Res.font.roboto_light, FontWeight.Light),
        Font(Res.font.roboto_regular, FontWeight.Normal),
        Font(Res.font.roboto_medium, FontWeight.Medium),
        Font(Res.font.roboto_semibold, FontWeight.SemiBold),
        Font(Res.font.roboto_bold, FontWeight.Bold),
        Font(Res.font.roboto_extrabold, FontWeight.ExtraBold),
        Font(Res.font.roboto_black, FontWeight.Black),
    )

    // Register the actual weight, including in single-face families. Otherwise inherited label
    // weights can synthesize bold on top of an already weighted outline.
    @Composable fun light() = face(Res.font.roboto_light, FontWeight.Light)
    @Composable fun regular() = face(Res.font.roboto_regular, FontWeight.Normal)
    @Composable fun medium() = face(Res.font.roboto_medium, FontWeight.Medium)
    @Composable fun semiBold() = face(Res.font.roboto_semibold, FontWeight.SemiBold)
    @Composable fun bold() = face(Res.font.roboto_bold, FontWeight.Bold)
    @Composable fun extraBold() = face(Res.font.roboto_extrabold, FontWeight.ExtraBold)
    @Composable fun black() = face(Res.font.roboto_black, FontWeight.Black)

    @Composable
    private fun face(resource: FontResource, weight: FontWeight) = FontFamily(Font(resource, weight))
}

/**
 * Optical compatibility for legacy Nunito screens: body / emphasis / heading become Roboto
 * 400 / 500 / 600. Keeping their old heavier weight distribution made the denser Roboto UI heavy.
 * New components use Material typography instead of these historical method names.
 */
@Immutable
object FontNunito {
    @Composable fun light() = FontRoboto.light()
    @Composable fun regular() = FontRoboto.regular()
    @Composable fun medium() = FontRoboto.regular()
    @Composable fun bold() = FontRoboto.semiBold()
    @Composable fun semiBoldBold() = FontRoboto.medium()
    @Composable fun extraBold() = FontRoboto.bold()
    @Composable fun black() = FontRoboto.extraBold()
}
