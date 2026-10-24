package com.markettwits.sportsouce.start

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.markettwits.sportsouce.start.presentation.start.component.*
import com.markettwits.sportsouce.start.root.RootStartScreenComponent.Config
import com.markettwits.sportsouce.start.root.openStartComments
import kotlin.test.*

class StartCommentsNavigationTest {
    @Test fun directReplyOpensOnlyTheRequestedThreadAndBackReturnsToCommentsBeforeStart() {
        val lifecycle = LifecycleRegistry().apply { resume() }
        val back = BackDispatcher()
        val context = DefaultComponentContext(lifecycle = lifecycle, backHandler = back)
        val navigation = StackNavigation<Config>()
        val start = Config.Start(StartScreenInput.Id(318))
        val stack = context.childStack(source = navigation, serializer = null, initialConfiguration = start,
            handleBackButton = true, childFactory = { config, _ -> config })
        val visited = mutableListOf<Config>()
        stack.subscribe { visited += it.active.instance }
        val reply = CommentMode.Reply("Автор", 172,
            CommentData("Автор", "", "Нужный вопрос", "24 октября", emptyList(), isQuestion = true))
        navigation.openStartComments(318, reply)
        navigation.openStartComments(318, reply) // A second tap during the transition must not duplicate the stack.
        assertEquals(Config.StartComments(318, reply), stack.value.active.instance)
        assertEquals(listOf(start, Config.StartComments(318, CommentMode.Base)), stack.value.backStack.map { it.instance })
        assertEquals(listOf(start, Config.StartComments(318, reply)), visited.distinct(), "The list must not flash before the thread")
        back.back()
        assertEquals(Config.StartComments(318, CommentMode.Base), stack.value.active.instance)
        back.back()
        assertEquals(start, stack.value.active.instance)
    }

    @Test fun openingAllCommentsAddsOneScreenOnly() {
        val context = DefaultComponentContext(LifecycleRegistry().apply { resume() })
        val navigation = StackNavigation<Config>()
        val start = Config.Start(StartScreenInput.Id(318))
        val stack = context.childStack(source = navigation, serializer = null, initialConfiguration = start,
            childFactory = { config, _ -> config })
        navigation.openStartComments(318, CommentMode.Base)
        assertEquals(Config.StartComments(318, CommentMode.Base), stack.value.active.instance)
        assertEquals(listOf(start), stack.value.backStack.map { it.instance })
    }
}
