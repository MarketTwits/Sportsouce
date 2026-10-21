package com.markettwits.sportsouce.start

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.presentation.album.domain.AlbumPhoto
import com.markettwits.sportsouce.start.presentation.album.presentation.components.StartAlbumPhotoGrid
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class)
class AlbumSelectionGestureTest {
    @BeforeTest fun setMain() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @AfterTest fun resetMain() { Dispatchers.resetMain() }

    @Test fun separateHandleDragsContinueSelectionWhileImageSwipesStillScroll() {
        val photos = List(60) { AlbumPhoto(it, "") }
        var selection by mutableStateOf(mapOf(1 to photos[1]))
        lateinit var grid: LazyGridState
        var opened = 0
        val feedback = mutableListOf<HapticFeedbackType>()
        val haptic = object : HapticFeedback {
            override fun performHapticFeedback(hapticFeedbackType: HapticFeedbackType) { feedback += hapticFeedbackType }
        }
        ImageComposeScene(width = 390, height = 800) {
            CompositionLocalProvider(LocalHapticFeedback provides haptic) {
                SauceTheme {
                    grid = rememberLazyGridState()
                    StartAlbumPhotoGrid(List(60) { "" }, grid, onSelectImage = { index ->
                        if (selection.isEmpty()) opened++
                        else selection = if (index in selection) selection - index else selection + (index to photos[index])
                    }, selection = selection, onEvent = { intent ->
                        if (intent is StartAlbumStore.Intent.SelectPhotos) selection = intent.photos.associateBy { it.id }
                    })
                }
            }
        }.use { scene ->
            var time = System.nanoTime()
            fun settle() { repeat(8) { time += 16_000_000; scene.render(time).close() } }
            fun photo(index: Int): SemanticsNode = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }.first {
                it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty().contains("Фото ${index + 1} из 60")
            }
            fun pointer(type: PointerEventType, point: Offset) {
                time += 16_000_000
                scene.sendPointerEvent(type, point, type = PointerType.Touch, timeMillis = time / 1_000_000)
                settle()
            }
            fun handle(index: Int) = photo(index).boundsInRoot.let { Offset(it.right - 18, it.top + 18) }
            fun drag(from: Offset, to: Offset) {
                pointer(PointerEventType.Press, from)
                repeat(12) { pointer(PointerEventType.Move, from + (to - from) * ((it + 1) / 12f)) }
                pointer(PointerEventType.Release, to)
            }
            settle()
            assertTrue(feedback.isEmpty(), "Restoring selection must not vibrate")
            drag(handle(5), photo(8).boundsInRoot.center)
            assertEquals(setOf(1, 5, 6, 7, 8), selection.keys)
            settle() // A released finger and idle recomposition must not prevent a new range.
            drag(handle(11), photo(14).boundsInRoot.center)
            assertEquals(setOf(1, 5, 6, 7, 8, 11, 12, 13, 14), selection.keys)
            assertEquals(0, opened)
            assertTrue(feedback.isNotEmpty())
            assertTrue(feedback.all { it == HapticFeedbackType.TextHandleMove }, "Selection uses only light feedback")

            val beforeTap = feedback.size
            val center = photo(4).boundsInRoot.center
            pointer(PointerEventType.Press, center)
            pointer(PointerEventType.Release, center)
            assertTrue(4 in selection)
            assertEquals(beforeTap + 1, feedback.size, "A tap must toggle and vibrate exactly once")

            val beforeScroll = selection
            val feedbackBeforeScroll = feedback.size
            val initialScroll = grid.firstVisibleItemIndex to grid.firstVisibleItemScrollOffset
            drag(photo(13).boundsInRoot.center, photo(4).boundsInRoot.center)
            assertNotEquals(initialScroll, grid.firstVisibleItemIndex to grid.firstVisibleItemScrollOffset)
            assertEquals(beforeScroll, selection, "Swiping the photo body must scroll, not select")
            assertEquals(feedbackBeforeScroll, feedback.size)
        }
    }

    private fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
}
