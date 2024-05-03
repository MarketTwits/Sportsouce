package com.markettwits.core_ui.items.image.exception

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val WarningYellowIc: ImageVector
    get() {
        if (_warningYellowIc != null) {
            return _warningYellowIc!!
        }
        _warningYellowIc = Builder(
            name = "WarningYellowIc", defaultWidth = 24.0.dp, defaultHeight =
            24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFFA800)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(4.0036f, 22.0f)
                curveTo(1.7003f, 22.0f, 0.2567f, 19.5102f, 1.4001f, 17.5099f)
                lineTo(9.402f, 3.511f)
                curveTo(10.554f, 1.4957f, 13.459f, 1.4965f, 14.6098f, 3.5125f)
                lineTo(22.6008f, 17.5115f)
                curveTo(23.7426f, 19.5118f, 22.2989f, 22.0f, 19.9965f, 22.0f)
                horizontalLineTo(4.0036f)
                close()
                moveTo(11.0f, 9.0f)
                verticalLineTo(14.0f)
                curveTo(11.0f, 14.5523f, 11.4477f, 15.0f, 12.0f, 15.0f)
                curveTo(12.5523f, 15.0f, 13.0f, 14.5523f, 13.0f, 14.0f)
                verticalLineTo(9.0f)
                curveTo(13.0f, 8.4477f, 12.5523f, 8.0f, 12.0f, 8.0f)
                curveTo(11.4477f, 8.0f, 11.0f, 8.4477f, 11.0f, 9.0f)
                close()
                moveTo(12.0f, 19.0f)
                curveTo(12.5523f, 19.0f, 13.0f, 18.5523f, 13.0f, 18.0f)
                curveTo(13.0f, 17.4477f, 12.5523f, 17.0f, 12.0f, 17.0f)
                curveTo(11.4477f, 17.0f, 11.0f, 17.4477f, 11.0f, 18.0f)
                curveTo(11.0f, 18.5523f, 11.4477f, 19.0f, 12.0f, 19.0f)
                close()
            }
        }
            .build()
        return _warningYellowIc!!
    }

private var _warningYellowIc: ImageVector? = null
