package com.markettwits.sportsouce.profile.upcoming

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.unit.Density
import androidx.compose.ui.use
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPaymentStatus
import com.markettwits.sportsouce.profile.upcoming.UpcomingStartsStore.Content
import com.markettwits.sportsouce.profile.upcoming.UpcomingStartsStore.Intent
import com.markettwits.sportsouce.profile.upcoming.UpcomingStartsStore.State
import com.markettwits.sportsouce.profile.upcoming.UpcomingStartsStore.ToolbarDestination
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Instant

@OptIn(ExperimentalComposeUiApi::class)
class UpcomingStartsLayoutTest {
    @Test
    fun paidHeroAndToolbarActionsAreAccessibleWithAnEarlierUnpaidStart() {
        val events = mutableListOf<Intent>()
        val now = Instant.parse("2027-01-31T10:00:00Z")
        val unpaid = order(2, "2027-01-31T16:00:00Z").copy(payment = StartOrderPaymentStatus.NotPaid())
        val registrations = upcomingRegistrations(listOf(order(1), unpaid))
        ImageComposeScene(width = 320, height = 1100, density = Density(1f, 1.45f)) {
            SauceTheme(darkTheme = true) {
                UpcomingStartsContent(State(now, Content.Ready(registrations)), 3, events::add)
            }
        }.use { scene ->
            repeat(12) { scene.render(it * 100_000_000L).close() }
            val nodes = scene.semanticsOwners.flatMap { it.rootSemanticsNode.descendants() }
            val hero = assertNotNull(nodes.firstOrNull { node ->
                node.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == "Зимний забег 1" }
            })
            val countdown = nodes.firstOrNull { node ->
                node.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == "1 день" }
            }
            assertNotNull(countdown)
            assertNotNull(nodes.findText("Ожидают оплаты"))
            val clickableHero = generateSequence(hero) { it.parent }.first { it.config.contains(SemanticsActions.OnClick) }
            clickableHero.config[SemanticsActions.OnClick].action?.invoke()
            val heading = assertNotNull(nodes.findText("Календарь и архив"))
            val browse = assertNotNull(nodes.findText("Смотреть старты"))
            val registrationsShortcut = assertNotNull(nodes.findText("Все регистрации"))
            val archive = assertNotNull(nodes.findText("Фотоархив"))
            val shortcuts = listOf(browse, registrationsShortcut, archive)
            assertEquals(1, shortcuts.map { it.boundsInRoot.left }.distinct().size)
            val actionBounds = shortcuts.map { it.actionNode().boundsInRoot }
            assertTrue(actionBounds.all { it.left == heading.boundsInRoot.left && it.width == actionBounds[0].width })
            assertTrue(actionBounds.zipWithNext().all { (upper, lower) -> upper.bottom <= lower.top })
            registrationsShortcut.click()
            browse.click()
            archive.click()
            assertNotNull(nodes.findText("Поиск старта")).click()
            assertNotNull(nodes.findAction("Настройки")).click()
            assertNotNull(nodes.findAction("Уведомления, непрочитанных: 3")).click()
            assertEquals(listOf(
                Intent.OpenRegistration(1), Intent.OpenHistory, Intent.Browse, Intent.OpenToolbar(ToolbarDestination.PhotoArchive),
                Intent.OpenToolbar(ToolbarDestination.Search), Intent.OpenToolbar(ToolbarDestination.Settings),
                Intent.OpenToolbar(ToolbarDestination.Inbox),
            ), events)
        }
    }

    @Test
    fun onlyUnpaidEntriesStayCompactAndOpenRegistrationDetails() {
        val events = mutableListOf<Intent>()
        val unpaid = order(1).copy(payment = StartOrderPaymentStatus.NotPaid())
        val state = State(Instant.parse("2027-01-31T10:00:00Z"), Content.Ready(upcomingRegistrations(listOf(unpaid))))
        ImageComposeScene(width = 360, height = 620) {
            SauceTheme { UpcomingStartsContent(state, 0, events::add) }
        }.use { scene ->
            repeat(12) { scene.render(it * 100_000_000L).close() }
            val nodes = scene.semanticsOwners.flatMap { it.rootSemanticsNode.descendants() }
            assertNotNull(nodes.findText("Ожидают оплаты"))
            assertNull(nodes.findText("До старта"))
            assertNull(nodes.findText("Пока нет предстоящих стартов"))
            assertNotNull(nodes.findText(unpaid.name)).click()
            assertEquals(listOf<Intent>(Intent.OpenRegistration(unpaid.id)), events)
        }
    }

    @Test
    fun regularStartsTakePriorityOverAnOngoingLongSeries() {
        val series = order(1, "2027-01-01T10:00:00Z").copy(dateEndCloud = "2027-03-01T10:00:00Z")
        val regular = order(2)
        val unpaid = order(3).copy(payment = StartOrderPaymentStatus.NotPaid())
        val now = Instant.parse("2027-01-31T10:00:00Z")
        ImageComposeScene(width = 360, height = 900) {
            SauceTheme {
                UpcomingStartsContent(State(now, Content.Ready(upcomingRegistrations(listOf(series, regular, unpaid)))), 0, {})
            }
        }.use { scene ->
            repeat(12) { scene.render(it * 100_000_000L).close() }
            val nodes = scene.semanticsOwners.flatMap { it.rootSemanticsNode.descendants() }
            val mainCard = assertNotNull(nodes.findText(regular.name))
            val unpaidRow = assertNotNull(nodes.findText(unpaid.name))
            val seriesRow = assertNotNull(nodes.findText(series.name))

            assertNotNull(nodes.findText("До старта"))
            assertNull(nodes.findText("До завершения"))
            assertNull(nodes.findText("Мои старты"))
            assertNotNull(nodes.findText("Длительные старты"))
            assertTrue(mainCard.boundsInRoot.top < unpaidRow.boundsInRoot.top)
            assertTrue(unpaidRow.boundsInRoot.top < seriesRow.boundsInRoot.top)
        }
    }

    private fun List<SemanticsNode>.findText(label: String) = firstOrNull { node ->
        node.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == label }
    }

    private fun List<SemanticsNode>.findAction(label: String) = firstOrNull { node ->
        node.config.getOrNull(SemanticsProperties.ContentDescription)?.contains(label) == true
    }

    private fun SemanticsNode.click() {
        actionNode().config[SemanticsActions.OnClick].action?.invoke()
    }

    private fun SemanticsNode.actionNode() =
        generateSequence(this) { it.parent }.first { it.config.contains(SemanticsActions.OnClick) }

    private fun SemanticsNode.descendants(): List<SemanticsNode> = listOf(this) + children.flatMap { it.descendants() }
}
