package com.markettwits.start.presentation.start

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreateSimple
import com.markettwits.start.data.StartDataSource
import com.markettwits.start.presentation.membres.list.StartMembersUi

class StartScreenComponent(
    componentContext: ComponentContext,
    private val startId: Int,
    private val service: StartDataSource,
    private val back: () -> Unit,
    private val members: (Int, List<StartMembersUi>) -> Unit,
) :
    ComponentContext by componentContext, StartScreen {
    private val keeper =
        instanceKeeper.getOrCreateSimple { StartScreenInstanceKeeper(service, startId) }
    override val commentsState: Value<CommentUiState> = keeper.commentsState

    override val start: Value<StartItemUi> = keeper.start

    override val commentMode: Value<CommentMode> = keeper.commentMode
    override fun sendComment(value: String, commentParentId: Int) {
        keeper.sendComment(value, startId)
    }

    override fun goBack() {
        back()
    }

    override fun goMembers(membersUi: List<StartMembersUi>) {
        members(startId, membersUi)
    }

    override fun messageHasBeenShowed() {
        keeper.updateAlertState()
    }

    override fun onClickReply(replier: String, id: Int) {
        keeper.onClickReply(replier, id)
    }

    override fun onClickCloseReply() {
        keeper.onClickCloseReply()
    }

    override fun retry() {
        keeper.launch()
    }

}