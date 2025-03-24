package com.markettwits.core.errors.impl.supcomponents.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val TablerError404: ImageVector
    get() {
        if (_TablerError404 != null) {
            return _TablerError404!!
        }
        _TablerError404 = ImageVector.Builder(
            name = "TablerError404",
            defaultWidth = 204.dp,
            defaultHeight = 204.dp,
            viewportWidth = 204f,
            viewportHeight = 204f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF70BFF5)),
                strokeLineWidth = 5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(25.5f, 59.5f)
                verticalLineTo(93.5f)
                curveTo(25.5f, 95.75f, 26.4f, 97.92f, 27.99f, 99.51f)
                curveTo(29.58f, 101.1f, 31.75f, 102f, 34f, 102f)
                horizontalLineTo(59.5f)
                moveTo(59.5f, 59.5f)
                verticalLineTo(144.5f)
                moveTo(144.5f, 59.5f)
                verticalLineTo(93.5f)
                curveTo(144.5f, 95.75f, 145.4f, 97.92f, 146.99f, 99.51f)
                curveTo(148.58f, 101.1f, 150.75f, 102f, 153f, 102f)
                horizontalLineTo(178.5f)
                moveTo(178.5f, 59.5f)
                verticalLineTo(144.5f)
                moveTo(85f, 68f)
                verticalLineTo(136f)
                curveTo(85f, 138.25f, 85.9f, 140.42f, 87.49f, 142.01f)
                curveTo(89.08f, 143.6f, 91.25f, 144.5f, 93.5f, 144.5f)
                horizontalLineTo(110.5f)
                curveTo(112.75f, 144.5f, 114.92f, 143.6f, 116.51f, 142.01f)
                curveTo(118.1f, 140.42f, 119f, 138.25f, 119f, 136f)
                verticalLineTo(68f)
                curveTo(119f, 65.75f, 118.1f, 63.58f, 116.51f, 61.99f)
                curveTo(114.92f, 60.4f, 112.75f, 59.5f, 110.5f, 59.5f)
                horizontalLineTo(93.5f)
                curveTo(91.25f, 59.5f, 89.08f, 60.4f, 87.49f, 61.99f)
                curveTo(85.9f, 63.58f, 85f, 65.75f, 85f, 68f)
                close()
            }
        }.build()

        return _TablerError404!!
    }

@Suppress("ObjectPropertyName")
private var _TablerError404: ImageVector? = null
