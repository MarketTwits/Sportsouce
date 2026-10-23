package com.markettwits.sportsouce.profile

import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.edit_profile.sizes.domain.*
import com.markettwits.sportsouce.edit_profile.sizes.presentation.screen.ProfileSizesContent
import com.markettwits.sportsouce.edit_profile.sizes.presentation.store.ProfileSizesStore
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class ProfileSizesDesignTest {
    @Test fun fieldsUseSavedValuesAllActionsRemainReachableAndSaveDoesNotChangeSize() {
        for (dark in listOf(false, true)) for ((width, height, scale) in listOf(Triple(390, 840, 1f), Triple(320, 560, 2f), Triple(800, 600, 1f))) {
            val original = ProfileSizes("M", "265", "42", "9.5", "27")
            val state = mutableStateOf(ProfileSizesStore.State(data = original, saved = original, isLoading = false))
            val events = mutableListOf<ProfileSizesStore.Intent>()
            ImageComposeScene(width, height, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) { ProfileSizesContent(state.value, events::add) }
            }.use { scene ->
                scene.settle()
                assertTrue(scene.button("Сохранить").config.contains(SemanticsProperties.Disabled))
                val fields = scene.nodes().filter { it.config.contains(SemanticsActions.SetText) }
                assertEquals(5, fields.size)
                val clothing = fields.single { "Размер одежды" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() }
                clothing.config[SemanticsActions.SetText].action!!.invoke(AnnotatedString("L"))
                assertEquals(ProfileSizesStore.Intent.Edit(ProfileSizeField.Clothing, "L"), events.last())
                state.value = state.value.copy(data = original.copy(clothingSize = "L"))
                scene.settle()
                val save = scene.button("Сохранить")
                assertFalse(save.config.contains(SemanticsProperties.Disabled))
                assertTrue(save.boundsInRoot.bottom <= height && save.boundsInRoot.top >= 0)
                val bounds = save.boundsInRoot
                save.config[SemanticsActions.OnClick].action!!.invoke()
                assertEquals(ProfileSizesStore.Intent.Save, events.last())
                state.value = state.value.copy(isSaving = true)
                scene.settle()
                assertEquals(bounds, scene.button("Сохранить").boundsInRoot)
                assertTrue(scene.button("Сохранить").config.contains(SemanticsProperties.Disabled))
                assertTrue(scene.nodes().filter { it.config.contains(SemanticsActions.SetText) }.all { it.config.contains(SemanticsProperties.Disabled) })
                state.value = state.value.copy(isSaving = false)
                scene.settle()
                scene.nodes().first { it.config.contains(SemanticsActions.ScrollBy) }.config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10000f)
                scene.settle()
                val reset = scene.button("Отменить изменения")
                assertTrue(reset.boundsInRoot.bottom <= scene.button("Сохранить").boundsInRoot.top,
                    "The form footer must not cover the last action")
                reset.config[SemanticsActions.OnClick].action!!.invoke()
                assertEquals(ProfileSizesStore.Intent.Reset, events.last())
                scene.save("profile-sizes-${if (dark) "dark" else "light"}-$width-${(scale * 100).toInt()}")
                scene.assertTextFits()
            }
        }
    }

    @Test fun loadingCannotSaveAndFailedLoadCanRetryWithoutAFormOfInventedValues() {
        val state = mutableStateOf(ProfileSizesStore.State())
        val events = mutableListOf<ProfileSizesStore.Intent>()
        ImageComposeScene(390, 840) {
            SauceTheme { ProfileSizesContent(state.value, events::add) }
        }.use { scene ->
            scene.settle()
            assertTrue(scene.button("Сохранить").config.contains(SemanticsProperties.Disabled))
            assertTrue(scene.nodes().none { it.config.contains(SemanticsActions.SetText) })
            state.value = state.value.copy(isLoading = false, loadError = "Нет соединения")
            scene.settle()
            scene.button("Повторить").config[SemanticsActions.OnClick].action!!.invoke()
            assertEquals(listOf<ProfileSizesStore.Intent>(ProfileSizesStore.Intent.Retry), events)
        }
    }

    @Test fun invalidLastFieldIsFocusedAndBroughtAboveSaveFooter() {
        val original = ProfileSizes()
        val state = mutableStateOf(ProfileSizesStore.State(data = original, saved = original, isLoading = false))
        ImageComposeScene(320, 560, density = Density(1f, 2f)) {
            SauceTheme(darkTheme = true) { ProfileSizesContent(state.value) {} }
        }.use { scene ->
            scene.settle()
            state.value = state.value.copy(data = original.copy(shoeSizeJp = "abc"),
                errors = mapOf(ProfileSizeField.ShoeJp to "Введите число больше нуля"), validationAttempt = 1)
            scene.settle()
            val field = scene.nodes().single {
                it.config.contains(SemanticsActions.SetText) && "Обувь JP" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty()
            }
            assertEquals("Введите число больше нуля", field.config[SemanticsProperties.Error])
            assertTrue(field.config[SemanticsProperties.Focused])
            assertTrue(field.boundsInRoot.bottom <= scene.button("Сохранить").boundsInRoot.top)
            scene.save("profile-sizes-field-error-dark-320-200")
        }
    }

    private var time = 1_000_000_000L
    private fun ImageComposeScene.settle() { repeat(14) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.button(label: String): SemanticsNode {
        var node = nodes().first { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { t -> t.text == label } }
        while (!node.config.contains(SemanticsActions.OnClick)) node = node.parent!!
        return node
    }
    private fun ImageComposeScene.assertTextFits() = nodes().forEach { node ->
        val layouts = mutableListOf<TextLayoutResult>()
        node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
        layouts.filter { it.layoutInput.overflow != TextOverflow.Ellipsis }.forEach {
            assertFalse(it.didOverflowHeight, it.layoutInput.text.text)
            // Text layout rounds its measured width to whole pixels; allow that rounding only.
            for (line in 0 until it.lineCount) assertTrue(it.getLineRight(line) <= it.size.width + .5f, it.layoutInput.text.text)
        }
    }
    private fun ImageComposeScene.save(name: String) { render(time + 100_000_000L).use { frame ->
        frame.encodeToData()!!.use { data -> File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) } }
    } }
}
