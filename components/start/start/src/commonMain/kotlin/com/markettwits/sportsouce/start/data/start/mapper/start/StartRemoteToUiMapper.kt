package com.markettwits.sportsouce.start.data.start.mapper.start

import com.markettwits.sportsouce.start.cloud.model.comments.response.Comment
import com.markettwits.sportsouce.start.cloud.model.members.StartMember
import com.markettwits.sportsouce.start.cloud.model.members.StartMemberItem
import com.markettwits.sportsouce.start.cloud.model.result.v1.StartMemberResultV1
import com.markettwits.sportsouce.start.cloud.model.result.v2.StartMemberResultV2
import com.markettwits.sportsouce.start.cloud.model.start.StartRemote
import com.markettwits.sportsouce.start.cloud.model.start.fields.album.StartAlbum
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult

interface StartRemoteToUiMapper {

    fun map(
        startRemote: StartRemote,
        startMembers: List<StartMember>,
        startMemberResults: List<MemberResult>,
        startAlbum: List<StartAlbum>,
        commentsRemote: List<Comment>,
    ): StartItem

    fun mapR1(startMemberResultsV1: List<StartMemberResultV1>): List<MemberResult>

    fun mapR2(startMembersResultsV2: List<StartMemberResultV2>): List<MemberResult>

    fun map(e: Exception): String

    fun map(commentsRemote: List<Comment>): StartItem.Comments

    fun map(startMember: List<StartMemberItem>, paymentDisabled: Boolean): List<StartMembersUi>
}