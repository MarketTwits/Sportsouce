package com.markettwits.core_ui.items.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.text.AnnotatedString
import com.markettwits.core_ui.items.components.textField.calendarDateMillis
import com.markettwits.core_ui.items.components.textField.calendarDateText
import androidx.compose.ui.unit.Density
import androidx.compose.ui.use
import com.markettwits.core_ui.items.components.textField.CalendarTextFiled
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.theme.SauceTheme
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalComposeUiApi::class)
class CalendarComponentsTest {
    @Test
    fun confirmedDateSurvivesReopeningAndCancelledDraftDoesNotReplaceIt() {
        val value = mutableStateOf("")
        var callbacks = 0
        ImageComposeScene(width = 440, height = 800) {
            TestTheme(false) { CalendarField(value.value) { value.value = it; callbacks++ } }
        }.use { scene ->
            settle(scene)
            scene.open()
            scene.tap(scene.button("10"))
            settle(scene)
            scene.tap(scene.button("ОК"))
            settle(scene)
            assertEquals(1, callbacks)
            val confirmed = value.value
            scene.open()
            assertTrue(!scene.button("ОК").config.contains(SemanticsProperties.Disabled), "Reopening lost the confirmed date")
            assertTrue(scene.button("10").config.getOrNull(SemanticsProperties.Selected) == true)
            scene.tap(scene.button("11"))
            settle(scene)
            scene.tap(scene.button("Отменить"))
            settle(scene)
            assertEquals(confirmed, value.value)
            assertEquals(1, callbacks)
            scene.open()
            assertTrue(scene.button("10").config.getOrNull(SemanticsProperties.Selected) == true)
        }
    }

    @Test
    fun constrainedCalendarKeepsActionsAndDateEntryInsideTheWindow() {
        for (dark in listOf(false, true)) for ((width, height) in listOf(320 to 480, 640 to 320)) {
            ImageComposeScene(width = width, height = height, density = Density(1f, 2f)) {
                TestTheme(dark) { CalendarField("") {} }
            }.use { scene ->
                settle(scene)
                scene.open()
                scene.save("calendar-${if (dark) "dark" else "light"}-$width-2x")
                for (text in listOf("ОК", "Отменить")) {
                    val action = scene.button(text)
                    assertTrue(action.boundsInRoot.height >= 48f && action.boundsInRoot.top >= 0f &&
                        action.boundsInRoot.bottom <= height.toFloat(), "Action $text is clipped: ${action.boundsInRoot}")
                    assertTrue(action.boundsInRoot.left >= 0f && action.boundsInRoot.right <= width.toFloat(),
                        "Action $text exceeds window width: ${action.boundsInRoot}")
                }
                assertTrue(scene.nodes().any { it.config.contains(SemanticsActions.SetText) },
                    "A constrained window should start in date input mode")
            }
        }
    }

    @Test
    fun existingFieldValuesAndMonthYearNavigationUseTheSameSelection() {
        for (dark in listOf(false, true)) {
            val value = mutableStateOf("15.10.2026")
            ImageComposeScene(width = 440, height = 800) {
                TestTheme(dark) { CalendarField(value.value) { value.value = it } }
            }.use { scene ->
                settle(scene)
                scene.open()
                assertTrue(scene.button("15").config.getOrNull(SemanticsProperties.Selected) == true)
                scene.save("calendar-${if (dark) "dark" else "light"}-days")
                scene.tap(scene.button("Октябрь 2026"))
                settle(scene)
                scene.save("calendar-${if (dark) "dark" else "light"}-months")
                scene.tap(scene.button("2026"))
                settle(scene)
                scene.save("calendar-${if (dark) "dark" else "light"}-years")
                scene.tap(scene.button("2025"))
                settle(scene)
                scene.tap(scene.button("Фев"))
                settle(scene)
                scene.tap(scene.nodes().first { it.config.contains(SemanticsActions.OnClick) &&
                    it.config.getOrNull(SemanticsProperties.ContentDescription)?.contains("28 февраля 2025") == true })
                settle(scene)
                scene.tap(scene.button("ОК"))
                settle(scene)
                assertEquals("28.02.2025", value.value)
                value.value = "01.02.2024"
                settle(scene)
                scene.open()
                assertTrue(scene.button("1").config.getOrNull(SemanticsProperties.Selected) == true)
                assertTrue(scene.nodes().any { "Февраль 2024" in it.texts() })
            }
        }
    }

    @Test
    fun manualInputRejectsImpossibleDatesAndAcceptsLeapDays() {
        var result = ""
        ImageComposeScene(width = 440, height = 800) {
            TestTheme(false) { CalendarField("") { result = it } }
        }.use { scene ->
            settle(scene)
            scene.open()
            val toggle = scene.nodes().first { it.config.contains(SemanticsActions.OnClick) &&
                it.config.getOrNull(SemanticsProperties.ContentDescription)?.contains("Ввести дату") == true }
            scene.tap(toggle)
            settle(scene)
            fun enter(text: String) {
                scene.nodes().first { it.config.contains(SemanticsActions.SetText) }
                    .config[SemanticsActions.SetText].action?.invoke(AnnotatedString(text))
                settle(scene)
            }
            enter("31022024")
            assertTrue(scene.button("ОК").config.contains(SemanticsProperties.Disabled))
            assertTrue(scene.nodes().any { "Введите существующую дату" in it.texts() })
            enter("29022024")
            assertTrue(!scene.button("ОК").config.contains(SemanticsProperties.Disabled))
            scene.tap(scene.button("ОК"))
            assertEquals("29.02.2024", result)
        }
    }

