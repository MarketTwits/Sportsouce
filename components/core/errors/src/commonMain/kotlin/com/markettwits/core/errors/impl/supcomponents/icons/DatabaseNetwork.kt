package com.markettwits.core.errors.impl.supcomponents.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val DatabaseNetwork: ImageVector
    get() {
        if (_DatabaseNetwork != null) {
            return _DatabaseNetwork!!
        }
        _DatabaseNetwork = ImageVector.Builder(
            name = "DatabaseNetwork",
            defaultWidth = 188.dp,
            defaultHeight = 188.dp,
            viewportWidth = 188f,
            viewportHeight = 188f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF70BFF5)),
                strokeLineWidth = 5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(172.33f, 121.42f)
                curveTo(172.33f, 143.05f, 154.8f, 160.58f, 133.17f, 160.58f)
                curveTo(126.16f, 160.58f, 119.57f, 158.74f, 113.87f, 155.51f)
                curveTo(107.84f, 152.09f, 102.82f, 147.14f, 99.33f, 141.15f)
                curveTo(95.84f, 135.16f, 94f, 128.35f, 94f, 121.42f)
                curveTo(94f, 111.36f, 97.79f, 102.19f, 104.02f, 95.25f)
                curveTo(107.69f, 91.16f, 112.18f, 87.88f, 117.21f, 85.64f)
                curveTo(122.23f, 83.4f, 127.67f, 82.24f, 133.17f, 82.25f)
                curveTo(154.8f, 82.25f, 172.33f, 99.78f, 172.33f, 121.42f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF70BFF5)),
                strokeLineWidth = 5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(133.17f, 47f)
                verticalLineTo(82.25f)
                curveTo(127.67f, 82.24f, 122.23f, 83.4f, 117.21f, 85.64f)
                curveTo(112.18f, 87.88f, 107.69f, 91.16f, 104.02f, 95.25f)
                curveTo(97.55f, 102.43f, 93.98f, 111.76f, 94f, 121.42f)
                curveTo(94.01f, 123.69f, 94.19f, 125.9f, 94.56f, 128.07f)
                curveTo(95.55f, 133.79f, 97.79f, 139.21f, 101.13f, 143.96f)
                curveTo(104.47f, 148.71f, 108.82f, 152.65f, 113.87f, 155.51f)
                curveTo(103.45f, 158.66f, 89.61f, 160.58f, 74.42f, 160.58f)
                curveTo(41.97f, 160.58f, 15.67f, 151.81f, 15.67f, 141f)
                verticalLineTo(47f)
            }
            path(
                stroke = SolidColor(Color(0xFF70BFF5)),
                strokeLineWidth = 5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(15.67f, 109.67f)
                curveTo(15.67f, 120.48f, 41.97f, 129.25f, 74.42f, 129.25f)
                curveTo(81.49f, 129.25f, 88.28f, 128.84f, 94.56f, 128.07f)
                moveTo(15.67f, 78.33f)
                curveTo(15.67f, 89.15f, 41.97f, 97.92f, 74.42f, 97.92f)
                curveTo(85.21f, 97.92f, 95.33f, 96.95f, 104.02f, 95.25f)
                moveTo(172.33f, 121.42f)
                horizontalLineTo(94f)
                moveTo(133.17f, 47f)
                curveTo(133.17f, 57.81f, 106.86f, 66.58f, 74.42f, 66.58f)
                curveTo(41.97f, 66.58f, 15.67f, 57.81f, 15.67f, 47f)
                curveTo(15.67f, 36.19f, 41.97f, 27.42f, 74.42f, 27.42f)
                curveTo(106.86f, 27.42f, 133.17f, 36.19f, 133.17f, 47f)
                close()
                moveTo(133.17f, 160.58f)
                curveTo(133.17f, 160.58f, 119.46f, 137.62f, 119.46f, 121.42f)
                curveTo(119.46f, 105.21f, 133.17f, 82.25f, 133.17f, 82.25f)
                curveTo(133.17f, 82.25f, 146.88f, 105.21f, 146.88f, 121.42f)
                curveTo(146.88f, 137.62f, 133.17f, 160.58f, 133.17f, 160.58f)
                close()
            }
        }.build()

        return _DatabaseNetwork!!
    }

@Suppress("ObjectPropertyName")
private var _DatabaseNetwork: ImageVector? = null
