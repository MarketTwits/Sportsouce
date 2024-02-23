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
	Image(dontPayment, null)
}

private var _DontPaymentIc: ImageVector? = null

val dontPayment: ImageVector
	get() {
		if (_DontPaymentIc != null) {
			return _DontPaymentIc!!
		}
		_DontPaymentIc = ImageVector.Builder(
			name = "DontPaymentIc",
			defaultWidth = 81.dp,
			defaultHeight = 80.dp,
			viewportWidth = 81f,
			viewportHeight = 80f
		).apply {
			group {
				path(
					fill = Brush.linearGradient(
						start = Offset(54.5f, 8f),
						end = Offset(16.425f, 52.5306f),
						colorStops = arrayOf(
							0f to Color(0xFFFC766C),
							1f to Color(0xFFF44336),
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
					moveTo(27.5f, 62f)
					curveTo(42.377f, 62f, 54.5f, 49.904f, 54.5f, 35f)
					curveTo(54.5f, 20.123f, 42.377f, 8f, 27.5f, 8f)
					curveTo(12.596f, 8f, 0.5f, 20.123f, 0.5f, 35f)
					curveTo(0.5f, 49.6514f, 12.1894f, 61.5892f, 26.7446f, 61.9896f)
					lineTo(27.5f, 62f)
					close()
				}
				group {
					path(
						fill = SolidColor(Color(0xFFE54D42)),
						fillAlpha = 0.5f,
						stroke = null,
						strokeAlpha = 0.5f,
						strokeLineWidth = 1.0f,
						strokeLineCap = StrokeCap.Butt,
						strokeLineJoin = StrokeJoin.Miter,
						strokeLineMiter = 1.0f,
						pathFillType = PathFillType.NonZero
					) {
						moveTo(27.3214f, 49.4f)
						curveTo(35.2558f, 49.4f, 41.7214f, 42.9488f, 41.7214f, 35f)
						curveTo(41.7214f, 27.0656f, 35.2558f, 20.6f, 27.3214f, 20.6f)
						curveTo(19.3726f, 20.6f, 12.9214f, 27.0656f, 12.9214f, 35f)
						curveTo(12.9214f, 42.814f, 19.1558f, 49.1809f, 26.9185f, 49.3944f)
						lineTo(27.3214f, 49.4f)
						close()
					}
				}
			}
			group {
				path(
					fill = SolidColor(Color(0xFFE54D42)),
					fillAlpha = 1.0f,
					stroke = null,
					strokeAlpha = 1.0f,
					strokeLineWidth = 1.0f,
					strokeLineCap = StrokeCap.Butt,
					strokeLineJoin = StrokeJoin.Miter,
					strokeLineMiter = 1.0f,
					pathFillType = PathFillType.NonZero
				) {
					moveTo(78.6746f, 59.0764f)
					lineTo(55.0511f, 17.7269f)
					curveTo(52.5815f, 13.3096f, 46.9599f, 11.6857f, 42.5407f, 14.1574f)
					curveTo(41.0134f, 14.9693f, 39.7786f, 16.2328f, 38.9663f, 17.6976f)
					lineTo(15.1803f, 59.3687f)
					curveTo(13.2631f, 64.0425f, 15.5377f, 69.4341f, 20.2494f, 71.3504f)
					curveTo(21.1918f, 71.7077f, 22.1991f, 71.9383f, 23.2064f, 72f)
					horizontalLineTo(70.7784f)
					curveTo(73.2155f, 71.8701f, 75.4576f, 70.7983f, 77.1149f, 68.9827f)
					curveTo(78.7721f, 67.1606f, 79.6169f, 64.7928f, 79.487f, 62.3569f)
					curveTo(79.422f, 61.3143f, 79.1945f, 60.3106f, 78.6746f, 59.0764f)
					verticalLineTo(59.0764f)
					close()
				}
				path(
					fill = null,
					fillAlpha = 1.0f,
					stroke = Brush.linearGradient(
						start = Offset(24.8469f, 19.8736f),
						end = Offset(61.5599f, 69.9676f),
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
					moveTo(79.1354f, 58.8823f)
					lineTo(78.6746f, 59.0764f)
					lineTo(79.1087f, 58.8284f)
					lineTo(55.4875f, 17.4829f)
					curveTo(55.4871f, 17.4822f, 55.4868f, 17.4815f, 55.4864f, 17.4809f)
					curveTo(52.8839f, 12.8285f, 46.963f, 11.1144f, 42.3012f, 13.7184f)
					curveTo(40.6868f, 14.578f, 39.3861f, 15.9108f, 38.5305f, 17.4524f)
					curveTo(38.53f, 17.4533f, 38.5295f, 17.4542f, 38.529f, 17.4551f)
					lineTo(14.7461f, 59.1209f)
					curveTo(14.7354f, 59.1396f, 14.7259f, 59.159f, 14.7177f, 59.179f)
					curveTo(12.6947f, 64.1108f, 15.095f, 69.7938f, 20.0611f, 71.8136f)
					lineTo(20.061f, 71.8137f)
					lineTo(20.0722f, 71.8179f)
					curveTo(21.0572f, 72.1914f, 22.1139f, 72.434f, 23.1759f, 72.4991f)
					curveTo(23.186f, 72.4997f, 23.1962f, 72.5f, 23.2064f, 72.5f)
					horizontalLineTo(70.7784f)
					curveTo(70.7873f, 72.5f, 70.7962f, 72.4998f, 70.805f, 72.4993f)
					curveTo(73.3751f, 72.3623f, 75.7397f, 71.2309f, 77.4841f, 69.3197f)
					lineTo(77.4847f, 69.3191f)
					curveTo(79.2313f, 67.3987f, 80.1234f, 64.9004f, 79.9863f, 62.3302f)
					lineTo(79.986f, 62.3257f)
					curveTo(79.9172f, 61.222f, 79.6751f, 60.1635f, 79.1354f, 58.8823f)
					close()
				}
			}
			group {
				path(
					fill = Brush.linearGradient(
						start = Offset(50.0012f, -69.9877f),
						end = Offset(39.8251f, -70.3116f),
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
					pathFillType = PathFillType.EvenOdd
				) {
					moveTo(47.5f, 33f)
					curveTo(46.3954f, 33f, 45.5f, 33.8954f, 45.5f, 35f)
					verticalLineTo(48f)
					curveTo(45.5f, 49.1046f, 46.3954f, 50f, 47.5f, 50f)
					curveTo(48.6046f, 50f, 49.5f, 49.1046f, 49.5f, 48f)
					verticalLineTo(35f)
					curveTo(49.5f, 33.8954f, 48.6046f, 33f, 47.5f, 33f)
					close()
					moveTo(45f, 58.5014f)
					curveTo(45f, 59.8614f, 46.1429f, 61f, 47.5143f, 61f)
					curveTo(48.8857f, 61f, 50f, 59.8614f, 50f, 58.4702f)
					curveTo(50f, 57.1102f, 48.8857f, 56f, 47.5143f, 56f)
					curveTo(46.1429f, 56f, 45f, 57.1357f, 45f, 58.5014f)
					close()
				}
				path(
					fill = Brush.linearGradient(
						start = Offset(45.7959f, 36.262f),
						end = Offset(53.501f, 37.9661f),
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
					moveTo(46f, 35f)
					curveTo(46f, 34.1716f, 46.6716f, 33.5f, 47.5f, 33.5f)
					verticalLineTo(32.5f)
					curveTo(46.1193f, 32.5f, 45f, 33.6193f, 45f, 35f)
					horizontalLineTo(46f)
					close()
					moveTo(46f, 48f)
					verticalLineTo(35f)
					horizontalLineTo(45f)
					verticalLineTo(48f)
					horizontalLineTo(46f)
					close()
					moveTo(47.5f, 49.5f)
					curveTo(46.6716f, 49.5f, 46f, 48.8284f, 46f, 48f)
					horizontalLineTo(45f)
					curveTo(45f, 49.3807f, 46.1193f, 50.5f, 47.5f, 50.5f)
					verticalLineTo(49.5f)
					close()
					moveTo(49f, 48f)
					curveTo(49f, 48.8284f, 48.3284f, 49.5f, 47.5f, 49.5f)
					verticalLineTo(50.5f)
					curveTo(48.8807f, 50.5f, 50f, 49.3807f, 50f, 48f)
					horizontalLineTo(49f)
					close()
					moveTo(49f, 35f)
					verticalLineTo(48f)
					horizontalLineTo(50f)
					verticalLineTo(35f)
					horizontalLineTo(49f)
					close()
					moveTo(47.5f, 33.5f)
					curveTo(48.3284f, 33.5f, 49f, 34.1716f, 49f, 35f)
					horizontalLineTo(50f)
					curveTo(50f, 33.6193f, 48.8807f, 32.5f, 47.5f, 32.5f)
					verticalLineTo(33.5f)
					close()
					moveTo(47.5143f, 60.5f)
					curveTo(46.4164f, 60.5f, 45.5f, 59.5827f, 45.5f, 58.5014f)
					horizontalLineTo(44.5f)
					curveTo(44.5f, 60.1402f, 45.8693f, 61.5f, 47.5143f, 61.5f)
					verticalLineTo(60.5f)
					close()
					moveTo(49.5f, 58.4702f)
					curveTo(49.5f, 59.5942f, 48.6007f, 60.5f, 47.5143f, 60.5f)
					verticalLineTo(61.5f)
					curveTo(49.1707f, 61.5f, 50.5f, 60.1287f, 50.5f, 58.4702f)
					horizontalLineTo(49.5f)
					close()
					moveTo(47.5143f, 56.5f)
					curveTo(48.6122f, 56.5f, 49.5f, 57.3889f, 49.5f, 58.4702f)
					horizontalLineTo(50.5f)
					curveTo(50.5f, 56.8314f, 49.1593f, 55.5f, 47.5143f, 55.5f)
					verticalLineTo(56.5f)
					close()
					moveTo(45.5f, 58.5014f)
					curveTo(45.5f, 57.4144f, 46.4164f, 56.5f, 47.5143f, 56.5f)
					verticalLineTo(55.5f)
					curveTo(45.8693f, 55.5f, 44.5f, 56.857f, 44.5f, 58.5014f)
					horizontalLineTo(45.5f)
					close()
				}
			}
		}.build()
		return _DontPaymentIc!!
	}

