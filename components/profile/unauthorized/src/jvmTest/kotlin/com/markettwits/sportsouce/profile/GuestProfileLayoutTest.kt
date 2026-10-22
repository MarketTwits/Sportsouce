package com.markettwits.sportsouce.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.components.navigation.LocalNavigationViewport
import com.markettwits.core_ui.items.components.navigation.NavigationViewport
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.unauthorized.presentation.components.UnAuthorizedProfileContent
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class GuestProfileLayoutTest {
    @Test fun photoInvitationAdaptsToPortraitLandscapeTabletAndLargeText() {
        for ((width, height, scale) in listOf(Triple(390, 840, 1f), Triple(800, 360, 1f),
            Triple(800, 1000, 1f), Triple(320, 560, 2f), Triple(800, 360, 2f))) for (dark in listOf(false, true)) {
            var entries = 0
            ImageComposeScene(width = width, height = height, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) { UnAuthorizedProfileContent { entries++ } }
            }.use { scene ->
                scene.settle()
                assertTextFits(scene.nodes())
                scene.save("profile-welcome-${if (dark) "dark" else "light"}-$width-$height-${(scale * 100).toInt()}")
                val scroll = scene.nodes().first { it.config.contains(SemanticsProperties.VerticalScrollAxisRange) }
                scroll.config[SemanticsActions.ScrollBy].action!!.invoke(0f, 10000f)
                scene.settle()
                val button = scene.signInButton()
                assertTrue(button.boundsInRoot.top >= 0 && button.boundsInRoot.bottom <= height)
                assertTrue(button.size.width >= 48 && button.size.height >= 48)
                button.config[SemanticsActions.OnClick].action!!.invoke()
                assertEquals(1, entries)
            }
        }
    }

    @Test fun signInRemainsAboveRootNavigationAndTheViewportOwnsItsOverlap() {
        for (dark in listOf(false, true)) {
            val viewport = NavigationViewport()
            var backdrop = Color.Transparent
            ImageComposeScene(width = 390, height = 840) {
                SauceTheme(darkTheme = dark) {
                    backdrop = SauceTheme.colors.background
                    Box(Modifier.fillMaxSize().background(backdrop).onGloballyPositioned { viewport.update(it, 96) }) {
                        CompositionLocalProvider(LocalNavigationViewport provides viewport) {
                            Box(Modifier.fillMaxSize().padding(bottom = 96.dp)) { UnAuthorizedProfileContent {} }
                        }
                        Box(Modifier.fillMaxWidth().height(96.dp).align(Alignment.BottomCenter)
                            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                            .background(SauceTheme.colors.surface))
                    }
                }
            }.use { scene ->
                scene.settle()
                val button = scene.signInButton()
                assertTrue(button.boundsInRoot.bottom <= 744, "Root navigation covers the sign-in button")
                assertTrue(button.boundsInRoot.top >= 0)
                scene.render(System.nanoTime() + 3_000_000_000L).use { frame ->
                    val pixels = frame.toComposeImageBitmap().toPixelMap()
                    for (x in listOf(0, 1, 388, 389)) for (y in 744..755) {
                        assertEquals(backdrop, pixels[x, y],
                            "Parallax overscan must not leak below the photo into rounded navigation corners")
                    }
                }
                scene.save("profile-welcome-navigation-${if (dark) "dark" else "light"}")
            }
        }
    }

    private fun assertTextFits(nodes: List<SemanticsNode>) {
        for (node in nodes) {
            val layouts = mutableListOf<TextLayoutResult>()
            node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
            for (layout in layouts) {
                assertFalse(layout.didOverflowHeight, "Clipped text: ${layout.layoutInput.text}")
                for (line in 0 until layout.lineCount) assertTrue(
                    layout.getLineRight(line) - layout.getLineLeft(line) <= layout.size.width + 1,
                    "Text exceeds its container: ${layout.layoutInput.text}")
            }
        }
    }

    private fun ImageComposeScene.signInButton(): SemanticsNode {
        var node = nodes().first { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { text -> text.text == "Войти" } }
        while (!node.config.contains(SemanticsActions.OnClick)) node = checkNotNull(node.parent)
        return node
    }
    private fun ImageComposeScene.settle() {
        repeat(16) { render(System.nanoTime() + it * 100_000_000L).close() }
    }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.save(name: String) {
        render(System.nanoTime() + 3_000_000_000L).use { img -> img.encodeToData()!!.use {
            File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(it.bytes) }
        } }
    }
}
