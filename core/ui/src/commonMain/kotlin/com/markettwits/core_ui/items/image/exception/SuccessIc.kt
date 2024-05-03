package com.markettwits.core_ui.items.image.exception

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val SuccessIc: ImageVector
    get() {
        if (_successIc != null) {
            return _successIc!!
        }
        _successIc = Builder(
            name = "SuccessIc", defaultWidth = 80.0.dp, defaultHeight = 80.0.dp,
            viewportWidth = 80.0f, viewportHeight = 80.0f
        ).apply {
            path(
                fill = linearGradient(
                    0.0f to Color(0xFF86D1A1), 1.0f to Color(0xFF3AC267), start =
                    Offset(54.0f, 3.0f), end = Offset(15.925f, 47.5306f)
                ), stroke = null,
                strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(27.0f, 57.0f)
                curveTo(41.877f, 57.0f, 54.0f, 44.904f, 54.0f, 30.0f)
                curveTo(54.0f, 15.123f, 41.877f, 3.0f, 27.0f, 3.0f)
                curveTo(12.096f, 3.0f, 0.0f, 15.123f, 0.0f, 30.0f)
                curveTo(0.0f, 44.6514f, 11.6894f, 56.5892f, 26.2446f, 56.9896f)
                lineTo(27.0f, 57.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF72DC60)), stroke = null, fillAlpha = 0.35f,
                strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(47.0f, 77.0f)
                curveTo(64.632f, 77.0f, 79.0f, 62.664f, 79.0f, 45.0f)
                curveTo(79.0f, 27.368f, 64.632f, 13.0f, 47.0f, 13.0f)
                curveTo(29.336f, 13.0f, 15.0f, 27.368f, 15.0f, 45.0f)
                curveTo(15.0f, 62.3646f, 28.8542f, 76.5131f, 46.1048f, 76.9877f)
                lineTo(47.0f, 77.0f)
                close()
            }
            path(
                fill = linearGradient(
                    0.0f to Color(0xFFFFFFFF),
                    1.0f to Color(0x33FFFFFF),
                    0.0f to
                            Color(0xFFFFFFFF),
                    1.0f to Color(0x00FFFFFF),
                    start = Offset(51.6057f, 49.5399f),
                    end = Offset(59.5272f, 57.8633f)
                ), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(57.5858f, 36.5858f)
                lineTo(43.0f, 51.1716f)
                lineTo(36.4142f, 44.5858f)
                curveTo(35.6332f, 43.8047f, 34.3669f, 43.8048f, 33.5858f, 44.5858f)
                curveTo(32.8047f, 45.3668f, 32.8047f, 46.6331f, 33.5858f, 47.4142f)
                lineTo(43.0f, 56.8284f)
                lineTo(60.4142f, 39.4142f)
                curveTo(61.1953f, 38.6332f, 61.1953f, 37.3669f, 60.4142f, 36.5858f)
                curveTo(59.6332f, 35.8047f, 58.3668f, 35.8047f, 57.5858f, 36.5858f)
                close()
            }
        }
            .build()
        return _successIc!!
    }

private var _successIc: ImageVector? = null
