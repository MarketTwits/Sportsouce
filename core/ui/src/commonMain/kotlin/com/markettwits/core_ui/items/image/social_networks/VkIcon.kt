package com.markettwits.core_ui.items.image.social_networks

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp


private var _Vk: ImageVector? = null

val IconVk: ImageVector
    get() {
        if (_Vk != null) {
            return _Vk!!
        }
        _Vk = ImageVector.Builder(
            name = "Vk",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(13.162f, 18.994f)
                curveToRelative(0.609f, 0f, 0.858f, -0.406f, 0.851f, -0.915f)
                curveToRelative(-0.031f, -1.917f, 0.714f, -2.949f, 2.059f, -1.604f)
                curveToRelative(1.488f, 1.488f, 1.796f, 2.519f, 3.603f, 2.519f)
                horizontalLineToRelative(3.2f)
                curveToRelative(0.808f, 0f, 1.126f, -0.26f, 1.126f, -0.668f)
                curveToRelative(0f, -0.863f, -1.421f, -2.386f, -2.625f, -3.504f)
                curveToRelative(-1.686f, -1.565f, -1.765f, -1.602f, -0.313f, -3.486f)
                curveToRelative(1.801f, -2.339f, 4.157f, -5.336f, 2.073f, -5.336f)
                horizontalLineToRelative(-3.981f)
                curveToRelative(-0.772f, 0f, -0.828f, 0.435f, -1.103f, 1.083f)
                curveToRelative(-0.995f, 2.347f, -2.886f, 5.387f, -3.604f, 4.922f)
                curveToRelative(-0.751f, -0.485f, -0.407f, -2.406f, -0.35f, -5.261f)
                curveToRelative(0.015f, -0.754f, 0.011f, -1.271f, -1.141f, -1.539f)
                curveToRelative(-0.629f, -0.145f, -1.241f, -0.205f, -1.809f, -0.205f)
                curveToRelative(-2.273f, 0f, -3.841f, 0.953f, -2.95f, 1.119f)
                curveToRelative(1.571f, 0.293f, 1.42f, 3.692f, 1.054f, 5.16f)
                curveToRelative(-0.638f, 2.556f, -3.036f, -2.024f, -4.035f, -4.305f)
                curveToRelative(-0.241f, -0.548f, -0.315f, -0.974f, -1.175f, -0.974f)
                horizontalLineToRelative(-3.255f)
                curveToRelative(-0.492f, 0f, -0.787f, 0.16f, -0.787f, 0.516f)
                curveToRelative(0f, 0.602f, 2.96f, 6.72f, 5.786f, 9.77f)
                curveToRelative(2.756f, 2.975f, 5.48f, 2.708f, 7.376f, 2.708f)
                close()
            }
        }.build()
        return _Vk!!
    }

