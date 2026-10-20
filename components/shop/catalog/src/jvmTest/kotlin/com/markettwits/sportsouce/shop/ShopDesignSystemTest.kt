package com.markettwits.sportsouce.shop

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import app.cash.paging.PagingData
import app.cash.paging.LoadStates
import app.cash.paging.LoadStateNotLoading
import app.cash.paging.compose.collectAsLazyPagingItems
import com.markettwits.core_ui.items.components.commerce.*
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.sportsouce.shop.orders.presentation.components.ShopUserOrdersContent
import com.markettwits.sportsouce.shop.orders.domain.models.ShopUserOrder
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.sauceColors
import com.markettwits.sportsouce.shop.catalog.presentation.components.*
import com.markettwits.sportsouce.shop.catalog.presentation.screen.ShopCatalogScreen
import com.markettwits.sportsouce.shop.catalog.presentation.component.ShopCatalogComponent
import com.markettwits.sportsouce.shop.catalog.presentation.component.bindShopFilterBackHandler
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.create
import com.markettwits.sportsouce.shop.catalog.presentation.store.ShopCatalogStore
import com.markettwits.sportsouce.shop.cart.presentation.catalog.component.ShopCartCatalogComponent
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarVisibilityListener
import com.markettwits.sportsouce.shop.domain.model.ShopItem
import com.markettwits.sportsouce.shop.cart.domain.ShopItemCart
import com.markettwits.sportsouce.shop.cart.presentation.cart.component.ShopCartComponent
import com.markettwits.sportsouce.shop.cart.presentation.cart.screen.ShopCartScreen
import com.markettwits.sportsouce.shop.cart.presentation.cart.store.ShopCartStore
import com.markettwits.sportsouce.shop.cart.presentation.page.component.ShopCartPageComponent
import com.markettwits.sportsouce.shop.item.presentation.component.ShopItemPageComponent
import com.markettwits.sportsouce.shop.item.presentation.screen.ShopItemPageScreen
import com.markettwits.sportsouce.shop.item.presentation.store.ShopItemPageStore
import com.markettwits.sportsouce.shop.item.domain.models.ShopExtraOptions
import com.markettwits.sportsouce.shop.filter.domain.models.*
import com.markettwits.sportsouce.shop.filter.presentation.component.ShopFilterComponent
import com.markettwits.sportsouce.shop.filter.presentation.screen.ShopFilterScreen
import com.markettwits.sportsouce.shop.filter.presentation.store.ShopFilterStore
import com.markettwits.sportsouce.shop.order.presentation.component.ShopCreateOrderComponent
import com.markettwits.sportsouce.shop.order.presentation.screen.ShopCreateOrderScreen
import com.markettwits.sportsouce.shop.order.presentation.store.ShopCreateOrderStore
import com.markettwits.sportsouce.shop.order.domain.model.*
import com.markettwits.sportsouce.shop.search.presentation.components.publish.ShopSearchBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class)
class ShopDesignSystemTest {
    @BeforeTest fun installMainDispatcher() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @AfterTest fun resetMainDispatcher() { Dispatchers.resetMain() }
    private val product = ShopItem("test", "SKU-123", 2, "https://shop.sportsauce.ru/product/test",
        ShopItem.Price("12 999", "15 999", null), ShopItem.Visual(emptyList(), "<p>Куртка для тренировок в прохладную погоду.</p>",
            "Очень длинное название спортивной куртки для осенних тренировок"),
        listOf(ShopItem.Option("material", "Материал", "Полиэстер")))

