package com.markettwits.start.presentation.start

import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.start.data.model.DistanceInfo
import com.markettwits.start.presentation.membres.StartMembersUi

sealed class StartItemUi{
    data class StartItemUiSuccess(
        val id : Int,
        val title : String,
        val startPlace : String,
        val image : String,
        val startStatus: StartStatus,
        val startData: String,
        val startTime : String,
        val description : String,
        val distanceInfo: List<DistanceInfo>,
        val membersUi: List<StartMembersUi>
    ) : StartItemUi()
    object Loading : StartItemUi()
}
