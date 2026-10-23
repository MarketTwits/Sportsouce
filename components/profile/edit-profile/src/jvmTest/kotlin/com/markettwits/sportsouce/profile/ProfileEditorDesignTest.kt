package com.markettwits.sportsouce.profile

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.sauceColors
import com.markettwits.core_ui.items.components.dialog.SauceDialog
import com.markettwits.core_ui.items.components.navigation.LocalNavigationViewport
import com.markettwits.core_ui.items.components.navigation.LocalScrollContentBottomPadding
import com.markettwits.core_ui.items.components.navigation.NavigationViewport
import com.markettwits.sportsouce.edit_profile.about.presentation.components.EditProfileAboutContent
import com.markettwits.sportsouce.edit_profile.about.presentation.store.EditProfileAboutStore
import com.markettwits.sportsouce.edit_profile.info.domain.models.UserData
import com.markettwits.sportsouce.edit_profile.info.presentation.components.EditProfileInfoFieldsContent
import com.markettwits.sportsouce.edit_profile.info.presentation.store.EditProfileInfoStore
import com.markettwits.sportsouce.edit_profile.menu.presentation.EditProfileScreen
import com.markettwits.sportsouce.edit_profile.menu.presentation.component.EditProfileMenuComponentComponent
import com.markettwits.sportsouce.edit_profile.menu.presentation.components.*
import com.markettwits.sportsouce.edit_profile.sign_out.presentation.components.EditProfileSignOutContent
import com.markettwits.sportsouce.edit_profile.image.presentation.components.EditProfileImageScreenContent
import com.markettwits.sportsouce.edit_profile.image.presentation.store.EditProfileImageStore
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class ProfileEditorDesignTest {
    @Test fun editorRowsUseRegularTextAndSemanticDangerColor() {
        for (dark in listOf(false, true)) {
            ImageComposeScene(width = 390, height = 840) {
                Palette(dark) { EditProfileMenu(menu = profileMenu() + securityMenu(), onClickItem = {}) }
            }.use { scene ->
                scene.settle()
                (profileMenu() + securityMenu()).filterIsInstance<MenuItem.Base>().forEach { item ->
                    val node = scene.nodes().single { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { t -> t.text == item.title } }
                    val layouts = mutableListOf<TextLayoutResult>()
                    node.config[SemanticsActions.GetTextLayoutResult].action!!.invoke(layouts)
                    val style = layouts.single().layoutInput.style
                    assertEquals(FontWeight.Normal, style.fontWeight)
                    assertEquals(if (item.isDangerZone) sauceColors(dark).danger.content else sauceColors(dark).textPrimary, style.color)
                }
                scene.save("profile-editor-menu-${if (dark) "dark" else "light"}")
            }
        }
    }

    @Test fun profileDialogsKeepActionsVisibleInShortWindowsAndIsolateRootInsets() {
        for (kind in listOf("about", "photo", "logout")) for (dark in listOf(false, true))
            for ((width, height, scale) in listOf(Triple(390, 840, 1f), Triple(640, 320, 2f), Triple(320, 480, 2f))) {
                var dismissed = 0
                var applied = 0
                ImageComposeScene(width = width, height = height, density = Density(1f, scale)) {
                    Palette(dark) {
                        CompositionLocalProvider(LocalNavigationViewport provides NavigationViewport(), LocalScrollContentBottomPadding provides 96.dp) {
                            SauceDialog(onDismissRequest = { dismissed++ }) {
                                assertNull(LocalNavigationViewport.current)
                                assertEquals(0.dp, LocalScrollContentBottomPadding.current)
                                when (kind) {
                                    "about" -> EditProfileAboutContent(EditProfileAboutStore.State(), { dismissed++ }, { applied++ }, {})
                                    "photo" -> EditProfileImageScreenContent(EditProfileImageStore.State(), { dismissed++ }, { applied++ })
                                    else -> EditProfileSignOutContent(dismiss = { dismissed++ }, apply = { applied++ })
                                }
                            }
                        }
                    }
                }.use { scene ->
                    scene.settle()
                    val cancel = scene.button("Отмена")
                    assertTrue(cancel.boundsInRoot.top >= 0 && cancel.boundsInRoot.bottom <= height)
                    assertTrue(cancel.boundsInRoot.height >= 48f)
                    if (kind != "photo") {
                        val primary = scene.button(if (kind == "about") "Сохранить" else "Выйти")
                        assertTrue(primary.boundsInRoot.top >= 0 && primary.boundsInRoot.bottom <= height)
                    }
                    val scroll = scene.nodes().first { it.config.contains(SemanticsActions.ScrollBy) }
                    scroll.config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10000f)
                    scene.settle()
                    scene.button("Отмена").config[SemanticsActions.OnClick].action!!.invoke()
                    assertEquals(1, dismissed)
                    assertEquals(0, applied, "Dismiss must never save, pick a photo or sign out")
                    scene.assertTextFits()
                    scene.save("profile-dialog-$kind-${if (dark) "dark" else "light"}-$width-${(scale * 100).toInt()}")
                }
            }
    }

    @Test
    fun fullWidthSaveStaysOutsideTheFormAndLoadingCannotSubmitAgain() {
        for (width in listOf(320, 390)) for (scale in listOf(1f, 2f)) for (dark in listOf(false, true)) {
            val user = UserData(1, "Александра", "Иванова", "Женский", "01.01.2000", "", "79991234567", "Новосибирск", "")
            val state = mutableStateOf(EditProfileInfoStore.State(userData = user))
            var saved = 0
            var changed: UserData? = null
            ImageComposeScene(width = width, height = 800, density = Density(1f, scale)) {
                Palette(dark) { EditProfileInfoFieldsContent(state = state.value, onUserChange = { changed = it },
                    onConsume = {}, onClickRetry = {}, onClickGoBack = {}, onClickSave = { saved++ }) }
            }.use { scene ->
                scene.settle()
                val save = scene.button("Сохранить")
                assertEquals((width - 32).toFloat(), save.boundsInRoot.width, 1f)
                assertTrue(save.size.height >= 48)
                assertTrue(save.boundsInRoot.bottom <= 800)
                val originalBounds = save.boundsInRoot
                val name = scene.nodes().single { it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() == listOf("Имя") && it.config.contains(SemanticsActions.SetText) }
                name.config[SemanticsActions.SetText].action!!.invoke(androidx.compose.ui.text.AnnotatedString("Анна"))
                assertEquals(user.copy(name = "Анна"), changed)
                val scroll = scene.nodes().first { it.config.contains(SemanticsProperties.VerticalScrollAxisRange) }
                scroll.config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10000f)
                scene.settle()
                val team = scene.nodes().first { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == "Команда" } }
                assertTrue(team.boundsInRoot.bottom <= scene.button("Сохранить").boundsInRoot.top,
                    "Last selector must remain above the save footer after scrolling")
                scene.button("Сохранить").config[SemanticsActions.OnClick].action!!.invoke()
                assertEquals(1, saved)
                state.value = state.value.copy(isLoading = true)
                scene.settle()
                assertTrue(scene.button("Сохранить").config.contains(SemanticsProperties.Disabled))
                assertEquals(originalBounds, scene.button("Сохранить").boundsInRoot)
                scene.assertTextFits()
                scene.save("profile-editor-${if (dark) "dark" else "light"}-$width-${(scale * 100).toInt()}")
            }
        }
    }

    @Test
    fun editorMenuRetainsEveryExistingRouteAndAccessibleTarget() {
        val routes = mutableListOf<EditProfileMenuComponentComponent.OutPut>()
        val component = object : EditProfileMenuComponentComponent {
            override fun obtainOutPut(output: EditProfileMenuComponentComponent.OutPut) { routes += output }
        }
        ImageComposeScene(width = 320, height = 1600, density = Density(1f, 2f)) {
            Palette(true) { EditProfileScreen(component) }
        }.use { scene ->
            scene.settle()
            (profileMenu() + securityMenu()).filterIsInstance<MenuItem.Base>().forEach {
                val button = scene.button(it.title)
                assertTrue(button.size.height >= 48)
                button.config[SemanticsActions.OnClick].action!!.invoke()
            }
            assertEquals(listOf(EditProfileMenuComponentComponent.OutPut.GoProfileImage,
                EditProfileMenuComponentComponent.OutPut.GoProfileAbout, EditProfileMenuComponentComponent.OutPut.GoChangeProfileInfo,
                EditProfileMenuComponentComponent.OutPut.GoSizes, EditProfileMenuComponentComponent.OutPut.GoSocialNetwork, EditProfileMenuComponentComponent.OutPut.GoChangePassword,
                EditProfileMenuComponentComponent.OutPut.GoSignOut), routes)
            scene.assertTextFits()
            scene.save("profile-editor-menu-dark-320-200")
        }
    }

    @Test
    fun aboutValidationStillDisablesSaveAndLoadingDoesNotChangeButtonSize() {
        val state = mutableStateOf(EditProfileAboutStore.State(fieldStat = EditProfileAboutStore.State.FiledState("Статус")))
        var saved = 0
        var dismissed = 0
        ImageComposeScene(width = 320, height = 1000, density = Density(1f, 2f)) {
            Palette(false) { EditProfileAboutContent(state.value, { dismissed++ }, { saved++ }, {}) }
        }.use { scene ->
            scene.settle()
            val bounds = scene.button("Сохранить").boundsInRoot
            scene.button("Сохранить").config[SemanticsActions.OnClick].action!!.invoke()
            assertEquals(1, saved)
            state.value = state.value.copy(isLoading = true)
            scene.settle()
            assertTrue(scene.button("Сохранить").config.contains(SemanticsProperties.Disabled))
            assertEquals(bounds, scene.button("Сохранить").boundsInRoot)
            state.value = state.value.copy(isLoading = false, fieldStat = EditProfileAboutStore.State.FiledState("x".repeat(121), true))
            scene.settle()
            assertTrue(scene.button("Сохранить").config.contains(SemanticsProperties.Disabled))
            scene.button("Отмена").config[SemanticsActions.OnClick].action!!.invoke()
            assertEquals(1, dismissed)
            scene.assertTextFits()
        }
    }

    @Composable private fun Palette(dark: Boolean, content: @Composable () -> Unit) {
        val c = sauceColors(dark)
        SauceTheme(darkTheme = dark, materialColors = c.material.copy(primary = c.surface, secondary = c.action,
            tertiary = c.textPrimary, primaryContainer = c.surfaceRaised, outline = c.textMuted), content = content)
    }
    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(12) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.button(s: String): SemanticsNode {
        var n = nodes().first { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == s } }
        while (!n.config.contains(SemanticsActions.OnClick)) n = checkNotNull(n.parent)
        return n
    }
    private fun ImageComposeScene.assertTextFits() { nodes().forEach { n ->
        val layouts = mutableListOf<TextLayoutResult>()
        n.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
        layouts.filter { it.layoutInput.overflow != TextOverflow.Ellipsis }.forEach {
            assertFalse(it.didOverflowHeight, "Clipped vertically: ${it.layoutInput.text}")
                for (line in 0 until it.lineCount) assertTrue(it.getLineRight(line) <= it.size.width + 0.5f,
                    "Clipped horizontally: ${it.layoutInput.text}")
        }
    } }
    private fun ImageComposeScene.save(name: String) { time += 100_000_000L; render(time).use { img -> img.encodeToData()!!.use {
        File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(it.bytes) }
    } } }
}
