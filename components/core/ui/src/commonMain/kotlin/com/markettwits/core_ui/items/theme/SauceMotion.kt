package com.markettwits.core_ui.items.theme

import androidx.compose.animation.core.CubicBezierEasing

/** For transitions owned by our components; Material keeps its native motion specs. */
object SauceMotion {
    const val feedbackMillis = 180
    const val progressMillis = 400
    val feedbackEasing = CubicBezierEasing(0.2f, 0.7f, 0.3f, 1f)
    val progressEasing = CubicBezierEasing(0.16f, 1f, 0.3f, 1f)
}
