package com.markettwits.sportsouce.start

import com.markettwits.sportsouce.start.presentation.album.presentation.components.AlbumScrollLayout
import kotlin.test.*

class AlbumScrollbarLayoutTest {
    @Test fun absolutePositionsIncludeCoverTagsPaddingAndAnIncompleteLastRow() {
        val layout = AlbumScrollLayout(listOf(400f, 56f, 40f), 1001, 3, 129f, 1f, 80f, 24f, listOf(0f))
        assertEquals(499f, layout.offset(3, 0))
        assertEquals(499f + 130f * 333, layout.offset(1003, 0))
        assertEquals(80f + 499f + 334 * 130f + 24f, layout.contentHeight)
        for (row in listOf(0, 1, 100, 333)) {
            val index = 3 + row * 3
            val offset = layout.offset(index, 47)
            assertEquals(index to 47, layout.position(offset))
        }
        assertEquals(0 to 0, layout.position(-20f))
        assertEquals(1 to 17, layout.position(418f))
    }

    @Test fun legacyAlbumStartsAtFirstPhotoAndMapsDistantRowsWithoutIterationOverPhotos() {
        val layout = AlbumScrollLayout(emptyList(), 100000, 3, 129f, 1f, 12f, 24f)
        assertEquals(0f, layout.offset(0, 0))
        assertEquals(99999 to 17, layout.position(layout.offset(99999, 17)))
    }
}