    @Test fun priceAndQuantityControlsFitAndUnavailableIncreaseCannotFire() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            var increases = 0; var decreases = 0
            ImageComposeScene(width = 240, height = 500, density = Density(1f, scale)) {
                Palette(dark) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    SaucePrice("12 999", "15 999", null)
                    SauceQuantityStepper(2, { decreases++ }, { increases++ }, canIncrease = false)
                } }
            }.use { scene ->
                scene.settle()
                assertTrue(scene.nodes().none { it.config.getOrNull(SemanticsProperties.Text).orEmpty().any { text -> text.text.contains("null") } })
                assertNotNull(scene.text("12 999 ₽")); assertNotNull(scene.text("15 999 ₽"))
                val plus = scene.clickable(scene.label("Увеличить количество")!!)
                assertTrue(plus.config.contains(SemanticsProperties.Disabled))
                assertTrue(plus.size.width >= 48 && plus.size.height >= 48)
                scene.pointerClick(plus)
                assertEquals(0, increases)
                scene.clickable(scene.label("Уменьшить количество")!!).config[SemanticsActions.OnClick].action!!.invoke()
                assertEquals(1, decreases)
                scene.assertTextFits()
                scene.save("shop-controls-${if(dark) "dark" else "light"}-${(scale*100).toInt()}")
            }
        }
    }

    @Test fun catalogueCardsAndPagingFitNarrowScreensWithoutZeroColumns() {
        for (width in listOf(240, 320, 390)) for (scale in listOf(1f, 2f)) {
            var selected: ShopItem? = null
            val flow = flowOf(PagingData.from(listOf(product, product.copy(id = "second")), sourceLoadStates = LoadStates(LoadStateNotLoading(false), LoadStateNotLoading(false), LoadStateNotLoading(false))))
            ImageComposeScene(width = width, height = 1200, density = Density(1f, scale)) {
                Palette(false) { ShopItemsContent(items = flow.collectAsLazyPagingItems(), onClickItem = { selected = it }) }
            }.use { scene ->
                scene.settle()
                scene.click(product.visual.displayName)
                assertEquals(product, selected)
                assertNotNull(scene.text("12 999 ₽"))
                scene.assertTextFits()
                scene.save("shop-catalog-$width-${(scale*100).toInt()}")
            }
        }
    }

    @Test fun productLoadingVariantsAndStockKeepCartActionsSafe() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            val item = FakeItem()
            val cart = FakeProductCart()
            ImageComposeScene(width = 390, height = 1200, density = Density(1f, scale)) {
                Palette(dark) { ShopItemPageScreen(item, cart) }
            }.use { scene ->
                scene.settle(); assertNotNull(scene.label("Загрузка товара"))
                item.state.value = item.state.value.copy(isLoading = false, shopItem = product,
                    shopItemOptions = listOf(ShopExtraOptions("Размер", listOf(ShopExtraOptions.OptionValue("s", "S", true), ShopExtraOptions.OptionValue("m", "M", false)))))
                scene.settle(); assertNull(scene.label("Загрузка товара"))
                scene.click("В корзину"); assertEquals(1, cart.added)
                scene.click("M"); assertEquals<List<ShopItemPageStore.Intent>>(listOf(ShopItemPageStore.Intent.OnClickOption("m")), item.events)
                item.state.value = item.state.value.copy(isLoading = true)
                scene.settle(); assertTrue(scene.button("В корзину").config.contains(SemanticsProperties.Disabled))
                item.state.value = item.state.value.copy(isLoading = false, shopItem = product.copy(quantity = 0))
                scene.settle(); assertTrue(scene.button("Нет в наличии").config.contains(SemanticsProperties.Disabled))
                cart.state.value = ShopCartPageComponent.State.InCart("2", 2, false)
                scene.settle(); assertTrue(scene.clickable(scene.label("Увеличить количество")!!).config.contains(SemanticsProperties.Disabled))
                scene.click("В корзине"); assertEquals(1, cart.opened)
                scene.clickable(scene.label("Уменьшить количество")!!).config[SemanticsActions.OnClick].action!!.invoke()
                assertEquals(1, cart.removed)
                scene.assertTextFits()
                scene.save("shop-product-${if(dark) "dark" else "light"}-${(scale*100).toInt()}")
            }
        }
    }

    @Test fun cartKeepsCallbacksAndEmptyCartDoesNotOfferCheckout() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            val cart = FakeCart()
            ImageComposeScene(width = 320, height = 1200, density = Density(1f, scale)) {
                Palette(dark) { ShopCartScreen(component = cart) }
            }.use { scene ->
                scene.settle(); scene.click("Продолжить покупки"); assertEquals(listOf("back"), cart.events)
                assertNull(scene.text("Оформить заказ"))
                cart.state.value = ShopCartStore.State(items = listOf(ShopItemCart(product, 2)), isAvailable = true)
                scene.settle(); scene.click(product.visual.displayName)
                scene.clickable(scene.label("Уменьшить количество")!!).config[SemanticsActions.OnClick].action!!.invoke()
                scene.click("Оформить заказ")
                assertEquals(listOf("back", "item", "decrease", "order"), cart.events)
                scene.assertTextFits()
                scene.save("shop-cart-${if(dark) "dark" else "light"}-${(scale*100).toInt()}")
            }
        }
    }

    @Test fun filtersKeepCallbacksAndRejectOversizedPriceWithoutCrashing() {
        val option = ShopOptionInfo.Value("Очень длинное название размера", "size", "size-xl")
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            val filter = FakeFilter(ShopFilterStore.State(options = listOf(ShopOptionInfo(true, "Размер", "size", listOf(option)))))
            ImageComposeScene(width = 320, height = 1400, density = Density(1f, scale)) {
                Palette(dark) { ShopFilterScreen(component = filter, onClickApplyFilter = {}) }
            }.use { scene ->
                scene.settle()
                val min = scene.nodes().first { "От, ₽" in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() && it.config.contains(SemanticsActions.SetText) }
                min.config[SemanticsActions.SetText].action!!.invoke(AnnotatedString("9".repeat(100)))
                assertTrue(filter.events.isEmpty())
                min.config[SemanticsActions.SetText].action!!.invoke(AnnotatedString("1234"))
                scene.click(option.name); scene.click("Применить"); scene.click("Сбросить")
                assertEquals(listOf(ShopFilterStore.Intent.OnUpdatePrice(true, "1234"), ShopFilterStore.Intent.OnClickOption(option),
                    ShopFilterStore.Intent.OnClickApplyFilter, ShopFilterStore.Intent.OnClickResetFilter), filter.events)
                scene.assertTextFits()
                scene.save("shop-filter-${if(dark) "dark" else "light"}-${(scale*100).toInt()}")
            }
        }
    }

    @Test fun checkoutPreservesDeliveryPaymentAndBusyStates() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            val order = FakeOrder(product)
            ImageComposeScene(width = 320, height = 1400, density = Density(1f, scale)) {
                Palette(dark) { ShopCreateOrderScreen(component = order) }
            }.use { scene ->
                scene.settle()
                assertNull(scene.text("Наличными"), "Cash is unavailable for delivery")
                scene.click("Самовывоз")
                assertEquals(ShopCreateOrderStore.Intent.ShopOrderOptionsIntent.OnClickChangeDeliveryType(ShopDeliveryType.Pickup), order.events.last())
                order.state.value = order.state.value.copy(deliveryType = order.state.value.deliveryType.copy(selectedOption = ShopDeliveryType.Pickup))
                scene.settle(); scene.click("Наличными")
                assertEquals(ShopCreateOrderStore.Intent.ShopOrderOptionsIntent.OnClickChangePaymentType(ShopPaymentType.Cache), order.events.last())
                val button = scene.button("Оформить")
                val bounds = button.boundsInRoot
                assertEquals(288f, bounds.width, 1f)
                assertFalse(button.config.contains(SemanticsProperties.Disabled))
                scene.click("Оформить")
                assertEquals(ShopCreateOrderStore.Intent.ShopCreateOrderIntent.OnClickCreateOrder, order.events.last())
                order.state.value = order.state.value.copy(shopCreateOrderState = order.state.value.shopCreateOrderState.copy(isLoading = true))
                scene.settle()
                assertTrue(scene.button("Оформить").config.contains(SemanticsProperties.Disabled))
                assertEquals(bounds, scene.button("Оформить").boundsInRoot)
                scene.assertTextFits()
                scene.save("shop-checkout-${if(dark) "dark" else "light"}-${(scale*100).toInt()}")
            }
        }
    }

    @Test fun catalogueSearchActionsRemainSeparateAndAccessibleForLongQueries() {
        val events = mutableListOf<String>()
        ImageComposeScene(width = 320, height = 260, density = Density(1f, 2f)) {
            Palette(false) { ShopSearchBar(query = "Длинный поисковый запрос товара для осеннего сезона", isShowFilter = true,
                onClickBack = { events += "back" }, onClickSearchPanel = { events += "search" },
                onClickFilter = { events += "filter" }, onBrushClicked = { events += "clear" }) }
        }.use { scene ->
            scene.settle()
            scene.clickable(scene.label("Назад")!!).config[SemanticsActions.OnClick].action!!.invoke()
            scene.click("Длинный поисковый запрос товара для осеннего сезона")
            scene.clickable(scene.label("Очистить поиск")!!).config[SemanticsActions.OnClick].action!!.invoke()
            scene.clickable(scene.label("Фильтры")!!).config[SemanticsActions.OnClick].action!!.invoke()
            assertEquals(listOf("back", "search", "clear", "filter"), events)
            scene.assertTextFits()
        }
    }

    @Test fun ordersKeepCachedContentAndStatusFollowsUpdatedData() {
        for (dark in listOf(false, true)) for (scale in listOf(1f, 2f)) {
            val orders = mutableStateOf(emptyList<ShopUserOrder>())
            val loading = mutableStateOf(true)
            val error = mutableStateOf<SauceError?>(null)
            var retries = 0
            ImageComposeScene(width = 320, height = 1400, density = Density(1f, scale)) {
                Palette(dark) { ShopUserOrdersContent(isLoading = loading.value, isSuccess = !loading.value,
                    error = error.value, items = orders.value, onClickGoBack = {}, onClickRetry = { retries++ }) }
            }.use { scene ->
                scene.settle(); assertNotNull(scene.label("Загрузка заказов"))
                orders.value = listOf(ShopUserOrder(ShopOrder("12 999", ShopOrderStatus.Pending, id = 42,
                    internalId = "test", paidOnline = false, createAt = "17 октября 2026", paymentMethod = ShopPaymentType.Online,
                    shippingMethod = ShopDeliveryType.Pickup), emptyList()))
                loading.value = false
                scene.settle(); assertNull(scene.label("Загрузка заказов"))
                assertNotNull(scene.text("В ожидании"))
                orders.value = orders.value.map { it.copy(order = it.order.copy(status = ShopOrderStatus.Completed)) }
                scene.settle(); assertNotNull(scene.text("Завершён")); assertNull(scene.text("В ожидании"))
                loading.value = true; scene.settle()
                assertNull(scene.label("Загрузка заказов")); assertNotNull(scene.text("№ 42"))
                loading.value = false; error.value = SauceError.Connection(Exception("offline")); scene.settle()
                assertNotNull(scene.text("№ 42")); scene.click("Повторить попытку"); assertEquals(1, retries)
                scene.assertTextFits()
                scene.save("shop-orders-${if(dark) "dark" else "light"}-${(scale*100).toInt()}")
            }
        }
    }

    @Test fun filterDrawerCanCloseAndReopenWithoutLeavingTheCatalogue() {
        val catalogue = FakeCatalogue()
        val cart = object : ShopCartCatalogComponent {
            override val state = MutableStateFlow<ShopCartCatalogComponent.State>(ShopCartCatalogComponent.State.Empty)
            override fun onClickCart() {}
        }
        val filter = FakeFilter(ShopFilterStore.State())
        ImageComposeScene(width = 390, height = 1000) {
            Palette(false) { ShopCatalogScreen(catalogComponent = catalogue, cartComponent = cart, filterComponent = filter) }
        }.use { scene ->
            scene.settle()
            repeat(3) {
                scene.clickable(scene.label("Фильтры")!!).config[SemanticsActions.OnClick].action!!.invoke()
                scene.settle()
                assertTrue(catalogue.state.value.isShowFilter)
                val close = scene.clickable(scene.label("Закрыть фильтры")!!)
                assertTrue(close.boundsInRoot.left >= 0 && close.boundsInRoot.right <= 390)
                close.config[SemanticsActions.OnClick].action!!.invoke()
                scene.settle()
                assertFalse(catalogue.state.value.isShowFilter)
            }
            assertEquals(6, catalogue.events.count { it == ShopCatalogStore.Intent.OnClickFilter })
            assertFalse(catalogue.events.contains(ShopCatalogStore.Intent.OnClickGoBack))
        }
    }

    @Test fun systemBackClosesFilterBeforeParentNavigationAndUnregistersOnDestroy() {
        val dispatcher = BackDispatcher()
        val lifecycle = LifecycleRegistry().apply { create() }
        val catalogue = FakeCatalogue()
        var parentBack = 0
        var closed = 0
        bindShopFilterBackHandler(dispatcher, lifecycle, catalogue.state) {
            closed++
            catalogue.state.value = catalogue.state.value.copy(isShowFilter = false)
        }
        dispatcher.register(BackCallback { parentBack++ })
        dispatcher.back(); assertEquals(1, parentBack)
        catalogue.state.value = catalogue.state.value.copy(isShowFilter = true)
        dispatcher.back(); assertEquals(1, closed); assertEquals(1, parentBack)
        dispatcher.back(); assertEquals(2, parentBack)
        catalogue.state.value = catalogue.state.value.copy(isShowFilter = true)
        lifecycle.destroy()
        dispatcher.back(); assertEquals(3, parentBack); assertEquals(1, closed)
    }

    private class FakeCatalogue : ShopCatalogComponent {
        override val state = MutableStateFlow(ShopCatalogStore.State(flowOf(PagingData.empty(
            sourceLoadStates = LoadStates(LoadStateNotLoading(false), LoadStateNotLoading(false), LoadStateNotLoading(false))))))
        override val listener = object : BottomBarVisibilityListener { override fun show() {}; override fun hide() {} }
        val events = mutableListOf<ShopCatalogStore.Intent>()
        override fun obtainEvent(intent: ShopCatalogStore.Intent) {
            events += intent
            if (intent == ShopCatalogStore.Intent.OnClickFilter) state.value = state.value.copy(isShowFilter = !state.value.isShowFilter)
        }
    }

    private class FakeItem : ShopItemPageComponent {
        override val state = MutableStateFlow(ShopItemPageStore.State(true, false, "", null, emptyList(), flowOf(PagingData.empty())))
        val events = mutableListOf<ShopItemPageStore.Intent>()
        override fun obtainEvent(intent: ShopItemPageStore.Intent) { events += intent }
    }
    private class FakeProductCart : ShopCartPageComponent {
        override val state = MutableStateFlow<ShopCartPageComponent.State>(ShopCartPageComponent.State.Empty)
        var added = 0; var removed = 0; var opened = 0
        override fun updateItem(shopItemCart: ShopItemCart) {}
        override fun onClickAddToCart() { added++ }
        override fun onClickRemove() { removed++ }
        override fun onClickCart() { opened++ }
    }
    private class FakeCart : ShopCartComponent {
        override val state = MutableStateFlow(ShopCartStore.State())
        val events = mutableListOf<String>()
        override fun onClickIncrease(item: ShopItemCart) { events += "increase" }
        override fun onClickDecrease(item: ShopItemCart) { events += "decrease" }
        override fun onClickDelete(item: ShopItemCart) { events += "delete" }
        override fun onClickShopCartItem(item: ShopItemCart) { events += "item" }
        override fun onClickGoBack() { events += "back" }
        override fun onClickCreateOrder() { events += "order" }
        override fun onClickGoAuth() { events += "auth" }
    }
    private class FakeFilter(initial: ShopFilterStore.State) : ShopFilterComponent {
        override val state = MutableStateFlow(initial)
        val events = mutableListOf<ShopFilterStore.Intent>()
        override fun obtainEvent(intent: ShopFilterStore.Intent) { events += intent }
    }
    private class FakeOrder(product: ShopItem) : ShopCreateOrderComponent {
        override val state = MutableStateFlow(ShopCreateOrderStore.DEFAULT_SHOP_ORDER_STATE.let { it.copy(
            shopCreateOrderState = it.shopCreateOrderState.copy(isAvailable = true),
            shopOrderPrice = ShopOrderPrice(25998, 6000, 2),
            shopOrderItems = listOf(ShopItemCart(product, 2)),
            shopOrderResultItems = listOf(ShopItemOrderResult(ShopItemCart(product, 2), true, null)),
            shopRecipientState = it.shopRecipientState.copy(currentShopRecipient = ShopRecipient("Анна Иванова", "79991234567"))) })
        val events = mutableListOf<ShopCreateOrderStore.Intent>()
        override fun obtainEvent(intent: ShopCreateOrderStore.Intent) { events += intent }
    }
    @Composable private fun Palette(dark: Boolean, content: @Composable () -> Unit) {
        val c = sauceColors(dark)
        SauceTheme(darkTheme = dark, materialColors = c.material.copy(primary = c.surface, secondary = c.action,
            tertiary = c.textPrimary, primaryContainer = c.surfaceRaised, outline = c.textMuted), content = content)
    }
    private var time = System.nanoTime()
    private fun ImageComposeScene.settle() { repeat(14) { time += 100_000_000L; render(time).close() } }
    private fun ImageComposeScene.nodes(): List<SemanticsNode> {
        fun walk(n: SemanticsNode): List<SemanticsNode> = listOf(n) + n.children.flatMap(::walk)
        return semanticsOwners.flatMap { walk(it.unmergedRootSemanticsNode) }
    }
    private fun ImageComposeScene.text(s: String) = nodes().firstOrNull { n -> n.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text == s } }
    private fun ImageComposeScene.label(s: String) = nodes().firstOrNull { s in it.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() }
    private fun ImageComposeScene.clickable(n: SemanticsNode): SemanticsNode = if (n.config.contains(SemanticsActions.OnClick)) n else clickable(checkNotNull(n.parent))
    private fun ImageComposeScene.button(s: String) = clickable(checkNotNull(text(s)) { "Missing: $s" })
    private fun ImageComposeScene.click(s: String) { button(s).config[SemanticsActions.OnClick].action!!.invoke(); settle() }
    private fun ImageComposeScene.pointerClick(n: SemanticsNode) {
        sendPointerEvent(PointerEventType.Press, n.boundsInRoot.center); sendPointerEvent(PointerEventType.Release, n.boundsInRoot.center); settle()
    }
    private fun ImageComposeScene.assertTextFits() {
        nodes().filter { !it.config.contains(SemanticsProperties.EditableText) }.forEach { n ->
            val layouts = mutableListOf<TextLayoutResult>()
            n.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
            layouts.filter { it.layoutInput.overflow != TextOverflow.Ellipsis }.forEach {
                assertFalse(it.didOverflowHeight, "Clipped vertically: ${it.layoutInput.text}")
                for (line in 0 until it.lineCount) assertTrue(it.getLineRight(line) <= it.size.width + 0.5f,
                    "Clipped horizontally: ${it.layoutInput.text}, width=${it.size.width}, right=${it.getLineRight(line)}, left=${it.getLineLeft(line)}, scale=${it.layoutInput.density.fontScale}")
            }
        }
    }
    private fun ImageComposeScene.save(name: String) { time += 100_000_000L; render(time).use { img -> img.encodeToData()!!.use {
        File("build/design-system/$name.png").apply { parentFile.mkdirs(); writeBytes(it.bytes) }
    } } }
}
