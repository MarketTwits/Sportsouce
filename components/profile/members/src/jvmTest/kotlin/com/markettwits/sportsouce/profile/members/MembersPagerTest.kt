package com.markettwits.sportsouce.profile.members

import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.Density
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.members.member_common.domain.*
import com.markettwits.sportsouce.profile.members.members_list.presentation.component.MembersListComponent
import com.markettwits.sportsouce.profile.members.members_list.presentation.components.MembersScreen
import com.markettwits.sportsouce.profile.members.members_list.presentation.store.store.MembersListStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class MembersPagerTest {
    private class FakeMembers : MembersListComponent {
        override val state = MutableStateFlow(MembersListStore.State(isSuccess = true, teamLoading = false,
            team = MemberIdentityTeam(), members = List(20) { emptyProfileMember.copy(id = it + 1, name = "Иван", surname = "Участник $it") }))
        val events = mutableListOf<MembersListStore.Intent>()
        override fun obtainEvent(intent: MembersListStore.Intent) { events += intent }
    }

    @Test fun swipesAndTabClicksStaySynchronizedAndTeamUpdatesKeepTheCurrentPage() {
        for (width in listOf(320, 390, 800)) for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            val component = FakeMembers()
            ImageComposeScene(width, 1000, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) { MembersScreen(component) }
            }.use { scene ->
                scene.settle()
                scene.assertSelected("Участники")
                scene.swipePage(forward = true)
                scene.assertSelected("Запросы")
                assertNotNull(scene.text("Запросов пока нет"))
                scene.swipePage(forward = true)
                scene.assertSelected("Доступ")
                assertNotNull(scene.text("Кто может регистрировать меня"))
                scene.swipePage(forward = false)
                scene.assertSelected("Запросы")
                scene.click("Участники"); scene.settle()
                scene.assertSelected("Участники")
                assertNotNull(scene.text("Добавить участника"))
                assertTrue(component.events.isEmpty(), "Changing pages must not reload data or trigger team actions")

                scene.click("Запросы"); scene.settle()
                component.state.value = component.state.value.copy(team = MemberIdentityTeam(incoming = listOf(
                    MemberConnectionRequest(6, 1775, "pending", 2310, "Иван Иванов", "Анна Петрова"))))
                scene.settle()
                scene.assertSelected("Запросы (1)")
                scene.click("Принять запрос")
                assertEquals(MembersListStore.Intent.RespondToRequest(6, true), component.events.single())

                scene.click("Доступ"); scene.click("Участники"); scene.settle()
                scene.assertSelected("Участники")
            }
        }
    }

    @Test fun participantScrollPositionSurvivesSwitchingAcrossBothOtherPages() {
        val component = FakeMembers()
        ImageComposeScene(390, 800) { SauceTheme { MembersScreen(component) } }.use { scene ->
            scene.settle()
            scene.vertical().config[SemanticsActions.ScrollBy].action!!.invoke(0f, 650f)
            scene.settle()
            val before = scene.vertical().config[SemanticsProperties.VerticalScrollAxisRange].value()
            assertTrue(before > 0)
            scene.click("Доступ"); scene.settle()
            scene.click("Запросы"); scene.settle()
            scene.click("Участники"); scene.settle()
            scene.assertSelected("Участники")
            val after = scene.vertical().config[SemanticsProperties.VerticalScrollAxisRange].value()
            assertEquals(before, after, 1f, "Returning to participants must retain its list position")
        }
    }

    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(24) { time += 50_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.text(value: String) = nodes().firstOrNull { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { t -> t.text == value } }
    private fun ImageComposeScene.click(value: String) {
        var node = checkNotNull(text(value)) { "Missing $value" }
        while (!node.config.contains(SemanticsActions.OnClick)) node = node.parent!!
        node.config[SemanticsActions.OnClick].action!!.invoke()
    }
    private fun ImageComposeScene.assertSelected(value: String) {
        var tab = checkNotNull(text(value)) { "Missing tab $value" }
        while (!tab.config.contains(SemanticsProperties.Selected)) tab = tab.parent!!
        assertEquals(true, tab.config[SemanticsProperties.Selected], "Selected tab must be $value")
    }
    private fun ImageComposeScene.vertical() = nodes().filter { it.config.contains(SemanticsProperties.VerticalScrollAxisRange) }
        .maxBy { it.boundsInRoot.width * it.boundsInRoot.height }
    private fun ImageComposeScene.swipePage(forward: Boolean) {
        val pager = nodes().filter { it.config.contains(SemanticsProperties.HorizontalScrollAxisRange) }.maxBy { it.size.height }
        val bounds = pager.boundsInRoot
        val left = Offset(bounds.left + bounds.width * .12f, bounds.center.y)
        val right = Offset(bounds.right - bounds.width * .12f, bounds.center.y)
        val start = if (forward) right else left
        val end = if (forward) left else right
        sendPointerEvent(PointerEventType.Press, start, timeMillis = time / 1_000_000, type = PointerType.Touch)
        repeat(12) { step ->
            time += 20_000_000L
            sendPointerEvent(PointerEventType.Move, start + (end - start) * ((step + 1) / 12f), timeMillis = time / 1_000_000, type = PointerType.Touch)
            render(time).close()
        }
        sendPointerEvent(PointerEventType.Release, end, timeMillis = time / 1_000_000, type = PointerType.Touch)
        settle()
    }
}
