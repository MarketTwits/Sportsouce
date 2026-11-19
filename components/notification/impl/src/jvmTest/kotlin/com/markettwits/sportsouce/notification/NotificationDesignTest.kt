package com.markettwits.sportsouce.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import app.cash.paging.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.notification.impl.domain.*
import com.markettwits.sportsouce.notification.api.model.*
import com.markettwits.sportsouce.notification.impl.presentation.screen.*
import com.markettwits.sportsouce.notification.impl.domain.*
import com.markettwits.sportsouce.notification.impl.presentation.store.NotificationStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import com.markettwits.sportsouce.notification.api.store.NotificationStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class)
class NotificationDesignTest {
    @BeforeTest fun main() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @AfterTest fun resetMain() { Dispatchers.resetMain() }
    private var time = 0L
    private val item = InboxNotification(20262, createdAt = "2026-10-11T09:00:07Z", notification = NotificationContent(
        "Поделитесь впечатлениями о старте", "Спасибо за участие! Оставьте отзыв — это поможет сделать следующие старты лучше.",
        "auto_start_review", "start_completed", 313, "Парковая эстафета"))

    @Test fun inboxFitsPhonesTabletsDarkThemeAndLargeFonts() {
        for (dark in listOf(false, true)) for ((width, scale) in listOf(390 to 1f, 320 to 2f, 800 to 1f)) {
            val feed = flowOf(PagingData.from(listOf(
                item,
                item.copy(id = 1, isRead = true, notification = item.notification.copy(templateKind = "registration_open")),
                item.copy(id = 2, notification = item.notification.copy(templateKind = "start_reminder")),
            ), sourceLoadStates =
                LoadStates(LoadStateNotLoading(true), LoadStateNotLoading(true), LoadStateNotLoading(true))))
            ImageComposeScene(width, 1700, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        NotificationContent(NotificationStore.State(feed = feed), 20) {}
                    }
                }
            }.use { scene ->
                scene.settle()
                assertTrue(scene.texts().contains(item.notification.title))
                scene.checkText(width)
                if (scale == 1f && width == 390) scene.save("inbox-${if (dark) "dark" else "light"}")
            }
        }
    }

    @Test fun unreadFilterLoadsPastCompletelyFilteredPagesAndReadDoesNotRefetchThem() {
        val requests = mutableListOf<Int>()
        val repository = object : NotificationRepository {
            override val unreadCount = MutableStateFlow(1)
            override val readState = MutableStateFlow(NotificationReadState())
            override suspend fun page(offset: Int, limit: Int): NotificationPage {
                requests += offset
                return NotificationPage(41, (offset until minOf(offset + limit, 41)).map { index ->
                    item.copy(id = index, isRead = index < 40)
                })
            }
            override suspend fun refreshUnreadCount() = Unit
            override suspend fun read(item: InboxNotification) {
                readState.value = readState.value.copy(readIds = setOf(item.id))
                unreadCount.value = 0
            }
            override suspend fun readAll() = Unit
            override fun clear() = Unit
        }
        val store = NotificationStoreFactory(DefaultStoreFactory(), repository).create()
        store.accept(NotificationStore.Intent.Filter(NotificationFilter.Unread))
        try {
            ImageComposeScene(390, 800) {
                val state by store.stateFlow.collectAsState()
                SauceTheme { NotificationContent(state, state.unreadCount, store::accept) }
            }.use { scene ->
                scene.settle()
                assertTrue(40 in requests, "Filtered pages should not hide unread messages on later pages: $requests")
                assertTrue(scene.texts().contains(item.notification.title))
                val before = requests.size
                store.accept(NotificationStore.Intent.Read(item.copy(id = 40)))
                scene.settle()
                assertTrue(scene.texts().contains("Всё прочитано"))
                assertEquals(before, requests.size, "Reading should update cached pages without another request")
            }
        } finally { store.dispose() }
    }

    @Test fun cardActionsMarkTheDeliveryIdAndOpenItsRelatedStart() {
        val events = mutableListOf<NotificationStore.Intent>()
        ImageComposeScene(390, 600) {
            SauceTheme { NotificationCard(item, false, events::add) }
        }.use { scene ->
            scene.settle()
            val open = scene.nodes().first { node -> node.config.getOrNull(SemanticsProperties.Text)?.any { it.text == "Открыть старт" } == true }
            var target: SemanticsNode? = open
            while (target != null && target.config.getOrNull(SemanticsActions.OnClick) == null) target = target.parent
            assertNotNull(target).config[SemanticsActions.OnClick].action!!.invoke()
            assertEquals(NotificationStore.Intent.Read(item, openStart = true), events.single())
        }
    }

    private fun ImageComposeScene.settle() { repeat(12) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.texts() = nodes().flatMap { it.config.getOrNull(SemanticsProperties.Text).orEmpty() }.map { it.text }
    private fun ImageComposeScene.checkText(width: Int) { nodes().forEach { node ->
        val layouts = mutableListOf<TextLayoutResult>()
        node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
        layouts.forEach { layout ->
            assertFalse(layout.didOverflowHeight, layout.layoutInput.text.text)
            for (line in 0 until layout.lineCount) assertTrue(layout.getLineRight(line) <= layout.size.width + 1f, "Clipped: ${layout.layoutInput.text.text}")
        }
    } }
    private fun ImageComposeScene.save(name: String) { render(time + 100_000_000L).use { frame ->
        frame.encodeToData()!!.use { data -> File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) } }
    } }
}
