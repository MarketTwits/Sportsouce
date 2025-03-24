package com.markettwits.start.data.start.mapper.start

import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import com.markettwits.start.presentation.result.model.MemberResult
import com.markettwits.start_cloud.model.comments.response.Comment
import com.markettwits.start_cloud.model.members.StartMember
import com.markettwits.start_cloud.model.members.StartMemberItem
import com.markettwits.start_cloud.model.result.StartMemberResult
import com.markettwits.start_cloud.model.start.StartRemote
import com.markettwits.start_cloud.model.start.fields.album.StartAlbum

interface StartRemoteToUiMapper {

    fun map(
        startRemote: StartRemote,
        startMember: List<StartMember>,
        startMemberResults : List<StartMemberResult>,
        startAlbum: List<StartAlbum>,
        commentsRemote: List<Comment>,
    ): StartItem

    fun map(startMemberResults : List<StartMemberResult>) : List<MemberResult>

    fun map(e: Exception): String

    fun map(commentsRemote: List<Comment>): StartItem.Comments

    fun map(startMember: List<StartMemberItem>, paymentDisabled: Boolean): List<StartMembersUi>
}