package com.markettwits.start.data.start.mapper.members

import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import com.markettwits.start_cloud.model.members.StartMemberItem


internal interface StartMembersToUiMapper {

    fun map(startMember: List<StartMemberItem>, paymentDisabled: Boolean): List<StartMembersUi>

}