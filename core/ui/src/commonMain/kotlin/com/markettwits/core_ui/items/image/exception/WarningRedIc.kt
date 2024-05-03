package com.markettwits.core_ui.items.image.exception

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val WarningRedIc: ImageVector
    get() {
        if (_warningRedIc != null) {
            return _warningRedIc!!
        }
        _warningRedIc = Builder(
            name = "WarningRedIc", defaultWidth = 80.0.dp, defaultHeight =
            80.0.dp, viewportWidth = 80.0f, viewportHeight = 80.0f
        ).apply {
            group {
                path(
                    fill = linearGradient(
                        0.0f to Color(0xFFFFAFA9), 1.0f to Color(0xFFF44336),
                        start = Offset(54.0f, 8.0f), end = Offset(15.925f, 52.5306f)
                    ), stroke = null,
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(27.0f, 62.0f)
                    curveTo(41.877f, 62.0f, 54.0f, 49.904f, 54.0f, 35.0f)
                    curveTo(54.0f, 20.123f, 41.877f, 8.0f, 27.0f, 8.0f)
                    curveTo(12.096f, 8.0f, 0.0f, 20.123f, 0.0f, 35.0f)
                    curveTo(0.0f, 49.6514f, 11.6894f, 61.5892f, 26.2446f, 61.9896f)
                    lineTo(27.0f, 62.0f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFFF44336)), stroke = null, fillAlpha = 0.5f,
                    strokeAlpha = 0.5f, strokeLineWidth = 0.0f, strokeLineCap = Butt,
                    strokeLineJoin = Miter, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(26.8214f, 49.4001f)
                    curveTo(34.7558f, 49.4001f, 41.2214f, 42.9489f, 41.2214f, 35.0001f)
                    curveTo(41.2214f, 27.0657f, 34.7558f, 20.6001f, 26.8214f, 20.6001f)
                    curveTo(18.8726f, 20.6001f, 12.4214f, 27.0657f, 12.4214f, 35.0001f)
                    curveTo(12.4214f, 42.8142f, 18.6558f, 49.181f, 26.4185f, 49.3946f)
                    lineTo(26.8214f, 49.4001f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFFF44336)), stroke = null, fillAlpha = 0.35f,
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(78.1746f, 59.0764f)
                    lineTo(54.5511f, 17.7269f)
                    curveTo(52.0815f, 13.3096f, 46.4599f, 11.6857f, 42.0407f, 14.1574f)
                    curveTo(40.5134f, 14.9693f, 39.2786f, 16.2328f, 38.4663f, 17.6976f)
                    lineTo(14.6803f, 59.3687f)
                    curveTo(12.7631f, 64.0425f, 15.0377f, 69.4341f, 19.7494f, 71.3504f)
                    curveTo(20.6918f, 71.7077f, 21.6991f, 71.9383f, 22.7064f, 72.0f)
                    horizontalLineTo(70.2784f)
                    curveTo(72.7155f, 71.8701f, 74.9576f, 70.7983f, 76.6149f, 68.9827f)
                    curveTo(78.2721f, 67.1606f, 79.1169f, 64.7928f, 78.987f, 62.3569f)
                    curveTo(78.922f, 61.3143f, 78.6945f, 60.3106f, 78.1746f, 59.0764f)
                    close()
                }
                path(
                    fill = linearGradient(
                        0.0f to Color(0xBFFFFFFF), 1.0f to Color(0x33FFFFFF),
                        start = Offset(49.5012f, -69.9877f), end = Offset(39.3251f, -70.3116f)
                    ),
                    stroke = null, strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin
                    = Miter, strokeLineMiter = 4.0f, pathFillType = EvenOdd
                ) {
                    moveTo(47.0f, 33.0f)
                    curveTo(45.8954f, 33.0f, 45.0f, 33.8954f, 45.0f, 35.0f)
                    verticalLineTo(48.0f)
                    curveTo(45.0f, 49.1046f, 45.8954f, 50.0f, 47.0f, 50.0f)
                    curveTo(48.1046f, 50.0f, 49.0f, 49.1046f, 49.0f, 48.0f)
                    verticalLineTo(35.0f)
                    curveTo(49.0f, 33.8954f, 48.1046f, 33.0f, 47.0f, 33.0f)
                    close()
                    moveTo(44.5f, 58.5014f)
                    curveTo(44.5f, 59.8614f, 45.6429f, 61.0f, 47.0143f, 61.0f)
                    curveTo(48.3857f, 61.0f, 49.5f, 59.8614f, 49.5f, 58.4702f)
                    curveTo(49.5f, 57.1102f, 48.3857f, 56.0f, 47.0143f, 56.0f)
                    curveTo(45.6429f, 56.0f, 44.5f, 57.1357f, 44.5f, 58.5014f)
                    close()
                }
            }
            group {
                path(
                    fill = linearGradient(
                        0.0f to Color(0x3FFFFFFF), 1.0f to Color(0x00FFFFFF),
                        start = Offset(45.2959f, 36.262f), end = Offset(53.001f, 37.9661f)
                    ), stroke =
                    null, strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(45.5f, 35.0f)
                    curveTo(45.5f, 34.1716f, 46.1716f, 33.5f, 47.0f, 33.5f)
                    verticalLineTo(32.5f)
                    curveTo(45.6193f, 32.5f, 44.5f, 33.6193f, 44.5f, 35.0f)
                    horizontalLineTo(45.5f)
                    close()
                    moveTo(45.5f, 48.0f)
                    verticalLineTo(35.0f)
                    horizontalLineTo(44.5f)
                    verticalLineTo(48.0f)
                    horizontalLineTo(45.5f)
                    close()
                    moveTo(47.0f, 49.5f)
                    curveTo(46.1716f, 49.5f, 45.5f, 48.8284f, 45.5f, 48.0f)
                    horizontalLineTo(44.5f)
                    curveTo(44.5f, 49.3807f, 45.6193f, 50.5f, 47.0f, 50.5f)
                    verticalLineTo(49.5f)
                    close()
                    moveTo(48.5f, 48.0f)
                    curveTo(48.5f, 48.8284f, 47.8284f, 49.5f, 47.0f, 49.5f)
                    verticalLineTo(50.5f)
                    curveTo(48.3807f, 50.5f, 49.5f, 49.3807f, 49.5f, 48.0f)
                    horizontalLineTo(48.5f)
                    close()
                    moveTo(48.5f, 35.0f)
                    verticalLineTo(48.0f)
                    horizontalLineTo(49.5f)
                    verticalLineTo(35.0f)
                    horizontalLineTo(48.5f)
                    close()
                    moveTo(47.0f, 33.5f)
                    curveTo(47.8284f, 33.5f, 48.5f, 34.1716f, 48.5f, 35.0f)
                    horizontalLineTo(49.5f)
                    curveTo(49.5f, 33.6193f, 48.3807f, 32.5f, 47.0f, 32.5f)
                    verticalLineTo(33.5f)
                    close()
                    moveTo(47.0143f, 60.5f)
                    curveTo(45.9164f, 60.5f, 45.0f, 59.5827f, 45.0f, 58.5014f)
                    horizontalLineTo(44.0f)
                    curveTo(44.0f, 60.1402f, 45.3693f, 61.5f, 47.0143f, 61.5f)
                    verticalLineTo(60.5f)
                    close()
                    moveTo(49.0f, 58.4702f)
                    curveTo(49.0f, 59.5942f, 48.1007f, 60.5f, 47.0143f, 60.5f)
                    verticalLineTo(61.5f)
                    curveTo(48.6707f, 61.5f, 50.0f, 60.1287f, 50.0f, 58.4702f)
                    horizontalLineTo(49.0f)
                    close()
                    moveTo(47.0143f, 56.5f)
                    curveTo(48.1122f, 56.5f, 49.0f, 57.3889f, 49.0f, 58.4702f)
                    horizontalLineTo(50.0f)
                    curveTo(50.0f, 56.8314f, 48.6593f, 55.5f, 47.0143f, 55.5f)
                    verticalLineTo(56.5f)
                    close()
                    moveTo(45.0f, 58.5014f)
                    curveTo(45.0f, 57.4144f, 45.9164f, 56.5f, 47.0143f, 56.5f)
                    verticalLineTo(55.5f)
                    curveTo(45.3693f, 55.5f, 44.0f, 56.857f, 44.0f, 58.5014f)
                    horizontalLineTo(45.0f)
                    close()
                }
            }
        }
            .build()
        return _warningRedIc!!
    }

private var _warningRedIc: ImageVector? = null
