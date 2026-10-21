package com.markettwits.sportsouce.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.sauceColors
import com.markettwits.sportsouce.news.common.model.NewsItem
import com.markettwits.sportsouce.news.news_list.components.NewsItemCard
import com.markettwits.sportsouce.review.review.domain.Review
import com.markettwits.sportsouce.review.review.presentation.component.ReviewComponent
import com.markettwits.sportsouce.review.review.presentation.screen.ReviewScreen
import com.markettwits.sportsouce.review.review.presentation.store.ReviewStore
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class ReviewScreenDesignTest {
    private val news = NewsItem(1, "Открыта регистрация на осенний забег", "",
        "<p>Приглашаем участников на старт. Дистанции для новичков и опытных спортсменов. Подробности и программа на сайте.</p>".repeat(6),
        "", "", emptyList())
    private val start = StartsListItem(315, "Кросс трейл Альбатрос", "", "20 сентября 2026", StartsListItem.StatusCode(3, ""),
        "", "albatros", "Новосибирск", false, "6 км, 12 км", views = 0)

    @Test
    fun loadingKeepsMenuUsableAndRefreshPreservesContentWithoutSkeletons() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) for (width in listOf(320, 390)) {
            val component = FakeReviewComponent()
            ImageComposeScene(width = width, height = 1400, density = Density(1f, scale)) {
                Palette(dark) { ReviewScreen(component, notification = {}) }
            }.use { scene ->
                scene.settle()
                assertEquals(setOf("Загрузка новостей", "Загрузка актуальных стартов"), scene.loadingLabels())
                val menuTop = scene.clickableForText("Популярные").boundsInRoot.top
                for (title in listOf("Популярные", "Новости", "Клуб", "Фотоархив", "Магазин")) {
                    val button = scene.clickableForText(title)
                    assertTrue(button.size.height >= 48)
                    assertTrue(button.config[SemanticsActions.OnClick].action!!.invoke())
                }
                assertEquals((0..4).toList(), component.events.filterIsInstance<ReviewStore.Intent.OnClickMenu>().map { it.item })
                scene.save("review-loading-${if (dark) "dark" else "light"}-$width-${(scale * 100).toInt()}")

                component.value.value = ReviewStore.State(review = Review(news = listOf(news), actualStarts = listOf(start)))
                scene.settle()
                assertTrue(scene.loadingLabels().isEmpty())
                assertEquals(menuTop, scene.clickableForText("Популярные").boundsInRoot.top, 2f,
                    "News skeleton must reserve the loaded text height at font scale $scale")
                scene.clickableForText(news.title).config[SemanticsActions.OnClick].action!!.invoke()
                assertEquals(news, component.events.filterIsInstance<ReviewStore.Intent.OnClickNews>().single().news)
                scene.save("review-loaded-${if (dark) "dark" else "light"}-$width-${(scale * 100).toInt()}")

                component.value.value = component.value.value.copy(isLoading = true)
                scene.settle()
                assertTrue(scene.loadingLabels().isEmpty(), "Refresh must retain cached content")
                assertNotNull(scene.text(news.title))
                component.value.value = ReviewStore.State()
                scene.settle()
                assertTrue(scene.loadingLabels().isEmpty(), "A finished empty response must not keep shimmering")
                assertNotNull(scene.text("Магазин"))
            }
        }
    }

    @Test
    fun newsDescriptionEndsWithEllipsisOnSecondLineAndGrowsWithFontScale() {
        for (scale in listOf(1f, 2f)) {
            ImageComposeScene(width = 300, height = 500, density = Density(1f, scale)) {
                Palette(false) { NewsItemCard(newsInfo = news, cardWidth = 240.dp, cardHeight = 220.dp, onClick = {}) }
            }.use { scene ->
                scene.settle()
                val layouts = scene.nodes().flatMap { node ->
                    mutableListOf<TextLayoutResult>().also { node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(it) }
                }
                val body = layouts.single { it.layoutInput.text.text.startsWith("Приглашаем") }
                scene.save("news-ellipsis-${(scale * 100).toInt()}")
                assertEquals(2, body.lineCount)
                assertEquals(TextOverflow.Ellipsis, body.layoutInput.overflow)
                assertTrue(body.getLineEnd(1, visibleEnd = true) < body.layoutInput.text.length)
                assertTrue(body.getLineBottom(1) <= body.size.height + 0.5f, "Both lines must fit at large font scale")
            }
        }
    }

    @Composable
    private fun Palette(dark: Boolean, content: @Composable () -> Unit) {
        val colors = sauceColors(dark)
        SauceTheme(darkTheme = dark, materialColors = colors.material.copy(primary = colors.surface, secondary = colors.action,
            tertiary = colors.textPrimary, primaryContainer = colors.surfaceRaised, outline = colors.textMuted)) {
            Box(Modifier.fillMaxSize().background(colors.background)) { content() }
        }
    }

    private class FakeReviewComponent : ReviewComponent {
        override val value = MutableStateFlow(ReviewStore.State(isLoading = true))
        val events = mutableListOf<ReviewStore.Intent>()
        override fun obtainEvent(event: ReviewStore.Intent) { events += event }
    }
    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(12) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.text(value: String) = nodes().single { node ->
        node.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == value }
    }
    private fun ImageComposeScene.clickableForText(value: String): SemanticsNode {
        var node = text(value)
        while (!node.config.contains(SemanticsActions.OnClick)) node = checkNotNull(node.parent)
        return node
    }
    private fun ImageComposeScene.loadingLabels() = nodes().filter {
        it.config.contains(SemanticsProperties.ProgressBarRangeInfo)
    }.flatMap { it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() }.toSet()
    private fun ImageComposeScene.save(name: String) {
        time += 100_000_000L
        render(time).use { image -> image.encodeToData()!!.use { data ->
            File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
        } }
    }
}
