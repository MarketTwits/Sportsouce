package com.markettwits.sportsouce.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.Density
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.unauthorized.presentation.components.UnAuthorizedProfileContent
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class GuestProfileDesignTest {
    @Test
    fun guestCanReachSignInOnShortScreensAtLargeFontScaleInEitherTheme() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            var signedIn = 0
            ImageComposeScene(width = 320, height = 560, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) { Box { UnAuthorizedProfileContent { signedIn++ } } }
            }.use { scene ->
                val time = System.nanoTime()
                repeat(12) { scene.render(time + it * 100_000_000L).close() }
                fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
                fun nodes() = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
                nodes().first { it.config.contains(SemanticsProperties.VerticalScrollAxisRange) }
                    .config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10000f)
                repeat(12) { scene.render(time + 2_000_000_000L + it * 100_000_000L).close() }
                var button = nodes().first { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == "Войти" } }
                while (!button.config.contains(SemanticsActions.OnClick)) button = checkNotNull(button.parent)
                assertTrue(button.size.height >= 48)
                assertTrue(button.boundsInRoot.top >= 0 && button.boundsInRoot.bottom <= 560)
                button.config[SemanticsActions.OnClick].action!!.invoke()
                assertEquals(1, signedIn)
                scene.render(time + 4_000_000_000L).use { img -> img.encodeToData()!!.use {
                    File("build/design-system/profile-guest-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}.png")
                        .apply { parentFile.mkdirs(); writeBytes(it.bytes) }
                } }
            }
        }
    }
}
