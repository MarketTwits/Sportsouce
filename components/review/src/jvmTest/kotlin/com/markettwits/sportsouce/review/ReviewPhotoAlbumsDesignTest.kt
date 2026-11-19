package com.markettwits.sportsouce.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.review.review.domain.ReviewPhotoAlbum
import com.markettwits.sportsouce.review.review.presentation.components.albums.ReviewPhotoAlbums
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class ReviewPhotoAlbumsDesignTest {
    private val albums = listOf(
        ReviewPhotoAlbum(27, "Юрманка трейл", "Фото", "17 октября 2026", 448, ""),
        ReviewPhotoAlbum(16, "Страшно добрый забег с длинным названием", "Фото", "26 октября 2025", 1250, ""))

    @Test fun coversAndSkeletonHaveSameHeightAndCardOpensItsAlbum() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) for (width in listOf(320, 800)) {
            var loading by mutableStateOf(true)
            var failed by mutableStateOf(false)
            val opened = mutableListOf<Int>()
            var all = 0
            var retries = 0
            ImageComposeScene(width, 1200, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Column(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        ReviewPhotoAlbums(if (loading || failed) emptyList() else albums, loading, failed,
                            { opened += it }, { all++ }, { retries++ })
                        androidx.compose.material3.Text("Следующий раздел")
                    }
                }
            }.use { scene ->
                scene.settle()
                assertTrue(scene.nodes().any { it.config.getOrNull(SemanticsProperties.ContentDescription)?.contains("Загрузка фотоальбомов") == true })
                val next = scene.text("Следующий раздел").boundsInRoot.top
                loading = false; scene.settle()
                assertEquals(next, scene.text("Следующий раздел").boundsInRoot.top, 1f)
                val title = scene.text(albums[0].startName)
                assertTrue(title.size.width < width - 32, "Carousel should leave a preview of the next card")
                scene.click(albums[0].startName); assertEquals(listOf(27), opened)
                scene.click("Все альбомы"); assertEquals(1, all)
                if (width == 320) scene.save("photo-albums-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
                failed = true; scene.settle()
                scene.click("Не удалось загрузить альбомы"); assertEquals(1, retries)
                assertTrue(scene.nodes().none { it.config.contains(SemanticsProperties.ProgressBarRangeInfo) })
            }
        }
    }

    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(12) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.text(text: String) = nodes().single { it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == text } == true }
    private fun ImageComposeScene.click(text: String) {
        var node = text(text)
        while (!node.config.contains(SemanticsActions.OnClick)) node = node.parent!!
        node.config[SemanticsActions.OnClick].action!!.invoke()
    }
    private fun ImageComposeScene.save(name: String) {
        time += 100_000_000L
        render(time).use { img -> img.encodeToData()!!.use { data -> File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) } } }
    }
}
