package com.markettwits.sportsouce.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.presentation.album.presentation.components.*
import com.markettwits.sportsouce.start.presentation.start.components.*
import com.markettwits.sportsouce.starts.common.presentation.startStatusMessage
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class StartMediaDesignTest {
    private val photos = (1..7).map { StartItem.Album.Photo(it, it, "", mapOf(1 to "preview")) }
    private val first = StartItem.Album(1, 315, photos, "Фотографии с осеннего старта Альбатрос")
    private val second = StartItem.Album(2, 315, photos.take(2), "Награждение участников")
    private val empty = StartItem.Album(3, 315, emptyList(), "Пустой альбом")

    @Test
    fun albumNamesAndCountsRemainReadableInBothThemesAndAt200Percent() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            ImageComposeScene(width = 390, height = 1500, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) { Column(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                    StartStatus(organizers = emptyList(), kindOfSports = listOf(StartItem.KindOfSport(1, "Бег"), StartItem.KindOfSport(2, "Трейл")),
                        startStatus = StartItem.StartStatus(3, ""))
                    StartAlbumChoices(listOf(first, second.copy(photos = second.photos.map { it.copy(imageUrl = "file:///sportsauce-ui-test-missing.png") }), empty), onCLickFullAlbum = {})
                } }
            }.use { scene ->
                scene.settle(); scene.assertTextFits()
                for (value in listOf(first.name, second.name, "7 фото", "2 фото", startStatusMessage(3))) assertTrue(scene.texts().contains(value), "Missing $value")
                assertFalse(scene.texts().contains(empty.name))
                val status = scene.nodes().first { it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == startStatusMessage(3) } == true }
                assertTrue(status.boundsInRoot.height >= 40f, "Details status must match the indicator row height")
                val label = scene.nodes(unmerged = true).single { it.config.contains(SemanticsActions.GetTextLayoutResult) &&
                    it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == startStatusMessage(3) } == true }
                val layouts = mutableListOf<TextLayoutResult>()
                label.config[SemanticsActions.GetTextLayoutResult].action!!.invoke(layouts)
                val layout = layouts.single()
                repeat(layout.lineCount) { line ->
                    val center = label.positionInRoot.x + (layout.getLineLeft(line) + layout.getLineRight(line)) / 2f
                    assertEquals(status.boundsInRoot.center.x + 5.5f, center, 1f,
                        "The dot and label must stay together as a centered group, including wrapped lines")
                }
                if (scale == 1f) {
                    val sport = scene.nodes().single { it.config.getOrNull(SemanticsProperties.ContentDescription)?.contains("Бег") == true }
                    assertEquals(sport.boundsInRoot.center.y, status.boundsInRoot.center.y, 0.5f)
                    val lastSport = scene.nodes().single { it.config.getOrNull(SemanticsProperties.ContentDescription)?.contains("Трейл") == true }
                    assertEquals(16f, status.boundsInRoot.left, 0.5f)
                    assertEquals(374f, lastSport.boundsInRoot.right + 8f, 0.5f, "Indicators must reach the right content edge")
                    assertEquals(sport.boundsInRoot.left, status.boundsInRoot.right + 16f, 0.5f,
                        "Status should take the available width before the fixed-size icons")
                }
                scene.save("start-media-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
            }
        }
    }

    @Test
    fun morePhotosAndAlbumSelectionKeepTheirOriginalAlbumPayloadAndCloseTheSheet() {
        val opened = mutableListOf<StartItem.Album>()
        ImageComposeScene(width = 390, height = 1100) {
            SauceTheme { StartAlbums(albums = listOf(first, second, empty)) { opened += it } }
        }.use { scene ->
            scene.settle(); scene.assertTextFits()
            val row = scene.nodes().single { it.config.contains(SemanticsActions.ScrollBy) }
            row.config[SemanticsActions.ScrollBy].action!!.invoke(10000f, 0f); scene.settle()
            scene.click("Ещё фото"); assertEquals(first, opened.single())
            scene.click("Все альбомы"); scene.assertTextFits()
            assertFalse(scene.texts().contains(empty.name))
            scene.click(second.name); assertEquals(listOf(first, second), opened)
            assertFalse(scene.nodes().any { it.config.getOrNull(SemanticsProperties.ContentDescription)?.contains("Закрыть") == true })
        }
    }

    @Test
    fun photoGridKeepsIncrementalLoadingAndReturnsTheOriginalIndex() {
        var selected = -1
        ImageComposeScene(width = 320, height = 500) {
            SauceTheme { StartAlbumPhotoGrid(List(55) { "" }, rememberLazyGridState(), { selected = it }) }
        }.use { scene ->
            scene.settle()
            repeat(4) {
                scene.nodes().first { it.config.contains(SemanticsActions.ScrollBy) }
                    .config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10000f)
                scene.settle()
            }
            scene.assertTextFits()
            val last = scene.nodes().single { it.config.getOrNull(SemanticsProperties.ContentDescription)?.contains("Фото 55 из 55") == true }
            last.config[SemanticsActions.OnClick].action!!.invoke(); scene.settle()
            assertEquals(54, selected)
        }
    }

    @Test
    fun changingTheAlbumResetsGalleryScrollAndVisibleBatch() {
        var images by mutableStateOf(List(45) { "" })
        ImageComposeScene(width = 320, height = 500) {
            SauceTheme { StartAlbumScreenContent(items = images) }
        }.use { scene ->
            scene.settle()
            repeat(3) {
                scene.nodes().first { it.config.contains(SemanticsActions.ScrollBy) }
                    .config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10000f)
                scene.settle()
            }
            images = List(2) { "" }; scene.settle()
            val descriptions = scene.nodes().flatMap { it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() }
            assertTrue(descriptions.contains("Фото 1 из 2")); assertTrue(descriptions.contains("Фото 2 из 2"))
            assertFalse(descriptions.any { it == "Прокрутить вверх" })
            scene.assertTextFits()
        }
    }

    @Test
    fun emptyAlbumAndStatusDoNotWaitForDecorativeDelaysAndStayLegible() {
        for (dark in listOf(false, true)) {
            var status by mutableIntStateOf(3)
            ImageComposeScene(width = 320, height = 850, density = Density(1f, 2f)) {
                SauceTheme(darkTheme = dark) { Column(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                    StartStatus(organizers = emptyList(), kindOfSports = emptyList(), startStatus = StartItem.StartStatus(status, ""))
                    AlbumEmptyCard()
                } }
            }.use { scene ->
                scene.settle(frames = 2)
                assertTrue(scene.texts().contains("Альбом пуст")); assertTrue(scene.texts().contains(startStatusMessage(3)))
                status = 4; scene.settle(frames = 2)
                assertTrue(scene.texts().contains(startStatusMessage(4))); assertFalse(scene.texts().contains(startStatusMessage(3)))
                scene.assertTextFits(); scene.save("start-empty-album-${if (dark) "dark" else "light"}-200")
            }
        }
    }

    private var time = System.nanoTime()
    private fun ImageComposeScene.settle(frames: Int = 16) { repeat(frames) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(unmerged: Boolean = false): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(if (unmerged) it.unmergedRootSemanticsNode else it.rootSemanticsNode) }
    }
    private fun ImageComposeScene.texts() = nodes().flatMap { it.config.getOrNull(SemanticsProperties.Text).orEmpty().map { text -> text.text } }
    private fun ImageComposeScene.click(label: String) {
        val targets = nodes().filter { it.config.contains(SemanticsActions.OnClick) && it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == label } == true }
        assertEquals(1, targets.size, "Expected one action for $label")
        targets.single().config[SemanticsActions.OnClick].action!!.invoke(); settle()
    }
    private fun ImageComposeScene.assertTextFits() {
        nodes(unmerged = true).forEach { node ->
            val layouts = mutableListOf<TextLayoutResult>()
            node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
            layouts.forEach { assertFalse(it.hasVisualOverflow, "Clipped ${it.layoutInput.text}, ${it.layoutInput.constraints}, ${it.size}") }
        }
    }
    private fun ImageComposeScene.save(name: String) {
        time += 100_000_000L
        render(time).use { image -> image.encodeToData()!!.use { data ->
            File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
        } }
    }
}
