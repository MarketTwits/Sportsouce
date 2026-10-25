package com.markettwits.sportsouce.starts.popular

import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.Density
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.popular.presentation.component.StartsPopularComponent
import com.markettwits.sportsouce.starts.popular.presentation.screen.PopularStartsScreen
import com.markettwits.sportsouce.starts.popular.presentation.store.StartsPopularStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class PopularStartsLayoutTest {
    @Test fun firstStartAndViewCountBeginBelowTheToolbarAndKeepTheirActions() {
        val starts = List(4) { StartsListItem(it + 1, "Популярный старт $it", "", "25 октября 2026",
            StartsListItem.StatusCode(3, "Регистрация"), "", "", "Новосибирск", false, "4 км", views = 12345 - it) }
        for (width in listOf(320, 390, 800)) for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            val events = mutableListOf<StartsPopularStore.Intent>()
            val component = object : StartsPopularComponent {
                override val state = MutableStateFlow(StartsPopularStore.State(starts = starts))
                override fun obtainEvent(intent: StartsPopularStore.Intent) { events += intent }
            }
            ImageComposeScene(width, 1000, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) { PopularStartsScreen(component) }
            }.use { scene ->
                scene.settle()
                val back = scene.nodes().single { "Назад" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() }
                val info = scene.text("Старты с наибольшим количеством просмотров за последнее время")
                val views = scene.text("Просмотры : 12345")
                assertTrue(info.boundsInRoot.top >= back.boundsInRoot.bottom, "The introductory card must start below the toolbar")
                assertTrue(views.boundsInRoot.top >= back.boundsInRoot.bottom, "The first view count must be fully below the toolbar")
                assertEquals(views.size.height.toFloat(), views.boundsInRoot.height, .5f, "The view count must not be clipped")
                var card = scene.text(starts.first().name)
                while (!card.config.contains(SemanticsActions.OnClick)) card = card.parent!!
                card.config[SemanticsActions.OnClick].action!!.invoke()
                assertEquals(StartsPopularStore.Intent.OnClickStart(starts.first()), events.last())
                var backAction = back
                while (!backAction.config.contains(SemanticsActions.OnClick)) backAction = backAction.parent!!
                backAction.config[SemanticsActions.OnClick].action!!.invoke()
                assertEquals(StartsPopularStore.Intent.OnClickBack, events.last())
            }
        }
    }

    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(12) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.text(label: String) = nodes().first { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { text -> text.text == label } }
}
