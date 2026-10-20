package com.markettwits.sportsouce.club

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.club.dashboard.presentation.screen.ClubDashboardContent
import com.markettwits.sportsouce.club.dashboard.presentation.components.ClubTrainingPreview
import com.markettwits.sportsouce.club.dashboard.presentation.components.ClubTrainerPreview
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore
import com.markettwits.sportsouce.club.dashboard.domain.SubscriptionItems
import com.markettwits.sportsouce.club.schedule.screen.ScheduleContent
import com.markettwits.sportsouce.club.schedule.store.ScheduleStore
import com.markettwits.sportsouce.club.subscription.screen.SubscriptionPricingContent
import com.markettwits.sportsouce.club.subscription.store.SubscriptionPricingStore
import com.markettwits.sportsouce.club.info.presentation.components.bottomsheet.MenuBottomSheet
import com.markettwits.sportsouce.club.info.presentation.components.bottomsheet.MenuBottomSheetType
import com.markettwits.sportsouce.club.info.domain.models.*
import com.markettwits.sportsouce.club.registration.domain.WorkoutPrice
import java.io.File
import kotlinx.datetime.LocalDate
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class ClubLayoutTest {
    @Test fun carouselCardsKeepTheirHeightForShortLongAndMissingTextAtBothFontScales() {
        val trainings = listOf(
            Training(1, "Бег", "<p>Коротко</p>", ""),
            Training(2, "Функциональные тренировки для начинающих", "<p>${"Подробное описание тренировки. ".repeat(8)}</p>", ""),
            Training(3, "Плавание", "", ""),
        )
        val trainers = listOf(
            Trainer(1, "Анна", "Иванова", "", "", listOf("бег")),
            Trainer(2, "Анастасия", "Константинопольская", "", "", listOf("лыжные гонки", "функциональный тренинг", "бег")),
            Trainer(3, "Пётр", "Петров", "", "", emptyList()),
        )
        for (scale in listOf(1f, 1.5f)) for (dark in listOf(false, true)) {
            val index = mutableStateOf(0)
            var trainingSize = IntSize.Zero
            var trainerSize = IntSize.Zero
            ImageComposeScene(width = 390, height = 1800, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Column {
                        ClubTrainingPreview(trainings[index.value], Modifier.onSizeChanged { trainingSize = it }) {}
                        ClubTrainerPreview(trainers[index.value], Modifier.onSizeChanged { trainerSize = it }) {}
                    }
                }
            }.use { scene ->
                scene.settle()
                val expectedTrainingSize = trainingSize
                val expectedTrainerSize = trainerSize
                assertTrue(trainingSize.height > 0); assertTrue(trainerSize.height > 0)
                for (i in trainings.indices) {
                    index.value = i; scene.settle()
                    assertEquals(expectedTrainingSize, trainingSize, "Workout card resized at fontScale=$scale, index=$i")
                    assertEquals(expectedTrainerSize, trainerSize, "Trainer card resized at fontScale=$scale, index=$i")
                    scene.assertTextFits()
                }
                scene.save("club-preview-cards-${if (dark) "dark" else "light"}-${if (scale == 1f) "normal" else "large"}")
            }
        }
    }

    @Test fun selectedTrainerAndWorkoutOpenAtTheirServerIdsAndKeepTheSelectedRegistrationAction() {
        val trainers = (0..19).map { Trainer(100 + it * 3, "Тренер", "Номер $it", "Описание", "", listOf("бег")) }
        val trainings = (0..19).map { Training(200 + it * 5, "Тренировка $it", "Описание", "") }
        val data = ClubDashboardStore.BottomSheetData(trainers = trainers, trainings = trainings)
        val selected = mutableStateOf<MenuBottomSheetType>(MenuBottomSheetType.TrainerDetails(trainers[10].id))
        var trainer: Trainer? = null
        var training: Training? = null
        ImageComposeScene(width = 390, height = 700) {
            SauceTheme {
                MenuBottomSheet(selected.value, data, {}, { trainer = it }, { training = it })
            }
        }.use { scene ->
            scene.settle()
            assertTrue(scene.text(trainers[10].fullName())!!.boundsInRoot.top in 0f..64f)
            scene.clickRegistrationAfter(trainers[10].fullName()); assertEquals(trainers[10], trainer)
            selected.value = MenuBottomSheetType.TrainerDetails(trainers[14].id); scene.settle()
            assertTrue(scene.text(trainers[14].fullName())!!.boundsInRoot.top in 0f..64f)
            scene.clickRegistrationAfter(trainers[14].fullName()); assertEquals(trainers[14], trainer)
            selected.value = MenuBottomSheetType.TrainingDetails(trainings[10].id); scene.settle()
            assertTrue(scene.text(trainings[10].type)!!.boundsInRoot.top in 0f..64f)
            scene.clickRegistrationAfter(trainings[10].type); assertEquals(trainings[10], training)
            selected.value = MenuBottomSheetType.TrainingDetails(-1); scene.settle()
            assertTrue(scene.text(trainings.first().type)!!.boundsInRoot.top in 0f..64f)
            scene.clickRegistrationAfter(trainings.first().type); assertEquals(trainings.first(), training)
        }
    }

    @Test fun migratedClubScreensFitBothThemesAndLargeTextAndKeepTheirActions() {
        for ((width, scale) in listOf(390 to 1f, 320 to 1.5f)) for (dark in listOf(false, true)) {
            val page = mutableStateOf(0)
            val dashboard = mutableStateOf(ClubDashboardStore.State(isLoading = true))
            val dashboardEvents = mutableListOf<ClubDashboardStore.Intent>()
            val scheduleEvents = mutableListOf<ScheduleStore.Intent>()
            val pricingEvents = mutableListOf<SubscriptionPricingStore.Intent>()
            val pricing = mutableStateOf(SubscriptionPricingStore.State(subscriptions = listOf(SubscriptionItems("Бег", listOf(subscriptionFixture)))))
            val schedules = listOf(session(49, 13, "Бег"), session(47, 12, "Лыжи"),
                session(51, 5, "Плавание"), session(52, 9, "Функциональный тренинг"))
            ImageComposeScene(width = width, height = 2600, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        when (page.value) {
                            0 -> ClubDashboardContent(dashboard.value) { dashboardEvents += it }
                            1 -> ScheduleContent(ScheduleStore.State(schedules = schedules, allSchedules = schedules), { scheduleEvents += it }, today = LocalDate(2026, 10, 20))
                            else -> SubscriptionPricingContent(pricing.value) { pricingEvents += it }
                        }
                    }
                }
            }.use { scene ->
                scene.settle()
                assertNotNull(scene.label("Загрузка клуба"))
                dashboard.value = ClubDashboardStore.State(hasLoaded = true)
                scene.settle(); assertNull(scene.label("Загрузка клуба"))
                for (title in listOf("Расписание тренировок", "Абонементы и цены", "Направления тренировок", "Наши тренеры",
                    "Возможности клуба", "Как начать", "Вопросы и ответы", "Клуб в цифрах")) scene.click(title)
                assertEquals(8, dashboardEvents.size)
                scene.assertTextFits(); scene.save("club-home-${if (dark) "dark" else "light"}-$width")
                dashboard.value = dashboard.value.copy(isLoading = true)
                scene.settle(); assertNull(scene.label("Загрузка клуба"), "Refresh must keep the loaded menu")
                page.value = 1; scene.settle()
                assertEquals(setOf("Бег", "Лыжи", "Плавание", "Функциональный тренинг"),
                    scene.label("20 октября 2026")!!.config[SemanticsProperties.StateDescription].split(", ").toSet())
                scene.click("Бег")
                assertEquals(ScheduleStore.Intent.OnClickWorkoutType(13), scheduleEvents.last())
                scene.click("Записаться")
                assertEquals(ScheduleStore.Intent.OnClickScheduleItem("47", "Лыжи"), scheduleEvents.last())
                scene.clickLabel("21 октября 2026"); assertNull(scene.text("Записаться"))
                scene.click("Сегодня"); assertNotNull(scene.text("Записаться"))
                scene.click("Месяц"); assertNotNull(scene.text("Октябрь 2026")); scene.assertTextFits()
                scene.clickLabel("20 октября 2026")
                scene.click("Список"); assertNotNull(scene.text("Понедельник, 19 октября")); scene.assertTextFits()
                scene.clickLabel("Следующий период"); assertNotNull(scene.text("26 октября — 1 ноября"))
                scene.click("Сегодня"); scene.click("Неделя")
                scene.assertTextFits(); scene.save("club-schedule-${if (dark) "dark" else "light"}-$width")
                page.value = 2; scene.settle()
                scene.clickLabel("Увеличить количество")
                assertEquals(SubscriptionPricingStore.Intent.UpdateMonthCount(11, 2), pricingEvents.last())
                pricing.value = pricing.value.copy(priceCalculations = mapOf("11" to SubscriptionPricingStore.PriceCalculationState(
                    workoutPrice = WorkoutPrice(6000, 4980), monthCount = 2)))
                scene.settle(); assertNotNull(scene.text("4 980 ₽")); assertNotNull(scene.text("6 000 ₽"))
                scene.click("Записаться")
                assertEquals(SubscriptionPricingStore.Intent.OnClickSubscribe(11, "Беговой на месяц"), pricingEvents.last())
                scene.assertTextFits(); scene.save("club-subscriptions-${if (dark) "dark" else "light"}-$width")
            }
        }
    }
    @Test fun infoSheetsKeepRegistrationAndQuestionActionsWithTheSharedTheme() {
        for (dark in listOf(false, true)) {
            val tab = mutableStateOf<MenuBottomSheetType>(MenuBottomSheetType.OurTeam)
            val trainer = Trainer(1, "Анна", "Иванова", "<p>Тренер по бегу</p>", "", listOf("бег"))
            val closed = Trainer(2, "Мария", "Петрова", "Тренер", "", emptyList(), isAcceptingRequests = false)
            val training = Training(13, "Беговые тренировки", "<p>Улучшаем технику</p>", "")
            val data = ClubDashboardStore.BottomSheetData(trainers = listOf(trainer, closed), trainings = listOf(training),
                questions = listOf(Question("<p>С семи лет</p>", 1, "С какого возраста?")),
                features = listOf(ClubFeature(1, "Лыжный сервис", "<p>Подготовка лыж</p>", null)),
                statistics = listOf(Statistic(1, "140+", "Участников клуба")),
                steps = listOf(ClubFeature(2, "", "<p>Приходите знакомиться</p>", null)))
            val trainers = mutableListOf<Trainer>(); val trainings = mutableListOf<Training>(); var subscriptions = 0
            ImageComposeScene(width = 320, height = 1500, density = Density(1f, 1.5f)) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.surface)) {
                        MenuBottomSheet(tab.value, data, { subscriptions++ }, { trainers += it }, { trainings += it })
                    }
                }
            }.use { scene ->
                scene.settle(); assertNotNull(scene.text("Набор временно закрыт"))
                assertEquals(1, scene.nodes().count { n -> n.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == "Записаться" } })
                scene.click("Записаться"); assertEquals(listOf(trainer), trainers); scene.assertTextFits()
                tab.value = MenuBottomSheetType.Trainings; scene.settle(); scene.click("Записаться")
                assertEquals(listOf(training), trainings); scene.assertTextFits()
                tab.value = MenuBottomSheetType.Plan; scene.settle(); scene.click("Записаться на тренировку")
                assertEquals(1, subscriptions); scene.assertTextFits()
                tab.value = MenuBottomSheetType.ClubBonuses; scene.settle(); assertNotNull(scene.text("Лыжный сервис")); scene.assertTextFits()
                tab.value = MenuBottomSheetType.Statistics; scene.settle(); assertNotNull(scene.text("140+")); scene.assertTextFits()
                tab.value = MenuBottomSheetType.Faq; scene.settle(); assertNull(scene.text("С семи лет"))
                scene.click("С какого возраста?"); assertNotNull(scene.text("С семи лет")); scene.assertTextFits()
                scene.click("С какого возраста?"); assertNull(scene.text("С семи лет"))
            }
        }
    }

    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(8) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.text(text: String) = nodes().firstOrNull { n -> n.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == text } }
    private fun ImageComposeScene.label(text: String) = nodes().firstOrNull { text in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() }
    private fun click(node: SemanticsNode) {
        var target = node
        while (!target.config.contains(SemanticsActions.OnClick)) target = checkNotNull(target.parent)
        assertTrue(target.size.height >= 48)
        target.config[SemanticsActions.OnClick].action!!.invoke()
    }
    private fun ImageComposeScene.click(text: String) { click(checkNotNull(text(text)) { "Missing $text" }); settle() }
    private fun ImageComposeScene.clickRegistrationAfter(title: String) {
        val top = checkNotNull(text(title)).boundsInRoot.bottom
        click(nodes().filter { node -> node.boundsInRoot.top >= top &&
            node.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == "Записаться" }
        }.minBy { it.boundsInRoot.top })
        settle()
    }
    private fun ImageComposeScene.clickLabel(text: String) { click(checkNotNull(label(text))); settle() }
    private fun ImageComposeScene.assertTextFits() {
        nodes().forEach { node ->
            val results = mutableListOf<TextLayoutResult>()
            node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(results)
            results.filter { it.layoutInput.overflow != TextOverflow.Ellipsis }.forEach { layout ->
                assertFalse(layout.didOverflowHeight, "Clipped: ${layout.layoutInput.text}")
                for (line in 0 until layout.lineCount) assertTrue(layout.getLineRight(line) <= layout.size.width + .5f, "Clipped: ${layout.layoutInput.text}")
            }
        }
    }
    private fun ImageComposeScene.save(name: String) {
        time += 100_000_000L
        render(time).use { image -> image.encodeToData()!!.use { data ->
            File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) }
        } }
    }
}
