package com.markettwits.sportsouce.start.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.Density
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.sauceColors
import com.markettwits.sportsouce.profile.members.member_common.domain.*
import com.markettwits.sportsouce.start.register.domain.StartStatement
import com.markettwits.sportsouce.start.register.presentation.registration.member.components.*
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class TeamRegistrationLayoutTest {
    @Test fun namesakesAreDistinguishableAndTheSelectedTeamBadgeFitsBothThemesAndLargeText() {
        val team = emptyProfileMember.copy(name = "Дмитрий", surname = "Сергеев", birthday = "17.01.1972", gender = "Мужской",
            city = "Новосибирск", team = "СпортСоюз", email = "runner@example.test", phone = "79000000001",
            participantUserId = 1775, source = ProfileMember.Source.Team)
        val manual = team.copy(participantUserId = null, source = ProfileMember.Source.Saved)
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            var statement by mutableStateOf(base())
            var chosen: ProfileMember? = null
            ImageComposeScene(width = 320, height = 1100, density = Density(1f, scale)) {
                val colors = sauceColors(dark)
                // The app root still supplies this adapter to existing registration screens.
                SauceTheme(darkTheme = dark, materialColors = colors.material.copy(primary = colors.surface,
                    onPrimary = colors.textPrimary, secondary = colors.action, onSecondary = colors.onAction,
                    tertiary = colors.textPrimary, outline = colors.textMuted)) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                    MemberScreenContent(userNumber = 0, statement = statement, members = listOf(team, manual),
                        onValueChanged = { statement = it }, isAddToProfileEnabled = false, onClickAddToProfile = {},
                        onMemberSelected = { chosen = it; statement = memberSelectApply(it, statement).copy(age = "54") })
                    }
                }
            }.use { scene ->
                scene.settle()
                scene.nodes().first { it.config.getOrNull(SemanticsActions.OnClick)?.label == "Открыть выбор" }
                    .config[SemanticsActions.OnClick].action!!.invoke()
                scene.settle()
                val options = scene.nodes().filter { it.config.getOrNull(SemanticsProperties.Role) == Role.RadioButton }
                assertEquals(2, options.size)
                val option = options.single { it.config.getOrNull(SemanticsProperties.Text)?.any { text -> text.text.contains("(команда)") } == true }
                option.config[SemanticsActions.OnClick].action!!.invoke()
                scene.settle()
                assertEquals(team, chosen)
                assertEquals(1775, statement.participantUserId)
                val badge = scene.nodes().first { it.config.getOrNull(SemanticsProperties.Text)?.any { text -> text.text == "Из команды" } == true }
                assertTrue(badge.boundsInRoot.left >= 0 && badge.boundsInRoot.right <= 320)
                scene.render(System.nanoTime() + 2_000_000_000L).use { img ->
                    img.encodeToData()!!.use { data -> File("build/design-system/team-registration-${if (dark) "dark" else "light"}-$scale.png")
                        .apply { parentFile.mkdirs(); writeBytes(data.bytes) } }
                }
            }
        }
    }
    private fun base() = StartStatement(StartStatement.UserId.WithoutId, 491, "", "", "", "0", "", "", "", "", "", "", 0,
        cities = emptyList(), teams = emptyList(), sexList = emptyList(), members = emptyList(), paymentDisabled = false,
        yearDiscountApplied = false, distanceTitle = "Ретро кросс")
    private fun ImageComposeScene.settle() { val t = System.nanoTime(); repeat(12) { render(t + it * 100_000_000L).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.rootSemanticsNode) }
    }
}
