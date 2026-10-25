package com.markettwits.sportsouce.start

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Timeline
import com.markettwits.sportsouce.bottom_bar.component.component.BottomBarComponent
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarVisibilityListener
import com.markettwits.sportsouce.bottom_bar.components.BottomBarScaffold
import com.markettwits.sportsouce.bottom_bar.model.BottomBarConfiguration
import com.markettwits.sportsouce.bottom_bar.model.BottomBarState
import com.markettwits.sportsouce.bottom_bar.model.BottomNavigationItem
import com.markettwits.sportsouce.start.presentation.start.component.StartScreenComponent
import com.markettwits.sportsouce.start.presentation.start.screen.StartScreen
import com.markettwits.sportsouce.start.support.presentation.component.StartSupportComponent
import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStore
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.*
import androidx.compose.ui.layout.onGloballyPositioned
import com.markettwits.core_ui.items.components.navigation.LocalNavigationViewport
import com.markettwits.core_ui.items.components.navigation.NavigationViewport
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.presentation.start.components.StartDetailsBody
import com.markettwits.sportsouce.start.presentation.start.store.StartScreenStore
import com.markettwits.sportsouce.start.presentation.start.store.StartScreenStoreFactory
import com.markettwits.sportsouce.start.presentation.start.component.StartScreenInput
import com.markettwits.sportsouce.start.domain.StartRefundRules
import com.markettwits.sportsouce.start.domain.StartRepository
import com.markettwits.sportsouce.start.domain.mapper.StartItemToStartsListItemMapper
import com.markettwits.IntentAction
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import java.lang.reflect.Proxy
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult
import com.markettwits.sportsouce.start.presentation.start.component.StartFavoriteState
import com.markettwits.sportsouce.start.presentation.start.screen.StartScreenContent
import java.io.File
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import androidx.compose.ui.graphics.toArgb
import com.markettwits.core_ui.items.theme.sauceColors
import kotlin.math.abs
import kotlin.test.*

/** Checks the real details screen viewport, rather than isolated sample cards. */
@OptIn(ExperimentalComposeUiApi::class)
class StartDetailsScreenTest {
    private val members = listOf(StartMembersUi.Single(1, "Анна", "Иванова", "Трейл 12 км", "", "", ""))
    private val start = StartItem(
        id = 315, title = "Кросс трейл Альбатрос", startPlace = "Новосибирск, комплекс «Альбатрос»", slug = "albatros", image = "",
        startMembersUi = members, kindOfSports = emptyList(), sponsors = emptyList(), startSeries = StartItem.StartSeries.Empty,
        startStatus = StartItem.StartStatus(3, "Регистрация открыта"), startData = "", startAlbum = emptyList(), startTime = "20 сентября 2026",
        description = "<p>Осенний трейл для участников с разным опытом.</p>".repeat(15),
        paymentDisabled = false, regLink = "", regOnSite = true, reviewState = StartItem.ReviewState.NoReviews,
        distanceInfoNew = listOf(DistinctDistance(null, null, null, null, format = null, id = 1, infiniteSlots = true,
            name = "Трейл 12 км", openSlots = null, orderNumber = 1, slots = null, stages = emptyList(), staticPrice = null)),
        conditionDetails = listOf(StartItem.ConditionDetail.Regulation("<p>Регламент</p>")), distanceMapNew = emptyList(), paymentType = "",
        organizers = emptyList(), membersResults = listOf(MemberResult("42", emptyMap(), "Трейл 12 км", "Ж18", 1, "Анна Иванова", 1,
            "00:50:00", "Ж", "", 315, "")), conditionFile = StartItem.ConditionFile.Empty,
        commentsRemote = StartItem.Comments(315, emptyList()), result = emptyList(), usefulLinks = emptyList(),
        startTimes = StartItem.StartTimes("", "", "", ""),
    )

