package com.markettwits.sportsouce.start.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.register.domain.StartRegType
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.*
import com.markettwits.sportsouce.start.register.presentation.registration.member.components.MemberContinueButton
import com.markettwits.sportsouce.start.register.presentation.registration.pay.components.price.*
import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.StartRegistrationStagePage
import com.markettwits.sportsouce.start.register.presentation.promo.components.RegistrationPromoTextField
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class RegistrationLayoutTest {
    private val distance = StartRegistrationDistance(1, "", "Дистанция", emptyList(), emptyList(), StartRegistrationFieldPrice.Empty, "")
    private val registration = StartRegistrationStagePage.Registration(0, "Дистанция", "", true, true, distance = distance)
    private val pay = StartRegistrationStagePage.Pay(2, "Оплата", "",
        price = StartRegistrationPriceResult.Value(50000, 300000, 300000),
        startInfo = StartRegistrationInfo(1, "Старт", paymentType = "", isPaymentDisabled = false, regType = StartRegType.Default),
        distances = listOf(distance))

    @Test
    fun forwardAndPaymentActionsMatchSummaryWidthInBothThemesAndLargeText() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            ImageComposeScene(width = 320, height = 1350, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Column(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        registration.ButtonContent(onClickGoBack = {}, onClickGoNext = {})
                        StartRegistrationPriceContent(priceResponse = pay.price, isLoading = false, onClickPromo = {})
                        pay.ButtonContent(onClickGoBack = {}, onClickPay = {}, onClickSave = {})
                        pay.copy(price = StartRegistrationPriceResult.Free).ButtonContent(onClickGoBack = {}, onClickPay = {}, onClickSave = {})
                    }
                }
            }.use { scene ->
                scene.settle()
                val actions = scene.targets().filter { it.config.getOrNull(SemanticsProperties.Role) == Role.Button }
                assertTrue(actions.size >= 7)
                for (button in actions) {
                    assertEquals(16f, button.boundsInRoot.left, 0.5f)
                    assertEquals(304f, button.boundsInRoot.right, 0.5f)
                    assertTrue(button.size.height >= 48)
                }
                val promo = scene.targets().single { it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == "Промокод" } == true }
                assertEquals(actions.first().boundsInRoot.left, promo.boundsInRoot.left, 0.5f)
                assertEquals(actions.first().boundsInRoot.right, promo.boundsInRoot.right, 0.5f)
                scene.assertNoClippedText()
                scene.save("registration-actions-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
            }
        }
    }

    @Test
    fun paymentCallbacksAndLoadingLockArePreserved() {
        var loading by mutableStateOf(false)
        var paid = 0; var saved = 0; var backed = 0
        ImageComposeScene(width = 360, height = 400) {
            SauceTheme { pay.ButtonContent(loading = loading,
                onClickGoBack = { backed++ }, onClickPay = { paid++ }, onClickSave = { saved++ }) }
        }.use { scene ->
            scene.settle()
            scene.targets().forEach { scene.click(it.boundsInRoot.center) }
            assertEquals(listOf(1, 1, 1), listOf(paid, saved, backed))
            loading = true; scene.settle()
            assertTrue(scene.targets().all { it.config.contains(SemanticsProperties.Disabled) })
            scene.targets().forEach { scene.click(it.boundsInRoot.center) }
            assertEquals(listOf(1, 1, 1), listOf(paid, saved, backed))
        }
    }

    @Test
    fun incompleteMemberFormStillTriggersExistingValidationWithoutAnOverlayTarget() {
        var valid by mutableStateOf(false)
        var continued = 0; var validated = 0
        ImageComposeScene(width = 320, height = 160) {
            SauceTheme { MemberContinueButton(enabled = valid,
                onClickContinue = { continued++ }, onClickDisabled = { validated++ }) }
        }.use { scene ->
            scene.settle()
            assertEquals(1, scene.targets().size)
            scene.click(scene.targets().single().boundsInRoot.center)
            assertEquals(1, validated); assertEquals(0, continued)
            valid = true; scene.settle()
            scene.click(scene.targets().single().boundsInRoot.center)
            assertEquals(1, validated); assertEquals(1, continued)
        }
    }

    @Test
    fun compactAdaptiveFooterReservesOnlyItsMeasuredHeight() {
        for (scale in listOf(1f, 2f)) {
            ImageComposeScene(width = 320, height = 800, density = Density(1f, scale)) {
                SauceTheme {
                    Column {
                        Text("Контент", Modifier.weight(1f).fillMaxWidth())
                        AdaptivePane(fillHeight = false) {
                            MemberContinueButton(Modifier.padding(16.dp), onClickContinue = {})
                        }
                    }
                }
            }.use { scene ->
                scene.settle()
                val button = scene.targets().single()
                assertEquals(16f, button.boundsInRoot.left, 0.5f)
                assertEquals(784f, button.boundsInRoot.bottom, 0.5f)
                val content = scene.nodes().single { it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == "Контент" } == true }
                assertTrue(content.boundsInRoot.height > 600, "Footer stole the content height")
                scene.assertNoClippedText()
            }
        }
    }

    @Test
    fun promoValidationUsesAnErrorFieldInBothThemes() {
        for (dark in listOf(false, true)) {
            ImageComposeScene(width = 320, height = 300, density = Density(1f, 2f)) {
                SauceTheme(darkTheme = dark) { RegistrationPromoTextField(value = "CODE", isError = true,
                    message = "Срок действия промокода истёк", onValueChange = {}) }
            }.use { scene ->
                scene.settle()
                assertTrue(scene.nodes().any { it.config.contains(SemanticsProperties.Error) })
                scene.assertNoClippedText()
            }
        }
    }

    private fun ImageComposeScene.assertNoClippedText() {
        for (node in nodes()) {
            if (node.config.contains(SemanticsProperties.EditableText)) continue
            val layouts = mutableListOf<TextLayoutResult>()
            node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
            layouts.forEach { assertFalse(it.hasVisualOverflow, "Clipped: ${it.layoutInput.text}, size=${it.size}, width=${it.didOverflowWidth}, height=${it.didOverflowHeight}, constraints=${it.layoutInput.constraints}") }
        }
    }
    private fun ImageComposeScene.click(at: Offset) {
        sendPointerEvent(PointerEventType.Press, at); sendPointerEvent(PointerEventType.Release, at); settle()
    }
    private fun ImageComposeScene.settle() {
        val time = System.nanoTime(); repeat(12) { render(time + it * 100_000_000L).close() }
    }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
    }
    private fun ImageComposeScene.targets() = nodes().filter { it.config.contains(SemanticsActions.OnClick) }
    private fun ImageComposeScene.save(name: String) {
        render(System.nanoTime() + 2_000_000_000L).use { img ->
            img.encodeToData()!!.use { data -> File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) } }
        }
    }
}
