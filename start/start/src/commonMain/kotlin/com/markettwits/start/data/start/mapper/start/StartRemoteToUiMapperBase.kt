package com.markettwits.start.data.start.mapper.start

import com.markettwits.start.data.start.mapper.albums.StartAlbumsToUiMapper
import com.markettwits.start.data.start.mapper.comments.StartCommentsToUiMapper
import com.markettwits.start.data.start.mapper.members.StartMembersNewToUiMapper
import com.markettwits.start.data.start.mapper.members.StartMembersToUiMapper
import com.markettwits.start.data.start.mapper.result.StartMembersResultsToUiMapper
import com.markettwits.start.data.start.mapper.time.StartTimesMapper
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import com.markettwits.start.presentation.result.model.MemberResult
import com.markettwits.start_cloud.model.comments.response.Comment
import com.markettwits.start_cloud.model.members.StartMember
import com.markettwits.start_cloud.model.members.StartMemberItem
import com.markettwits.start_cloud.model.result.StartMemberResult
import com.markettwits.start_cloud.model.start.StartRemote
import com.markettwits.start_cloud.model.start.StartRemoteNew
import com.markettwits.start_cloud.model.start.StartRemoteOld
import com.markettwits.start_cloud.model.start.fields.album.StartAlbum
import com.markettwits.time.TimeMapper
import com.markettwits.time.TimePattern

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
                        code = startRemote.start_status.code,
                        name = startRemote.start_status.name
                    ),
                    startTime = timeMapper.mapTime(
                        TimePattern.FullWithEmptySpace,
                        startRemote.start_date
                    ),
                    slug = startRemote.slug ?: "",
                    startData = startRemote.start_date,
                    description = startRemote.description ?: "",
                    paymentDisabled = startRemote.payment_disabled ?: false,
                    organizers = startRemote.organizers,
                    membersUi = StartMembersNewToUiMapper().map(startMember),
                    commentsRemote = commentsMapper.map(commentsRemote),
                    conditionFile = if (startRemote.conditionFile != null) {
                        StartItem.ConditionFile.Base(startRemote.conditionFile?.fullPath ?: "")
                    } else {
                        StartItem.ConditionFile.Empty
                    },
                    paymentType = startRemote.payment_type ?: "",
                    result = emptyList(),
                    usefulLinks = startRemote.useful_links?.map {
                        StartItem.Result(
                            id = it.id,
                            name = it.text,
                            url = it.url,
                        )
                    } ?: emptyList(),
                    startAlbum = albumsMapper.map(startAlbum),
                    regLink = startRemote.reg_link ?: "",
                    startTimes = startTimesMapper.map(
                        beginningRegistry = "",
                        endRegistry = "",
                        beginningStart = startRemote.start_date,
                        endStart = ""
                    ),
                    distanceInfoNew = startRemote.distinctDistances.values.toList(),
                    distanceMapNew = startRemote.distances,
                    membersResults = membersResultsMapper.map(startMemberResults)
                )
            }

            is StartRemoteOld -> {
                StartItem(
                    id = startRemote.start_data.id,
                    title = startRemote.start_data.name,
                    startPlace = startRemote.start_data.coordinates ?: "",
                    image = startRemote.start_data.posterLinkFile?.fullPath ?: "",
                    startStatus = StartItem.StartStatus(
                        code = startRemote.start_data.start_status.code,
                        name = startRemote.start_data.start_status.name
                    ),
                    startTime = timeMapper.mapTime(
                        TimePattern.FullWithEmptySpace,
                        startRemote.start_data.start_date
                    ),
                    startData = startRemote.start_data.start_date,
                    description = startRemote.start_data.description ?: "",
                    paymentDisabled = startRemote.start_data.payment_disabled ?: false,
                    organizers = startRemote.start_data.organizers,
                    membersUi = emptyList(),
                    commentsRemote = commentsMapper.map(commentsRemote),
                    conditionFile = if (startRemote.start_data.conditionFile != null) {
                        StartItem.ConditionFile.Base(
                            startRemote.start_data.conditionFile?.fullPath ?: ""
                        )
                    } else {
                        StartItem.ConditionFile.Empty
                    },
                    paymentType = startRemote.start_data.payment_type ?: "",
                    result = startRemote.start_data.results.filter {
                        it.file != null
                    }.map {
                        StartItem.Result(
                            id = it.id,
                            name = it.name,
                            url = it.file?.fullPath ?: ""
                        )
                    },
                    usefulLinks = startRemote.start_data.useful_links?.map {
                        StartItem.Result(
                            id = it.id,
                            name = it.text,
                            url = it.url,
                        )
                    } ?: emptyList(),
                    startAlbum = albumsMapper.map(startAlbum),
                    regLink = startRemote.start_data.reg_link ?: "",
                    startTimes = startTimesMapper.map(
                        beginningRegistry = startRemote.start_data.registration_start_date,
                        endRegistry = startRemote.start_data.registration_end_date,
                        beginningStart = startRemote.start_data.start_date,
                        endStart = startRemote.start_data.end_date
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