    @Test
    fun primaryFactsAndActionsPrecedeLongDescriptionAndFooterNeverCoversTheLastContent() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            ImageComposeScene(width = 390, height = 844, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) { Details(start) }
            }.use { scene ->
                scene.settle(); scene.assertTextFits()
                val title = scene.text(start.title)
                val date = scene.text(start.startTime)
                val footer = scene.action("Зарегистрироваться")
                assertTrue(title.boundsInRoot.top >= 64f)
                if (scale == 1f) assertTrue(date.boundsInRoot.bottom < footer.boundsInRoot.top, "Date should fit on the first screen")
                val description = scene.text("О старте")
                for (label in listOf("Участники", "Результаты", "Дистанции", "Регламент")) {
                    assertTrue(scene.text(label).positionInRoot.y < description.positionInRoot.y, "$label is buried below the description")
                }
                val initialFooterTop = footer.boundsInRoot.top
                scene.save("start-screen-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
                scene.scrollToEnd(); scene.assertTextFits()
                val last = scene.text("COMMENTS_END")
                assertTrue(last.boundsInRoot.height > 0f && last.boundsInRoot.bottom <= scene.action("Зарегистрироваться").boundsInRoot.top,
                    "Last content must be reachable above registration")
                assertEquals(initialFooterTop, scene.action("Зарегистрироваться").boundsInRoot.top)
                scene.save("start-screen-end-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
            }
        }
    }

    @Test
    fun actualDetailsScreenFeedsItsPosterIntoRootGlassAndKeepsRegistrationSafe() {
        val tabs = listOf(
            Triple("Старты", Icons.Default.Flag, BottomBarConfiguration.Home),
            Triple("Обзор", Icons.Default.Timeline, BottomBarConfiguration.Review),
            Triple("Профиль", Icons.Default.AccountCircle, BottomBarConfiguration.Profile),
        ).map { (title, icon, destination) -> BottomNavigationItem(title, icon, icon, false, bottomBarConfiguration = destination) }
        val bottom = object : BottomBarComponent {
            override val state = MutableStateFlow(BottomBarState(true, BottomBarConfiguration.Home, true, tabs))
            override val listener = object : BottomBarVisibilityListener {
                override fun show() {}
                override fun hide() {}
            }
            override fun onClickTabBar(bottomBarConfiguration: BottomBarConfiguration) {}
        }
        val support = object : StartSupportComponent {
            override val state = MutableStateFlow(StartSupportStore.State())
            override fun obtainEvent(intent: StartSupportStore.Intent) {}
        }
        for (status in listOf(3, 6)) for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            var capture by mutableStateOf(true)
            val component = object : StartScreenComponent {
                override val start = MutableStateFlow(StartScreenStore.State(
                    startItem = this@StartDetailsScreenTest.start.copy(startStatus = StartItem.StartStatus(status, "")),
                    favoriteState = StartFavoriteState.Default(false),
                ))
                override fun obtainEvent(intent: StartScreenStore.Intent) {}
            }
            // A short real viewport puts the existing poster behind the navigation material.
            ImageComposeScene(width = 390, height = 400, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    BottomBarScaffold(bottom) {
                        CompositionLocalProvider(LocalNavigationViewport provides if (capture) LocalNavigationViewport.current else null) {
                            StartScreen(component, support)
                        }
                    }
                }
            }.use { scene ->
                scene.settle()
                fun pixels() = scene.render((time + 100_000_000L).also { time = it }).use { image ->
                    image.encodeToData()!!.use { data -> ImageIO.read(ByteArrayInputStream(data.bytes)) }
                }
                val navTop = scene.nodes().first { it.config.contains(SemanticsProperties.Selected) }.boundsInRoot.top
                val button = if (status == 3) scene.action("Зарегистрироваться").boundsInRoot else null
                if (button != null) assertTrue(button.bottom <= navTop)
                val glass = pixels()
                val name = "start-actual-glass-${if (dark) "dark" else "light"}-$status-${(scale * 100).toInt()}"
                scene.save(name)
                capture = false; scene.settle()
                val opaque = pixels()
                val y = (navTop + 20f).toInt()
                val difference = (100..150).sumOf { x -> listOf(0, 8, 16).sumOf { shift ->
                    abs(((glass.getRGB(x, y) shr shift) and 255) - ((opaque.getRGB(x, y) shr shift) and 255))
                } }
                assertTrue(difference > 50,
                    "Actual screen must feed its poster into glass: difference=$difference dark=$dark status=$status font=$scale")
                if (button != null) assertEquals(button, scene.action("Зарегистрироваться").boundsInRoot,
                    "Capturing the scroll layer must not move the pinned registration button")
            }
        }
    }

    @Test
    fun nestedDetailsScrollUnderRootNavigationWhileRegistrationStaysAboveIt() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            ImageComposeScene(width = 390, height = 844, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) {
                    val viewport = remember { NavigationViewport() }
                    Box(Modifier.fillMaxSize().onGloballyPositioned { viewport.update(it, 104) }) {
                        CompositionLocalProvider(LocalNavigationViewport provides viewport) {
                            Box(Modifier.fillMaxSize().padding(bottom = 104.dp)) { Details(start) }
                        }
                    }
                }
            }.use { scene ->
                scene.settle()
                val button = scene.action("Зарегистрироваться").boundsInRoot
                assertEquals(844f, scene.scroll().boundsInRoot.bottom)
                assertTrue(button.bottom <= 740f)
                scene.scrollToEnd()
                assertEquals(button, scene.action("Зарегистрироваться").boundsInRoot)
                assertTrue(scene.text("COMMENTS_END").boundsInRoot.bottom <= button.top)
                scene.assertTextFits()
                scene.save("start-screen-global-glass-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
            }
        }
    }

    @Test
    fun floatingRegistrationHasNoBackplateAndKeepsItsBoundsThroughoutLoadingAndFailure() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            var loading by mutableStateOf(false)
            var partial by mutableStateOf(true)
            var error by mutableStateOf<Throwable?>(null)
            ImageComposeScene(width = 390, height = 844, density = Density(1f, scale)) {
                SauceTheme(darkTheme = dark) { Details(start, loading = loading, partial = partial, error = error) }
            }.use { scene ->
                scene.settle()
                val bounds = scene.action("Зарегистрироваться").boundsInRoot
                fun assertGeometryAndBackground() {
                    repeat(6) {
                        time += 50_000_000L; scene.render(time).close()
                        assertEquals(bounds, scene.action("Зарегистрироваться").boundsInRoot,
                            "Floating button must keep its width, height and position")
                    }
                    assertTrue(scene.scroll().boundsInRoot.bottom >= bounds.bottom,
                        "Content should scroll behind the floating button, without a reserved opaque footer")
                    time += 100_000_000L
                    scene.render(time).use { image -> image.encodeToData()!!.use { data ->
                        val bitmap = ImageIO.read(ByteArrayInputStream(data.bytes))
                        assertEquals(sauceColors(dark).background.toArgb(), bitmap.getRGB(1, bounds.center.y.toInt()),
                            "No footer surface should be painted beside the button")
                    } }
                }
                assertGeometryAndBackground()
                scene.save("start-screen-floating-loading-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
                loading = true; assertGeometryAndBackground()
                loading = false; partial = false; assertGeometryAndBackground()
                loading = true; assertGeometryAndBackground()
                loading = false; error = IllegalStateException("offline"); assertGeometryAndBackground()
                error = null; assertGeometryAndBackground()
                scene.save("start-screen-floating-ready-${if (dark) "dark" else "light"}-${(scale * 100).toInt()}")
            }
        }
    }

    @Test
    fun originalPosterFadesIntoTheActualPageBackgroundInBothAppThemes() {
        for (dark in listOf(false, true)) {
            val palette = sauceColors(dark)
            // The application uses primary as a surface; it differs from the page background.
            ImageComposeScene(width = 390, height = 844) {
                SauceTheme(darkTheme = dark, materialColors = palette.material.copy(primary = palette.surface)) { Details(start) }
            }.use { scene ->
                scene.settle()
                val bottom = scene.descriptionAction("Открыть афишу старта").boundsInRoot.bottom.toInt()
                time += 100_000_000L
                scene.render(time).use { image -> image.encodeToData()!!.use { data ->
                    val bitmap = ImageIO.read(ByteArrayInputStream(data.bytes))
                    for (x in listOf(2, 20, 370, 387)) {
                        val before = bitmap.getRGB(x, bottom - 1)
                        val after = bitmap.getRGB(x, bottom)
                        for (shift in listOf(0, 8, 16)) {
                            assertTrue(abs(((before shr shift) and 255) - ((after shr shift) and 255)) <= 2,
                                "Visible seam at poster edge: dark=$dark, x=$x")
                        }
                        assertEquals(palette.background.toArgb(), after)
                    }
                } }
                scene.save("start-screen-seam-${if (dark) "dark" else "light"}")
            }
        }
    }

    @Test
    fun detailsKeepNavigationPayloadsAndFloatingActions() {
        var selected: List<StartMembersUi>? = null
        val actions = mutableListOf<String>()
        ImageComposeScene(width = 390, height = 844) {
            SauceTheme { Details(start, onMembers = { selected = it }, onAction = { actions += it }) }
        }.use { scene ->
            scene.settle()
            scene.click("Зарегистрироваться"); scene.clickDescription("Назад")
            scene.clickDescription("Поделиться стартом"); scene.clickDescription("Добавить старт в избранное")
            scene.clickDescription("Открыть афишу старта")
            assertEquals(listOf("register", "back", "share", "favorite", "image"), actions)
            scene.scrollBy(350f)
            scene.click("Участники"); assertEquals(members, selected)
            scene.click("Результаты"); assertEquals("results", actions.last())
            // Floating actions stay above the scrolling viewport, including after scrolling.
            val back = scene.descriptionAction("Назад")
            assertTrue(back.boundsInRoot.bottom <= 64f)
            val participants = scene.action("Участники")
            assertTrue(participants.boundsInRoot.top >= 64f)
        }
    }

    @Test
    fun registrationAndFavoriteRespectLoadingAndErrorAndClosedStartHasNoFooter() {
        var loading by mutableStateOf(true)
        var error by mutableStateOf<Throwable?>(null)
        var status by mutableIntStateOf(3)
        var favoriteLoading by mutableStateOf(true)
        val actions = mutableListOf<String>()
        ImageComposeScene(width = 320, height = 650, density = Density(1f, 2f)) {
            SauceTheme(darkTheme = true) { Details(start.copy(startStatus = StartItem.StartStatus(status, "")), loading = loading,
                error = error, favorite = if (favoriteLoading) StartFavoriteState.Loading() else StartFavoriteState.Default(false),
                onAction = { actions += it }) }
        }.use { scene ->
            scene.settle(); scene.assertTextFits()
            assertTrue(scene.action("Зарегистрироваться").config.contains(SemanticsProperties.Disabled))
            val favorite = scene.descriptionAction("Добавить старт в избранное")
            assertTrue(favorite.config.contains(SemanticsProperties.Disabled))
            favorite.config[SemanticsActions.OnClick].action!!.invoke(); scene.settle()
            assertTrue(actions.isEmpty())
            loading = false; favoriteLoading = false; error = IllegalStateException("test")
            scene.settle(); assertTrue(scene.action("Зарегистрироваться").config.contains(SemanticsProperties.Disabled))
            error = null; scene.settle(); assertFalse(scene.action("Зарегистрироваться").config.contains(SemanticsProperties.Disabled))
            status = 4; scene.settle(); assertFalse(scene.nodes().any { it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == "Зарегистрироваться" } == true })
            scene.scrollToEnd(); assertTrue(scene.text("COMMENTS_END").boundsInRoot.height > 0f)
        }
    }

    @Test
    fun changingStartResetsScrollWhilePartialDataCompletionPreservesIt() {
        var data by mutableStateOf(start)
        var partial by mutableStateOf(true)
        ImageComposeScene(width = 390, height = 844) { SauceTheme { Details(data, partial = partial) } }.use { scene ->
            scene.settle(); assertFalse(scene.nodes().any { it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == "Участники" } == true })
            scene.click("Подробнее")
            scene.scrollToEnd()
            val value = scene.scroll().config[SemanticsProperties.VerticalScrollAxisRange].value()
            assertTrue(value > 0)
            partial = false; scene.settle()
            assertEquals(value, scene.scroll().config[SemanticsProperties.VerticalScrollAxisRange].value())
            data = start.copy(id = 316, title = "Другой старт"); scene.settle()
            assertEquals(0f, scene.scroll().config[SemanticsProperties.VerticalScrollAxisRange].value())
            assertTrue(scene.text("Другой старт").boundsInRoot.height > 0f)
        }
    }

    @Test
    fun wideAndShortViewportsKeepTheFooterAndContentSeparate() {
        for ((width, height) in listOf(1024 to 768, 844 to 390, 320 to 650)) {
            ImageComposeScene(width = width, height = height) { SauceTheme { Details(start) } }.use { scene ->
                scene.settle(); scene.assertTextFits()
                val footer = scene.action("Зарегистрироваться")
                assertTrue(footer.boundsInRoot.bottom <= height)
                if (width == 1024) assertTrue(footer.boundsInRoot.left >= width * 0.4f)
                scene.scrollToEnd()
                assertTrue(scene.text("COMMENTS_END").boundsInRoot.bottom <= scene.action("Зарегистрироваться").boundsInRoot.top)
                scene.save("start-screen-${width}x$height")
            }
        }
    }

    @Test
    fun initialErrorAndLoadingAreExclusiveAndRetryAndBackKeepTheirActions() {
        for (dark in listOf(false, true)) {
            var loading by mutableStateOf(false)
            var ready by mutableStateOf(false)
            var retries = 0
            var backs = 0
            ImageComposeScene(width = 320, height = 650, density = Density(1f, 2f)) {
                SauceTheme(darkTheme = dark) {
                    StartDetailsBody(ready, loading, IllegalStateException("test"), { retries++ }, { backs++ }) {
                        Text("DETAILS_READY", Modifier.fillMaxWidth())
                    }
                }
            }.use { scene ->
                scene.settle(); scene.assertTextFits()
                assertTrue(scene.text("Не удалось загрузить старт").positionInRoot.y >= 64f)
                assertFalse(scene.nodes().any { it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == "DETAILS_READY" } == true })
                scene.save("start-screen-error-${if (dark) "dark" else "light"}-200")
                scene.scrollToEnd()
                for (label in listOf("Повторить", "Сообщить об ошибке")) {
                    val action = scene.action(label)
                    assertTrue(action.boundsInRoot.height >= 48f && action.boundsInRoot.bottom <= 650f)
                }
                scene.click("Повторить"); assertEquals(1, retries)
                scene.clickDescription("Назад"); assertEquals(1, backs)
                scene.save("start-screen-error-end-${if (dark) "dark" else "light"}-200")
                loading = true; scene.settle()
                assertFalse(scene.nodes().any { it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == "Не удалось загрузить старт" } == true })
                ready = true; scene.settle()
                assertEquals("DETAILS_READY", scene.text("DETAILS_READY").config[SemanticsProperties.Text].single().text)
            }
        }
    }

    @Test
    fun refreshFailureKeepsLoadedSectionsAndPlacesOneRetryNearTheOverview() {
        for (partial in listOf(false, true)) {
            var failed by mutableStateOf(true)
            ImageComposeScene(width = 390, height = 844) {
                SauceTheme {
                    StartDetailsBody(true, false, if (failed) IllegalStateException("test") else null, {}, {}) {
                        Details(start, partial = partial, error = if (failed) IllegalStateException("test") else null)
                    }
                }
            }.use { scene ->
                scene.settle(); scene.assertTextFits()
                val errorTitle = scene.text("Не удалось обновить данные")
                assertTrue(errorTitle.positionInRoot.y > scene.text(start.title).positionInRoot.y)
                assertEquals(1, scene.nodes().count { it.config.contains(SemanticsActions.OnClick) &&
                    it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == "Повторить" } == true })
                if (!partial) {
                    assertTrue(scene.text("Участники").positionInRoot.y > errorTitle.positionInRoot.y)
                    assertNotNull(scene.text("Дистанции"))
                } else assertFalse(scene.nodes().any { it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == "Участники" } == true })
                failed = false; scene.settle()
                assertFalse(scene.nodes().any { it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == "Не удалось обновить данные" } == true })
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun failedRefreshDoesNotDowngradeFullDetailsButAnInitialPreviewStaysPartial() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        var response: Result<StartItem> = Result.success(start)
        val unused = Proxy.newProxyInstance(StartRepository::class.java.classLoader, arrayOf(StartRepository::class.java)) { _, method, _ ->
            error("Unexpected repository call: ${method.name}")
        } as StartRepository
        val repository = object : StartRepository by unused {
            override suspend fun refundRules(startId: Int) = Result.success(
                StartRefundRules(startId, "", StartRefundRules.Source.Global, emptyList()))
            override suspend fun start(startId: String, relaunch: Boolean) = response
            override suspend fun startsRecommended(startId: String) = Result.success(emptyList<com.markettwits.sportsouce.starts.common.domain.StartsListItem>())
            override suspend fun isStartInFavorite(startId: String) = Result.success(false)
        }
        val intent = object : IntentAction {
            override fun openWebPage(url: String) = Unit
            override fun openPhone(phone: String) = Unit
            override fun shareImage(byteArray: ByteArray, filename: String) = Unit
            override fun saveImage(byteArray: ByteArray, filename: String) = Unit
            override fun copyToSystemBuffer(text: String) = Unit
            override fun sharePlainText(text: String) = Unit
        }
        val tracker = object : ExceptionTracker {
            override fun reportException(exception: Throwable, key: String?) = Unit
            override fun setLog(message: String) = Unit
            override fun setUserId(userId: String) = Unit
            override fun setKey(key: Pair<String, String>) = Unit
        }
        try {
            val factory = StartScreenStoreFactory(DefaultStoreFactory(), repository, tracker, intent)
            val loaded = factory.create(StartScreenInput.Id(start.id))
            try {
                advanceUntilIdle()
                assertFalse(loaded.state.isPartialData); assertEquals(start, loaded.state.startItem)
                response = Result.failure(IllegalStateException("offline"))
                loaded.accept(StartScreenStore.Intent.OnClickRetry); advanceUntilIdle()
                assertFalse(loaded.state.isPartialData)
                assertEquals(start, loaded.state.startItem)
                assertNotNull(loaded.state.error); assertFalse(loaded.state.isLoading)
                response = Result.success(start)
                loaded.accept(StartScreenStore.Intent.OnClickRetry); advanceUntilIdle()
                assertNull(loaded.state.error); assertFalse(loaded.state.isPartialData)
            } finally { loaded.dispose() }
            response = Result.failure(IllegalStateException("offline"))
            val preview = factory.create(StartScreenInput.Item(StartItemToStartsListItemMapper.map(start)))
            try {
                advanceUntilIdle()
                assertTrue(preview.state.isPartialData)
                assertNotNull(preview.state.startItem); assertNotNull(preview.state.error)
            } finally { preview.dispose() }
        } finally { Dispatchers.resetMain() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun registrationIntentWaitsForFullDataAndCannotUseStaleDataDuringRefresh() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        var response = CompletableDeferred<Result<StartItem>>()
        val unused = Proxy.newProxyInstance(StartRepository::class.java.classLoader, arrayOf(StartRepository::class.java)) { _, method, _ ->
            error("Unexpected repository call: ${method.name}")
        } as StartRepository
        val repository = object : StartRepository by unused {
            override suspend fun refundRules(startId: Int) = Result.success(
                StartRefundRules(startId, "", StartRefundRules.Source.Global, emptyList()))
            override suspend fun start(startId: String, relaunch: Boolean) = response.await()
            override suspend fun startsRecommended(startId: String) = Result.success(emptyList<com.markettwits.sportsouce.starts.common.domain.StartsListItem>())
            override suspend fun isStartInFavorite(startId: String) = Result.success(false)
        }
        val intent = object : IntentAction {
            override fun openWebPage(url: String) = error("Premature external registration: $url")
            override fun openPhone(phone: String) = Unit
            override fun shareImage(byteArray: ByteArray, filename: String) = Unit
            override fun saveImage(byteArray: ByteArray, filename: String) = Unit
            override fun copyToSystemBuffer(text: String) = Unit
            override fun sharePlainText(text: String) = Unit
        }
        val tracker = object : ExceptionTracker {
            override fun reportException(exception: Throwable, key: String?) = Unit
            override fun setLog(message: String) = Unit
            override fun setUserId(userId: String) = Unit
            override fun setKey(key: Pair<String, String>) = Unit
        }
        val store = StartScreenStoreFactory(DefaultStoreFactory(), repository, tracker, intent)
            .create(StartScreenInput.Item(StartItemToStartsListItemMapper.map(start)))
        val labels = mutableListOf<StartScreenStore.Label>()
        val collector = store.labels.onEach { labels += it }.launchIn(backgroundScope)
        try {
            assertTrue(store.state.isPartialData); assertFalse(store.state.isLoading)
            val previewEvent = store.state.event
            store.accept(StartScreenStore.Intent.OnClickRegistration)
            assertEquals(previewEvent, store.state.event, "Preview must not attempt registration before the loading coroutine starts")
            runCurrent()
            assertTrue(store.state.isPartialData); assertTrue(store.state.isLoading)
            store.accept(StartScreenStore.Intent.OnClickRegistration); runCurrent()
            assertTrue(labels.isEmpty())
            response.complete(Result.success(start)); advanceUntilIdle()
            store.accept(StartScreenStore.Intent.OnClickRegistration); runCurrent()
            assertEquals(1, labels.filterIsInstance<StartScreenStore.Label.OnClickDistanceNew>().size)
            response = CompletableDeferred()
            store.accept(StartScreenStore.Intent.OnClickRetry); runCurrent()
            assertTrue(store.state.isLoading); assertFalse(store.state.isPartialData)
            store.accept(StartScreenStore.Intent.OnClickRegistration); runCurrent()
            assertEquals(1, labels.filterIsInstance<StartScreenStore.Label.OnClickDistanceNew>().size)
            response.complete(Result.failure(IllegalStateException("offline"))); advanceUntilIdle()
            assertNotNull(store.state.error)
            store.accept(StartScreenStore.Intent.OnClickRegistration); runCurrent()
            assertEquals(1, labels.filterIsInstance<StartScreenStore.Label.OnClickDistanceNew>().size)
            response = CompletableDeferred()
            store.accept(StartScreenStore.Intent.OnClickRetry); runCurrent()
            response.complete(Result.success(start)); advanceUntilIdle()
            store.accept(StartScreenStore.Intent.OnClickRegistration); runCurrent()
            val registrations = labels.filterIsInstance<StartScreenStore.Label.OnClickDistanceNew>()
            assertEquals(2, registrations.size)
            registrations.forEach { assertEquals(start.id, it.startId) }
        } finally {
            collector.cancel(); store.dispose(); Dispatchers.resetMain()
        }
    }

    @Composable
    private fun Details(data: StartItem, loading: Boolean = false, partial: Boolean = false, error: Throwable? = null,
        favorite: StartFavoriteState = StartFavoriteState.Default(false), onMembers: (List<StartMembersUi>) -> Unit = {}, onAction: (String) -> Unit = {}) {
        StartScreenContent(data, favorite, emptyList(), emptyList(), error = error, isLoading = loading, isPartialData = partial,
            onClickRetry = { onAction("retry") }, onClickBack = { onAction("back") }, onClickRegistration = { onAction("register") },
            onClickMembers = onMembers, onClickMembersResults = { onAction("results") }, onClickImage = { onAction("image") },
            onClickFullAlbum = {}, onClickUrl = {}, onClickPhone = {}, onClickShare = { onAction("share") }, onClickRelatedStarts = {},
            onClickRecommendedStart = {}, onClickAddToFavourites = { onAction("favorite") },
            comments = { Text("COMMENTS_END", Modifier.fillMaxWidth().padding(16.dp)) }, donations = {})
    }

    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(12) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(unmerged: Boolean = false): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(if (unmerged) it.unmergedRootSemanticsNode else it.rootSemanticsNode) }
    }
    private fun ImageComposeScene.text(label: String) = nodes().first { it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == label } == true }
    private fun ImageComposeScene.action(label: String) = nodes().single { it.config.contains(SemanticsActions.OnClick) && it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text == label } == true }
    private fun ImageComposeScene.descriptionAction(label: String) = nodes().single { it.config.contains(SemanticsActions.OnClick) && it.config.getOrNull(SemanticsProperties.ContentDescription)?.contains(label) == true }
    private fun ImageComposeScene.click(label: String) { action(label).config[SemanticsActions.OnClick].action!!.invoke(); settle() }
    private fun ImageComposeScene.clickDescription(label: String) { descriptionAction(label).config[SemanticsActions.OnClick].action!!.invoke(); settle() }
    private fun ImageComposeScene.scroll() = nodes().first { it.config.contains(SemanticsProperties.VerticalScrollAxisRange) }
    private fun ImageComposeScene.scrollBy(y: Float) { scroll().config[SemanticsActions.ScrollBy].action!!.invoke(0f, y); settle() }
    private fun ImageComposeScene.scrollToEnd() = scrollBy(100000f)
    private fun ImageComposeScene.assertTextFits() {
        nodes(true).forEach { n ->
            val results = mutableListOf<TextLayoutResult>()
            n.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(results)
            results.forEach { if (it.layoutInput.maxLines == Int.MAX_VALUE) assertFalse(it.hasVisualOverflow, "Clipped: ${it.layoutInput.text}") }
        }
    }
    private fun ImageComposeScene.save(name: String) {
        time += 100_000_000L
        render(time).use { img -> img.encodeToData()!!.use { data -> File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(data.bytes) } } }
    }
}
