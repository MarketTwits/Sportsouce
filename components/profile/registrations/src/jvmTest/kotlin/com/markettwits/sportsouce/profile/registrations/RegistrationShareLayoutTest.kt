package com.markettwits.sportsouce.profile.registrations

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.unit.Density
import androidx.compose.ui.use
import com.markettwits.IntentAction
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderMember
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPaymentStatus
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderType
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderShareTab
import kotlinx.coroutines.Dispatchers
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalComposeUiApi::class)
class RegistrationShareLayoutTest {
    @Test
    fun changingStyleUpdatesThePreviewAndBothActionsExportOnTheFirstTap() {
        val intent = TestIntentAction()
        ImageComposeScene(width = 390, height = 950, coroutineContext = Dispatchers.Unconfined) {
            SauceTheme { OrderShareTab(order, intent) }
        }.use { scene ->
            scene.renderFrames()
            assertEquals(true, scene.action("Постер").config.getOrNull(SemanticsProperties.Selected))
            scene.action("Поделиться").click()
            scene.renderFrames(1_200_000_000L)

            scene.action("Классика").click()
            scene.renderFrames(2_400_000_000L)
            assertEquals(true, scene.action("Классика").config.getOrNull(SemanticsProperties.Selected))
            scene.action("Скачать PNG").click()
            scene.renderFrames(3_600_000_000L)

            scene.action("Билет").click()
            scene.renderFrames(4_800_000_000L)
            assertEquals(true, scene.action("Билет").config.getOrNull(SemanticsProperties.Selected))
            scene.action("Скачать PNG").click()
            scene.renderFrames(6_000_000_000L)

            assertEquals(listOf("share", "save", "save"), intent.exports.map { it.first })
            val (poster, classic, ticket) = intent.exports.map { (_, bytes) ->
                val image = assertNotNull(ImageIO.read(ByteArrayInputStream(bytes)))
                assertTrue(image.height > 150)
                image
            }
            assertEquals(poster.width, classic.width)
            assertEquals(poster.width, ticket.width)
            assertFalse(intent.exports[0].second.contentEquals(intent.exports[1].second))
            assertFalse(intent.exports[2].second.contentEquals(intent.exports[0].second))
            assertFalse(intent.exports[2].second.contentEquals(intent.exports[1].second))
        }
    }

    @Test
    fun allStylesAndActionsFitOnANarrowScreenWithLargeText() {
        ImageComposeScene(width = 320, height = 1500, density = Density(1f, 2f)) {
            SauceTheme { OrderShareTab(order, TestIntentAction()) }
        }.use { scene ->
            scene.renderFrames()
            val poster = scene.action("Постер").boundsInRoot
            val classic = scene.action("Классика").boundsInRoot
            val ticket = scene.action("Билет").boundsInRoot
            val share = scene.action("Поделиться карточкой").boundsInRoot
            val save = scene.action("Скачать PNG").boundsInRoot
            assertTrue(poster.bottom <= classic.top)
            assertTrue(classic.bottom <= ticket.top)
            assertTrue(share.bottom <= save.top)
            assertTrue(listOf(poster, classic, ticket, share, save).all { it.left >= 16f && it.right <= 304f })
        }
    }

    private fun ImageComposeScene.renderFrames(start: Long = 0L) {
        repeat(12) { render(start + it * 100_000_000L).close() }
    }

    private fun ImageComposeScene.action(title: String): SemanticsNode {
        fun descendants(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::descendants)
        val nodes = semanticsOwners.flatMap { descendants(it.rootSemanticsNode) }
        return assertNotNull(nodes.firstOrNull { node ->
            node.config.contains(SemanticsActions.OnClick) &&
                node.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == title }
        }, "Missing action ${title}")
    }

    private fun SemanticsNode.click() {
        assertEquals(true, config[SemanticsActions.OnClick].action?.invoke())
    }

    private class TestIntentAction : IntentAction {
        val exports = mutableListOf<Pair<String, ByteArray>>()

        override fun shareImage(byteArray: ByteArray, filename: String) {
            assertEquals("sportsauce-registration-42", filename)
            exports += "share" to byteArray
        }

        override fun saveImage(byteArray: ByteArray, filename: String) {
            assertEquals("sportsauce-registration-42", filename)
            exports += "save" to byteArray
        }

        override fun openWebPage(url: String) = Unit
        override fun openPhone(phone: String) = Unit
        override fun copyToSystemBuffer(text: String) = Unit
        override fun sharePlainText(text: String) = Unit
    }

    private val order = StartOrderInfo(
        id = 42,
        startId = 1,
        name = "Новогодний кросс",
        image = "",
        dateStartPreview = "5 октября — 31 декабря 2026",
        dateStartCloud = "2026-10-05T10:00:00Z",
        promo = "",
        payment = StartOrderPaymentStatus.Success(),
        members = listOf(StartOrderMember("Ольга", "Иванова", "", "Общая", "5 км", "", "", emptyList())),
        orderType = StartOrderType.Default,
        startTitle = "Новогодний кросс",
        cost = "1000",
    )
}
