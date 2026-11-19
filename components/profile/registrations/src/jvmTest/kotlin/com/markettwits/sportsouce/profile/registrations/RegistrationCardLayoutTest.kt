package com.markettwits.sportsouce.profile.registrations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.cloud.model.registrations.ExternalRegistration
import com.markettwits.sportsouce.profile.cloud.model.registrations.RegistrationOption
import com.markettwits.sportsouce.profile.cloud.model.registrations.RegistrationOptions
import com.markettwits.sportsouce.profile.registrations.domain.*
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start.OrderStartCard
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderOverviewTab
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderMembersTab
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderPaymentTab
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore.StartPriceResult
import com.markettwits.sportsouce.profile.registrations.presentation.list.components.starts.RegistrationsStart
import com.markettwits.sportsouce.profile.registrations.presentation.management.ExternalRegistrationForm
import com.markettwits.sportsouce.profile.registrations.presentation.management.ExternalRegistrationList
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent.Intent
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent.Command
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationOptionsCheckout
import com.markettwits.sportsouce.profile.registrations.presentation.management.optionsPaymentBody
import kotlinx.serialization.json.*
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class RegistrationCardLayoutTest {
    @Test
    fun compactCardsPreserveOrderIdentityPaymentAndNavigation() {
        val paymentStates = listOf(StartOrderPaymentStatus.Success(), StartOrderPaymentStatus.NotPaid(),
            StartOrderPaymentStatus.Free(), StartOrderPaymentStatus.PaymentCancelled(),
            StartOrderPaymentStatus.OnPlace(), StartOrderPaymentStatus.WithoutStatus())
        for (dark in listOf(false, true)) for (payment in paymentStates) {
            var selected = -1
            val order = order(payment).let { order ->
                if (payment is StartOrderPaymentStatus.Success) order.copy(members = order.members.map { member ->
                    member.copy(results = listOf(StartOrderMemberResult("12", emptyMap(), "4 км", 1, 1, "Даниил Кулаков", 1, "00:24:15", "М", "", "СпортСоюз")))
                }) else order
            }
            ImageComposeScene(width = 390, height = 800) {
                SauceTheme(darkTheme = dark) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background).padding(16.dp)) { OrderStartCard(item = order, onClickStart = { selected = it }) }
                }
            }.use { scene ->
                scene.settle()
                val card = scene.nodes().single { it.config.contains(SemanticsActions.OnClick) }
                assertTrue(card.boundsInRoot.height <= 250f, "Card should stay compact: ${card.boundsInRoot}")
                for (label in listOf("№ 52803", order.startTitle, order.dateStartPreview,
                    payment.title, "Кулаков Даниил", "Ретро кросс 2020", "1 020 ₽")) {
                    assertNotNull(scene.text(label), "Missing $label")
                }
                if (payment is StartOrderPaymentStatus.Success) scene.save("registration-${if (dark) "dark" else "light"}")
                scene.assertTextFits()
                scene.sendPointerEvent(PointerEventType.Press, card.boundsInRoot.center)
                scene.sendPointerEvent(PointerEventType.Release, card.boundsInRoot.center)
                assertEquals(order.startId, selected)

            }
        }
    }

    @Test
    fun narrowCardsAndLargeTextKeepTeamStatusAndPriceReadable() {
        for (width in listOf(320, 390, 600)) for (scale in listOf(1f, 2f)) {
            val order = order(StartOrderPaymentStatus.PaymentCancelled()).let {
                it.copy(startTitle = "Лыжная гонка патрулей — Приз Николая и Октябрины Соколовых",
                    members = it.members + it.members, dateStartPreview = "9 августа — 31 декабря 2026", cost = "12 345")
            }
            ImageComposeScene(width = width, height = 1400, density = Density(1f, scale)) {
                SauceTheme {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background).padding(16.dp)) { OrderStartCard(item = order, onClickStart = {}) }
                }
            }.use { scene ->
                scene.settle(); scene.assertTextFits()
                assertNotNull(scene.text("Участники: 2"))
                assertNotNull(scene.text("12 345 ₽"))
                val card = scene.nodes().single { it.config.contains(SemanticsActions.OnClick) }
                assertTrue(card.boundsInRoot.right <= width - 16f)
                if (width == 320 && scale == 2f) scene.save("registration-large-text")
            }
        }
    }

    @Test
    fun startOverviewKeepsDistanceAndAvailableActionsReadableAtLargeTextSizes() {
        val order = order(StartOrderPaymentStatus.NotPaid()).let {
            it.copy(
                name = "Домашний ультра трейл",
                dateStartCloud = "2099-08-14T00:00:00Z",
                dateStartPreview = "14–15 августа",
                members = it.members + it.members.map { member -> member.copy(distanceName = "Трейл · 40 км") },
                paymentCode = 0,
                hasUnpaidOptions = true,
            )
        }
        for ((width, scale) in listOf(390 to 1f, 320 to 2f)) {
            var opened = false
            ImageComposeScene(width = width, height = 1700, density = Density(1f, scale)) {
                SauceTheme(darkTheme = true) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        OrderOverviewTab(
                            order = order,
                            price = StartPriceResult.Success("7 500"),
                            options = null,
                            onOpenStart = { opened = true },
                            onPay = {},
                            onUpdatePrice = {},
                            onHelp = {},
                            onChangeDistance = {},
                            onManageOptions = {},
                            onRefund = null,
                        )
                    }
                }
            }.use { scene ->
                scene.settle()
                scene.save("registration-start-${width}dp")
                scene.assertTextFits()
                for (label in listOf("Домашний ультра трейл", "14–15 августа", "Дистанции",
                    "Трейл · 40 км", "Оплатить 7 500 ₽", "Оплатить опции", "Изменить дистанцию")) {
                    assertNotNull(scene.text(label), "Missing $label at ${width}dp")
                }
                val link = assertNotNull(scene.text("Открыть страницу старта"))
                scene.sendPointerEvent(PointerEventType.Press, link.boundsInRoot.center)
                scene.sendPointerEvent(PointerEventType.Release, link.boundsInRoot.center)
                assertTrue(opened)
            }
        }
    }

    @Test
    fun partialRegistrationMakesOptionsPayableWithoutRepayingTheSlotOrAddingNewOptions() {
        val unpaid = option(315, "Беговой абонемент", 1_650_000, "unpaid")
        val data = RegistrationOptions(
            options = listOf(option(314, "Фирменный бафф", 50_000, "available"), unpaid),
            paidOptions = listOf(option(313, "Страховка", 50_000, "paid")),
            totalPrice = 1_700_000,
        )
        val registration = order(StartOrderPaymentStatus.Success(title = "Частично оплачено")).copy(
            paymentCode = 7, paidKopecks = 170_000, dateStartCloud = "2099-12-20T05:00:00Z",
        )
        var paymentOpened = false
        ImageComposeScene(width = 390, height = 900) {
            SauceTheme {
                OrderPaymentTab(registration, StartPriceResult.Free, true,
                    onPay = { fail("The slot is already paid") }, onUpdatePrice = {}, onRefund = null,
                    options = data, onManageOptions = { paymentOpened = true })
            }
        }.use { scene ->
            scene.settle()
            assertNotNull(scene.text("Уже оплачено"))
            assertNotNull(scene.text("1 700 ₽"))
            assertNull(scene.text("Оплатить опции · 17 000 ₽"))
            assertNotNull(scene.text("Оплатить опции · 16 500 ₽"))
            scene.save("registration-options-payment")
            scene.click("Оплатить опции · 16 500 ₽")
            assertTrue(paymentOpened)
        }

        var paymentBody: JsonObject? = null
        ImageComposeScene(width = 390, height = 900) {
            SauceTheme {
                Column {
                    RegistrationOptionsCheckout(data, false, { null },
                        onPay = { paymentBody = optionsPaymentBody(it) }, onPrivacy = {})
                }
            }
        }.use { scene ->
            scene.settle()
            assertNotNull(scene.text(unpaid.name))
            assertNull(scene.text("Фирменный бафф"))
            assertNull(scene.text("Страховка"))
            scene.click("Согласен с условиями оплаты и обработкой персональных данных")
            scene.settle()
            scene.save("registration-options-checkout")
            scene.click("Оплатить опции · 16 500 ₽")
            val request = assertNotNull(paymentBody)
            assertEquals("""[{"additionalFieldId":315,"optionId":null,"distanceId":502,"memberIndex":0}]""",
                request.getValue("additional_fields_for_payment").toString())
            assertEquals(JsonArray(emptyList()), request.getValue("answers"))
        }
    }

    @Test
    fun externalFormExplainsInvalidInputAndKeepsTheSubmitActionReachable() {
        var submitted: Command.SaveExternal? = null
        ImageComposeScene(width = 390, height = 660) {
            SauceTheme {
                Box(Modifier.fillMaxSize()) {
                    ExternalRegistrationForm(null, false, { submitted = it as Command.SaveExternal })
                }
            }
        }.use { scene ->
            scene.settle()
            val button = assertNotNull(scene.text("Добавить регистрацию")).boundsInRoot
            scene.click("Добавить регистрацию")
            scene.settle()
            assertEquals(button, assertNotNull(scene.text("Добавить регистрацию")).boundsInRoot)
            assertNotNull(scene.text("Введите название старта"))
            assertNotNull(scene.text("Введите дистанцию"))
            scene.setExternalField("Название старта *", "Пробный трейл")
            scene.setExternalField("Дистанция *", "10 км")
            scene.setExternalField("Дата старта *", "31.02.2027")
            scene.setExternalField("Ссылка на страницу старта *", "https://")
            scene.settle()
            assertNull(submitted)
            assertNotNull(scene.text("Введите существующую дату в формате ДД.ММ.ГГГГ"))
            scene.setExternalField("Дата старта *", "28.02.2027")
            scene.setExternalField("Ссылка на страницу старта *", "organizer.example/trail")
            scene.settle()
            scene.click("Добавить регистрацию")
            assertEquals("2027-02-28", assertNotNull(submitted).draft.body(null).getValue("startDate").jsonPrimitive.content)
        }
    }

    private fun ImageComposeScene.setExternalField(label: String, value: String) {
        val field = nodes().first { label in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() }
        assertTrue(field.config[SemanticsActions.SetText].action!!.invoke(AnnotatedString(value)))
        settle()
    }

    @Test
    fun siteRegistrationsReachPastTheFirstFiveWithOneContinuousScroll() {
        val orders = (1..12).map { id -> order(StartOrderPaymentStatus.Success()).copy(id = id, startTitle = "Старт $id") }
        val grid = LazyGridState()
        ImageComposeScene(width = 390, height = 680) {
            SauceTheme {
                RegistrationsStart(Modifier.fillMaxSize(), orders, orders, emptyList(), onClick = {}, gridState = grid)
            }
        }.use { scene ->
            scene.settle()
            assertNull(scene.text("Показать ещё"))
            grid.requestScrollToItem(9)
            scene.settle()
            assertNotNull(scene.text("Старт 9"))
            assertTrue(grid.firstVisibleItemIndex > 5)
        }
    }

    @Test
    fun externalRegistrationsRequestTheNextPageNearTheEndAndRetryOnlyOnTapAfterAnError() {
        val entries = (1..10).map { id ->
            ExternalRegistration(id, "Внешний старт $id", "10 км", "2027-06-12", "organizer.example/start")
        }
        var data by mutableStateOf(RegistrationManagementComponent.State(
            external = entries, externalHasMore = true, externalNextOffset = entries.size,
        ))
        val grid = LazyGridState()
        var requests = 0
        ImageComposeScene(width = 390, height = 680) {
            SauceTheme {
                ExternalRegistrationList(data, grid) { intent ->
                    assertEquals(Intent.ExternalList(more = true), intent)
                    requests++
                    data = data.copy(externalLoading = true, externalRetryMore = true, externalError = null)
                }
            }
        }.use { scene ->
            scene.settle()
            assertEquals(0, requests)
            grid.requestScrollToItem(9)
            scene.settle()
            assertEquals(1, requests)
            assertNull(scene.text("Показать ещё"))
            data = data.copy(externalLoading = false, externalError = "Нет соединения")
            grid.requestScrollToItem(entries.size + 1)
            scene.settle()
            assertEquals(1, requests)
            assertNotNull(scene.text(entries.last().startName))
            scene.click("Повторить")
            scene.settle()
            assertEquals(2, requests)
            data = data.copy(external = entries + entries.last().copy(id = 11, startName = "Внешний старт 11"),
                externalLoading = false, externalHasMore = false)
            grid.requestScrollToItem(entries.size + 1)
            scene.settle()
            assertNotNull(scene.text("Внешний старт 11"))
            assertEquals(2, requests)
        }
    }

    private fun option(id: Int, title: String, price: Int, status: String) =
        RegistrationOption(id, null, 502, 0, 55970, null, title, price, status, "Чекбокс",
            RegistrationOption.Field(title))

    private fun ImageComposeScene.click(label: String) {
        val node = assertNotNull(text(label), "Missing $label")
        sendPointerEvent(PointerEventType.Press, node.boundsInRoot.center)
        sendPointerEvent(PointerEventType.Release, node.boundsInRoot.center)
    }

    @Test
    fun orderMemberAndPaymentSectionsKeepServerDetailsAndActionsReadable() {
        val registration = order(StartOrderPaymentStatus.NotPaid()).copy(
            dateStartCloud = "2099-08-14T00:00:00Z",
            paymentCode = 0,
            cost = "7 500",
            costWithoutDiscount = "8 000",
            additionalFieldsCost = "500",
            promo = "СТАРТ",
            members = listOf(
                StartOrderMember("Анна", "Тестова", "Легкоатлетический клуб", "2009 г.р. и старше",
                    "Ретро кросс 2020", "Женский", "Личный", emptyList(), id = 17, birthday = "2005-01-14")
            ),
        )
        val error = "Платёж недоступен. Обратитесь к организатору."
        for ((width, scale) in listOf(390 to 1f, 320 to 2f)) {
            ImageComposeScene(width = width, height = 2100, density = Density(1f, scale)) {
                SauceTheme(darkTheme = true) {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        OrderPaymentTab(registration, StartPriceResult.Failed(error), true, {}, {}, null)
                    }
                }
            }.use { scene ->
                scene.settle()
                scene.assertTextFits()
                for (label in listOf(error, "Пересчитать стоимость", "7 500 ₽", "Скидка", "СТАРТ")) {
                    assertNotNull(scene.text(label))
                }
                if (scale == 1f) scene.save("registration-details-payment")
            }
            ImageComposeScene(width = width, height = 1500, density = Density(1f, scale)) {
                SauceTheme {
                    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
                        OrderMembersTab(registration, null, {}, {}, "Нет доступа к истории передач.", {}, null)
                    }
                }
            }.use { scene ->
                scene.settle()
                scene.assertTextFits()
                for (label in listOf("Тестова Анна", "Дистанция", "Группа", "Исправить опечатку", "Повторить проверку")) {
                    assertNotNull(scene.text(label))
                }
                if (scale == 1f) scene.save("registration-details-members")
            }
        }
    }

    private fun order(payment: StartOrderPaymentStatus) = StartOrderInfo(
        id = 52803, startId = 326, name = "", image = "", dateStartPreview = "9 августа 2026", dateStartCloud = "",
        promo = "", payment = payment,
        members = listOf(StartOrderMember("Даниил", "Кулаков", "СпортСоюз", "Общая", "Ретро кросс 2020", "М", "", emptyList())),
        orderType = StartOrderType.Default, startTitle = "Онлайн забеги — ретро кросс", cost = "1 020",
    )
    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(15) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(node: SemanticsNode): List<SemanticsNode> = listOf(node) + node.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.text(label: String) = nodes().firstOrNull {
        it.config.getOrNull(SemanticsProperties.Text)?.any { t -> t.text.replace('\u00a0', ' ') == label } == true
    }
    private fun ImageComposeScene.assertTextFits() {
        for (node in nodes().filter { it.config.contains(SemanticsActions.GetTextLayoutResult) }) {
            val layouts = mutableListOf<TextLayoutResult>()
            node.config[SemanticsActions.GetTextLayoutResult].action!!.invoke(layouts)
            for (layout in layouts) {
                if (layout.layoutInput.overflow != TextOverflow.Ellipsis) {
                    assertTrue(layout.multiParagraph.height <= layout.size.height + 1f, "Text too tall: ${layout.layoutInput.text}")
                    for (line in 0 until layout.lineCount) assertTrue(layout.getLineRight(line) <= layout.size.width + 1f,
                        "Text too wide: ${layout.layoutInput.text}")
                }
                assertTrue(node.boundsInRoot.width + 1f >= layout.size.width, "Text clipped: ${layout.layoutInput.text}")
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
