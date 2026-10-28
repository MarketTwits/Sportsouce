package com.markettwits.sportsouce.auth.flow

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.auth.flow.internal.common.AuthAlreadySomeActionBox
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class AuthFooterInsetsTest {
    @Test fun footerClearsSystemNavigationAndDoesNotRepeatInsetsConsumedByTheAppBar() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) for (parentBottom in listOf(0, 24, 80)) {
            var navigationBottom by mutableIntStateOf(0)
            var footerBounds = Rect.Zero
            var clicks = 0
            ImageComposeScene(width = 390, height = 1000, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    val parentPadding = PaddingValues(bottom = parentBottom.dp)
                    Box(Modifier.fillMaxSize().padding(parentPadding).consumeWindowInsets(parentPadding)) {
                        AuthAlreadySomeActionBox(
                            modifier = Modifier.align(Alignment.BottomCenter).onGloballyPositioned { footerBounds = it.boundsInRoot() },
                            onClick = { clicks++ }, actionText = "Зарегистрироваться", descriptionText = "Ещё нет аккаунт ?",
                            windowInsets = WindowInsets(bottom = navigationBottom.dp),
                        )
                    }
                }
            }.use { scene ->
                var time = System.nanoTime()
                fun settle() { repeat(8) { time += 100_000_000L; scene.render(time).close() } }
                fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
                fun action() = scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }.single {
                    it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { text -> text.text == "Зарегистрироваться" }
                }
                settle()
                val baseHeight = footerBounds.height
                val baseAction = action().boundsInRoot
                navigationBottom = 24
                settle()
                assertEquals(1000f - parentBottom, footerBounds.bottom, .5f)
                if (parentBottom >= 24) {
                    assertEquals(baseHeight, footerBounds.height, .5f, "An existing bottom bar must not add a second system inset")
                    assertEquals(baseAction, action().boundsInRoot)
                } else {
                    assertEquals(baseHeight + 24f, footerBounds.height, .5f)
                    assertEquals(baseAction.bottom - 24f, action().boundsInRoot.bottom, .5f,
                        "Without an app bar, move the footer action above navigation")
                }
                assertTrue(action().boundsInRoot.bottom <= 976f)
                var target = action()
                while (!target.config.contains(SemanticsActions.OnClick)) target = target.parent!!
                target.config[SemanticsActions.OnClick].action!!.invoke()
                assertEquals(1, clicks)
                navigationBottom = 0
                settle()
                assertEquals(baseHeight, footerBounds.height, .5f, "Changing navigation mode must remove the obsolete inset")
                assertEquals(baseAction, action().boundsInRoot)
            }
        }
    }
}
