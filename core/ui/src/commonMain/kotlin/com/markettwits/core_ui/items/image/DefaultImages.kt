package com.markettwits.core_ui.items.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import sportsouce.core.ui.generated.resources.Res
import sportsouce.core.ui.generated.resources.default_start_image
import sportsouce.core.ui.generated.resources.ic_launcher_black_foreground
import sportsouce.core.ui.generated.resources.ic_launcher_light_foreground

object DefaultImages {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun emptyImageStart(): Painter = painterResource(Res.drawable.default_start_image)

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun SportSauceDarkLogo(): Painter = painterResource(Res.drawable.ic_launcher_black_foreground)

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun SportSauceLightLogo(): Painter = painterResource(Res.drawable.ic_launcher_light_foreground)

}