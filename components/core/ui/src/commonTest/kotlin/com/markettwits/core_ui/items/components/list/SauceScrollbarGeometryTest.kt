package com.markettwits.core_ui.items.components.list

import kotlin.test.*

class SauceScrollbarGeometryTest {
    @Test fun thumbKeepsTheSameSizeForShortAndLongListsAndPinsAtBothEnds() {
        for (content in listOf(901f, 1500f, 9000f, 90000f)) {
            val metrics = SauceScrollbarMetrics(900f, content)
            val top = sauceScrollbarThumb(metrics, 90f, 870f, 36f, 18f)
            val middle = sauceScrollbarThumb(metrics.copy(offset = metrics.maxOffset / 2f), 90f, 870f, 36f, 18f)
            val bottom = sauceScrollbarThumb(metrics.copy(offset = metrics.maxOffset), 90f, 870f, 36f, 18f)
            assertEquals(36f, top.length)
            assertEquals(36f, middle.length)
            assertEquals(36f, bottom.length)
            assertEquals(90f, top.top)
            assertEquals(870f, bottom.top + bottom.length, .01f)
            assertEquals((top.top + bottom.top) / 2f, middle.top, .01f)
        }
    }

    @Test fun overscrollCompressesTowardsTheSameEdgeAndRestoresWithoutChangingProgress() {
        for (content in listOf(901f, 1500f, 90000f)) for (atEnd in listOf(false, true)) {
            val metrics = SauceScrollbarMetrics(900f, content)
            val base = metrics.copy(offset = if (atEnd) metrics.maxOffset else 0f)
            val resting = sauceScrollbarThumb(base, 90f, 870f, 36f, 18f)
            val pulling = sauceScrollbarThumb(base.copy(overscroll = if (atEnd) -90f else 90f), 90f, 870f, 36f, 18f)
            val stretched = sauceScrollbarThumb(base.copy(overscroll = if (atEnd) -180f else 180f), 90f, 870f, 36f, 18f)
            assertEquals(36f, resting.length)
            assertTrue(pulling.length < resting.length)
            assertEquals(18f, stretched.length)
            if (atEnd) assertEquals(870f, stretched.top + stretched.length)
            else assertEquals(90f, stretched.top)
            assertEquals(resting, sauceScrollbarThumb(base, 90f, 870f, 36f, 18f))
        }
    }

    @Test fun emptyAndShortTracksNeverProduceNegativeOrNonFiniteGeometry() {
        for (content in listOf(0f, 100f, 10000f)) for (end in listOf(0f, 10f, 100f)) {
            val thumb = sauceScrollbarThumb(SauceScrollbarMetrics(100f, content, 200f, -400f), 0f, end, 36f, 18f)
            assertTrue(thumb.top.isFinite() && thumb.length.isFinite())
            assertTrue(thumb.top >= 0f && thumb.length in 0f..end)
            assertTrue(thumb.top + thumb.length <= end + .01f)
        }
    }
}
