import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
private fun VectorPreview() {
    Image(InstagramIcon, null)
}

private var _MdiInstagram: ImageVector? = null

val InstagramIcon: ImageVector
    get() {
        if (_MdiInstagram != null) {
            return _MdiInstagram!!
        }
        _MdiInstagram = ImageVector.Builder(
            name = "MdiInstagram",
            defaultWidth = 30.dp,
            defaultHeight = 30.dp,
            viewportWidth = 30f,
            viewportHeight = 30f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFF75C96)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(9.75f, 2.5f)
                horizontalLineTo(20.25f)
                curveTo(24.25f, 2.5f, 27.5f, 5.75f, 27.5f, 9.75f)
                verticalLineTo(20.25f)
                curveTo(27.5f, 22.1728f, 26.7362f, 24.0169f, 25.3765f, 25.3765f)
                curveTo(24.0169f, 26.7362f, 22.1728f, 27.5f, 20.25f, 27.5f)
                horizontalLineTo(9.75f)
                curveTo(5.75f, 27.5f, 2.5f, 24.25f, 2.5f, 20.25f)
                verticalLineTo(9.75f)
                curveTo(2.5f, 7.8272f, 3.2638f, 5.9831f, 4.6235f, 4.6235f)
                curveTo(5.9831f, 3.2638f, 7.8272f, 2.5f, 9.75f, 2.5f)
                close()
                moveTo(9.5f, 5f)
                curveTo(8.3065f, 5f, 7.1619f, 5.4741f, 6.318f, 6.318f)
                curveTo(5.4741f, 7.1619f, 5f, 8.3065f, 5f, 9.5f)
                verticalLineTo(20.5f)
                curveTo(5f, 22.9875f, 7.0125f, 25f, 9.5f, 25f)
                horizontalLineTo(20.5f)
                curveTo(21.6935f, 25f, 22.8381f, 24.5259f, 23.682f, 23.682f)
                curveTo(24.5259f, 22.8381f, 25f, 21.6935f, 25f, 20.5f)
                verticalLineTo(9.5f)
                curveTo(25f, 7.0125f, 22.9875f, 5f, 20.5f, 5f)
                horizontalLineTo(9.5f)
                close()
                moveTo(21.5625f, 6.875f)
                curveTo(21.9769f, 6.875f, 22.3743f, 7.0396f, 22.6674f, 7.3327f)
                curveTo(22.9604f, 7.6257f, 23.125f, 8.0231f, 23.125f, 8.4375f)
                curveTo(23.125f, 8.8519f, 22.9604f, 9.2493f, 22.6674f, 9.5424f)
                curveTo(22.3743f, 9.8354f, 21.9769f, 10f, 21.5625f, 10f)
                curveTo(21.1481f, 10f, 20.7507f, 9.8354f, 20.4576f, 9.5424f)
                curveTo(20.1646f, 9.2493f, 20f, 8.8519f, 20f, 8.4375f)
                curveTo(20f, 8.0231f, 20.1646f, 7.6257f, 20.4576f, 7.3327f)
                curveTo(20.7507f, 7.0396f, 21.1481f, 6.875f, 21.5625f, 6.875f)
                close()
                moveTo(15f, 8.75f)
                curveTo(16.6576f, 8.75f, 18.2473f, 9.4085f, 19.4194f, 10.5806f)
                curveTo(20.5915f, 11.7527f, 21.25f, 13.3424f, 21.25f, 15f)
                curveTo(21.25f, 16.6576f, 20.5915f, 18.2473f, 19.4194f, 19.4194f)
                curveTo(18.2473f, 20.5915f, 16.6576f, 21.25f, 15f, 21.25f)
                curveTo(13.3424f, 21.25f, 11.7527f, 20.5915f, 10.5806f, 19.4194f)
                curveTo(9.4085f, 18.2473f, 8.75f, 16.6576f, 8.75f, 15f)
                curveTo(8.75f, 13.3424f, 9.4085f, 11.7527f, 10.5806f, 10.5806f)
                curveTo(11.7527f, 9.4085f, 13.3424f, 8.75f, 15f, 8.75f)
                close()
                moveTo(15f, 11.25f)
                curveTo(14.0054f, 11.25f, 13.0516f, 11.6451f, 12.3483f, 12.3483f)
                curveTo(11.6451f, 13.0516f, 11.25f, 14.0054f, 11.25f, 15f)
                curveTo(11.25f, 15.9946f, 11.6451f, 16.9484f, 12.3483f, 17.6517f)
                curveTo(13.0516f, 18.3549f, 14.0054f, 18.75f, 15f, 18.75f)
                curveTo(15.9946f, 18.75f, 16.9484f, 18.3549f, 17.6517f, 17.6517f)
                curveTo(18.3549f, 16.9484f, 18.75f, 15.9946f, 18.75f, 15f)
                curveTo(18.75f, 14.0054f, 18.3549f, 13.0516f, 17.6517f, 12.3483f)
                curveTo(16.9484f, 11.6451f, 15.9946f, 11.25f, 15f, 11.25f)
                close()
            }
        }.build()
        return _MdiInstagram!!
    }

