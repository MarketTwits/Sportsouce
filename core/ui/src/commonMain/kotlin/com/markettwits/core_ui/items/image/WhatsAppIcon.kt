package com.markettwits.core_ui.items.image

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp


private var _BaselineWhatsapp: ImageVector? = null

val WhatsappIcon: ImageVector
    get() {
        if (_BaselineWhatsapp != null) {
            return _BaselineWhatsapp!!
        }
        _BaselineWhatsapp = ImageVector.Builder(
            name = "BaselineWhatsapp",
            defaultWidth = 30.dp,
            defaultHeight = 30.dp,
            viewportWidth = 30f,
            viewportHeight = 30f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF13990A)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(23.8125f, 6.13757f)
                curveTo(22.6665f, 4.98f, 21.3015f, 4.0622f, 19.797f, 3.4376f)
                curveTo(18.2926f, 2.8131f, 16.6789f, 2.4944f, 15.05f, 2.5001f)
                curveTo(8.225f, 2.5001f, 2.6625f, 8.0626f, 2.6625f, 14.8876f)
                curveTo(2.6625f, 17.0751f, 3.2375f, 19.2001f, 4.3125f, 21.0751f)
                lineTo(2.5625f, 27.5001f)
                lineTo(9.125f, 25.7751f)
                curveTo(10.9375f, 26.7626f, 12.975f, 27.2876f, 15.05f, 27.2876f)
                curveTo(21.875f, 27.2876f, 27.4375f, 21.7251f, 27.4375f, 14.9001f)
                curveTo(27.4375f, 11.5876f, 26.15f, 8.4751f, 23.8125f, 6.1376f)
                close()
                moveTo(15.05f, 25.1876f)
                curveTo(13.2f, 25.1876f, 11.3875f, 24.6876f, 9.8f, 23.7501f)
                lineTo(9.425f, 23.5251f)
                lineTo(5.525f, 24.5501f)
                lineTo(6.5625f, 20.7501f)
                lineTo(6.3125f, 20.3626f)
                curveTo(5.2847f, 18.7213f, 4.7389f, 16.8241f, 4.7375f, 14.8876f)
                curveTo(4.7375f, 9.2126f, 9.3625f, 4.5876f, 15.0375f, 4.5876f)
                curveTo(17.7875f, 4.5876f, 20.375f, 5.6626f, 22.3125f, 7.6126f)
                curveTo(23.2719f, 8.5675f, 24.0321f, 9.7034f, 24.5492f, 10.9544f)
                curveTo(25.0664f, 12.2053f, 25.33f, 13.5465f, 25.325f, 14.9001f)
                curveTo(25.35f, 20.5751f, 20.725f, 25.1876f, 15.05f, 25.1876f)
                close()
                moveTo(20.7f, 17.4876f)
                curveTo(20.3875f, 17.3376f, 18.8625f, 16.5876f, 18.5875f, 16.4751f)
                curveTo(18.3f, 16.3751f, 18.1f, 16.3251f, 17.8875f, 16.6251f)
                curveTo(17.675f, 16.9376f, 17.0875f, 17.6376f, 16.9125f, 17.8376f)
                curveTo(16.7375f, 18.0501f, 16.55f, 18.0751f, 16.2375f, 17.9126f)
                curveTo(15.925f, 17.7626f, 14.925f, 17.4251f, 13.75f, 16.3751f)
                curveTo(12.825f, 15.5501f, 12.2125f, 14.5376f, 12.025f, 14.2251f)
                curveTo(11.85f, 13.9126f, 12f, 13.7501f, 12.1625f, 13.5876f)
                curveTo(12.3f, 13.4501f, 12.475f, 13.2251f, 12.625f, 13.0501f)
                curveTo(12.775f, 12.8751f, 12.8375f, 12.7376f, 12.9375f, 12.5376f)
                curveTo(13.0375f, 12.3251f, 12.9875f, 12.1501f, 12.9125f, 12.0001f)
                curveTo(12.8375f, 11.8501f, 12.2125f, 10.3251f, 11.9625f, 9.7001f)
                curveTo(11.7125f, 9.1001f, 11.45f, 9.1751f, 11.2625f, 9.1626f)
                horizontalLineTo(10.6625f)
                curveTo(10.45f, 9.1626f, 10.125f, 9.2376f, 9.8375f, 9.5501f)
                curveTo(9.5625f, 9.8626f, 8.7625f, 10.6126f, 8.7625f, 12.1376f)
                curveTo(8.7625f, 13.6626f, 9.875f, 15.1376f, 10.025f, 15.3376f)
                curveTo(10.175f, 15.5501f, 12.2125f, 18.6751f, 15.3125f, 20.0126f)
                curveTo(16.05f, 20.3376f, 16.625f, 20.5251f, 17.075f, 20.6626f)
                curveTo(17.8125f, 20.9001f, 18.4875f, 20.8626f, 19.025f, 20.7876f)
                curveTo(19.625f, 20.7001f, 20.8625f, 20.0376f, 21.1125f, 19.3126f)
                curveTo(21.375f, 18.5876f, 21.375f, 17.9751f, 21.2875f, 17.8376f)
                curveTo(21.2f, 17.7001f, 21.0125f, 17.6376f, 20.7f, 17.4876f)
                close()
            }
        }.build()
        return _BaselineWhatsapp!!
    }

