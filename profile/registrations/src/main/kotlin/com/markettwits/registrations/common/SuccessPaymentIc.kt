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
	Image(successPayment, null)
}

private var _SuccessPaymentIc: ImageVector? = null

val successPayment: ImageVector
	get() {
		if (_SuccessPaymentIc != null) {
			return _SuccessPaymentIc!!
		}
		_SuccessPaymentIc = ImageVector.Builder(
			name = "SuccessPaymentIc",
			defaultWidth = 81.dp,
			defaultHeight = 80.dp,
			viewportWidth = 81f,
			viewportHeight = 80f
		).apply {
			path(
				fill = Brush.linearGradient(
					start = Offset(54.5f, 3f),
					end = Offset(16.425f, 47.5306f),
					colorStops = arrayOf(
						0f to Color(0xFF5AE4C0),
						1f to Color(0xFF3AC267),
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
				moveTo(27.5f, 57f)
				curveTo(42.377f, 57f, 54.5f, 44.904f, 54.5f, 30f)
				curveTo(54.5f, 15.123f, 42.377f, 3f, 27.5f, 3f)
				curveTo(12.596f, 3f, 0.5f, 15.123f, 0.5f, 30f)
				curveTo(0.5f, 44.6514f, 12.1894f, 56.5892f, 26.7446f, 56.9896f)
				lineTo(27.5f, 57f)
				close()
			}
			group {
				path(
					fill = SolidColor(Color(0xFF5AE4C0)),
					fillAlpha = 1.0f,
					stroke = null,
					strokeAlpha = 1.0f,
					strokeLineWidth = 1.0f,
					strokeLineCap = StrokeCap.Butt,
					strokeLineJoin = StrokeJoin.Miter,
					strokeLineMiter = 1.0f,
					pathFillType = PathFillType.NonZero
				) {
					moveTo(47.5f, 77f)
					curveTo(65.132f, 77f, 79.5f, 62.664f, 79.5f, 45f)
					curveTo(79.5f, 27.368f, 65.132f, 13f, 47.5f, 13f)
					curveTo(29.836f, 13f, 15.5f, 27.368f, 15.5f, 45f)
					curveTo(15.5f, 62.3646f, 29.3542f, 76.5131f, 46.6048f, 76.9877f)
					lineTo(47.5f, 77f)
					close()
				}
				path(
					fill = null,
					fillAlpha = 1.0f,
					stroke = Brush.linearGradient(
						start = Offset(25.6878f, 20.4561f),
						end = Offset(65.0785f, 73.9422f),
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
					moveTo(47.4931f, 77.5f)
					horizontalLineTo(47.5f)
					curveTo(65.4078f, 77.5f, 80f, 62.9405f, 80f, 45f)
					curveTo(80f, 27.0919f, 65.4081f, 12.5f, 47.5f, 12.5f)
					curveTo(29.5595f, 12.5f, 15f, 27.0922f, 15f, 45f)
					curveTo(15f, 62.6362f, 29.0705f, 77.0055f, 46.591f, 77.4876f)
					lineTo(46.5979f, 77.4877f)
					lineTo(47.4931f, 77.5f)
					close()
				}
			}
			group {
				path(
					fill = Brush.linearGradient(
						start = Offset(52.1057f, 49.5399f),
						end = Offset(60.0272f, 57.8633f),
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
					moveTo(58.0858f, 36.5858f)
					lineTo(43.5f, 51.1716f)
					lineTo(36.9142f, 44.5858f)
					curveTo(36.1332f, 43.8047f, 34.8669f, 43.8048f, 34.0858f, 44.5858f)
					curveTo(33.3047f, 45.3668f, 33.3047f, 46.6331f, 34.0858f, 47.4142f)
					lineTo(43.5f, 56.8284f)
					lineTo(60.9142f, 39.4142f)
					curveTo(61.6953f, 38.6332f, 61.6953f, 37.3669f, 60.9142f, 36.5858f)
					curveTo(60.1332f, 35.8047f, 58.8668f, 35.8047f, 58.0858f, 36.5858f)
					close()
				}
				path(
					fill = null,
					fillAlpha = 1.0f,
					stroke = Brush.linearGradient(
						start = Offset(38.8061f, 44.7566f),
						end = Offset(60.4026f, 44.8935f),
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
					moveTo(43.3586f, 51.313f)
					lineTo(43.5f, 51.4544f)
					lineTo(43.6414f, 51.313f)
					lineTo(58.2272f, 36.7272f)
					lineTo(58.0858f, 36.5858f)
					lineTo(58.2272f, 36.7272f)
					curveTo(58.9301f, 36.0243f, 60.0699f, 36.0243f, 60.7727f, 36.7272f)
					lineTo(60.9142f, 36.5858f)
					lineTo(60.7728f, 36.7272f)
					curveTo(61.4758f, 37.4302f, 61.4757f, 38.5699f, 60.7728f, 39.2728f)
					lineTo(60.9142f, 39.4142f)
					lineTo(60.7728f, 39.2728f)
					lineTo(43.5f, 56.5456f)
					lineTo(34.2272f, 47.2728f)
					curveTo(33.5242f, 46.5698f, 33.5243f, 45.4301f, 34.2272f, 44.7272f)
					curveTo(34.9302f, 44.0243f, 36.0699f, 44.0243f, 36.7728f, 44.7272f)
					lineTo(36.7728f, 44.7272f)
					lineTo(43.3586f, 51.313f)
					close()
				}
			}
		}.build()
		return _SuccessPaymentIc!!
	}

