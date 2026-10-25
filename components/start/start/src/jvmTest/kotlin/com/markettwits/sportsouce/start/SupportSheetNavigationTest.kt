package com.markettwits.sportsouce.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.IntentAction
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.sauceColors
import com.markettwits.sportsouce.start.presentation.start.components.StartSupport
import com.markettwits.sportsouce.start.support.domain.*
import com.markettwits.sportsouce.start.support.presentation.component.StartSupportComponent
import com.markettwits.sportsouce.start.support.presentation.component.StartSupportComponentBase
import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStoreFactory
import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import java.io.ByteArrayInputStream
import java.io.File
import javax.imageio.ImageIO
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class)
class SupportSheetNavigationTest {
    private enum class Page { Start, Login }
    private data class Child(val support: StartSupportComponent? = null)

    @Test fun returningFromLoginRecreatesTheSheetAndKeepsItsScrimAcrossRepeatedBackNavigation() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        try {
            for (dark in listOf(false, true)) {
                var signedIn = false
                var checks = 0
                val lifecycle = LifecycleRegistry().apply { resume() }
                val navigation = StackNavigation<Page>()
                val useCase = object : StartSupportUseCase {
                    override suspend fun checkDonation(startId: Int): Result<StartDonationCheck> {
                        checks++
                        return if (signedIn) Result.success(StartDonationCheck("none", null, null))
                            else Result.failure(SupportAuthorizationRequired())
                    }
                    override suspend fun donation(startId: Int, price: String): Result<String> = error("No donation during navigation")
                    override suspend fun payRegistration(groupId: Int): Result<String> = error("No registration payment during navigation")
                    override fun validate(price: String) = Result.success(500)
                }
                val storeFactory = StartSupportStoreFactory(DefaultStoreFactory(), useCase, object : IntentAction {
                    override fun openWebPage(url: String) = error("No external navigation during the test")
                    override fun openPhone(phone: String) = Unit
                    override fun shareImage(byteArray: ByteArray, filename: String) = Unit
                    override fun saveImage(byteArray: ByteArray, filename: String) = Unit
                    override fun copyToSystemBuffer(text: String) = Unit
                    override fun sharePlainText(text: String) = Unit
                })
                val stack = DefaultComponentContext(lifecycle).childStack(
                    source = navigation, serializer = null, initialConfiguration = Page.Start,
                    handleBackButton = true,
                    childFactory = { page, context ->
                        if (page == Page.Start) Child(StartSupportComponentBase(context, storeFactory,
                            onSignIn = { navigation.pushNew(Page.Login) }).also {
                            it.obtainEvent(StartSupportStore.Intent.ApplyStartId(326))
                        }) else Child()
                    },
                )
                try {
                    ImageComposeScene(390, 900) {
                        SauceTheme(darkTheme = dark) {
                            val active by stack.subscribeAsState()
                            Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                                Children(stack = stack, animation = stackAnimation(fade())) { child ->
                                    val support = child.instance.support
                                    if (support != null) StartSupport(Modifier.padding(16.dp), support, eventContent = {},
                                        isActive = active.active.instance === child.instance)
                                    else Column { Text("Экран входа"); SauceButton("Назад", { navigation.pop() }) }
                                }
                            }
                        }
                    }.use { scene ->
                        scene.settle()
                        scene.click("Поддержать проект")
                        assertEquals(1, checks)
                        repeat(4) { iteration ->
                            scene.assertSheetVisible("Войти", dark)
                            scene.click("Войти")
                            assertEquals(Page.Login, stack.value.active.configuration)
                            assertTrue(scene.nodes().none { "Закрыть" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() },
                                "The outgoing start must not keep a modal above login")
                            if (iteration == 3) signedIn = true
                            val previousChecks = checks
                            // Router pop also models system Back, which bypasses the auth screen callback.
                            navigation.pop()
                            scene.settle()
                            assertEquals(previousChecks + 1, checks, "Each return must check exactly once")
                            assertEquals(Page.Start, stack.value.active.configuration)
                            scene.assertSheetVisible(if (signedIn) "Поддержать" else "Войти", dark)
                        }
                        scene.save("support-auth-return-${if (dark) "dark" else "light"}")
                        val support = stack.value.active.instance.support!!
                        support.obtainEvent(StartSupportStore.Intent.OnClose); scene.settle()
                        assertTrue(scene.nodes().none { "Закрыть" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() })
                    }
                } finally { lifecycle.destroy() }
            }
        } finally { Dispatchers.resetMain() }
    }

    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(18) { time += 50_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
    }
    private fun ImageComposeScene.action(label: String) = nodes().single { node ->
        node.config.contains(SemanticsActions.OnClick) && node.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == label }
    }
    private fun ImageComposeScene.click(label: String) { action(label).config[SemanticsActions.OnClick].action!!.invoke(); settle() }
    private fun ImageComposeScene.assertSheetVisible(action: String, dark: Boolean) {
        val bounds = action(action).boundsInRoot
        assertTrue(bounds.top > 0 && bounds.bottom <= 900 && bounds.height >= 48, "Sheet action must stay inside the viewport: $bounds")
        assertEquals(1, nodes().count { "Закрыть" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() })
        time += 50_000_000L
        render(time).use { image -> image.encodeToData()!!.use { data ->
            val pixel = ImageIO.read(ByteArrayInputStream(data.bytes)).getRGB(380, 200)
            val background = sauceColors(dark).background
            val rgb = (255 shl 24) or ((background.red * 255).toInt() shl 16) or
                ((background.green * 255).toInt() shl 8) or (background.blue * 255).toInt()
            assertNotEquals(rgb, pixel, "The modal scrim must remain visible after Back")
        } }
    }
    private fun ImageComposeScene.save(name: String) {
        time += 50_000_000L
        render(time).use { image -> image.encodeToData()!!.use { data ->
            File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
        } }
    }
}
