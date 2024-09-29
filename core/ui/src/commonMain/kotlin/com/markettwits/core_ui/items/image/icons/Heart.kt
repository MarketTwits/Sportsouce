package com.markettwits.core_ui.items.image.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val OutlineHeartIcon: ImageVector
    get() {
        if (_outlineHeart != null) {
            return _outlineHeart!!
        }
        _outlineHeart = ImageVector.Builder(
            name = "Heart", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
            viewportWidth = 24.0f, viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, fillAlpha = 0.0f, strokeAlpha
                = 0.0f, strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(0.0f, 0.0f)
                horizontalLineToRelative(24.0f)
                verticalLineToRelative(24.0f)
                horizontalLineToRelative(-24.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF231f20)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(12.0f, 21.0f)
                horizontalLineToRelative(0.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, false, true, -0.71f, -0.29f)
                lineTo(3.52f, 12.93f)
                arcToRelative(5.26f, 5.26f, 0.0f, false, true, 0.0f, -7.4f)
                arcToRelative(5.24f, 5.24f, 0.0f, false, true, 7.4f, 0.0f)
                lineTo(12.0f, 6.61f)
                lineToRelative(1.08f, -1.08f)
                arcToRelative(5.24f, 5.24f, 0.0f, false, true, 7.4f, 0.0f)
                arcToRelative(5.26f, 5.26f, 0.0f, false, true, 0.0f, 7.4f)
                lineToRelative(-7.77f, 7.78f)
                arcTo(1.0f, 1.0f, 0.0f, false, true, 12.0f, 21.0f)
                close()
                moveTo(7.22f, 6.0f)
                arcToRelative(3.2f, 3.2f, 0.0f, false, false, -2.28f, 0.94f)
                arcToRelative(3.24f, 3.24f, 0.0f, false, false, 0.0f, 4.57f)
                lineTo(12.0f, 18.58f)
                lineToRelative(7.06f, -7.07f)
                arcToRelative(3.24f, 3.24f, 0.0f, false, false, 0.0f, -4.57f)
                arcToRelative(3.32f, 3.32f, 0.0f, false, false, -4.56f, 0.0f)
                lineToRelative(-1.79f, 1.8f)
                arcToRelative(1.0f, 1.0f, 0.0f, false, true, -1.42f, 0.0f)
                lineTo(9.5f, 6.94f)
                arcTo(3.2f, 3.2f, 0.0f, false, false, 7.22f, 6.0f)
                close()
            }
        }
            .build()
        return _outlineHeart!!
    }

private var _outlineHeart: ImageVector? = null

val FillHeartIcon: ImageVector
    get() {
        if (_filledHeart != null) {
            return _filledHeart!!
        }
        _filledHeart = ImageVector.Builder(
            name = "Heart", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
            viewportWidth = 24.0f, viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, fillAlpha = 0.0f, strokeAlpha
                = 0.0f, strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(0.0f, 0.0f)
                horizontalLineToRelative(24.0f)
                verticalLineToRelative(24.0f)
                horizontalLineToRelative(-24.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF231f20)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(12.0f, 21.0f)
                horizontalLineToRelative(0.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, false, true, -0.71f, -0.29f)
                lineTo(3.52f, 12.93f)
                arcToRelative(5.26f, 5.26f, 0.0f, false, true, 0.0f, -7.4f)
                arcToRelative(5.24f, 5.24f, 0.0f, false, true, 7.4f, 0.0f)
                lineTo(12.0f, 6.61f)
                lineToRelative(1.08f, -1.08f)
                arcToRelative(5.24f, 5.24f, 0.0f, false, true, 7.4f, 0.0f)
                arcToRelative(5.26f, 5.26f, 0.0f, false, true, 0.0f, 7.4f)
                lineToRelative(-7.77f, 7.78f)
                arcTo(1.0f, 1.0f, 0.0f, false, true, 12.0f, 21.0f)
                close()
            }
        }
            .build()
        return _filledHeart!!
    }

private var _filledHeart: ImageVector? = null
