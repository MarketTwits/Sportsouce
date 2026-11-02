package com.markettwits.sportsouce.root

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.markettwits.sportsouce.auth.flow.api.root.RootAuthFlowComponent
import com.markettwits.sportsouce.bottom_bar.model.BottomBarConfiguration
import com.markettwits.sportsauce.deeplink.model.Deeplink
import com.markettwits.sportsouce.review.root.RootReviewComponent
import com.markettwits.sportsouce.root_profile.RootProfileComponent
import com.markettwits.sportsouce.starts.root.RootStartsComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class RootTabReselectionTest {
    @BeforeTest fun setMain() { Dispatchers.setMain(StandardTestDispatcher()) }
    @AfterTest fun resetMain() { Dispatchers.resetMain() }
    @Test fun switchingTabsPreservesHistoryButReselectingStartsKeepsOnlyItsExistingRoot() = runTest {
        val lifecycle = LifecycleRegistry()
        try {
            val root = RootComponentBase(DefaultComponentContext(lifecycle))
            val bottom = assertIs<RootComponent.Navigation.BottomBar>(root.slotChild.value.child?.instance).component
            bottom.onClickTabBar(BottomBarConfiguration.Home)
            val starts = assertIs<RootComponent.Child.Starts>(root.childStack.value.active.instance).component
            val dashboard = assertIs<RootStartsComponent.Child.Starts>(starts.configStack.value.active.instance)
            dashboard.component.onSettingsClick()
            val settings = assertIs<RootStartsComponent.Child.Settings>(starts.configStack.value.active.instance)
            bottom.onClickTabBar(BottomBarConfiguration.Review)
            bottom.onClickTabBar(BottomBarConfiguration.Home)
            assertSame(settings, starts.configStack.value.active.instance)
            bottom.onClickTabBar(BottomBarConfiguration.Home)
            assertEquals(1, starts.configStack.value.items.size)
            assertSame(dashboard, starts.configStack.value.active.instance)
            bottom.onClickTabBar(BottomBarConfiguration.Home)
            assertSame(dashboard, starts.configStack.value.active.instance, "Repeated root taps must not recreate its state")
        } finally { lifecycle.destroy(); runCurrent() }
    }

    @Test fun reviewReselectionDisposesNestedScreensAndPreservesTheDashboard() = runTest {
        val lifecycle = LifecycleRegistry()
        try {
            val root = RootComponentBase(DefaultComponentContext(lifecycle))
            val bottom = assertIs<RootComponent.Navigation.BottomBar>(root.slotChild.value.child?.instance).component
            val review = assertIs<RootComponent.Child.Review>(root.childStack.value.active.instance).component
            val dashboard = assertIs<RootReviewComponent.Child.Review>(review.childStack.value.active.instance)
            review.handleDeeplink(Deeplink.News.NewsDetail(99))
            review.handleDeeplink(Deeplink.News.NewsDetail(100))
            val detail = assertIs<RootReviewComponent.Child.NewsEvent>(review.childStack.value.active.instance)
            bottom.onClickTabBar(BottomBarConfiguration.Home)
            bottom.onClickTabBar(BottomBarConfiguration.Review)
            assertSame(detail, review.childStack.value.active.instance)
            bottom.onClickTabBar(BottomBarConfiguration.Review)
            assertEquals(1, review.childStack.value.items.size)
            assertSame(dashboard, review.childStack.value.active.instance)
        } finally { lifecycle.destroy(); runCurrent() }
    }

    @Test fun profileReselectionClosesTheWholeAuthenticationFlowAndReopeningStartsFresh() = runTest {
        val lifecycle = LifecycleRegistry()
        try {
            val root = RootComponentBase(DefaultComponentContext(lifecycle))
            val bottom = assertIs<RootComponent.Navigation.BottomBar>(root.slotChild.value.child?.instance).component
            bottom.onClickTabBar(BottomBarConfiguration.Profile)
            val profile = assertIs<RootComponent.Child.Profile>(root.childStack.value.active.instance).component
            val dashboard = assertIs<RootProfileComponent.Child.UnAuthProfile>(profile.childStack.value.active.instance)
            dashboard.component.signIn()
            val auth = assertIs<RootProfileComponent.Child.AuthFlow>(profile.childStack.value.active.instance).component
            assertIs<RootAuthFlowComponent.Child.SignIn>(auth.childStack.value.active.instance).component.forgotPassword()
            val forgot = assertIs<RootAuthFlowComponent.Child.ForgotPassword>(auth.childStack.value.active.instance)
            bottom.onClickTabBar(BottomBarConfiguration.Review)
            bottom.onClickTabBar(BottomBarConfiguration.Profile)
            assertSame(forgot, auth.childStack.value.active.instance)
            bottom.onClickTabBar(BottomBarConfiguration.Profile)
            assertEquals(1, profile.childStack.value.items.size)
            assertSame(dashboard, profile.childStack.value.active.instance)
            dashboard.component.signIn()
            val reopened = assertIs<RootProfileComponent.Child.AuthFlow>(profile.childStack.value.active.instance).component
            assertNotSame(auth, reopened)
            assertIs<RootAuthFlowComponent.Child.SignIn>(reopened.childStack.value.active.instance)
        } finally { lifecycle.destroy(); runCurrent() }
    }
}
