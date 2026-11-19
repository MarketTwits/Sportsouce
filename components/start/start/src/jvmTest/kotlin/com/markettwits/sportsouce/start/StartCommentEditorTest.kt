package com.markettwits.sportsouce.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.WindowInfo
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import com.markettwits.sportsouce.start.presentation.start.component.CommentData
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.presentation.comments.components.StartCommentTextField
import com.markettwits.sportsouce.start.presentation.start.component.CommentMode
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class, InternalComposeUiApi::class)
class StartCommentEditorTest {
    @Test fun multilineDraftSurvivesSendingAndTheSendButtonDoesNotJump() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            var text by mutableStateOf("")
            var loading by mutableStateOf(false)
            val sent = mutableListOf<String>()
            ImageComposeScene(width = 320, height = 650, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Column(Modifier.fillMaxSize().background(SauceTheme.colors.surface)) {
                        StartCommentTextField(isLoading = loading, comment = text, onCommentChanged = { text = it },
                            mode = CommentMode.Base, onClickCloseReply = {}, sendComment = sent::add)
                    }
                }
            }.use { scene ->
                scene.settle()
                assertTrue(scene.sendButton().config.contains(SemanticsProperties.Disabled))
                scene.sendButton().config[SemanticsActions.OnClick].action!!.invoke()
                assertTrue(sent.isEmpty())
                val draft = "Вопрос о старте\nВторая строка"
                scene.editor().config[SemanticsActions.SetText].action!!.invoke(AnnotatedString(draft))
                scene.settle()
                assertEquals(draft, text)
                val fieldBounds = scene.editor().boundsInRoot
                val sendBounds = scene.sendButton().boundsInRoot
                loading = true; scene.settle()
                assertEquals(fieldBounds, scene.editor().boundsInRoot)
                assertEquals(sendBounds, scene.sendButton().boundsInRoot)
                assertTrue(scene.sendButton().config.contains(SemanticsProperties.Disabled))
                scene.sendButton().config[SemanticsActions.OnClick].action!!.invoke()
                assertTrue(sent.isEmpty())
                loading = false; scene.settle()
                assertEquals(draft, text)
                scene.sendButton().config[SemanticsActions.OnClick].action!!.invoke()
                assertEquals(listOf(draft), sent)
                assertEquals(draft, text, "Only successful submission in the store may clear the draft")
                scene.editor().config[SemanticsActions.SetText].action!!.invoke(AnnotatedString((1..20).joinToString("\n") { "Строка $it" }))
                scene.settle()
                assertTrue(scene.editor().boundsInRoot.height <= 4 * 24 * scale + 40,
                    "Long drafts should scroll inside the editor after four lines")
                scene.save("comment-editor-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
            }
        }
    }

    @Test fun enterInsertsNewLineAndControlEnterSendsWithoutClearingTheDraft() {
        var text by mutableStateOf("Комментарий")
        val sent = mutableListOf<String>()
        ImageComposeScene(width = 390, height = 300) {
            SauceTheme {
                StartCommentTextField(isLoading = false, comment = text, onCommentChanged = { text = it },
                    mode = CommentMode.Base, onClickCloseReply = {}, sendComment = sent::add)
            }
        }.use { scene ->
            scene.settle()
            scene.editor().config[SemanticsActions.RequestFocus].action!!.invoke()
            scene.settle()
            scene.sendKeyEvent(KeyEvent(Key.Enter, KeyEventType.KeyDown))
            scene.sendKeyEvent(KeyEvent(Key.Enter, KeyEventType.KeyUp)); scene.settle()
            assertTrue(text.contains('\n'))
            assertTrue(sent.isEmpty())
            scene.sendKeyEvent(KeyEvent(Key.Enter, KeyEventType.KeyDown, isCtrlPressed = true))
            scene.sendKeyEvent(KeyEvent(Key.Enter, KeyEventType.KeyUp, isCtrlPressed = true)); scene.settle()
            assertEquals(listOf(text), sent)
        }
    }

    @Test fun previewOpensCommentsWithoutEditingAndRespectsRoundedFieldGeometry() {
        var opened = 0
        var changed = 0
        ImageComposeScene(width = 320, height = 160) {
            SauceTheme {
                StartCommentTextField(isLoading = false, readOnly = true, onCommentChanged = { changed++ },
                    mode = CommentMode.Base, onClickTextField = { opened++ }, onClickCloseReply = {}, sendComment = {})
            }
        }.use { scene ->
            scene.settle()
            val preview = scene.nodes().single { it.config.getOrNull(SemanticsProperties.Role) == Role.Button && it.config.contains(SemanticsActions.OnClick) }
            preview.config[SemanticsActions.OnClick].action!!.invoke()
            assertEquals(1, opened)
            assertEquals(0, changed)
            assertTrue(preview.boundsInRoot.left >= 16f && preview.boundsInRoot.right <= 304f)
        }
    }

    @Test fun viewingAThreadLeavesTheEditorUnfocusedWhileReplyRequestsTheKeyboard() = java.awt.EventQueue.invokeAndWait {
        for (focusEditor in listOf(false, true)) {
            var focused by mutableStateOf(false)
            val window = object : WindowInfo { override val isWindowFocused get() = focused }
            var keyboardShows = 0
            val keyboard = object : SoftwareKeyboardController {
                override fun show() { keyboardShows++ }
                override fun hide() = Unit
            }
            ImageComposeScene(width = 390, height = 300) {
                CompositionLocalProvider(LocalWindowInfo provides window, LocalSoftwareKeyboardController provides keyboard) {
                    SauceTheme {
                        StartCommentTextField(isLoading = false,
                            mode = CommentMode.Reply("Автор", 1, CommentData("Автор", "", "Вопрос", "", emptyList()), focusEditor),
                            onClickCloseReply = {}, sendComment = {})
                    }
                }
            }.use { scene ->
                scene.settle()
                assertFalse(scene.editor().config[SemanticsProperties.Focused])
                focused = true; scene.settle()
                assertEquals(focusEditor, scene.editor().config[SemanticsProperties.Focused])
                if (focusEditor) assertTrue(keyboardShows > 0)
                else {
                    assertEquals(0, keyboardShows)
                    scene.editor().config[SemanticsActions.OnClick].action!!.invoke(); scene.settle()
                    assertTrue(scene.editor().config[SemanticsProperties.Focused], "The viewing mode must still allow composing by hand")
                }
            }
        }
    }

    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(12) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.editor() = nodes().single { it.config.contains(SemanticsProperties.EditableText) }
    private fun ImageComposeScene.sendButton() = nodes().single { it.config.getOrNull(SemanticsProperties.Role) == Role.Button && it.config.contains(SemanticsActions.OnClick) }
    private fun ImageComposeScene.save(name: String) {
        time += 100_000_000L
        render(time).use { img -> img.encodeToData()!!.use { data -> File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) } } }
    }
}
