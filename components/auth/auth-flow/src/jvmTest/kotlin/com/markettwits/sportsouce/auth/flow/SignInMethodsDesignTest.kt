package com.markettwits.sportsouce.auth.flow

import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.auth.flow.internal.sign_in.domain.LoginMethod
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.component.SignInScreen
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.components.SignInContent
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.store.SignInStore
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class SignInMethodsDesignTest {
    @Test fun vkReplacesTheWholeFormAndPasswordDraftReturnsInBothThemes() {
        for (dark in listOf(false, true)) for ((width, scale) in listOf(390 to 1f, 320 to 2f)) {
            var viewState by mutableStateOf(SignInStore.State(emailOrPhone = "ivan@example.ru", password = "secret123"))
            var registrations = 0
            val component = object : SignInScreen {
                override val state = MutableStateFlow(SignInStore.State())
                override fun obtainEvent(intent: SignInStore.Intent) {
                    when (intent) {
                        is SignInStore.Intent.SetLoginMethod -> viewState = viewState.copy(loginMethod = intent.method)
                        SignInStore.Intent.SignUp -> registrations++
                        else -> Unit
                    }
                }
                override fun back() = Unit
                override fun forgotPassword() = Unit
            }
            ImageComposeScene(width = width, height = if (scale == 1f) 1000 else 2200, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) { SignInContent(viewState, component) }
            }.use { scene ->
                fun settle() { repeat(16) { scene.render(System.nanoTime() + it * 100_000_000L).close() } }
                fun nodes(): List<SemanticsNode> {
                    fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
                    return scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
                }
                fun clickTab(index: Int) {
                    val tabs = nodes().filter { it.config.getOrNull(SemanticsProperties.Role) == Role.Tab }
                    tabs[index].config[SemanticsActions.OnClick].action!!.invoke()
                    settle()
                }
                settle()
                assertEquals(2, nodes().count { it.config.contains(SemanticsProperties.EditableText) })
                clickTab(1)
                assertEquals(LoginMethod.VK, viewState.loginMethod)
                assertEquals(0, nodes().count { it.config.contains(SemanticsProperties.EditableText) })
                val texts = nodes().flatMap { it.config.getOrNull(SemanticsProperties.Text).orEmpty() }.map { it.text }
                assertTrue("Вход через ВКонтакте сейчас недоступен" in texts)
                assertFalse(texts.any { "SMS" in it || "СМС" in it || "Забыли пароль" in it })
                for (node in nodes()) {
                    val layouts = mutableListOf<TextLayoutResult>()
                    node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
                    layouts.forEach { layout -> assertFalse(layout.didOverflowHeight, "Clipped at scale=$scale: ${layout.layoutInput.text}") }
                }
                scene.render(System.nanoTime() + 2_000_000_000L).use { image -> image.encodeToData()!!.use { data ->
                    File("build/design-system/sign-in-vk-${if (dark) "dark" else "light"}-$width.png")
                        .apply { parentFile.mkdirs(); writeBytes(data.bytes) }
                } }
                val signup = nodes().single { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { text -> text.text == "Зарегистрироваться" } }
                fun clickable(n: SemanticsNode): SemanticsNode = if (n.config.contains(SemanticsActions.OnClick)) n else clickable(n.parent!!)
                clickable(signup).config[SemanticsActions.OnClick].action!!.invoke()
                assertEquals(1, registrations)
                clickTab(0)
                assertEquals(2, nodes().count { it.config.contains(SemanticsProperties.EditableText) })
                assertEquals("ivan@example.ru", viewState.emailOrPhone); assertEquals("secret123", viewState.password)
            }
        }
    }
}
