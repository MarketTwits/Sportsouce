package com.markettwits.core_ui.items.components

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.unit.Density
import androidx.compose.ui.use
import com.markettwits.core_ui.items.components.commerce.SauceQuantityStepper
import com.markettwits.core_ui.items.theme.SauceTheme
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import kotlin.math.roundToInt
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@OptIn(ExperimentalComposeUiApi::class)
class QuantityStepperMotionTest {
    @Test
    fun onlyChangedDigitsMoveAndRapidReversalKeepsTheVisibleFrame() {
        val count = mutableIntStateOf(17)
        ImageComposeScene(width = 240, height = 64) {
            SauceTheme { SauceQuantityStepper(count.intValue, {}, {}) }
        }.use { scene ->
            val initial = scene.frame(0)
            val number = scene.labeled("Количество: 17").boundsInRoot
            val center = number.center.x.roundToInt().toFloat()
            val tens = number.copy(right = center)
            val units = number.copy(left = center)

            count.intValue = 18
            Snapshot.sendApplyNotifications()
            scene.frame(16)
            scene.frame(32)
            scene.frame(60)
            scene.frame(100)
            Snapshot.sendApplyNotifications()
            val moving = scene.frame(100)
            assertContentEquals(initial.pixels(tens), moving.pixels(tens))
            assertFalse(initial.pixels(units).contentEquals(moving.pixels(units)))

            count.intValue = 17
            Snapshot.sendApplyNotifications()
            val retargeted = scene.frame(100)
            assertContentEquals(moving.pixels(number), retargeted.pixels(number))
            scene.frame(116)
            scene.frame(132)
            scene.frame(400)
            val settled = scene.frame(450)
            assertContentEquals(initial.pixels(number), settled.pixels(number))
            scene.labeled("Количество: 17")
        }
    }

    @Test
    fun carryingFromNineToTenKeepsTheButtonsStillWithLargeText() {
        val count = mutableIntStateOf(9)
        ImageComposeScene(width = 240, height = 64, density = Density(1f, 2f)) {
            SauceTheme { SauceQuantityStepper(count.intValue, {}, {}) }
        }.use { scene ->
            scene.frame(0)
            val minus = scene.labeled("Уменьшить количество").boundsInRoot
            val plus = scene.labeled("Увеличить количество").boundsInRoot
            val initialNumber = scene.labeled("Количество: 9").boundsInRoot

            count.intValue = 10
            Snapshot.sendApplyNotifications()
            for (time in listOf(16L, 32L, 70L, 260L)) {
                scene.frame(time)
                assertEquals(minus, scene.labeled("Уменьшить количество").boundsInRoot)
                assertEquals(plus, scene.labeled("Увеличить количество").boundsInRoot)
                assertEquals(initialNumber, scene.labeled("Количество: 10").boundsInRoot)
            }
        }
    }

    private fun ImageComposeScene.labeled(label: String): SemanticsNode {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }.single {
            label in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty()
        }
    }

    private fun ImageComposeScene.frame(ms: Long): BufferedImage = render(ms * 1_000_000L).use { image ->
        image.encodeToData()!!.use { ImageIO.read(ByteArrayInputStream(it.bytes)) }
    }

    private fun BufferedImage.pixels(bounds: Rect): IntArray {
        val width = bounds.width.roundToInt()
        val height = bounds.height.roundToInt()
        return getRGB(bounds.left.roundToInt(), bounds.top.roundToInt(), width, height, null, 0, width)
    }
}
