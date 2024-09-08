package com.markettwits.start.presentation.common

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

object Animation {
    /** Value in ms */
    const val DEFAULT_LOW_VELOCITY_SWIPE_DURATION = 150

    /** Smooth enough at 300ms */
    const val DEFAULT_NAVIGATION_ANIMATION_DURATION = 300

    /** Syncs with status bar fade in/out */
    const val DEFAULT_TOP_BAR_ANIMATION_DURATION = 500

    val enterAnimation = fadeIn(tween(DEFAULT_LOW_VELOCITY_SWIPE_DURATION))
    val exitAnimation = fadeOut(tween(DEFAULT_LOW_VELOCITY_SWIPE_DURATION))
}
