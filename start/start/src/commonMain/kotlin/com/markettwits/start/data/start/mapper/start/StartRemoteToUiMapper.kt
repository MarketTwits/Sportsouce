package com.markettwits.start.data.start.mapper.start

import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import com.markettwits.start_cloud.model.comments.response.Comment
import com.markettwits.start_cloud.model.members.StartMember
import com.markettwits.start_cloud.model.start.StartRemote
import com.markettwits.start_cloud.model.start.fields.album.StartAlbum

interface StartRemoteToUiMapper {

    fun map(
        startRemote: StartRemote,
        startMember: List<StartMember>,
        startAlbum: List<StartAlbum>,
        commentsRemote: List<Comment>,
    ): StartItem

    fun map(e: Exception): String
    fun map(commentsRemote: List<Comment>): StartItem.Comments
    fun map(startMember: List<StartMemberItem>, paymentDisabled: Boolean): List<StartMembersUi>
}