package com.markettwits.sportsouce.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.cloud.model.start.fields.*
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.start.components.*
import com.markettwits.sportsouce.start.presentation.start.screen.StartScreenInnerContent
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class StartDetailsDesignTest {
    private val person = Organizer(1, "", true, name = "Беговой клуб Спорт Союз", phone = "+7 999 000-00-00",
        socialNetworks = listOf(SocialNetwork("vk", 1, 1, null, "https://example.com/vk"),
            SocialNetwork("telegram", 2, 1, null, "https://example.com/tg"),
            SocialNetwork("other", 3, 1, null, "https://example.com/other")), startId = 315)
    private val members = listOf(
        StartMembersUi.Single(1, "Анна", "Иванова", "Трейл 12 км", "", "", ""),
        StartMembersUi.Single(2, "Иван", "Петров", "Трейл 12 км", "", "", ""),
        StartMembersUi.Team(emptyList(), "Эстафета 4 × 5 км", "Спорт Союз", "", ""))
    private fun distance(id: Int, name: String) = DistinctDistance(null, null, null, null,
        format = null, id = id, infiniteSlots = true, name = name, openSlots = null,
        orderNumber = id, slots = null, stages = emptyList(), staticPrice = null)
    private val distances = listOf(distance(1, "Трейл 12 км"), distance(2, "Трейл 21 км"),
        distance(3, "Эстафета 4 × 5 км"), distance(4, "Детский старт 1 км"))
    private val rating = StartItem.ReviewState.Rated(StartItem.ReviewScores(4.25f, 4, 5, 3))
    private val files = (1..4).map { StartItem.Result(it, "Документ $it: информация для участников", "https://example.com/$it.pdf") }

    @Test
    fun sectionsKeepLongTextReadableInBothThemesAndAtLargeFontSizes() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            ImageComposeScene(width = 390, height = 1800, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Column(Modifier.fillMaxSize().background(SauceTheme.colors.background).verticalScroll(rememberScrollState()).padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        StartTitle(title = "Кросс трейл Альбатрос — осенний старт")
                        StartExtraFieldsPanel(place = "Новосибирск, Горнолыжный комплекс Альбатрос", organizers = listOf(person), startDate = "20 сентября 2026")
                        StartMembersPanel(membersCount = 123456) {}
                        StartMembersResultPanel(membersResultCount = 1234) {}
                        StartConditionGrid(conditionItems = listOf(StartItem.ConditionDetail.Regulation("<p>Регламент</p>"), StartItem.ConditionDetail.Statement("<p>Положение</p>")))
                        StartDistanceInfo(distances = distances, membersUi = members)
                        StartReviewPanel(reviewState = rating)
                        StartAllFiles(results = files) {}
                        StartOrganizers(organizer = listOf(person), onClickPhone = {}, onClickUrl = {})
                    }
                }
            }.use { scene ->
                scene.settle(); scene.assertTextFits()
                for (value in listOf("Кросс трейл Альбатрос — осенний старт", "123456", "1234", person.name)) {
                    assertTrue(scene.texts().contains(value), "Missing $value")
                }
                assertTrue(scene.nodes().any { it.config.getOrNull(SemanticsProperties.ContentDescription)?.contains("Локация") == true })
                val headingLefts = listOf("Участники", "Результаты", "Дистанции", "Регистрации").map { label ->
                    scene.nodes(unmerged = true).first { node -> node.config.contains(SemanticsActions.GetTextLayoutResult) &&
                        node.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == label } == true }.positionInRoot.x
                }
                headingLefts.forEach { assertEquals(headingLefts.first(), it, 0.5f,
                    "Navigation and section headings must share the same icon/text grid") }
                scene.save("start-details-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
            }
        }
    }

    @Test
    fun entryRowsKeepOneActionAndOriginalCountsAndHideWhenEmpty() {
        var participants = 0; var results = 0
        ImageComposeScene(width = 320, height = 650, density = Density(1f, 2f)) {
            SauceTheme { Column {
                StartMembersPanel(membersCount = 0) { error("Empty row clicked") }
                StartMembersResultPanel(membersResultCount = 0) { error("Empty row clicked") }
                StartMembersPanel(membersCount = 12345) { participants++ }
                StartMembersResultPanel(membersResultCount = 678) { results++ }
            } }
        }.use { scene ->
            scene.settle(); scene.assertTextFits(); assertEquals(2, scene.targets().size)
            scene.targets().forEach { assertTrue(it.size.height >= 48 && it.size.width >= 48) }
            scene.click("Участники"); scene.click("Результаты")
            assertEquals(1, participants); assertEquals(1, results)
            assertTrue(scene.texts().contains("12345")); assertTrue(scene.texts().contains("678"))
        }
    }

    @Test
    fun fileExpansionAndLinksKeepTheirExactPayloads() {
        val opened = mutableListOf<String>()
        ImageComposeScene(width = 320, height = 1600, density = Density(1f, 2f)) {
            SauceTheme { StartAllFiles(results = files, conditionFile = StartItem.ConditionFile.Base("https://example.com/rules.pdf")) { opened += it } }
        }.use { scene ->
            scene.settle(); scene.assertTextFits()
            assertFalse(scene.texts().contains(files.last().name))
            scene.click("Ещё 2"); scene.assertTextFits()
            scene.click(files.last().name)
            assertEquals(files.last().url, opened.last())
            scene.click("Положение"); assertEquals("https://example.com/rules.pdf", opened.last())
            scene.click("Свернуть"); assertFalse(scene.texts().contains(files.last().name))
        }
    }

    @Test
    fun distanceExpansionAndChartValuesKeepTheOriginalGroupingIncludingTeams() {
        ImageComposeScene(width = 320, height = 1400, density = Density(1f, 2f)) {
            SauceTheme { StartDistanceInfo(distances = distances, membersUi = members) }
        }.use { scene ->
            scene.settle(); scene.assertTextFits()
            assertFalse(scene.texts().contains(distances.last().name))
            scene.click("Дистанции"); assertTrue(scene.texts().contains(distances.last().name))
            val progress = scene.nodes().mapNotNull { it.config.getOrNull(SemanticsProperties.ProgressBarRangeInfo)?.current }
            assertEquals(listOf(1f, 0.5f), progress)
            scene.click("Регистрации")
            assertFalse(scene.nodes().any { it.config.contains(SemanticsProperties.ProgressBarRangeInfo) })
            scene.click("Регистрации"); scene.assertTextFits()
            assertEquals(2, scene.nodes().count { it.config.contains(SemanticsProperties.ProgressBarRangeInfo) })
        }
    }

    @Test
    fun contactTargetsKeepPhoneAndSocialUrlsAndRemainAtLeast48Dp() {
        val opened = mutableListOf<String>(); val phones = mutableListOf<String>()
        ImageComposeScene(width = 320, height = 1000, density = Density(1f, 2f)) {
            SauceTheme(darkTheme = true) { OrganizerContactContent(person, { opened += it }, { phones += it }) }
        }.use { scene ->
            scene.settle(); scene.assertTextFits(); assertEquals(4, scene.targets().size)
            scene.targets().forEach { assertTrue(it.size.width >= 48 && it.size.height >= 48) }
            scene.click(person.phone!!); assertEquals(listOf(person.phone), phones)
            for (label in listOf("ВКонтакте", "Telegram", "other")) scene.clickDescription(label)
            assertEquals(person.socialNetworks.map { it.url }, opened)
        }
    }

    @Test
    fun conditionDocumentKeepsItsHtmlAndFileAction() {
        var opened = ""
        ImageComposeScene(width = 320, height = 1000, density = Density(1f, 2f)) {
            SauceTheme { ConditionDetailContent("<p>Условия участия</p><ul><li>Документы участника</li></ul>",
                StartItem.ConditionFile.Base("https://example.com/statement.pdf"), onClickFile = { opened = it }) }
        }.use { scene ->
            scene.settle(); scene.assertTextFits()
            assertTrue(scene.texts().any { it.contains("Документы участника") })
            scene.click("Положение"); assertEquals("https://example.com/statement.pdf", opened)
        }
    }

    @Test
    fun readySectionsAppearTogetherWithoutTheOldStaggerAndSlotsArePreserved() {
        var partial by mutableStateOf(true)
        val start = startItem()
        ImageComposeScene(width = 390, height = 1400) {
            SauceTheme { Column(Modifier.verticalScroll(rememberScrollState())) {
                StartScreenInnerContent(Modifier, start, recommendedStarts = emptyList(), seriesStarts = emptyList(), isPartialData = partial,
                    onClickMembers = {}, onClickMembersResults = {}, onClickFullAlbum = {}, onClickUrl = {}, onClickPhone = {},
                    onClickRetry = {}, onClickRelatedStarts = {}, onClickRecommendedStart = {},
                    comments = { Text("COMMENTS_SLOT") }, donations = { Text("DONATIONS_SLOT") })
            } }
        }.use { scene ->
            scene.settle()
            assertFalse(scene.texts().contains("Файлы и ссылки"))
            partial = false
            scene.settle(frames = 6, step = 50_000_000L)
            for (label in listOf("Файлы и ссылки", "Контактное лицо", "Дистанции", "Участники", "COMMENTS_SLOT", "DONATIONS_SLOT"))
                assertTrue(scene.texts().contains(label), "Ready section still hidden: $label")
        }
    }

    private fun startItem() = StartItem(id = 315, title = "Кросс трейл Альбатрос", startPlace = "Новосибирск", slug = "albatros", image = "",
        startMembersUi = members, kindOfSports = emptyList(), sponsors = emptyList(), startSeries = StartItem.StartSeries.Empty,
        startStatus = StartItem.StartStatus(3, "Регистрация открыта"), startData = "", startAlbum = emptyList(), startTime = "20 сентября 2026",
        description = "<p>Осенний трейл</p>", paymentDisabled = false, regLink = "", regOnSite = true, reviewState = StartItem.ReviewState.NoReviews,
        distanceInfoNew = distances, conditionDetails = emptyList(), distanceMapNew = emptyList(), paymentType = "", organizers = listOf(person),
        membersResults = emptyList(), conditionFile = StartItem.ConditionFile.Empty, commentsRemote = StartItem.Comments(315, emptyList()),
        result = files, usefulLinks = emptyList(), startTimes = StartItem.StartTimes("", "", "", ""))

    private var time = System.nanoTime()
    private fun ImageComposeScene.settle(frames: Int = 14, step: Long = 100_000_000L) { repeat(frames) { time += step; render(time).close() } }
    private fun ImageComposeScene.nodes(unmerged: Boolean = false): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(if (unmerged) it.unmergedRootSemanticsNode else it.rootSemanticsNode) }
    }
    private fun ImageComposeScene.texts() = nodes().flatMap { it.config.getOrNull(SemanticsProperties.Text).orEmpty().map { text -> text.text } }
    private fun ImageComposeScene.targets() = nodes().filter { it.config.contains(SemanticsActions.OnClick) }
    private fun ImageComposeScene.click(label: String) {
        val candidates = targets().filter { it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == label } == true }
        assertEquals(1, candidates.size, "Expected one action for $label")
        candidates.single().config[SemanticsActions.OnClick].action!!.invoke(); settle()
    }
    private fun ImageComposeScene.clickDescription(label: String) {
        val target = targets().single { it.config.getOrNull(SemanticsProperties.ContentDescription)?.contains(label) == true }
        target.config[SemanticsActions.OnClick].action!!.invoke(); settle()
    }
    private fun ImageComposeScene.assertTextFits() {
        nodes(unmerged = true).forEach { node ->
            val layouts = mutableListOf<TextLayoutResult>()
            node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
            layouts.forEach { assertFalse(it.hasVisualOverflow, "Clipped: ${it.layoutInput.text}, ${it.layoutInput.constraints}, ${it.size}") }
        }
    }
    private fun ImageComposeScene.save(name: String) {
        time += 100_000_000L
        render(time).use { img -> img.encodeToData()!!.use { data ->
            File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
        } }
    }
}
