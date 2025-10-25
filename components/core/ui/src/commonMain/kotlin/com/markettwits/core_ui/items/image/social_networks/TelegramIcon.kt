package com.markettwits.core_ui.items.image.social_networks

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IconTelegram: ImageVector
    get() {
        if (_TelegramSvgrepoCom1 != null) {
            return _TelegramSvgrepoCom1!!
        }
        _TelegramSvgrepoCom1 = ImageVector.Builder(
            name = "TelegramSvgrepoCom1",
            defaultWidth = 800.dp,
            defaultHeight = 800.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveToRelative(12f, 0f)
                curveToRelative(-6.627f, 0f, -12f, 5.373f, -12f, 12f)
                reflectiveCurveToRelative(5.373f, 12f, 12f, 12f)
                reflectiveCurveToRelative(12f, -5.373f, 12f, -12f)
                curveToRelative(0f, -6.627f, -5.373f, -12f, -12f, -12f)
                close()
                moveTo(17.894f, 8.221f)
                lineTo(15.924f, 17.501f)
                curveToRelative(-0.145f, 0.658f, -0.537f, 0.818f, -1.084f, 0.508f)
                lineToRelative(-3f, -2.21f)
                lineToRelative(-1.446f, 1.394f)
                curveToRelative(-0.14f, 0.18f, -0.357f, 0.295f, -0.6f, 0.295f)
                curveToRelative(-0.002f, 0f, -0.003f, 0f, -0.005f, 0f)
                lineToRelative(0.213f, -3.054f)
                lineToRelative(5.56f, -5.022f)
                curveToRelative(0.24f, -0.213f, -0.054f, -0.334f, -0.373f, -0.121f)
                lineToRelative(-6.869f, 4.326f)
                lineToRelative(-2.96f, -0.924f)
                curveToRelative(-0.64f, -0.203f, -0.658f, -0.64f, 0.135f, -0.954f)
                lineToRelative(11.566f, -4.458f)
                curveToRelative(0.538f, -0.196f, 1.006f, 0.128f, 0.832f, 0.941f)
                close()
            }
        }.build()

        return _TelegramSvgrepoCom1!!
    }

@Suppress("ObjectPropertyName")
private var _TelegramSvgrepoCom1: ImageVector? = null
