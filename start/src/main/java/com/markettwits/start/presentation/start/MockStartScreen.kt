package com.markettwits.start.presentation.start

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.markettwits.start.presentation.membres.list.StartMembersUi

class MockStartScreen : StartScreen {
    override val start: Value<StartItemUi> = MutableValue(StartItemUi.Loading)
    override val commentsState: Value<CommentUiState> = MutableValue(CommentUiState.Success)
    override fun sendComment(value: String, commentParentId: Int) {

    }

    override val commentMode: Value<CommentMode> = MutableValue(CommentMode.Base)

    override fun goBack() = Unit
    override fun goMembers(membersUi: List<StartMembersUi>) = Unit
    override fun messageHasBeenShowed() {

    }

    override fun onClickReply(replier: String, id: Int) {

    }

    override fun onClickCloseReply() {

    }

    override fun retry() {

    }

    override fun onClickDistance() {

    }


}