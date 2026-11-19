package com.markettwits.sportsouce.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.*
import app.cash.paging.PagingData
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.presentation.common.components.StartListSearchBar
import com.markettwits.sportsouce.start.presentation.membres.components.*
import com.markettwits.sportsouce.start.presentation.membres.models.*
import com.markettwits.sportsouce.start.presentation.result.components.*
import com.markettwits.sportsouce.start.presentation.result.model.*
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStore
import kotlinx.coroutines.flow.flowOf
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class StartListsDesignTest {
    private val single = StartMembersUi.Single(11, "Александр Константинович", "Константинопольский", "Трейл 21 километр", "Клуб любителей длинных дистанций", "Мужчины 35–39 лет", "Петропавловск-Камчатский")
    private val team = StartMembersUi.Team(listOf(StartMembersUi.TeamMember(21, "Александра", "Иванова"), StartMembersUi.TeamMember(22, "Константин", "Петров")), "Эстафета 4 × 5 километров", "Команда с длинным названием", "Смешанная", "Новосибирск")
    private val result = MemberResult("12345", linkedMapOf(1 to "00:18:34.52", 2 to "01:12:40.16"), single.distance, single.group, 11, "${single.surname} ${single.name}", 123, "01:12:40.16", "Мужчины", "+00:03:15.07", 315, single.team)

    @Test
    fun participantsAndTeamsKeepTheirDetailsAndExpansionDoesNotLeakToAnotherItem() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            var current by mutableStateOf<StartMembersUi>(single)
            ImageComposeScene(width = 320, height = 1300, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) { Column(Modifier.fillMaxSize().background(SauceTheme.colors.background).padding(16.dp)) { StartMemberCard(current) } }
            }.use { scene ->
                scene.settle()
                assertTrue(scene.texts().any { it == "${single.surname} ${single.name}" }, "Full participant name must be preserved")
                assertFalse(scene.texts().any { it.contains(single.city) })
                scene.click(scene.targets().single().boundsInRoot.center)
                assertTrue(scene.texts().any { it == "${single.surname} ${single.name}" }, "Expansion must preserve the full name")
                assertTrue(scene.texts().any { it.contains(single.city) })
                assertTrue(scene.texts().any { it.contains(single.team) })
                scene.assertTextFits()
                scene.save("participants-refined-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
                current = team; scene.settle()
                assertTrue(scene.texts().any { it.contains("Иванова Александра") })
                assertTrue(scene.texts().any { it.contains("Петров Константин") })
                assertEquals("Подробности свёрнуты", scene.targets().single().config[SemanticsProperties.StateDescription])
                scene.click(scene.targets().single().boundsInRoot.center)
                assertTrue(scene.texts().any { it.contains(team.city) })
                scene.assertTextFits()
            }
        }
    }

    @Test
    fun resultValuesRemainCompleteAndClickOpensTheSameRecord() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            var opened: MemberResult? = null
            ImageComposeScene(width = 320, height = 1300, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) { Column(Modifier.fillMaxSize().background(SauceTheme.colors.background).padding(16.dp)) {
                    ResultCard(result = result, onClickMemberResult = { opened = it })
                } }
            }.use { scene ->
                scene.settle()
                for (value in listOf(result.name, result.group, result.team, result.distance, result.result, result.shift)) {
                    assertTrue(scene.texts().any { it.contains(value) }, "Missing $value")
                }
                assertTrue(scene.texts().any { it == result.place.toString() })
                assertTrue(scene.texts().any { it.contains(result.bodyNumber) })
                scene.assertTextFits()
                scene.assertSingleLine(result.result)
                scene.save("results-refined-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
                scene.click(scene.targets().single().boundsInRoot.center)
                assertEquals(result, opened)
            }
        }
    }

    @Test
    fun searchClearBackAndFilterActionsHaveOneAccessibleTargetEach() {
        var query by mutableStateOf("")
        var changes = 0; var back = 0; var filters = 0
        ImageComposeScene(width = 320, height = 220, density = Density(1f, 2f)) {
            SauceTheme { StartListSearchBar(query, { query = it; changes++ }, { back++ }, { query = "" }, filterCount = 0, onFilter = { filters++ }) }
        }.use { scene ->
            scene.settle()
            assertFalse(scene.texts().contains("0"))
            assertEquals(2, scene.targets().size)
            val editor = scene.nodes().single { it.config.contains(SemanticsActions.SetText) }
            editor.config[SemanticsActions.SetText].action!!.invoke(AnnotatedString("Иванов")); scene.settle()
            assertEquals("Иванов", query); assertEquals(1, changes)
            fun clickAction(label: String) {
                val node = scene.targets().single { it.config.getOrNull(SemanticsProperties.ContentDescription)?.contains(label) == true }
                assertTrue(node.size.height >= 48 && node.size.width >= 48)
                scene.click(node.boundsInRoot.center)
            }
            clickAction("Очистить поиск"); assertEquals("", query)
            clickAction("Назад"); assertEquals(1, back)
            clickAction("Фильтры"); assertEquals(1, filters)
        }
    }

    @Test
    fun sharedFilterChipsPreserveSelectedStatesAndEventPayloads() {
        val state = StartMemberResultsStore.State(flowOf(PagingData.empty()), filterState = FilterState(
            distanceFilters = listOf(DistanceFilter("Длинная дистанция 21 километр", true)),
            groupFilters = listOf(GroupFilter("Мужчины 35–39 лет", false)), genderFilters = listOf(GenderFilter("Мужчины", true))))
        val intents = mutableListOf<StartMemberResultsStore.Intent>()
        ImageComposeScene(width = 320, height = 1300, density = Density(1f, 2f)) {
            SauceTheme { ResultFiltersContent(state) { intents += it } }
        }.use { scene ->
            scene.settle(); scene.assertTextFits()
            val selected = scene.nodes().filter { it.config.getOrNull(SemanticsProperties.Selected) == true }
            assertEquals(2, selected.size)
            val group = scene.targets().single { it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == "Мужчины 35–39 лет" } == true }
            scene.click(group.boundsInRoot.center)
            assertEquals(StartMemberResultsStore.Intent.OnGroupFilterToggle("Мужчины 35–39 лет"), intents.single())
            val distance = scene.targets().single { it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == "Длинная дистанция 21 километр" } == true }
            scene.click(distance.boundsInRoot.center)
            assertEquals(StartMemberResultsStore.Intent.OnDistanceFilterToggle(state.filterState.distanceFilters.single()), intents.last())
        }
    }

    @Test
    fun participantFilterOnlyChangesTheExistingDistanceAndGenderOptions() {
        val groups = listOf(MembersFilterGroup("Дистанция", listOf(MembersFilterItem.Selected("21 км"))),
            MembersFilterGroup("Пол", listOf(MembersFilterItem.Base("Женщины"))),
            MembersFilterGroup("Группа", listOf(MembersFilterItem.Selected("Неизменяемая группа"))))
        val actions = mutableListOf<Pair<String, String>>()
        ImageComposeScene(width = 320, height = 700) {
            SauceTheme { MembersFilterContent(groups, { group, value -> actions += group to value }, {}) }
        }.use { scene ->
            scene.settle()
            assertFalse(scene.texts().contains("Неизменяемая группа"))
            val gender = scene.targets().single { it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == "Женщины" } == true }
            scene.click(gender.boundsInRoot.center)
            assertEquals(listOf("Пол" to "Женщины"), actions)
        }
    }

    @Test
    fun detailSheetRetainsCheckpointsAndDoesNotAddInventedMetrics() {
        var closed = 0
        ImageComposeScene(width = 320, height = 2000, density = Density(1f, 1f)) {
            SauceTheme { MemberResultDetailed(result, { closed++ }) }
        }.use { scene ->
            scene.settle(); scene.assertTextFits()
            assertTrue(scene.texts().contains("КП 1")); assertTrue(scene.texts().contains("КП 2"))
            assertTrue(scene.texts().contains("00:18:34.52"))
            assertFalse(scene.texts().contains("Темп"))
            val close = scene.targets().single { it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == "Закрыть" } == true }
            scene.click(close.boundsInRoot.center); assertEquals(1, closed)
        }
    }

    @Test
    fun listPreviewsShowActualRowsInBothThemes() {
        for (dark in listOf(false, true)) for (results in listOf(false, true)) {
            ImageComposeScene(width = 390, height = 1000, density = Density(1f)) {
                SauceTheme(darkTheme = dark) {
                    Column(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        StartListSearchBar("", {}, {}, {}, onFilter = {})
                        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(if (results) "Результаты" else "Участники", Modifier.fillMaxWidth(), style = androidx.compose.material3.MaterialTheme.typography.titleLarge, color = SauceTheme.colors.textPrimary)
                            if (results) {
                                listOf(
                                    result.copy(name = "Иванова Александра", place = 1, bodyNumber = "128", distance = "Трейл 21 км", group = "Женщины 30–39", team = "Беговой клуб", result = "01:42:18", shift = "00:00:00"),
                                    result.copy(name = "Петров Константин", place = 2, bodyNumber = "247", distance = "Трейл 21 км", group = "Мужчины 30–39", team = "", result = "01:44:05", shift = "+00:01:47"),
                                    result.copy(name = "Смирнова Анна", place = 3, bodyNumber = "312", distance = "Трейл 21 км", group = "Женщины 30–39", team = "Спорт Союз", result = "01:46:32", shift = "+00:04:14"),
                                ).forEach { ResultCard(result = it, onClickMemberResult = {}) }
                            } else {
                                StartMemberCard(single.copy(name = "Александра", surname = "Иванова", distance = "Трейл 21 км", group = "Женщины 30–39", city = "Новосибирск", team = "Беговой клуб"))
                                StartMemberCard(single.copy(id = 12, name = "Константин", surname = "Петров", distance = "Трейл 12 км"))
                                StartMemberCard(team.copy(team = "Спорт Союз", distance = "Эстафета 4 × 5 км"))
                            }
                        }
                    }
                }
            }.use { scene ->
                scene.settle(); scene.assertTextFits()
                scene.save("${if (results) "results" else "participants"}-list-refined-${if (dark) "dark" else "light"}")
            }
        }
    }

    @Test
    fun wideParticipantsExposeDetailsAndEmptyResultFieldsDoNotCreateLabels() {
        ImageComposeScene(width = 1000, height = 700) {
            SauceTheme { StartMemberCard(single) }
        }.use { scene ->
            scene.settle(); scene.assertTextFits()
            assertTrue(scene.texts().contains(single.city))
            assertTrue(scene.targets().isEmpty(), "A fully expanded desktop row must not have a disclosure action")
        }
        ImageComposeScene(width = 240, height = 1300, density = Density(1f, 2f)) {
            SauceTheme(darkTheme = true) {
                ResultCard(result = result.copy(bodyNumber = "", distance = "", team = "", group = "", shift = ""), onClickMemberResult = {})
            }
        }.use { scene ->
            scene.settle(); scene.assertTextFits()
            assertTrue(scene.texts().contains(result.result))
            scene.assertSingleLine(result.result)
            assertFalse(scene.texts().any { it.contains("Отставание") || it.startsWith("№") })
        }
    }

    private fun ImageComposeScene.assertTextFits() {
        for (node in unmergedNodes()) {
            if (node.config.contains(SemanticsProperties.EditableText)) continue
            val layouts = mutableListOf<TextLayoutResult>()
            node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
            layouts.forEach { assertFalse(it.hasVisualOverflow, "Clipped: ${it.layoutInput.text}, size=${it.size}, width=${it.didOverflowWidth}, height=${it.didOverflowHeight}, font=${it.layoutInput.style.fontSize}, lineHeight=${it.layoutInput.style.lineHeight}, constraints=${it.layoutInput.constraints}") }
        }
    }
    private fun ImageComposeScene.assertSingleLine(value: String) {
        val layouts = unmergedNodes().flatMap { node ->
            val results = mutableListOf<TextLayoutResult>()
            node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(results)
            results
        }.filter { it.layoutInput.text.text == value }
        assertTrue(layouts.isNotEmpty(), "Time must expose its text layout")
        layouts.forEach { assertEquals(1, it.lineCount, "Time must remain readable as one complete value") }
    }

    private var frameTime = System.nanoTime()
    private fun ImageComposeScene.click(at: Offset) {
        sendPointerEvent(PointerEventType.Press, at); settle()
        sendPointerEvent(PointerEventType.Release, at)
        sendPointerEvent(PointerEventType.Move, Offset(-1f, -1f)); settle()
    }
    private fun ImageComposeScene.settle() { repeat(12) { frameTime += 100_000_000L; render(frameTime).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
    }
    private fun ImageComposeScene.unmergedNodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.targets() = nodes().filter { it.config.contains(SemanticsActions.OnClick) && !it.config.contains(SemanticsProperties.EditableText) }
    private fun ImageComposeScene.texts() = nodes().flatMap { it.config.getOrNull(SemanticsProperties.Text).orEmpty().map { text -> text.text } }
    private fun ImageComposeScene.save(name: String) {
        frameTime += 100_000_000L
        render(frameTime).use { img -> img.encodeToData()!!.use { data ->
            File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
        } }
    }
}
