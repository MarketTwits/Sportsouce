package com.markettwits.sportsouce.root

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class)
class RootNotificationInteractionTest {
    @Test
    fun tappingFailureStripDismissesItAndFinishesTheExitAnimation() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        try {
            val root = RootComponentBase(DefaultComponentContext(lifecycle))
            root.updateNotification.obtainEvent(InAppNotificationStore.Intent.OnNewNotification(
                InAppNotification.SelfUpdateError(IllegalStateException("signature mismatch"))))
            runCurrent()
            val scene = ImageComposeScene(width = 411, height = 844) {
                SauceTheme { RootContent(root) }
            }
            try {
                var time = System.nanoTime()
                repeat(15) { time += 100_000_000L; scene.render(time).close(); runCurrent() }
                fun nodes(): List<SemanticsNode> {
                    fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
                    return scene.semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
                }
                val text = nodes().single { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any {
                    it.text == "Не удалось подготовить обновление"
                } }
                scene.sendPointerEvent(PointerEventType.Press, text.boundsInRoot.center)
                scene.sendPointerEvent(PointerEventType.Release, text.boundsInRoot.center)
                runCurrent()
                assertEquals(InAppNotificationStore.State.NotPresent, root.updateNotification.state.value)
                repeat(15) { time += 100_000_000L; scene.render(time).close(); runCurrent() }
                assertEquals(0, nodes().count { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any {
                    it.text == "Не удалось подготовить обновление"
                } })
            } finally { scene.close() }
        } finally { lifecycle.destroy(); Dispatchers.resetMain() }
    }
}
