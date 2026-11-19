package com.markettwits.sportsouce.profile.results

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import app.cash.paging.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.results.domain.*
import com.markettwits.sportsouce.profile.results.presentation.actions.*
import com.markettwits.sportsouce.profile.results.presentation.screen.*
import com.markettwits.sportsouce.profile.results.presentation.store.MyResultsStore
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class)
class MyResultsLayoutTest {
    @BeforeTest fun main() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @AfterTest fun resetMain() { Dispatchers.resetMain() }
    private val category = ResultCategory("run:10000", "10 км", count = 3, best = PersonalBest(2196.24, 270, "Заельцовский забег", "2025-07-20"))
    private val points = listOf(2238.1, 2250.0, 2196.24).mapIndexed { i, seconds -> ProgressPoint("point:$i", "2025-0${i + 5}-15", 2025, 235 + i,
        listOf("Парковый кросс", "Забег Арена Биатлона", "Заельцовский забег")[i], "run:10000", "10 км", seconds, seconds / 10, seconds, 7, "М2_30-39 лет", ComparisonValue(2866.26, 45, "М2_30-39 лет")) }
    private val rows = listOf(
        MyResult("p:1", startId = 235, startName = "Парковый кросс", date = "2025-06-15", categoryLabel = "10 км", resultSeconds = 2238.1, place = 7, finishers = 200, group = "М2_30-39 лет", groupPlace = 2, bib = "457"),
        MyResult("p:2", "awaiting", startId = 326, startName = "Страшно добрый забег", date = "2026-10-25", distance = "4 км"),
        MyResult("p:3", "pending", "external", startName = "Очень длинное название внешнего старта на другой спортивной площадке", date = "2026-09-22", resultText = "1:42:18", categoryLabel = "Полумарафон"),
        MyResult("p:4", "file", startId = 264, startName = "Сибирский предел 2025 — 1 этап", date = "2025-04-26", fileUrl = "https://example.test/file.pdf"),
    )
    private fun loaded() = MyResultsStore.State(summary = ResultsSummary(6, 6, 2025, listOf(category)), summaryLoading = false,
        category = category.key, comparison = ResultComparison.Group, progress = ResultProgress(points = points), count = 40,
        feed = flowOf(PagingData.from(rows, sourceLoadStates = LoadStates(LoadStateNotLoading(false), LoadStateNotLoading(false), LoadStateNotLoading(false)))))

