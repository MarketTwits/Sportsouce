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

val FailBlueIc: ImageVector
    get() {
        if (_failBlueIc != null) {
            return _failBlueIc!!
        }
        _failBlueIc = Builder(
            name = "FailBlueIc", defaultWidth = 80.0.dp, defaultHeight = 80.0.dp,
            viewportWidth = 80.0f, viewportHeight = 80.0f
        ).apply {
            group {
                path(
                    fill = linearGradient(
                        0.0f to Color(0xFF7DA3EF), 1.0f to Color(0xFF216BFF),
                        start = Offset(20.7204f, 11.5706f), end = Offset(33.2777f, 58.4344f)
                    ), stroke
                    = null, strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin =
                    Miter, strokeLineMiter = 4.0f, pathFillType = NonZero
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
                    fill = SolidColor(Color(0xFF216BFF)), stroke = null, fillAlpha = 0.25f,
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
                    fill = SolidColor(Color(0xFF8AADF1)), stroke = null, fillAlpha = 0.33f,
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
                        start = Offset(22.4859f, 42.0595f), end = Offset(12.2013f, 69.2375f)
                    ), stroke
                    = null, strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin =
                    Miter, strokeLineMiter = 4.0f, pathFillType = EvenOdd
                ) {
                    moveTo(58.339f, 35.6756f)
                    curveTo(57.558f, 34.8946f, 56.2916f, 34.8946f, 55.5106f, 35.6756f)
                    lineTo(47.0253f, 44.1609f)
                    lineTo(38.54f, 35.6756f)
                    curveTo(37.759f, 34.8946f, 36.4927f, 34.8946f, 35.7116f, 35.6756f)
                    curveTo(34.9306f, 36.4567f, 34.9306f, 37.723f, 35.7116f, 38.5041f)
                    lineTo(44.1969f, 46.9893f)
                    lineTo(35.6761f, 55.5101f)
                    curveTo(34.8951f, 56.2912f, 34.8951f, 57.5575f, 35.6761f, 58.3385f)
                    curveTo(36.4572f, 59.1196f, 37.7235f, 59.1196f, 38.5045f, 58.3385f)
                    lineTo(47.0253f, 49.8178f)
                    lineTo(55.5461f, 58.3385f)
                    curveTo(56.3271f, 59.1196f, 57.5935f, 59.1196f, 58.3745f, 58.3385f)
                    curveTo(59.1556f, 57.5575f, 59.1556f, 56.2911f, 58.3745f, 55.5101f)
                    lineTo(49.8537f, 46.9893f)
                    lineTo(58.339f, 38.5041f)
                    curveTo(59.1201f, 37.723f, 59.1201f, 36.4567f, 58.339f, 35.6756f)
                    close()
                }
            }
            group {
                path(
                    fill = linearGradient(
                        0.0f to Color(0x3FFFFFFF), 1.0f to Color(0x00FFFFFF),
                        start = Offset(37.8712f, 55.1303f), end = Offset(56.7074f, 39.899f)
                    ), stroke =
                    null, strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(47.0253f, 44.1609f)
                    lineTo(46.6718f, 44.5145f)
                    lineTo(47.0253f, 44.868f)
                    lineTo(47.3789f, 44.5145f)
                    lineTo(47.0253f, 44.1609f)
                    close()
                    moveTo(38.54f, 35.6756f)
                    lineTo(38.1865f, 36.0292f)
                    lineTo(38.1865f, 36.0292f)
                    lineTo(38.54f, 35.6756f)
                    close()
                    moveTo(35.7116f, 35.6756f)
                    lineTo(36.0652f, 36.0292f)
                    lineTo(36.0652f, 36.0292f)
                    lineTo(35.7116f, 35.6756f)
                    close()
                    moveTo(35.7116f, 38.5041f)
                    lineTo(36.0652f, 38.1505f)
                    horizontalLineTo(36.0652f)
                    lineTo(35.7116f, 38.5041f)
                    close()
                    moveTo(44.1969f, 46.9893f)
                    lineTo(44.5504f, 47.3429f)
                    lineTo(44.904f, 46.9893f)
                    lineTo(44.5504f, 46.6358f)
                    lineTo(44.1969f, 46.9893f)
                    close()
                    moveTo(35.6761f, 55.5101f)
                    lineTo(36.0297f, 55.8637f)
                    lineTo(35.6761f, 55.5101f)
                    close()
                    moveTo(38.5045f, 58.3385f)
                    lineTo(38.8581f, 58.6921f)
                    verticalLineTo(58.6921f)
                    lineTo(38.5045f, 58.3385f)
                    close()
                    moveTo(47.0253f, 49.8178f)
                    lineTo(47.3789f, 49.4642f)
                    lineTo(47.0253f, 49.1107f)
                    lineTo(46.6718f, 49.4642f)
                    lineTo(47.0253f, 49.8178f)
                    close()
                    moveTo(55.5461f, 58.3385f)
                    lineTo(55.1925f, 58.6921f)
                    lineTo(55.1925f, 58.6921f)
                    lineTo(55.5461f, 58.3385f)
                    close()
                    moveTo(58.3745f, 58.3385f)
                    lineTo(58.7281f, 58.6921f)
                    lineTo(58.7281f, 58.6921f)
                    lineTo(58.3745f, 58.3385f)
                    close()
                    moveTo(58.3745f, 55.5101f)
                    lineTo(58.7281f, 55.1565f)
                    lineTo(58.7281f, 55.1565f)
                    lineTo(58.3745f, 55.5101f)
                    close()
                    moveTo(49.8537f, 46.9893f)
                    lineTo(49.5002f, 46.6358f)
                    lineTo(49.1466f, 46.9893f)
                    lineTo(49.5002f, 47.3429f)
                    lineTo(49.8537f, 46.9893f)
                    close()
                    moveTo(58.339f, 38.5041f)
                    lineTo(58.6926f, 38.8576f)
                    lineTo(58.339f, 38.5041f)
                    close()
                    moveTo(55.8641f, 36.0292f)
                    curveTo(56.4499f, 35.4434f, 57.3997f, 35.4434f, 57.9855f, 36.0292f)
                    lineTo(58.6926f, 35.3221f)
                    curveTo(57.7163f, 34.3458f, 56.1334f, 34.3458f, 55.157f, 35.3221f)
                    lineTo(55.8641f, 36.0292f)
                    close()
                    moveTo(47.3789f, 44.5145f)
                    lineTo(55.8641f, 36.0292f)
                    lineTo(55.157f, 35.3221f)
                    lineTo(46.6718f, 43.8074f)
                    lineTo(47.3789f, 44.5145f)
                    close()
                    moveTo(38.1865f, 36.0292f)
                    lineTo(46.6718f, 44.5145f)
                    lineTo(47.3789f, 43.8074f)
                    lineTo(38.8936f, 35.3221f)
                    lineTo(38.1865f, 36.0292f)
                    close()
                    moveTo(36.0652f, 36.0292f)
                    curveTo(36.6509f, 35.4434f, 37.6007f, 35.4434f, 38.1865f, 36.0292f)
                    lineTo(38.8936f, 35.3221f)
                    curveTo(37.9173f, 34.3458f, 36.3344f, 34.3458f, 35.3581f, 35.3221f)
                    lineTo(36.0652f, 36.0292f)
                    close()
                    moveTo(36.0652f, 38.1505f)
                    curveTo(35.4794f, 37.5647f, 35.4794f, 36.615f, 36.0652f, 36.0292f)
                    lineTo(35.3581f, 35.3221f)
                    curveTo(34.3817f, 36.2984f, 34.3817f, 37.8813f, 35.3581f, 38.8576f)
                    lineTo(36.0652f, 38.1505f)
                    close()
                    moveTo(44.5504f, 46.6358f)
                    lineTo(36.0652f, 38.1505f)
                    lineTo(35.3581f, 38.8576f)
                    lineTo(43.8433f, 47.3429f)
                    lineTo(44.5504f, 46.6358f)
                    close()
                    moveTo(36.0297f, 55.8637f)
                    lineTo(44.5504f, 47.3429f)
                    lineTo(43.8433f, 46.6358f)
                    lineTo(35.3226f, 55.1566f)
                    lineTo(36.0297f, 55.8637f)
                    close()
                    moveTo(36.0297f, 57.985f)
                    curveTo(35.4439f, 57.3992f, 35.4439f, 56.4494f, 36.0297f, 55.8637f)
                    lineTo(35.3226f, 55.1566f)
                    curveTo(34.3463f, 56.1329f, 34.3463f, 57.7158f, 35.3226f, 58.6921f)
                    lineTo(36.0297f, 57.985f)
                    close()
                    moveTo(38.151f, 57.985f)
                    curveTo(37.5652f, 58.5708f, 36.6155f, 58.5708f, 36.0297f, 57.985f)
                    lineTo(35.3226f, 58.6921f)
                    curveTo(36.2989f, 59.6684f, 37.8818f, 59.6684f, 38.8581f, 58.6921f)
                    lineTo(38.151f, 57.985f)
                    close()
                    moveTo(46.6718f, 49.4642f)
                    lineTo(38.151f, 57.985f)
                    lineTo(38.8581f, 58.6921f)
                    lineTo(47.3789f, 50.1713f)
                    lineTo(46.6718f, 49.4642f)
                    close()
                    moveTo(55.8996f, 57.985f)
                    lineTo(47.3789f, 49.4642f)
                    lineTo(46.6718f, 50.1713f)
                    lineTo(55.1925f, 58.6921f)
                    lineTo(55.8996f, 57.985f)
                    close()
                    moveTo(58.021f, 57.985f)
                    curveTo(57.4352f, 58.5708f, 56.4854f, 58.5708f, 55.8996f, 57.985f)
                    lineTo(55.1925f, 58.6921f)
                    curveTo(56.1688f, 59.6684f, 57.7518f, 59.6684f, 58.7281f, 58.6921f)
                    lineTo(58.021f, 57.985f)
                    close()
                    moveTo(58.021f, 55.8637f)
                    curveTo(58.6067f, 56.4494f, 58.6068f, 57.3992f, 58.021f, 57.985f)
                    lineTo(58.7281f, 58.6921f)
                    curveTo(59.7044f, 57.7158f, 59.7044f, 56.1329f, 58.7281f, 55.1565f)
                    lineTo(58.021f, 55.8637f)
                    close()
                    moveTo(49.5002f, 47.3429f)
                    lineTo(58.021f, 55.8637f)
                    lineTo(58.7281f, 55.1565f)
                    lineTo(50.2073f, 46.6358f)
                    lineTo(49.5002f, 47.3429f)
                    close()
                    moveTo(57.9855f, 38.1505f)
                    lineTo(49.5002f, 46.6358f)
                    lineTo(50.2073f, 47.3429f)
                    lineTo(58.6926f, 38.8576f)
                    lineTo(57.9855f, 38.1505f)
                    close()
                    moveTo(57.9855f, 36.0292f)
                    curveTo(58.5713f, 36.615f, 58.5713f, 37.5647f, 57.9855f, 38.1505f)
                    lineTo(58.6926f, 38.8576f)
                    curveTo(59.6689f, 37.8813f, 59.6689f, 36.2984f, 58.6926f, 35.3221f)
                    lineTo(57.9855f, 36.0292f)
                    close()
                }
            }
        }
            .build()
        return _failBlueIc!!
    }

private var _failBlueIc: ImageVector? = null
