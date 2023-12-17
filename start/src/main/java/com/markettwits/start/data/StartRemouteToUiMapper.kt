package com.markettwits.start.data

import com.markettwits.cloud.model.start.StartRemote
import com.markettwits.cloud.model.start_member.StartMember
import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.start.data.model.DistanceInfo
import com.markettwits.start.presentation.membres.StartMembersUi
import com.markettwits.start.presentation.start.StartItemUi
import kotlinx.serialization.json.Json

interface StartRemoteToUiMapper {
    fun map(startRemote: StartRemote) : StartItemUi
    fun map(startMember: List<StartMemberItem>): List<StartMembersUi>
    class Base : StartRemoteToUiMapper{
        override fun map(startRemote: StartRemote): StartItemUi {
            return StartItemUi.StartItemUiSuccess(
                id = startRemote.start_data.id,
                title = startRemote.start_data.name,
                startPlace = startRemote.start_data.coordinates,
                image = startRemote.start_data.posterLinkFile?.fullPath ?: "",
                startStatus = startRemote.start_data.start_status,
                startData = startRemote.start_data.start_date,
                description = startRemote.start_data.description,
                distanceInfo = mapDistanceInfo(startRemote.start_data.select_kinds_sport),
            )
        }

        override fun map(startMember: List<StartMemberItem>): List<StartMembersUi> {
            val list = mutableListOf<StartMembersUi>()
            startMember.forEach {
                list.add(
                    StartMembersUi(
                        id = it.id,
                        member = it.surname + it.name,
                        distance = it.distance,
                        team = it.team,
                        group = it.mapStartMember(it.group).name,
                        city = it.city
                    )
                )
            }
            return list
        }
        private fun mapDistanceInfo(text: String): List<DistanceInfo> {
            val json = Json {
                ignoreUnknownKeys = true
            }
            return json.decodeFromString<List<DistanceInfo>>(text)
        }
    }

}