    @Test fun summaryProgressAndHistoryAdaptToPhonesTabletsAndLargeFonts() {
        for (dark in listOf(false, true)) for ((width, scale) in listOf(390 to 1f, 320 to 2f, 800 to 1f)) {
            val state = mutableStateOf(loaded())
            val events = mutableListOf<MyResultsStore.Intent>()
            ImageComposeScene(width, 1000, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) { Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) { MyResultsContent(state.value, events::add) {} } }
            }.use { scene ->
                scene.settle(); scene.checkText(); scene.save("results-${if (dark) "dark" else "light"}-$width-${(scale*100).toInt()}-top")
                scene.click("Найти результат")
                repeat(4) { scene.scroll(500f); scene.checkText() }
                scene.save("results-${if (dark) "dark" else "light"}-$width-${(scale*100).toInt()}-history")
                scene.scroll(10000f); scene.checkText()
                assertTrue(scene.nodes().any { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { text -> text.text == "Открыть протокол" } })
            }
        }
    }

    @Test fun graphTableEmptyOnePointAndMissingComparisonRenderWithoutExceptions() {
        for (points in listOf(emptyList(), this.points.take(1), this.points.map { it.copy(value = 2200.0, compare = null) })) for (table in listOf(false, true)) {
            ImageComposeScene(390, 1500) { SauceTheme(darkTheme = true) {
                ResultsProgressCard(loaded().copy(progress = ResultProgress(points = points), table = table)) {}
            } }.use { scene -> scene.settle(); scene.checkText() }
        }
    }

    @Test fun actionFormsKeepFieldErrorsAndRequestActionsReachable() {
        for (dark in listOf(false, true)) for (action in ResultsAction.entries) {
            val state = ResultsActionStore.State(action, errors = ExternalResultDraft().errors(false),
                results = listOf(UnmatchedResult(1, "Иван Иванов", birthYear = 1991, result = "18:20.71", distance = "5 км")),
                claims = listOf(ResultClaim(1, "USER_CLAIM", "PENDING", result = ClaimedResult(1, "Иван Иванов", NamedEntity(235, "Парковый кросс")))))
            ImageComposeScene(390, 900) { SauceTheme(darkTheme = dark) { ResultsActionContent(state, listOf(category)) {} } }.use { scene ->
                scene.settle(); scene.checkText(); scene.save("results-form-${action.name}-${if (dark) "dark" else "light"}")
                scene.scroll(10000f); scene.checkText()
            }
        }
    }

    @Test fun compactFiltersOpenSharedSheetsAndOnlyCommitTheChosenValue() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            val state = mutableStateOf(loaded().copy(series = listOf(ResultSeries("park", "Парковый кросс", categories = listOf(category.key)))))
            val events = mutableListOf<MyResultsStore.Intent>()
            ImageComposeScene(390, 840, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        ProgressFilters(state.value) { intent ->
                            events += intent
                            state.value = when (intent) {
                                is MyResultsStore.Intent.Category -> state.value.copy(category = intent.key)
                                is MyResultsStore.Intent.Compare -> state.value.copy(comparison = intent.comparison)
                                is MyResultsStore.Intent.Series -> state.value.copy(seriesKey = intent.key)
                                else -> state.value
                            }
                        }
                    }
                }
            }.use { scene ->
                scene.settle()
                scene.click("Дистанция")
                assertTrue(events.isEmpty(), "Opening a selector must not change the filter")
                scene.checkText()
                scene.click("Все дистанции (темп)")
                assertEquals(MyResultsStore.Intent.Category(null), events.last())
                scene.click("Сравнить")
                scene.checkText()
                scene.click("Только мои результаты")
                assertEquals(MyResultsStore.Intent.Compare(ResultComparison.Self), events.last())
                scene.click("Старт")
                scene.checkText()
                scene.click("Парковый кросс")
                assertEquals(MyResultsStore.Intent.Series("park"), events.last())
                assertEquals(3, events.size)
            }
        }
    }

    @Test fun resultCardsStayCompactAndNonFinishesAreNotMarkedAsAFinish() {
        for (dark in listOf(false, true)) {
            ImageComposeScene(390, 800) { SauceTheme(darkTheme = dark) {
                Box(Modifier.padding(16.dp)) { MyResultCard(rows.first()) {} }
            } }.use { scene ->
                scene.settle(); scene.checkText()
                val card = scene.nodes().first { it.config.contains(SemanticsActions.OnClick) }
                assertTrue(card.boundsInRoot.height < 260, "A standard result should not grow into a large panel")
                assertTrue(scene.nodes().any { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { text -> text.text == "7 из 200" } })
                scene.save("result-card-${if (dark) "dark" else "light"}")
            }
            ImageComposeScene(390, 800) { SauceTheme(darkTheme = dark) {
                MyResultCard(rows.first().copy(status = "dnf", resultSeconds = null)) {}
            } }.use { scene ->
                scene.settle(); scene.checkText()
                assertFalse(scene.nodes().any { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { text -> text.text == "Финиш" } })
            }
        }
    }

    private var time = 1_000_000_000L
    private fun ImageComposeScene.settle() { repeat(12) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.click(text: String) {
        var node = nodes().first { text in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() || it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { t -> t.text == text } }
        while (!node.config.contains(SemanticsActions.OnClick)) node = node.parent!!
        node.config[SemanticsActions.OnClick].action!!.invoke(); settle()
    }
    private fun ImageComposeScene.scroll(by: Float) {
        nodes().firstOrNull { it.config.contains(SemanticsActions.ScrollBy) }?.config?.get(SemanticsActions.ScrollBy)?.action?.invoke(0f, by)
        settle()
    }
    private fun ImageComposeScene.checkText() = nodes().forEach { node ->
        val layouts = mutableListOf<TextLayoutResult>()
        node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
        layouts.filter { it.layoutInput.overflow != TextOverflow.Ellipsis }.forEach { layout ->
            assertFalse(layout.didOverflowHeight, layout.layoutInput.text.text)
            for (line in 0 until layout.lineCount) assertTrue(layout.getLineRight(line) <= layout.size.width + 1f, "Clipped: ${layout.layoutInput.text.text}")
        }
    }
    private fun ImageComposeScene.save(name: String) { render(time + 100_000_000L).use { frame ->
        frame.encodeToData()!!.use { data -> File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) } }
    } }
}
