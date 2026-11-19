package com.markettwits.sportsouce.start.root

import com.arkivanov.decompose.router.stack.StackNavigation
import com.markettwits.sportsouce.start.presentation.start.component.CommentMode

/** Install the list and the requested thread in one transition, without flashing the list. */
internal fun StackNavigation<RootStartScreenComponent.Config>.openStartComments(startId: Int, mode: CommentMode) {
    navigate(transformer = { stack ->
        if ((stack.lastOrNull() as? RootStartScreenComponent.Config.StartComments)?.startId == startId) stack
        else stack + listOf(RootStartScreenComponent.Config.StartComments(startId, CommentMode.Base)) +
            if (mode is CommentMode.Reply) listOf(RootStartScreenComponent.Config.StartComments(startId, mode)) else emptyList()
    }, onComplete = { _, _ -> })
}
