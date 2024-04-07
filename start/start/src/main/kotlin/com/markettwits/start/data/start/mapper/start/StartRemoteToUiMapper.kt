package com.markettwits.start.data.start.mapper.start

import com.markettwits.cloud.model.start.StartRemote
import com.markettwits.cloud.model.start_album.StartAlbumRemote
import com.markettwits.cloud.model.start_comments.response.StartCommentsRemote
import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.cloud.model.time.TimeRemote
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.membres.list.StartMembersUi

interface StartRemoteToUiMapper {

    fun map(
        startRemote: StartRemote,
        startMember: List<StartMemberItem>,
        startAlbumRemote: StartAlbumRemote,
        commentsRemote: StartCommentsRemote,
        timeRemote: TimeRemote
    ): StartItem

    fun map(e: Exception): String
    fun map(commentsRemote: StartCommentsRemote): StartItem.Comments
    fun map(startMember: List<StartMemberItem>, paymentDisabled: Boolean): List<StartMembersUi>
}