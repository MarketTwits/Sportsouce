package com.markettwits.sportsouce.profile.registrations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.registrations.domain.*
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start.OrderStartCard
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class RegistrationCardLayoutTest {
    @Test
    fun compactCardsPreserveOrderIdentityPaymentAndNavigation() {
        val paymentStates = listOf(StartOrderPaymentStatus.Success(), StartOrderPaymentStatus.NotPaid(),
            StartOrderPaymentStatus.Free(), StartOrderPaymentStatus.PaymentCancelled(),
            StartOrderPaymentStatus.OnPlace(), StartOrderPaymentStatus.WithoutStatus())
        for (dark in listOf(false, true)) for (payment in paymentStates) {
            var selected = -1
            val order = order(payment).let { order ->
                if (payment is StartOrderPaymentStatus.Success) order.copy(members = order.members.map { member ->
                    member.copy(results = listOf(StartOrderMemberResult("12", emptyMap(), "4 км", 1, 1, "Даниил Кулаков", 1, "00:24:15", "М", "", "СпортСоюз")))
                }) else order
            }
            ImageComposeScene(width = 390, height = 800) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background).padding(16.dp)) { OrderStartCard(item = order, onClickStart = { selected = it }) }
                }
            }.use { scene ->
                scene.settle()
                val card = scene.nodes().single { it.config.contains(SemanticsActions.OnClick) }
                assertTrue(card.boundsInRoot.height <= 250f, "Card should stay compact: ${card.boundsInRoot}")
                for (label in listOf("№ 52803", order.startTitle, order.dateStartPreview,
                    payment.title, "Кулаков Даниил", "Ретро кросс 2020", "1 020 ₽")) {
                    assertNotNull(scene.text(label), "Missing $label")
                }
                if (payment is StartOrderPaymentStatus.Success) scene.save("registration-${if (dark) "dark" else "light"}")
                scene.assertTextFits()
                scene.sendPointerEvent(PointerEventType.Press, card.boundsInRoot.center)
                scene.sendPointerEvent(PointerEventType.Release, card.boundsInRoot.center)
                assertEquals(order.startId, selected)

            }
        }
    }

    @Test
    fun narrowCardsAndLargeTextKeepTeamStatusAndPriceReadable() {
        for (width in listOf(320, 390, 600)) for (scale in listOf(1f, 2f)) {
            val order = order(StartOrderPaymentStatus.PaymentCancelled()).let {
                it.copy(startTitle = "Лыжная гонка патрулей — Приз Николая и Октябрины Соколовых",
                    members = it.members + it.members, dateStartPreview = "9 августа — 31 декабря 2026", cost = "12 345")
            }
            ImageComposeScene(width = width, height = 1400, density = Density(1f, scale)) {
                SauceTheme {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background).padding(16.dp)) { OrderStartCard(item = order, onClickStart = {}) }
                }
            }.use { scene ->
                scene.settle(); scene.assertTextFits()
                assertNotNull(scene.text("Участники: 2"))
                assertNotNull(scene.text("12 345 ₽"))
                val card = scene.nodes().single { it.config.contains(SemanticsActions.OnClick) }
                assertTrue(card.boundsInRoot.right <= width - 16f)
                if (width == 320 && scale == 2f) scene.save("registration-large-text")
            }
        }
    }

    private fun order(payment: StartOrderPaymentStatus) = StartOrderInfo(
        id = 52803, startId = 326, name = "", image = "", dateStartPreview = "9 августа 2026", dateStartCloud = "",
        promo = "", payment = payment,
        members = listOf(StartOrderMember("Даниил", "Кулаков", "СпортСоюз", "Общая", "Ретро кросс 2020", "М", "", emptyList())),
        orderType = StartOrderType.Default, startTitle = "Онлайн забеги — ретро кросс", cost = "1 020",
    )
    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(15) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.text(label: String) = nodes().firstOrNull {
        it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == label } == true
    }
    private fun ImageComposeScene.assertTextFits() {
        for (node in nodes().filter { it.config.contains(SemanticsActions.GetTextLayoutResult) }) {
            val layouts = mutableListOf<TextLayoutResult>()
            node.config[SemanticsActions.GetTextLayoutResult].action!!.invoke(layouts)
            for (layout in layouts) {
                if (layout.layoutInput.overflow != TextOverflow.Ellipsis) {
                    assertTrue(layout.multiParagraph.height <= layout.size.height + 1f, "Text too tall: ${layout.layoutInput.text}")
                    for (line in 0 until layout.lineCount) assertTrue(layout.getLineRight(line) <= layout.size.width + 1f,
                        "Text too wide: ${layout.layoutInput.text}")
                }
                assertTrue(node.boundsInRoot.width + 1f >= layout.size.width, "Text clipped: ${layout.layoutInput.text}")
            }
        }
    }
    private fun ImageComposeScene.save(name: String) {
        time += 100_000_000L
        render(time).use { image -> image.encodeToData()!!.use { data ->
            File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
        } }
    }
}
