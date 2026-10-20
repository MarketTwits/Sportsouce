package com.markettwits.sportsouce.profile.members

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.components.ImprovedEditMemberContent
import com.markettwits.sportsouce.profile.members.member_common.domain.*
import com.markettwits.sportsouce.profile.members.member_search.*
import com.markettwits.sportsouce.profile.members.members_list.presentation.components.components.MemberTeamContent
import com.markettwits.sportsouce.profile.members.members_list.presentation.store.store.MembersListStore
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class MemberWorkflowLayoutTest {
    @Test fun searchAndRequestCardsFitBothThemesAndLargeText() {
        for (dark in listOf(false, true)) for (width in listOf(320, 390)) for (scale in listOf(1f, 2f)) {
            val candidate = MemberCandidate(1282, "Александра", "Константинопольская", 2005, "Новосибирск", true, "MEDIUM", listOf("В имени возможна опечатка", "Год рождения совпадает"))
            val state = MemberSearchState(name = "Александра", surname = "Константинопольская", birthYear = "2005", accessReady = true,
                result = MemberSearchResult(listOf(candidate), "Найдены возможные совпадения", emptyList(), false))
            ImageComposeScene(width = width, height = 850, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        MemberSearchContent(state, {}, {}, {}, {}, {}, {}, {}, {})
                    }
                }
            }.use { scene ->
                scene.settle(); scene.assertTextFits()
                scene.scroll(600f); scene.settle(); scene.assertTextFits()
                scene.save("member-search-${if (dark) "dark" else "light"}-$width-$scale")
            }
            val team = MemberIdentityTeam(incoming = listOf(MemberConnectionRequest(6, 1775, "pending", 2310, "Александр Константинопольский", "Иван Иванов")),
                outgoing = listOf(MemberConnectionRequest(5, 1282, "pending", 2310, "Иван Иванов", "Александра Константинопольская")))
            ImageComposeScene(width = width, height = 850, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        MemberTeamContent(MembersListStore.State(team = team, teamLoading = false), true, {}, {})
                    }
                }
            }.use { scene ->
                scene.settle(); scene.assertTextFits()
                scene.scroll(500f); scene.settle(); scene.assertTextFits()
                scene.save("member-requests-${if (dark) "dark" else "light"}-$width-$scale")
            }
        }
    }

    @Test fun editorUsesSharedFieldsForCoachStudentAndChildWithoutContacts() {
        for (child in listOf(false, true)) for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            val member = emptyProfileMember.copy(name = "Анна", surname = "Иванова", birthday = "01.12.2005", type = if (child) "Ученик" else "Тренер",
                gender = "Женский", team = "Лично", child = child)
            ImageComposeScene(width = 320, height = 850, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        ImprovedEditMemberContent(Modifier.fillMaxSize().sauceVerticalScroll(rememberScrollState()), {}, member, emptyList())
                    }
                }
            }.use { scene ->
                scene.settle(); scene.assertTextFits()
                scene.scroll(2000f); scene.settle(); scene.assertTextFits()
                assertEquals(!child, scene.nodes().any { it.config.getOrNull(SemanticsProperties.ContentDescription)?.contains("Email (необязательно)") == true })
                scene.save("member-editor-${if (dark) "dark" else "light"}-$child-$scale")
            }
        }
    }

    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(12) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.text(label: String) = nodes().firstOrNull {
        it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == label } == true
    }
    private fun ImageComposeScene.scroll(y: Float) {
        nodes().single { it.config.contains(SemanticsActions.ScrollBy) && it.config.contains(SemanticsProperties.VerticalScrollAxisRange) }.config[SemanticsActions.ScrollBy].action!!.invoke(0f, y)
    }
    private fun ImageComposeScene.assertTextFits() {
        for (node in nodes().filter { it.config.contains(SemanticsActions.GetTextLayoutResult) }) {
            if (node.boundsInRoot.isEmpty || node.config.contains(SemanticsProperties.EditableText)) continue
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
