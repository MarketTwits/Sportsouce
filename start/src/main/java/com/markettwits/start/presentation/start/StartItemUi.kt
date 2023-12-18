package com.markettwits.start.presentation.start

import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.cloud.model.start.Organizer
import com.markettwits.cloud.model.start.SocialNetwork
import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.sportsourcedemo.all.ConditionFile
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
        val organizers: List<Organizer>,
        val membersUi: List<StartMembersUi>,
        val conditionFile: ConditionFile,
    ) : StartItemUi(){
        sealed class ConditionFile{
            data class Base(val url : String) : ConditionFile()
            object Empty : ConditionFile()
        }
    }
    object Loading : StartItemUi()
}
