package com.markettwits.sportsouce.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.presentation.comments.components.StartCommentCard
import com.markettwits.sportsouce.start.presentation.comments.screen.StartCommentsContent
import com.markettwits.sportsouce.start.presentation.comments.store.StartCommentsStore
import com.markettwits.sportsouce.start.presentation.comments.screen.StartCommentsFilters
import com.markettwits.sportsouce.start.presentation.comments.store.StartCommentsFilter
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class StartCommentsDesignTest {
    @Test fun questionsAnswersAndReviewsFitSmallScreensInBothThemesAndLargeFonts() {
        for (dark in listOf(false, true)) for (width in listOf(320, 390)) for (scale in listOf(1f, 2f)) {
            ImageComposeScene(width = width, height = 2400, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Column(Modifier.fillMaxSize().background(SauceTheme.colors.background)
                        .verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        StartCommentsFilters(StartCommentsFilter.Unanswered, 12, modifier = Modifier.requiredWidth(width.dp)) {}
                        Comment(isQuestion = true)
                        Comment(isQuestion = true, isAnswered = true)
                        Comment(isReply = true, isAdminAnswer = true)
                        Comment(isReview = true)
                    }
                }
            }.use { scene ->
                scene.settle()
                scene.nodes().forEach { node ->
                    val layouts = mutableListOf<TextLayoutResult>()
                    node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
                    layouts.filter { it.layoutInput.maxLines == Int.MAX_VALUE }.forEach { layout ->
                        // A cached paragraph may keep the available width after Text chooses its intrinsic width.
                        // Check the rendered lines rather than hasVisualOverflow's paragraph-width comparison.
                        val inkWidth = (0 until layout.lineCount).maxOfOrNull { layout.getLineRight(it) - layout.getLineLeft(it) } ?: 0f
                        assertTrue(inkWidth <= layout.size.width + 1f && layout.multiParagraph.height <= layout.size.height + 1f,
                            "Clipped text: ${layout.layoutInput.text}, width=$width scale=$scale")
                    }
                    if (layouts.isNotEmpty()) {
                        assertTrue(node.boundsInRoot.left >= -1f && node.boundsInRoot.right <= width + 1f,
                            "Text outside screen: ${layouts.first().layoutInput.text}")
                    }
                }
                assertTrue(scene.texts().any { it == "Ждёт ответа" })
                assertTrue(scene.texts().any { it == "Администратор" })
                assertTrue(scene.texts().any { it == "★ 4.5" })
                if (width == 390 && scale == 1f) scene.save("start-comments-${if (dark) "dark" else "light"}")
            }
        }
    }

    @Test fun replyFooterKeepsItsTouchTargetWithoutLargeGaps() {
        ImageComposeScene(width = 390, height = 400, density = Density(1f)) {
            SauceTheme { Box(Modifier.fillMaxSize().padding(16.dp)) { Comment() } }
        }.use { scene ->
            scene.settle()
            val action = scene.nodes().single { it.config.contains(SemanticsActions.OnClick) }
            val message = scene.nodes().single { node -> node.config.getOrNull(SemanticsProperties.Text)?.any {
                it.text == "Могу ли я сделать возврат билета, если не смогу принять участие?"
            } == true }
            assertTrue(action.boundsInRoot.height >= 48f, "Keep a comfortable touch target")
            assertTrue(action.boundsInRoot.top - message.boundsInRoot.bottom <= 10f,
                "The reply action must sit next to the comment without a separate spacious footer")
            assertTrue(action.boundsInRoot.bottom <= 240f, "A short comment should remain compact")
        }
    }

    @Test fun filterActionsReturnTheirOwnFilter() {
        val selected = mutableListOf<StartCommentsFilter>()
        ImageComposeScene(width = 320, height = 300, density = Density(1f)) {
            SauceTheme { StartCommentsFilters(StartCommentsFilter.All, 3, onSelected = selected::add) }
        }.use { scene ->
            scene.settle()
            val actions = scene.nodes().filter { it.config.contains(SemanticsActions.OnClick) }
            assertEquals(4, actions.size)
            assertEquals(1, actions.map { it.positionInRoot.y }.distinct().size, "Filters stay in one row")
            val scroll = scene.nodes().single { it.config.contains(SemanticsProperties.HorizontalScrollAxisRange) }
            assertTrue(scroll.config[SemanticsProperties.HorizontalScrollAxisRange].maxValue() > 0f)
            scroll.config[SemanticsActions.ScrollBy].action!!.invoke(10000f, 0f)
            scene.settle()
            val last = scene.nodes().single { it.config.getOrNull(SemanticsProperties.Text)?.any { text -> text.text == "Отвеченные" } == true }
            assertTrue(last.boundsInRoot.width > 0f && last.boundsInRoot.right <= 320f, "The last filter is reachable by scrolling")
            actions.forEach { it.config[SemanticsActions.OnClick].action!!.invoke() }
            assertEquals(StartCommentsFilter.entries.toList(), selected)
        }
    }

    @Test fun filterViewportReachesScreenEdgesWhileOnlyContentEndsAreInset() {
        ImageComposeScene(width = 320, height = 800, density = Density(1f)) {
            SauceTheme { StartCommentsContent(StartCommentsStore.State()) {} }
        }.use { scene ->
            scene.settle()
            fun viewport() = scene.nodes().single { it.config.contains(SemanticsProperties.HorizontalScrollAxisRange) }
            fun chips(): List<SemanticsNode> {
                fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
                return walk(viewport()).filter { it.config.contains(SemanticsActions.OnClick) }
            }
            assertEquals(0f, viewport().boundsInRoot.left, .5f)
            assertEquals(320f, viewport().boundsInRoot.right, .5f)
            assertEquals(16f, chips().first().boundsInRoot.left, .5f)
            viewport().config[SemanticsActions.ScrollBy].action!!.invoke(10f, 0f)
            scene.settle()
            assertEquals(6f, chips().first().boundsInRoot.left, .5f, "Scrolling chips must pass through the screen inset")
            viewport().config[SemanticsActions.ScrollBy].action!!.invoke(10000f, 0f)
            scene.settle()
            assertEquals(304f, chips().last().boundsInRoot.right, .5f)
        }
    }

    @Composable private fun Comment(isQuestion: Boolean = false, isAnswered: Boolean = false, isReply: Boolean = false,
                                    isAdminAnswer: Boolean = false, isReview: Boolean = false) {
        StartCommentCard(userImageUrl = "", userName = "Александра Константинопольская", commentCreateDate = "24 октября 2026, 11:48",
            message = "Могу ли я сделать возврат билета, если не смогу принять участие?", isQuestion = isQuestion,
            isAnswered = isAnswered, isReply = isReply, isAdminAnswer = isAdminAnswer, isReview = isReview,
            averageScore = 4.5, onClickReply = {})
    }
    private var now = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(10) { now += 100_000_000L; render(now).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.texts() = nodes().flatMap { it.config.getOrNull(SemanticsProperties.Text).orEmpty().map { text -> text.text } }
    private fun ImageComposeScene.save(name: String) {
        now += 100_000_000L
        render(now).use { img -> img.encodeToData()!!.use { data -> File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) } } }
    }
}
