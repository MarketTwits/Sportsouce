import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
private fun VectorPreview() {
    Image(freePayment, null)
}

private var _Freepayment: ImageVector? = null

val freePayment: ImageVector
    get() {
        if (_Freepayment != null) {
            return _Freepayment!!
        }
        _Freepayment = ImageVector.Builder(
            name = "Freepayment",
            defaultWidth = 80.dp,
            defaultHeight = 80.dp,
            viewportWidth = 80f,
            viewportHeight = 80f
        ).apply {
            path(
                fill = Brush.linearGradient(
                    start = Offset(54f, 3f),
                    end = Offset(15.925f, 47.5306f),
                    colorStops = arrayOf(
                        0f to Color(0xFFFFD62C),
                        1f to Color(0xFFF7B200),
                    ),
                ),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(27f, 57f)
                curveTo(41.877f, 57f, 54f, 44.904f, 54f, 30f)
                curveTo(54f, 15.123f, 41.877f, 3f, 27f, 3f)
                curveTo(12.096f, 3f, 0f, 15.123f, 0f, 30f)
                curveTo(0f, 44.6514f, 11.6894f, 56.5892f, 26.2446f, 56.9896f)
                lineTo(27f, 57f)
                close()
            }
            group {
                path(
                    fill = SolidColor(Color(0xFFCC9916)),
                    fillAlpha = 1.0f,
                    stroke = null,
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(47f, 77f)
                    curveTo(64.632f, 77f, 79f, 62.664f, 79f, 45f)
                    curveTo(79f, 27.368f, 64.632f, 13f, 47f, 13f)
                    curveTo(29.336f, 13f, 15f, 27.368f, 15f, 45f)
                    curveTo(15f, 62.3646f, 28.8542f, 76.5131f, 46.1048f, 76.9877f)
                    lineTo(47f, 77f)
                    close()
                }
                path(
                    fill = null,
                    fillAlpha = 1.0f,
                    stroke = Brush.linearGradient(
                        start = Offset(25.1878f, 20.4561f),
                        end = Offset(64.5785f, 73.9422f),
                        colorStops = arrayOf(
                            0f to Color(0xFFFFFFFF),
                            1f to Color(0xFFFFFFFF),
                        ),
                    ),
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(46.9931f, 77.5f)
                    horizontalLineTo(47f)
                    curveTo(64.9078f, 77.5f, 79.5f, 62.9405f, 79.5f, 45f)
                    curveTo(79.5f, 27.0919f, 64.9081f, 12.5f, 47f, 12.5f)
                    curveTo(29.0595f, 12.5f, 14.5f, 27.0922f, 14.5f, 45f)
                    curveTo(14.5f, 62.6362f, 28.5705f, 77.0055f, 46.091f, 77.4876f)
                    lineTo(46.0979f, 77.4877f)
                    lineTo(46.9931f, 77.5f)
                    close()
                }
            }
            group {
                path(
                    fill = Brush.linearGradient(
                        start = Offset(52.6057f, 50.5399f),
                        end = Offset(60.5272f, 58.8633f),
                        colorStops = arrayOf(
                            0f to Color(0xFFFFFFFF),
                            1f to Color(0xFFFFFFFF),
                        ),
                    ),
                    fillAlpha = 1.0f,
                    stroke = null,
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(58.5858f, 37.5858f)
                    lineTo(44f, 52.1716f)
                    lineTo(37.4142f, 45.5858f)
                    curveTo(36.6332f, 44.8047f, 35.3669f, 44.8048f, 34.5858f, 45.5858f)
                    curveTo(33.8047f, 46.3668f, 33.8047f, 47.6331f, 34.5858f, 48.4142f)
                    lineTo(44f, 57.8284f)
                    lineTo(61.4142f, 40.4142f)
                    curveTo(62.1953f, 39.6332f, 62.1953f, 38.3669f, 61.4142f, 37.5858f)
                    curveTo(60.6332f, 36.8047f, 59.3668f, 36.8047f, 58.5858f, 37.5858f)
                    close()
                }
                path(
                    fill = null,
                    fillAlpha = 1.0f,
                    stroke = Brush.linearGradient(
                        start = Offset(39.3061f, 45.7566f),
                        end = Offset(60.9026f, 45.8935f),
                        colorStops = arrayOf(
                            0f to Color(0xFFFFFFFF),
                            1f to Color(0xFFFFFFFF),
                        ),
                    ),
                    strokeAlpha = 0.5f,
                    strokeLineWidth = 0.4f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(43.8586f, 52.313f)
                    lineTo(44f, 52.4544f)
                    lineTo(44.1414f, 52.313f)
                    lineTo(58.7272f, 37.7272f)
                    lineTo(58.5858f, 37.5858f)
                    lineTo(58.7272f, 37.7272f)
                    curveTo(59.4301f, 37.0243f, 60.5699f, 37.0243f, 61.2727f, 37.7272f)
                    lineTo(61.4142f, 37.5858f)
                    lineTo(61.2728f, 37.7272f)
                    curveTo(61.9758f, 38.4302f, 61.9757f, 39.5699f, 61.2728f, 40.2728f)
                    lineTo(61.4142f, 40.4142f)
                    lineTo(61.2728f, 40.2728f)
                    lineTo(44f, 57.5456f)
                    lineTo(34.7272f, 48.2728f)
                    curveTo(34.0242f, 47.5698f, 34.0243f, 46.4301f, 34.7272f, 45.7272f)
                    curveTo(35.4302f, 45.0243f, 36.5699f, 45.0243f, 37.2728f, 45.7272f)
                    lineTo(37.2728f, 45.7272f)
                    lineTo(43.8586f, 52.313f)
                    close()
                }
            }
        }.build()
        return _Freepayment!!
    }

