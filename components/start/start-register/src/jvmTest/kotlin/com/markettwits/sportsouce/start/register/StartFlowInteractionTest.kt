package com.markettwits.sportsouce.start.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.cloud.model.register.price.fields.StartRegisterAnswer
import com.markettwits.sportsouce.start.cloud.model.start.fields.*
import com.markettwits.sportsouce.start.register.domain.StartRegType
import com.markettwits.sportsouce.start.register.presentation.distances.component.StartDistancesInput
import com.markettwits.sportsouce.start.register.presentation.distances.components.StartDistancesContent
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.*
import com.markettwits.sportsouce.start.register.presentation.registration.distance.components.fields.*
import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.*
import java.io.File as LocalFile
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class StartFlowInteractionTest {
    private val distance = StartRegistrationDistance(1, "Описание этапа", "Длинное название дистанции для командного зачёта", emptyList(), emptyList(), StartRegistrationFieldPrice.Empty, "")
    private val page = StartRegistrationStagePage.Registration(0, distance.title, distance.description, false, true, distance = distance)
    private val field = StartRegistrationStatementAnswer(
        StartRegistrationAdditionalField(8, StartRegistrationAdditionalField.Type.SINGLE_SELECT, "Размер футболки",
            listOf(Option(8, 51, null, "Одинаковая подпись"), Option(8, 98, null, "Одинаковая подпись")), StartRegistrationFieldPrice.Empty, false, emptyList()),
        StartRegisterAnswer(8, false, "Синглселект", singleSelect = 98),
    )

    @Test
    fun stageIndicatorFitsLongTitlesAndUsesPageIdentityInsteadOfPosition() {
        val stages = (0..3).map { page.copy(id = it * 2) }
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            ImageComposeScene(width = 320, height = 1400, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    StartStagesIndicatorContent(Modifier.fillMaxWidth().background(SauceTheme.colors.background).padding(16.dp), 4, stages)
                }
            }.use { scene ->
                scene.settle()
                val states = scene.nodes().mapNotNull { it.config.getOrNull(SemanticsProperties.StateDescription) }
                assertEquals(2, states.count { it == "Завершён" })
                assertEquals(1, states.count { it == "Текущий шаг" })
                assertEquals(1, states.count { it == "Предстоящий шаг" })
                assertTrue("Шаг 3 из 4" in states)
                scene.assertNoClippedText()
                scene.save("start-flow-steps-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
            }
        }
    }

    @Test
    fun everyStageStartsAtTopAndRestoresItsOwnScrollPositionOnReturn() {
        var current by mutableStateOf("A")
        ImageComposeScene(width = 320, height = 500) {
            SauceTheme {
                val holder = rememberSaveableStateHolder()
                holder.SaveableStateProvider(current) {
                    RegistrationStageViewport { repeat(20) { Text("$current Участник $it", Modifier.fillMaxWidth().height(60.dp)) } }
                }
            }
        }.use { scene ->
            scene.settle()
            scene.scrollNode().config[SemanticsActions.ScrollBy].action!!.invoke(0f, 650f)
            scene.settle()
            val firstScroll = scene.scrollNode().config[SemanticsProperties.VerticalScrollAxisRange].value()
            assertTrue(firstScroll > 400)
            current = "B"; scene.settle()
            assertEquals(0f, scene.scrollNode().config[SemanticsProperties.VerticalScrollAxisRange].value())
            current = "A"; scene.settle()
            assertEquals(firstScroll, scene.scrollNode().config[SemanticsProperties.VerticalScrollAxisRange].value(), 1f)
        }
    }

    @Test
    fun singleSelectShowsTheCurrentSelectionAndUsesUpdatedServerOptions() {
        var current by mutableStateOf(field)
        val answers = mutableListOf<StartRegistrationStatementAnswer>()
        ImageComposeScene(width = 360, height = 500) {
            SauceTheme { AdditionalFieldSingleSelect(field = current, onFieldChanged = { answers += it; current = it }) }
        }.use { scene ->
            scene.settle(); scene.openSelector()
            val options = scene.nodes().filter { it.config.getOrNull(SemanticsProperties.Role) == Role.RadioButton }
            assertEquals(2, options.size)
            assertEquals(1, options.count { it.config[SemanticsProperties.Selected] })
            assertTrue(options[1].config[SemanticsProperties.Selected])
            options[0].config[SemanticsActions.OnClick].action!!.invoke(); scene.settle()
            assertEquals(51, answers.single().answer.singleSelect)
            current = current.copy(field = current.field.copy(options = listOf(Option(8, 777, null, "XL"))))
            scene.settle(); scene.openSelector()
            val updated = scene.nodes().single { it.config.getOrNull(SemanticsProperties.Role) == Role.RadioButton }
            assertTrue(updated.config[SemanticsProperties.Text].any { it.text == "XL" })
            updated.config[SemanticsActions.OnClick].action!!.invoke(); scene.settle()
            assertEquals(777, answers.last().answer.singleSelect)
        }
    }

    @Test
    fun overflowingNumericInputDoesNotClearThePreviousAnswer() {
        var current by mutableStateOf(field.copy(field = field.field.copy(type = StartRegistrationAdditionalField.Type.NUMBER),
            answer = field.answer.copy(number = 12, singleSelect = null)))
        var changes = 0
        ImageComposeScene(width = 320, height = 180) {
            SauceTheme { AdditionalFieldNumber(field = current, onFieldChanged = { current = it; changes++ }) }
        }.use { scene ->
            scene.settle()
            fun enter(text: String) {
                scene.nodes().single { it.config.contains(SemanticsActions.SetText) }
                    .config[SemanticsActions.SetText].action!!.invoke(AnnotatedString(text)); scene.settle()
            }
            enter("999999999999999999999")
            assertEquals(12, current.answer.number); assertEquals(0, changes)
            enter("24"); assertEquals(24, current.answer.number)
            enter(""); assertNull(current.answer.number)
        }
    }

    @Test
    fun distanceDetailsDoNotRegisterAndSoldOutDistanceHasNoActiveSelectionTarget() {
        val available = DistinctDistance(null, null, null, null, detailedDescription = "<p>Описание дистанции</p>", format = "", id = 10,
            infiniteSlots = false, name = "Длинная дистанция для командного участия с дополнительными условиями",
            openSlots = 2, orderNumber = 0, slots = 10, stages = emptyList(), staticPrice = 300000)
        val soldOut = available.copy(id = 11, name = "Нет свободных мест", openSlots = 0)
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            var selected = 0
            ImageComposeScene(width = 320, height = 1800, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    StartDistancesContent(state = StartDistancesInput(1, "Тестовый старт", listOf(available, soldOut), emptyList(), false, "", StartRegType.Default),
                        onClickGoBack = {}, onClickSelectedDistance = { selected++ })
                }
            }.use { scene ->
                scene.settle()
                val toggles = scene.nodes().filter { it.config.getOrNull(SemanticsProperties.StateDescription) == "Свёрнуто" }
                assertEquals(2, toggles.size)
                assertTrue(toggles.all { it.size.height >= 48 })
                toggles.first().config[SemanticsActions.OnClick].action!!.invoke(); scene.settle()
                assertEquals(0, selected)
                val soldButton = scene.nodes().single { it.config.getOrNull(SemanticsProperties.Role) == Role.Button &&
                    it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == "Мест нет" } == true }
                assertTrue(soldButton.config.contains(SemanticsProperties.Disabled))
                val priceButton = scene.nodes().single { it.config.getOrNull(SemanticsProperties.Role) == Role.Button &&
                    it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == "Выбрать" } == true }
                assertTrue(priceButton.size.height >= 48)
                assertTrue(priceButton.size.width < 248, "Distance action must remain compact")
                assertFalse(priceButton.config[SemanticsProperties.Text].any { it.text.contains("₽") }, "Price is information, separate from the action")
                scene.click(priceButton.boundsInRoot.center)
                assertEquals(1, selected)
                scene.nodes().forEach { node ->
                    val layouts = mutableListOf<TextLayoutResult>()
                    node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
                    layouts.filter { it.layoutInput.text.text.endsWith("₽") }.forEach {
                        assertEquals(1, it.lineCount, "The price must remain a single readable value")
                    }
                }
                scene.assertNoClippedText()
                scene.save("start-flow-distances-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
            }
        }
    }

    private fun ImageComposeScene.openSelector() {
        nodes().single { it.config.getOrNull(SemanticsProperties.Role) == Role.Button }
            .config[SemanticsActions.OnClick].action!!.invoke(); settle()
    }
    private fun ImageComposeScene.scrollNode() = nodes().single { it.config.contains(SemanticsProperties.VerticalScrollAxisRange) }
    private fun ImageComposeScene.assertNoClippedText() {
        for (node in nodes()) {
            if (node.config.contains(SemanticsProperties.EditableText)) continue
            val layouts = mutableListOf<TextLayoutResult>()
            node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
            layouts.forEach { assertFalse(it.hasVisualOverflow, "Clipped: ${it.layoutInput.text}, size=${it.size}") }
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
    private fun ImageComposeScene.save(name: String) {
        render(System.nanoTime() + 2_000_000_000L).use { img ->
            img.encodeToData()!!.use { data -> LocalFile("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) } }
        }
    }
}
