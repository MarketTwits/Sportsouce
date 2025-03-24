package com.markettwits.core.errors.impl.supcomponents.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val NetworkOffline: ImageVector
    get() {
        if (_NetworkOffline != null) {
            return _NetworkOffline!!
        }
        _NetworkOffline = ImageVector.Builder(
            name = "NetworkOffline",
            defaultWidth = 162.dp,
            defaultHeight = 162.dp,
            viewportWidth = 162f,
            viewportHeight = 162f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF70BFF5)),
                strokeLineWidth = 4f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(81f, 81f)
                verticalLineTo(135f)
                moveTo(77.63f, 47.68f)
                curveTo(78.71f, 47.4f, 79.83f, 47.25f, 81f, 47.25f)
                curveTo(83.06f, 47.25f, 85.09f, 47.72f, 86.94f, 48.63f)
                curveTo(88.79f, 49.53f, 90.41f, 50.85f, 91.68f, 52.48f)
                curveTo(92.94f, 54.11f, 93.81f, 56.01f, 94.23f, 58.02f)
                curveTo(94.64f, 60.04f, 94.59f, 62.13f, 94.07f, 64.13f)
                moveTo(13.5f, 13.5f)
                lineTo(148.5f, 148.5f)
                moveTo(114.47f, 40.5f)
                curveTo(118.93f, 46.37f, 121.5f, 53.31f, 121.5f, 60.75f)
                curveTo(121.5f, 68.19f, 118.93f, 75.13f, 114.47f, 81f)
                moveTo(47.52f, 81f)
                curveTo(43.07f, 75.13f, 40.5f, 68.19f, 40.5f, 60.75f)
                curveTo(40.5f, 56f, 41.55f, 51.45f, 43.47f, 47.25f)
                moveTo(137.13f, 27f)
                curveTo(144.32f, 36.65f, 148.5f, 48.26f, 148.5f, 60.75f)
                curveTo(148.5f, 73.24f, 144.32f, 84.85f, 137.13f, 94.5f)
                moveTo(24.87f, 27f)
                curveTo(17.68f, 36.65f, 13.5f, 48.26f, 13.5f, 60.75f)
                curveTo(13.5f, 73.24f, 17.68f, 84.85f, 24.87f, 94.5f)
            }
        }.build()

        return _NetworkOffline!!
    }

@Suppress("ObjectPropertyName")
private var _NetworkOffline: ImageVector? = null
