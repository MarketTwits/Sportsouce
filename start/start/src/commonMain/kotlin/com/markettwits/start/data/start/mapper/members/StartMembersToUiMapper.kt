package com.markettwits.start.data.start.mapper.members

import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.start.presentation.membres.list.models.StartMembersUi


internal interface StartMembersToUiMapper {
    fun map(startMember: List<StartMemberItem>, paymentDisabled: Boolean): List<StartMembersUi>
}