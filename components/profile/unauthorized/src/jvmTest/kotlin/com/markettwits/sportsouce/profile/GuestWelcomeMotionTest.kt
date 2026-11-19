package com.markettwits.sportsouce.profile

import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.unauthorized.presentation.components.UnAuthorizedProfileContent
import com.markettwits.sportsouce.unauthorized.presentation.components.GuestProfileTitle
import com.markettwits.sportsouce.unauthorized.presentation.components.GuestProfileDescription
import kotlinx.coroutines.Dispatchers
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class GuestWelcomeMotionTest {
    @Test fun typingAndBlurKeepTheHeadingStableAndSignInWorksFromTheFirstFrame() {
        for (dark in listOf(false, true)) {
            var clicks = 0
            val enabled = object : MotionDurationScale { override val scaleFactor = 1f }
            ImageComposeScene(width = 390, height = 840, coroutineContext = Dispatchers.Unconfined + enabled) {
                SauceTheme(darkTheme = dark) { UnAuthorizedProfileContent(playIntro = true) { clicks++ } }
            }.use { scene ->
                scene.frame(0).close()
                val heading = scene.nodes().single { it.config.contains(SemanticsProperties.Heading) }
                val bounds = heading.boundsInRoot
                val fullText = heading.config[SemanticsProperties.Text].single().text
                assertEquals("Ваш следующий\nстарт — здесь", fullText)
                var button = scene.nodes().first { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { text -> text.text == "Войти" } }
                while (!button.config.contains(SemanticsActions.OnClick)) button = button.parent!!
                assertFalse(button.config.contains(SemanticsProperties.Disabled))
                button.config[SemanticsActions.OnClick].action!!.invoke()
                assertEquals(1, clicks)
                val panelText = listOf(GuestProfileTitle, GuestProfileDescription)
                val textBounds = panelText.associateWith { scene.textNode(it).boundsInRoot }
                val frames = mutableListOf<ByteArray>()
                for (ms in listOf(16L, 600L, 1100L, 1800L, 3000L, 3300L)) {
                    frames += scene.frame(ms).use { image -> image.encodeToData()!!.use { data ->
                        data.bytes.also {
                            File("build/design-system/guest-intro-${if (dark) "dark" else "light"}-$ms.png")
                                .apply { parentFile.mkdirs(); writeBytes(it) }
                        }
                    } }
                    val current = scene.nodes().single { it.config.contains(SemanticsProperties.Heading) }
                    assertEquals(bounds, current.boundsInRoot, "Typing must not recenter or resize the heading")
                    assertEquals(fullText, current.config[SemanticsProperties.Text].single().text)
                    for (text in panelText) {
                        val node = scene.textNode(text)
                        assertEquals(text, node.config[SemanticsProperties.Text].single().text)
                        // Only the panel's entrance translation may move text; line wrapping stays fixed.
                        assertEquals(textBounds.getValue(text).width, node.boundsInRoot.width, .5f)
                        assertEquals(textBounds.getValue(text).height, node.boundsInRoot.height, .5f)
                    }
                }
                assertFalse(frames[1].contentEquals(frames[2]), "The title and photo should be moving during the reveal")
                assertTrue(frames[4].contentEquals(frames[5]), "The entrance must finish rather than animate forever")
            }
        }
    }

    @Test fun disabledSystemAnimationsShowTheFinishedScreenImmediately() {
        val disabled = object : MotionDurationScale { override val scaleFactor = 0f }
        ImageComposeScene(width = 390, height = 840, coroutineContext = Dispatchers.Unconfined + disabled) {
            SauceTheme { UnAuthorizedProfileContent(playIntro = true) {} }
        }.use { scene ->
            scene.frame(0).close()
            scene.frame(16).close()
            val before = scene.frame(32).use { it.encodeToData()!!.use { data -> data.bytes } }
            val after = scene.frame(3300).use { it.encodeToData()!!.use { data -> data.bytes } }
            assertTrue(before.contentEquals(after), "Disabled system animations must also disable typing and photo movement")
        }
    }

    private fun ImageComposeScene.textNode(value: String) = nodes().single {
        it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { text -> text.text == value }
    }
    private fun ImageComposeScene.frame(ms: Long) = render(1_000_000_000L + ms * 1_000_000L)
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
}