    @Test
    fun formattingNeverMovesTheSelectedCalendarDayToAnotherTimeZone() {
        val original = java.util.TimeZone.getDefault()
        try {
            for (zone in listOf("GMT-12:00", "UTC", "Asia/Novosibirsk", "GMT+14:00")) {
                java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone(zone))
                assertEquals("29.02.2024", calendarDateText(checkNotNull(calendarDateMillis("29.02.2024", 1925..2026))))
            }
        } finally { java.util.TimeZone.setDefault(original) }
    }

    @Test
    fun calendarDayTargetsFitAtNormalPhoneWidth() {
        ImageComposeScene(width = 360, height = 800) {
            TestTheme(false) { CalendarField("15.10.2026") {} }
        }.use { scene ->
            settle(scene)
            scene.open()
            val dates = scene.nodes().filter { it.config.contains(SemanticsProperties.Selected) &&
                it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty().any { text ->
                    Regex("\\d{1,2} [а-я]+ \\d{4}").matches(text) } }
            assertEquals(42, dates.size)
            assertTrue(dates.all { it.size.width >= 48 && it.size.height >= 48 }, "Calendar targets overlap or are too small")
            scene.save("calendar-light-phone-360")
        }
    }

    @Test
    fun optionalClearRequiresConfirmationAndCancelKeepsTheOriginalValue() {
        val value = mutableStateOf("15.10.2026")
        var callbacks = 0
        ImageComposeScene(width = 440, height = 800) {
            TestTheme(false) {
                CalendarTextFiled(clearable = true, textFiled = { modifier ->
                    OutlinedTextFieldBase(modifier, "Дата", value.value, isEnabled = false, onValueChange = {})
                }, onValueChanged = { value.value = it; callbacks++ })
            }
        }.use { scene ->
            settle(scene)
            scene.open()
            scene.save("calendar-light-clearable")
            scene.tap(scene.button("Очистить"))
            settle(scene)
            assertTrue(!scene.button("ОК").config.contains(SemanticsProperties.Disabled))
            assertEquals(0, callbacks)
            scene.tap(scene.button("Отменить"))
            settle(scene)
            assertEquals("15.10.2026", value.value)
            scene.open()
            scene.tap(scene.button("Очистить"))
            settle(scene)
            scene.tap(scene.button("ОК"))
            settle(scene)
            assertEquals(1, callbacks)
            assertEquals("", value.value)
        }
    }

    @Test
    fun restoredOpenDialogKeepsTheDraftButDoesNotCommitItOnCancel() {
        var registry = SaveableStateRegistry(null) { true }
        var saved: Map<String, List<Any?>> = emptyMap()
        val value = mutableStateOf("15.10.2026")
        var callbacks = 0
        val content: @Composable () -> Unit = {
            CompositionLocalProvider(LocalSaveableStateRegistry provides registry) {
                TestTheme(false) { CalendarField(value.value) { value.value = it; callbacks++ } }
            }
        }
        ImageComposeScene(width = 440, height = 800, content = content).use { scene ->
            settle(scene)
            scene.open()
            scene.tap(scene.button("10"))
            settle(scene)
            saved = registry.performSave()
        }
        registry = SaveableStateRegistry(saved) { true }
        ImageComposeScene(width = 440, height = 800, content = content).use { scene ->
            settle(scene)
            assertTrue(scene.button("10").config.getOrNull(SemanticsProperties.Selected) == true)
            scene.tap(scene.button("Отменить"))
            settle(scene)
            assertEquals(0, callbacks)
            assertEquals("15.10.2026", value.value)
            scene.open()
            assertTrue(scene.button("15").config.getOrNull(SemanticsProperties.Selected) == true)
        }
    }

    @Composable
    private fun CalendarField(value: String, onValueChange: (String) -> Unit) {
        CalendarTextFiled(textFiled = { modifier ->
            OutlinedTextFieldBase(modifier, "Дата", value, isEnabled = false, onValueChange = {})
        }, onValueChanged = onValueChange)
    }
    @Composable
    private fun TestTheme(dark: Boolean, content: @Composable () -> Unit) {
        SauceTheme(darkTheme = dark) {
            Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) { content() }
        }
    }
    private fun ImageComposeScene.open() {
        tap(nodes().first { it.config.contains(SemanticsActions.OnClick) })
        settle(this)
    }
    private fun ImageComposeScene.button(label: String) = nodes().first {
        it.config.contains(SemanticsActions.OnClick) && it.boundsInRoot.height > 0f &&
            if (label.all(Char::isDigit)) label in it.texts() || it.config.contains(SemanticsProperties.Selected) &&
                it.texts().any { text -> Regex("(?<!\\d)$label(?!\\d)").containsMatchIn(text) }
            else label in it.texts()
    }
    private fun SemanticsNode.texts() = config.getOrNull(SemanticsProperties.Text)?.map { it.text }.orEmpty()
    private fun ImageComposeScene.tap(node: SemanticsNode) {
        sendPointerEvent(PointerEventType.Press, node.boundsInRoot.center)
        sendPointerEvent(PointerEventType.Release, node.boundsInRoot.center)
    }
    private fun settle(scene: ImageComposeScene) {
        val time = System.nanoTime()
        repeat(15) { scene.render(time + it * 100_000_000L).close() }
    }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
    }
    private fun ImageComposeScene.save(name: String) {
        File("build/design-system/$name.png").apply {
            parentFile.mkdirs()
            render(System.nanoTime() + 2_000_000_000L).use { image ->
                checkNotNull(image.encodeToData()).use { writeBytes(it.bytes) }
            }
        }
    }
}
