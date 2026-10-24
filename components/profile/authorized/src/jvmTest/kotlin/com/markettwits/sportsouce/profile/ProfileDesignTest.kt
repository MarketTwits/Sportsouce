package com.markettwits.sportsouce.profile

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.sauceColors
import com.markettwits.sportsouce.profile.authorized.authorized.domain.UserProfile
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.component.AuthorizedProfileComponent
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable.ProfileRegistrationsBlock
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable.ProfileInfoTopBar
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.screen.AuthorizedProfileScreen
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.store.AuthorizedProfileStore
import com.markettwits.sportsouce.profile.registrations.domain.*
import com.markettwits.sportsouce.start.presentation.start.component.StartScreenInput
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class ProfileDesignTest {
    private val payments = listOf(StartOrderPaymentStatus.Success(), StartOrderPaymentStatus.Free(),
        StartOrderPaymentStatus.NotPaid(), StartOrderPaymentStatus.OnPlace(),
        StartOrderPaymentStatus.PaymentCancelled(), StartOrderPaymentStatus.WithoutStatus())
    private val registrations = payments.mapIndexed { index, payment -> StartOrderInfo(index, 300 + index,
        "Дистанция 12 км", "", "25 октября 2026", "", "", payment, emptyList(), StartOrderType.Default,
        "Осенний кросс $index", "500") }
    private val user = UserProfile(1, UserProfile.SocialNetwork("", "https://t.me/sportsauce", "", ""),
        UserProfile.Activity(registrations, 2), UserProfile.UserInfo("Александра", "Иванова", "", "", "", ""))
    private val recent = StartsListItem(315, "Просмотренный старт", "", "20 сентября 2026", StartsListItem.StatusCode(3, ""),
        "", "albatros", "Новосибирск", false, "6 км", views = 0)

    @Test
    fun initialLoadingRefreshAndErrorsKeepTheCorrectProfileStateAndActions() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            val fake = FakeProfile()
            ImageComposeScene(width = 390, height = 1800, density = Density(1f, scale)) {
                Palette(dark) { AuthorizedProfileScreen(fake) }
            }.use { scene ->
                scene.settle()
                assertNotNull(scene.label("Загрузка профиля"))
                fake.state.value = AuthorizedProfileStore.State(user = user, recentStarts = listOf(recent))
                scene.settle()
                assertNull(scene.label("Загрузка профиля"))
                for (unnecessary in listOf("Редактировать профиль", "Сохранённые старты", "История и статусы",
                    "Тренировки, расписание и предложения", "Мои старты")) assertNull(scene.text(unnecessary))
                assertNotNull(scene.text("Покупки и оплаты"))
                assertEquals(1, scene.nodes().count { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { t -> t.text == "Мои регистрации" } })
                scene.save("profile-loaded-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
                scene.clickLabel("Редактировать профиль")
                scene.clickLabel("Изменить социальные сети")
                scene.click("Мои результаты")
                scene.click("Избранное")
                scene.click("Мои заказы")
                scene.click("Оплаченные квитанции")
                scene.click("Все 6")
                scene.scrollTo("Осенний кросс 0")
                scene.click("Осенний кросс 0")
                scene.scrollTo("Клуб"); scene.click("Клуб")
                scene.scrollTo(recent.name); scene.click(recent.name)
                scene.scrollTo("Участники"); scene.click("Участники")
                scene.scrollTo("Настройки"); scene.click("Настройки")
                assertEquals(listOf(AuthorizedProfileComponent.Output.EditProfile, AuthorizedProfileComponent.Output.SocialNetwork,
                    AuthorizedProfileComponent.Output.MyResults, AuthorizedProfileComponent.Output.Favorites,
                    AuthorizedProfileComponent.Output.UserOrders, AuthorizedProfileComponent.Output.PaidReceipts, AuthorizedProfileComponent.Output.AllRegistries,
                    AuthorizedProfileComponent.Output.Start(StartScreenInput.Id(300)), AuthorizedProfileComponent.Output.Club,
                    AuthorizedProfileComponent.Output.Start(StartScreenInput.Item(recent)), AuthorizedProfileComponent.Output.Members,
                    AuthorizedProfileComponent.Output.Settings), fake.outputs)
                fake.state.value = fake.state.value.copy(isLoading = true)
                scene.settle()
                assertNull(scene.label("Загрузка профиля"), "Cached refresh must keep the existing profile")
                assertNotNull(scene.text("Александра"))
                fake.state.value = fake.state.value.copy(isLoading = false, error = SauceError.Connection(Exception("offline")))
                scene.settle()
                scene.scrollTo("Повторить попытку")
                assertNotNull(scene.text("Александра"))
                scene.click("Повторить попытку")
                assertEquals<List<AuthorizedProfileStore.Intent>>(listOf(AuthorizedProfileStore.Intent.Retry), fake.events)
                scene.save("profile-refresh-error-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
                fake.state.value = AuthorizedProfileStore.State(error = SauceError.Connection(Exception("offline")))
                scene.settle()
                assertNull(scene.text("Александра"), "First-load failure must not show a fake profile")
                assertNotNull(scene.text("Что то пошло не так :("))
                fake.state.value = AuthorizedProfileStore.State(user = user.copy(
                    activity = UserProfile.Activity(emptyList(), 0), userInfo = user.userInfo.copy(name = ""),
                    socialNetwork = UserProfile.SocialNetwork("", "", "", "")))
                scene.settle()
                assertNotNull(scene.text("Мой профиль"))
                assertNull(scene.label("Изменить социальные сети"), "Empty social links should not leave an isolated add tile")
                scene.click("Все")
                assertEquals(AuthorizedProfileComponent.Output.AllRegistries, fake.outputs.last())
                assertNotNull(scene.text("Регистраций пока нет"))
                assertNull(scene.text("Все 6"))
                assertNull(scene.text("Вы смотрели"))
                scene.save("profile-empty-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
            }
        }
    }

    @Test
    fun socialTargetsAndAllPaymentLabelsFitNarrowScreensAndLargeFonts() {
        for (width in listOf(320, 390)) for (scale in listOf(1f, 2f)) for (dark in listOf(false, true)) {
            val links = mutableListOf<String>()
            ImageComposeScene(width = width, height = 1600, density = Density(1f, scale)) {
                Palette(dark) { Column(Modifier.padding(16.dp)) {
                    ProfileInfoTopBar(imageUrl = "", userName = "Александра Александровна Константинопольская",
                        userPhoneNumber = "+79991234567", socialNetwork = UserProfile.SocialNetwork("insta", "telegram", "+7 999 1234567", "vk"),
                        onClickImage = {}, onClickEditProfile = {}, onSocialNetworkClick = { links += it }, onAddSocialNetwork = {})
                    ProfileRegistrationsBlock(registrations = registrations.take(3),
                        onClickRegistration = {}, onClickViewAll = {})
                    ProfileRegistrationsBlock(registrations = registrations.drop(3),
                        onClickRegistration = {}, onClickViewAll = {})
                } }
            }.use { scene ->
                scene.settle()
                for (label in listOf("Instagram", "Telegram", "ВКонтакте", "WhatsApp", "Изменить социальные сети")) {
                    val node = clickable(checkNotNull(scene.label(label)))
                    assertTrue(node.size.height >= 48 && node.size.width >= 48)
                    node.config[SemanticsActions.OnClick].action!!.invoke()
                }
                assertEquals(listOf("insta", "telegram", "vk", "https://wa.me/79991234567"), links)
                payments.forEach { assertNotNull(scene.text(it.title)) }
                scene.assertTextFits()
                scene.save("profile-summary-${if (dark) "dark" else "light"}-$width-${(scale * 100).toInt()}")
            }
        }
    }

    @Composable private fun Palette(dark: Boolean, content: @Composable () -> Unit) {
        val c = sauceColors(dark)
        SauceTheme(darkTheme = dark, materialColors = c.material.copy(primary = c.surface, secondary = c.action,
            tertiary = c.textPrimary, primaryContainer = c.surfaceRaised, outline = c.textMuted), content = content)
    }
    private class FakeProfile : AuthorizedProfileComponent {
        override val state = MutableStateFlow(AuthorizedProfileStore.State(isLoading = true))
        val outputs = mutableListOf<AuthorizedProfileComponent.Output>()
        val events = mutableListOf<AuthorizedProfileStore.Intent>()
        override fun obtainEvent(intent: AuthorizedProfileStore.Intent) { events += intent }
        override fun obtainOutput(outPut: AuthorizedProfileComponent.Output) { outputs += outPut }
    }
    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(12) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.text(s: String) = nodes().firstOrNull { n -> n.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == s } }
    private fun ImageComposeScene.label(s: String) = nodes().firstOrNull { s in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() }
    private fun clickable(n: SemanticsNode): SemanticsNode = if (n.config.contains(SemanticsActions.OnClick)) n else clickable(checkNotNull(n.parent))
    private fun ImageComposeScene.click(s: String) { clickable(checkNotNull(text(s), { "Missing $s" })).config[SemanticsActions.OnClick].action!!.invoke(); settle() }
    private fun ImageComposeScene.clickLabel(s: String) { clickable(checkNotNull(label(s))).config[SemanticsActions.OnClick].action!!.invoke(); settle() }
    private fun ImageComposeScene.scrollTo(s: String) {
        val target = checkNotNull(text(s), { "Missing $s" })
        var ancestor = target.parent
        while (ancestor != null && !ancestor.config.contains(SemanticsProperties.VerticalScrollAxisRange)) ancestor = ancestor.parent
        checkNotNull(ancestor).config[SemanticsActions.ScrollBy].action!!.invoke(0f, target.positionInRoot.y - 60f)
        settle()
    }
    private fun ImageComposeScene.assertTextFits() {
        nodes().forEach { n -> val layouts = mutableListOf<TextLayoutResult>()
            n.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
            layouts.filter { it.layoutInput.overflow != TextOverflow.Ellipsis }.forEach {
                assertFalse(it.didOverflowHeight, "Clipped vertically: ${it.layoutInput.text}")
                for (line in 0 until it.lineCount) assertTrue(it.getLineRight(line) <= it.size.width + 0.5f,
                    "Clipped horizontally: ${it.layoutInput.text}")
            }
        }
    }
    private fun ImageComposeScene.save(name: String) { time += 100_000_000L; render(time).use { img -> img.encodeToData()!!.use {
        File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(it.bytes) }
    } } }
}
