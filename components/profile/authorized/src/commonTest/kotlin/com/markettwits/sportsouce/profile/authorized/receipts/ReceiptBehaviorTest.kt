package com.markettwits.sportsouce.profile.authorized.receipts

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.*
import com.markettwits.core_ui.items.theme.sauceColors

@OptIn(ExperimentalCoroutinesApi::class)
class ReceiptBehaviorTest {
    @Test fun repeatedRefreshDoesNotDuplicateRequestsAndFailuresPreserveAlreadyLoadedData() = runTest {
        var calls = 0
        val first = CompletableDeferred<List<String>>()
        val controller = ReceiptLoadController(backgroundScope) {
            calls++
            if (calls == 1) first.await() else error("offline")
        }
        assertTrue(controller.state.value.loading)
        repeat(5) { controller.reload() }
        runCurrent(); assertEquals(1, calls)
        first.complete(listOf("квитанция")); runCurrent()
        assertEquals(listOf("квитанция"), controller.state.value.data)
        assertFalse(controller.state.value.loading)
        controller.reload(); runCurrent()
        assertEquals(2, calls)
        assertNotNull(controller.state.value.error)
        assertEquals(listOf("квитанция"), controller.state.value.data)
        assertFalse(controller.state.value.loading)
        controller.onDestroy()
    }

    @Test fun initialErrorCanBeRetriedAndDestroyCancelsPendingDocumentRequest() = runTest {
        var calls = 0
        val document = CompletableDeferred<String>()
        val controller = ReceiptLoadController(backgroundScope) {
            if (++calls == 1) error("offline") else document.await()
        }
        runCurrent()
        assertNotNull(controller.state.value.error); assertNull(controller.state.value.data)
        controller.reload(); runCurrent()
        assertNull(controller.state.value.error); assertTrue(controller.state.value.loading)
        controller.onDestroy(); document.complete("<html>квитанция</html>"); runCurrent()
        assertNull(controller.state.value.data)
    }

    @Test fun currencyIsNotDividedBy100AndDoesNotUseFloatingPointRounding() {
        assertEquals("1 020 ₽", receiptAmount("1020.00"))
        assertEquals("3 901,50 ₽", receiptAmount("3901.50"))
        assertEquals("0 ₽", receiptAmount("0.00"))
        assertEquals("12,05 ₽", receiptAmount("12.05"))
        assertEquals("—", receiptAmount(null)); assertEquals("—", receiptAmount("NaN"))
    }

    @Test fun responsiveDocumentPreservesServerReceiptAndOverridesDesktopWidth() {
        val html = "<html><head><style>.receipt{width:620px}</style></head><body><main class='receipt'>ИНН 1234567890; ИТОГ: 3901.50; Заказ: order-42</main></body></html>"
        for (dark in listOf(false, true)) {
            val colors = receiptDocumentColors(sauceColors(dark), dark)
            val display = receiptDisplayHtml(html, colors)
            assertContains(display, "ИНН 1234567890; ИТОГ: 3901.50; Заказ: order-42")
            assertContains(display, "name=\"viewport\"")
            assertContains(display, "width:100%!important")
            assertTrue(display.indexOf("width:100%!important") > display.indexOf("width:620px"))
            assertContains(display, if (dark) "color-scheme:dark" else "color-scheme:light")
            assertContains(display, if (dark) "background:#111722!important" else "background:#ffffff!important")
            assertContains(display, "@media print{")
            assertContains(display.substringAfter("@media print{"), "background:#fff!important;color:#111!important")
            assertContains(receiptDisplayHtml("<main>ИТОГ: 0.00</main>", colors), "<body><main>ИТОГ: 0.00</main></body>")
        }
    }
}
