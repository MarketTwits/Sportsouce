package com.markettwits.sportsouce.unauthorized.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import com.markettwits.sportsouce.unauthorized.domain.GuestWelcomeContent

internal const val GuestWelcomeDurationMillis = 2800
internal val GuestWelcomeLines = GuestWelcomeContent.Local.headingLines
internal val GuestProfileTitle = GuestWelcomeContent.Local.panelTitle
internal val GuestProfileDescription = GuestWelcomeContent.Local.description
private val WelcomeRevealEasing = CubicBezierEasing(.22f, .61f, .36f, 1f)

/** One finite timeline; glyph opacity changes without changing the text's measured layout. */
@Immutable
internal data class GuestWelcomeMotion(val fraction: Float, val lines: List<String> = GuestWelcomeLines) {
    private val elapsed get() = fraction.coerceIn(0f, 1f) * GuestWelcomeDurationMillis
    private fun phase(start: Int, duration: Int) = ((elapsed - start) / duration).coerceIn(0f, 1f)
    private fun reveal(start: Int, duration: Int) = WelcomeRevealEasing.transform(phase(start, duration))
    private fun lineStart(line: Int) = 180 + line * 700

    fun characterAlpha(line: Int, character: Int): Float =
        reveal(lineStart(line) + (character * minOf(48f, 600f / lines[line].length.coerceAtLeast(1))).toInt(), 240)

    fun lineBlur(line: Int): Float =
        10f * (1f - reveal(lineStart(line), 1000))

    fun titleWordAlpha(word: Int, count: Int): Float = reveal(650 + (word * minOf(130f, 900f / (count - 1).coerceAtLeast(1))).toInt(), 620)
    val titleBlur get() = 5f * (1f - reveal(650, 880))
    fun descriptionWordAlpha(word: Int, count: Int): Float = reveal(1150 + (word * minOf(55f, 800f / (count - 1).coerceAtLeast(1))).toInt(), 480)
    val descriptionBlur get() = 2f * (1f - reveal(1150, 950))

    val panelOffset get() = 14f * (1f - reveal(80, 1100))
    val photoScale get() = 1f + .045f * (1f - WelcomeRevealEasing.transform(fraction.coerceIn(0f, 1f)))
}

@Composable
internal fun rememberGuestWelcomeMotion(playIntro: Boolean, lines: List<String> = GuestWelcomeLines): GuestWelcomeMotion {
    val progress = remember(playIntro) { Animatable(if (playIntro) 0f else 1f) }
    LaunchedEffect(progress) {
        // Animatable respects Compose's MotionDurationScale, including the system's disabled animations.
        if (playIntro) progress.animateTo(1f, tween(GuestWelcomeDurationMillis, easing = LinearEasing))
    }
    return GuestWelcomeMotion(progress.value, lines)
}
