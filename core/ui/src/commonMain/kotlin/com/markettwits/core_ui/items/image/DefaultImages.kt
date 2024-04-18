package com.markettwits.core_ui.items.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.painterResource
import sportsouce.core.ui.generated.resources.Res
import sportsouce.core.ui.generated.resources.default_start_image
import sportsouce.core.ui.generated.resources.ic_launcher_black_foreground
import sportsouce.core.ui.generated.resources.ic_launcher_light_foreground

object DefaultImages {

    @Composable
    fun EmptyImageStart(): Painter = painterResource(Res.drawable.default_start_image)

    @Composable
    fun SportSauceDarkLogo(): Painter = painterResource(Res.drawable.ic_launcher_black_foreground)

    @Composable
    fun SportSauceLightLogo(): Painter = painterResource(Res.drawable.ic_launcher_light_foreground)

}