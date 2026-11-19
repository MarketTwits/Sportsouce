package com.markettwits.sportsouce.profile.members

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.profile.members.member_common.presentation.MemberItemCard
import com.markettwits.sportsouce.profile.members.member_detail.presentation.components.components.MemberDetailButtons
import com.markettwits.sportsouce.profile.members.member_detail.presentation.components.components.MemberDetailContent
import com.markettwits.sportsouce.profile.members.members_list.presentation.components.components.MembersList
import com.markettwits.sportsouce.profile.members.members_list.presentation.components.components.MembersLoadingContent
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class MembersLayoutTest {
    @Test
    fun cardsKeepIdentityContactsAndNavigationInBothThemes() {
        for (dark in listOf(false, true)) {
            var selected: ProfileMember? = null
            val member = member()
            ImageComposeScene(width = 390, height = 800) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background).padding(16.dp)) {
                        MemberItemCard(item = member, onClick = { selected = it })
                    }
                }
            }.use { scene ->
                scene.settle()
                for (label in listOf("Иванов Александр", member.type, member.birthday, member.team, member.phone, member.email))
                    assertNotNull(scene.text(label), "Missing $label")
                assertNull(scene.text("Без аккаунта"), "The API does not expose account link status")
                val card = scene.nodes().single { it.config.contains(SemanticsActions.OnClick) }
                assertTrue(card.boundsInRoot.height <= 220f, "A normal participant should stay compact")
                scene.assertTextFits()
                scene.save("members-card-${if (dark) "dark" else "light"}")
                scene.tap(card)
                assertEquals(member, selected)
            }
        }
    }

    @Test
    fun longNamesAndLargeTextFitNarrowCardsAndScrollableDetails() {
        val member = member().copy(name = "Александра-Мария", surname = "Константинопольская-Рождественская",
            email = "a.very.long.participant.address@example.org", team = "Беговой клуб любителей длинных дистанций", child = true)
        for (width in listOf(320, 390, 600)) for (scale in listOf(1f, 2f)) for (dark in listOf(false, true)) {
            ImageComposeScene(width = width, height = 1400, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background).padding(16.dp)) {
                        MemberItemCard(item = member, onClick = {})
                    }
                }
            }.use { scene ->
                scene.settle(); scene.assertTextFits()
                assertNotNull(scene.text("Ребёнок"))
                val card = scene.nodes().single { it.config.contains(SemanticsActions.OnClick) }
                assertTrue(card.boundsInRoot.right <= width - 16f)
                if (width == 320 && scale == 2f) scene.save("members-large-${if (dark) "dark" else "light"}")
            }
            ImageComposeScene(width = width, height = 700, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.surface)) {
                        MemberDetailContent(item = member, isDeleteLoading = false, onClickEdit = {}, onClickDelete = {})
                    }
                }
            }.use { scene ->
                scene.settle(); scene.assertTextFits()
                for (label in listOf("${member.surname} ${member.name}", member.gender, member.birthday, member.team, member.phone, member.email))
                    assertNotNull(scene.text(label), "Details must retain the complete value: $label")
                scene.nodes().single { it.config.contains(SemanticsActions.ScrollBy) }
                    .config[SemanticsActions.ScrollBy].action!!.invoke(0f, 5000f)
                scene.settle(); scene.assertTextFits()
                assertNotNull(scene.text("Редактировать"))
                assertNotNull(scene.text("Удалить"))
            }
        }
    }

    @Test
    fun listAndEmptyStateExposeOneAddActionWithoutDelayingContent() {
        for (empty in listOf(false, true)) {
            var add = 0
            var selected = -1
            var refreshing by mutableStateOf(false)
            ImageComposeScene(width = 390, height = 800) {
                SauceTheme {
                    MembersList(items = if (empty) emptyList() else listOf(member()),
                        modifier = Modifier.fillMaxSize().background(SauceTheme.colors.background), isRefreshing = refreshing, onRefresh = {},
                        onClick = { selected = it.id }, onClickAddMember = { add++ })
                }
            }.use { scene ->
                // No delay/index-dependent effects may hide the first participant or the action.
                scene.render(time).close()
                scene.save(if (empty) "members-empty" else "members-list")
                scene.tap(scene.text("Добавить участника")!!)
                assertEquals(1, add)
                if (empty) assertNotNull(scene.text("У вас ещё нет участников")) else {
                    val card = scene.nodes().single { it.config.contains(SemanticsActions.OnClick) &&
                        it.config.getOrNull(SemanticsProperties.Text) == null && it.children.any { child ->
                            child.config.getOrNull(SemanticsProperties.Text)?.any { it.text == "Иванов Александр" } == true
                        } }
                    scene.tap(card); assertEquals(7, selected)
                    refreshing = true; scene.settle()
                    assertNotNull(scene.text("Иванов Александр"), "Refreshing must retain loaded members")
                }
                scene.assertTextFits()
            }
        }
    }

    @Test
    fun loadingSkeletonHasOneAnnouncementAndNoFakeActions() {
        ImageComposeScene(width = 390, height = 800) {
            SauceTheme { MembersLoadingContent() }
        }.use { scene ->
            scene.settle()
            assertEquals(1, scene.nodes().count {
                it.config.getOrNull(SemanticsProperties.ContentDescription)?.contains("Загрузка участников") == true
            })
            assertTrue(scene.nodes().none { it.config.contains(SemanticsActions.OnClick) })
            scene.save("members-loading")
        }
    }

    @Test
    fun detailActionsKeepTheirSizeAndDisableDuplicateRequestsWhileDeleting() {
        var loading by mutableStateOf(false)
        var edits = 0
        var deletes = 0
        ImageComposeScene(width = 320, height = 500, density = Density(1f, 2f)) {
            SauceTheme {
                Box(Modifier.fillMaxSize().padding(16.dp)) {
                    MemberDetailButtons(deleteLoading = loading, onClickEdit = { edits++ }, onClickDelete = { deletes++ })
                }
            }
        }.use { scene ->
            scene.settle()
            val actions = scene.nodes().filter { it.config.contains(SemanticsActions.OnClick) }
            val bounds = actions.map { it.boundsInRoot }
            scene.tap(scene.text("Редактировать")!!); assertEquals(1, edits)
            scene.tap(scene.text("Удалить")!!); assertEquals(1, deletes)
            loading = true; scene.settle()
            val disabled = scene.nodes().filter { it.config.contains(SemanticsActions.OnClick) }
            assertEquals(bounds, disabled.map { it.boundsInRoot })
            assertTrue(disabled.all { it.config.contains(SemanticsProperties.Disabled) })
            scene.tap(scene.text("Редактировать")!!)
            scene.tap(scene.text("Удалить")!!)
            assertEquals(1, edits); assertEquals(1, deletes)
            scene.assertTextFits()
        }
    }

    private fun member() = ProfileMember(7, 42, "Александр", "Иванов", "alex@example.org", "+7 900 123-45-67",
        "Мужской", "СпортСоюз", "01.12.2005", "Партнер по команде", false)
    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(12) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.text(label: String) = nodes().firstOrNull {
        it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == label } == true
    }
    private fun ImageComposeScene.tap(node: SemanticsNode) {
        sendPointerEvent(PointerEventType.Press, node.boundsInRoot.center)
        sendPointerEvent(PointerEventType.Release, node.boundsInRoot.center)
    }
    private fun ImageComposeScene.assertTextFits() {
        for (node in nodes().filter { it.config.contains(SemanticsActions.GetTextLayoutResult) }) {
            if (node.boundsInRoot.isEmpty) continue // Offscreen details are intentionally clipped by their scroll viewport.
            val layouts = mutableListOf<TextLayoutResult>()
            node.config[SemanticsActions.GetTextLayoutResult].action!!.invoke(layouts)
            for (layout in layouts) {
                if (layout.layoutInput.overflow != TextOverflow.Ellipsis) {
                    assertTrue(layout.multiParagraph.height <= layout.size.height + 1f, "Text too tall: ${layout.layoutInput.text}")
                    for (line in 0 until layout.lineCount) assertTrue(layout.getLineRight(line) <= layout.size.width + 1f,
                        "Text too wide: ${layout.layoutInput.text}")
                }
                assertTrue(node.boundsInRoot.width + 1f >= layout.size.width, "Text clipped: ${layout.layoutInput.text}")
            }
        }
    }
    private fun ImageComposeScene.save(name: String) {
        time += 100_000_000L
        render(time).use { image -> image.encodeToData()!!.use { data ->
            File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
        } }
    }
}
