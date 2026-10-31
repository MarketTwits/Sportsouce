import com.markettwits.selfupdater.components.selft_update.components.isSafeReleaseNotesLink
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ReleaseNotesLinkTest {
    @Test
    fun onlyWebLinksAreOfferedToThePlatform() {
        listOf("https://github.com/MarketTwits/Sportsouce/releases", "http://example.org/path?q=1")
            .forEach { assertTrue(isSafeReleaseNotesLink(it), it) }
        listOf("javascript:alert(1)", "file:///etc/passwd", "intent://something", "https://", "https://example.org\n.evil", "https://user@example.org")
            .forEach { assertFalse(isSafeReleaseNotesLink(it), it) }
    }
}
