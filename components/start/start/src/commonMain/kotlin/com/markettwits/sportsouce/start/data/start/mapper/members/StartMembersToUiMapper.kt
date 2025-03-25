package com.markettwits.sportsouce.start.data.start.mapper.members

import com.markettwits.sportsouce.start.cloud.model.members.StartMemberItem
import com.markettwits.sportsouce.start.presentation.membres.list.models.StartMembersUi


internal interface StartMembersToUiMapper {

    fun map(startMember: List<StartMemberItem>, paymentDisabled: Boolean): List<StartMembersUi>

}