package com.markettwits.sportsouce.start.data.start.mapper.start

import com.markettwits.core.time.TimeMapper
import com.markettwits.core.time.TimePattern
import com.markettwits.sportsouce.start.cloud.model.comments.response.Comment
import com.markettwits.sportsouce.start.cloud.model.members.StartMember
import com.markettwits.sportsouce.start.cloud.model.members.StartMemberItem
import com.markettwits.sportsouce.start.cloud.model.result.StartMemberResult
import com.markettwits.sportsouce.start.cloud.model.start.StartRemote
import com.markettwits.sportsouce.start.cloud.model.start.StartRemoteNew
import com.markettwits.sportsouce.start.cloud.model.start.StartRemoteOld
import com.markettwits.sportsouce.start.cloud.model.start.fields.album.StartAlbum
import com.markettwits.sportsouce.start.data.start.mapper.albums.StartAlbumsToUiMapper
import com.markettwits.sportsouce.start.data.start.mapper.comments.StartCommentsToUiMapper
import com.markettwits.sportsouce.start.data.start.mapper.members.StartMembersNewToUiMapper
import com.markettwits.sportsouce.start.data.start.mapper.members.StartMembersToUiMapper
import com.markettwits.sportsouce.start.data.start.mapper.result.StartMembersResultsToUiMapper
import com.markettwits.sportsouce.start.data.start.mapper.time.StartTimesMapper
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.presentation.membres.list.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult

internal class StartRemoteToUiMapperBase(
    private val timeMapper: TimeMapper,
    private val membersMapper: StartMembersToUiMapper,
    private val membersResultsMapper: StartMembersResultsToUiMapper,
    private val commentsMapper: StartCommentsToUiMapper,
    private val albumsMapper: StartAlbumsToUiMapper,
    private val startTimesMapper: StartTimesMapper
) : StartRemoteToUiMapper {
    override fun map(
        startRemote: StartRemote,
        startMember: List<StartMember>,
        startMemberResults: List<StartMemberResult>,
        startAlbum: List<StartAlbum>,
        commentsRemote: List<Comment>,
    ): StartItem {
        return when (startRemote) {
            is StartRemoteNew -> {
                StartItem(
                    id = startRemote.id,
                    title = startRemote.name,
                    startPlace = startRemote.coordinates ?: "",
                    image = startRemote.posterLinkFile?.fullPath ?: "",
                    startStatus = StartItem.StartStatus(
                        code = startRemote.startStatus.code,
                        name = startRemote.startStatus.name
                    ),
                    startTime = timeMapper.mapTime(
                        TimePattern.FullWithEmptySpace,
                        startRemote.startDate
                    ),
                    slug = startRemote.slug ?: "",
                    startData = startRemote.startDate,
                    description = startRemote.description ?: "",
                    paymentDisabled = startRemote.paymentDisabled ?: false,
                    organizers = startRemote.organizers,
                    membersUi = StartMembersNewToUiMapper().map(startMember),
                    commentsRemote = commentsMapper.map(commentsRemote),
                    conditionFile = if (startRemote.conditionFile != null) {
                        StartItem.ConditionFile.Base(startRemote.conditionFile?.fullPath ?: "")
                    } else {
                        StartItem.ConditionFile.Empty
                    },
                    paymentType = startRemote.paymentType ?: "",
                    result = startRemote.results?.filter {
                        it.file != null
                    }?.map {
                        StartItem.Result(
                            id = it.id,
                            name = it.name,
                            url = it.file?.fullPath ?: ""
                        )
                    } ?: emptyList(),
                    usefulLinks = startRemote.usefulLinks?.map {
                        StartItem.Result(
                            id = it.id,
                            name = it.text,
                            url = it.url,
                        )
                    } ?: emptyList(),
                    startAlbum = albumsMapper.map(startAlbum),
                    regLink = startRemote.regLink ?: "",
                    startTimes = startTimesMapper.map(
                        beginningRegistry = "",
                        endRegistry = "",
                        beginningStart = startRemote.startDate,
                        endStart = ""
                    ),
                    distanceInfoNew = startRemote.distinctDistances.values.toList(),
                    distanceMapNew = startRemote.distances,
                    membersResults = membersResultsMapper.map(startMemberResults)
                )
            }

            is StartRemoteOld -> {
                StartItem(
                    id = startRemote.startData.id,
                    title = startRemote.startData.name,
                    startPlace = startRemote.startData.coordinates ?: "",
                    image = startRemote.startData.posterLinkFile?.fullPath ?: "",
                    startStatus = StartItem.StartStatus(
                        code = startRemote.startData.startStatus.code,
                        name = startRemote.startData.startStatus.name
                    ),
                    startTime = timeMapper.mapTime(
                        TimePattern.FullWithEmptySpace,
                        startRemote.startData.startDate
                    ),
                    startData = startRemote.startData.startDate,
                    description = startRemote.startData.description ?: "",
                    paymentDisabled = startRemote.startData.paymentDisabled ?: false,
                    organizers = startRemote.startData.organizers,
                    membersUi = emptyList(),
                    commentsRemote = commentsMapper.map(commentsRemote),
                    conditionFile = if (startRemote.startData.conditionFile != null) {
                        StartItem.ConditionFile.Base(
                            startRemote.startData.conditionFile?.fullPath ?: ""
                        )
                    } else {
                        StartItem.ConditionFile.Empty
                    },
                    paymentType = startRemote.startData.paymentType ?: "",
                    result = startRemote.startData.results.filter {
                        it.file != null
                    }.map {
                        StartItem.Result(
                            id = it.id,
                            name = it.name,
                            url = it.file?.fullPath ?: ""
                        )
                    },
                    usefulLinks = startRemote.startData.usefulLinks?.map {
                        StartItem.Result(
                            id = it.id,
                            name = it.text,
                            url = it.url,
                        )
                    } ?: emptyList(),
                    startAlbum = albumsMapper.map(startAlbum),
                    regLink = startRemote.startData.regLink ?: "",
                    startTimes = startTimesMapper.map(
                        beginningRegistry = startRemote.startData.registrationStartDate,
                        endRegistry = startRemote.startData.registrationEndDate,
                        beginningStart = startRemote.startData.startDate,
                        endStart = startRemote.startData.endDate
                    ),
                    distanceInfoNew = emptyList(),
                    distanceMapNew = emptyList(),
                    membersResults = membersResultsMapper.map(startMemberResults),
                    slug = ""
                )
            }
        }
    }

    override fun map(startMemberResults: List<StartMemberResult>): List<MemberResult> {
        return membersResultsMapper.map(startMemberResults)
    }


    override fun map(e: Exception): String = e.message.toString()

    override fun map(commentsRemote: List<Comment>): StartItem.Comments =
        commentsMapper.map(commentsRemote)

    override fun map(
        startMember: List<StartMemberItem>,
        paymentDisabled: Boolean
    ): List<StartMembersUi> {
        return membersMapper.map(startMember, paymentDisabled)
    }

}