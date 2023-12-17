package com.markettwits.start.data

import com.markettwits.cloud.model.start_member.StartMember
import com.markettwits.start.presentation.membres.StartMembersUi
import com.markettwits.start.presentation.start.StartItemUi

interface StartDataSource {
    suspend fun start(startId : Int) : StartItemUi
    suspend fun startMember(startId: Int) : List<StartMembersUi>
}