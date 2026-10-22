package com.markettwits.sportsouce.profile

import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.MotionDurationScale
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.use
import com.markettwits.sportsouce.unauthorized.presentation.components.GuestWelcomeMotion
import com.markettwits.sportsouce.unauthorized.presentation.components.rememberGuestWelcomeMotion
import kotlinx.coroutines.Dispatchers
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class GuestWelcomeHapticsTest {
    @Test fun typingTicksFollowVisibleLettersAndStopWithTheHeading() {
        val lines = listOf("Ваш старт", "уже здесь")
        val ticks = mutableListOf<Int>()
        var visible = 0
        var motion: GuestWelcomeMotion? = null
        val haptic = object : HapticFeedback {
            override fun performHapticFeedback(hapticFeedbackType: HapticFeedbackType) {
                assertEquals(HapticFeedbackType.TextHandleMove, hapticFeedbackType)
                ticks += visible
            }
        }
        val scale = object : MotionDurationScale { override val scaleFactor = 1f }
        ImageComposeScene(390, 840, coroutineContext = Dispatchers.Unconfined + scale) {
            CompositionLocalProvider(LocalHapticFeedback provides haptic) {
                motion = rememberGuestWelcomeMotion(true, lines)
            }
        }.use { scene ->
            var previous = 0
            for (ms in 0L..3200L step 16) {
                // The callback observes the same timeline state used to render the letters.
                visible = ms.toInt()
                scene.render(1_000_000_000L + ms * 1_000_000L).close()
                assertTrue(ticks.size - previous <= 1, "A frame must not replay a burst of ticks")
                previous = ticks.size
            }
            assertEquals(lines.sumOf { it.count { c -> !c.isWhitespace() } }, ticks.size)
            assertTrue(ticks.first() >= 180, "The intro delay must remain silent")
            assertTrue(ticks.last() < 1600, "Panel copy and the remainder of the intro must be silent")
            assertEquals(lines.sumOf { it.count { c -> !c.isWhitespace() } }, motion!!.revealedCharacterCount())
            val settled = ticks.size
            scene.render(6_000_000_000L).close()
            assertEquals(settled, ticks.size)
        }
    }

    @Test fun settledWelcomeDisabledAnimationsAndDisposalStaySilent() {
        for ((intro, scaleValue) in listOf(false to 1f, true to 0f)) {
            var ticks = 0
            val haptic = object : HapticFeedback {
                override fun performHapticFeedback(hapticFeedbackType: HapticFeedbackType) { ticks++ }
            }
            val scale = object : MotionDurationScale { override val scaleFactor = scaleValue }
            ImageComposeScene(390, 840, coroutineContext = Dispatchers.Unconfined + scale) {
                CompositionLocalProvider(LocalHapticFeedback provides haptic) { rememberGuestWelcomeMotion(intro) }
            }.use { scene ->
                repeat(20) { scene.render(1_000_000_000L + it * 200_000_000L).close() }
                assertEquals(0, ticks)
            }
        }
        var ticks = 0
        val visible = mutableStateOf(true)
        val haptic = object : HapticFeedback {
            override fun performHapticFeedback(hapticFeedbackType: HapticFeedbackType) { ticks++ }
        }
        val scale = object : MotionDurationScale { override val scaleFactor = 1f }
        ImageComposeScene(390, 840, coroutineContext = Dispatchers.Unconfined + scale) {
            CompositionLocalProvider(LocalHapticFeedback provides haptic) {
                if (visible.value) rememberGuestWelcomeMotion(true)
            }
        }.use { scene ->
            repeat(40) { scene.render(1_000_000_000L + it * 16_000_000L).close() }
            assertTrue(ticks > 0)
            visible.value = false
            scene.render(1_640_000_000L).close()
            val atDisposal = ticks
            repeat(20) { scene.render(1_700_000_000L + it * 100_000_000L).close() }
            assertEquals(atDisposal, ticks, "Opening another screen must cancel typing haptics")
        }
    }
}
