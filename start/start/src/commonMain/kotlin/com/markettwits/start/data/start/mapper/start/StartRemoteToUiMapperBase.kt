package com.markettwits.start.data.start.mapper.start

import com.markettwits.cloud.model.start.StartRemote
import com.markettwits.cloud.model.start_album.StartAlbumRemote
import com.markettwits.cloud.model.start_comments.response.StartCommentsRemote
import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.cloud.model.time.TimeRemote
import com.markettwits.start.data.start.mapper.albums.StartAlbumsToUiMapper
import com.markettwits.start.data.start.mapper.comments.StartCommentsToUiMapper
import com.markettwits.start.data.start.mapper.distance.StartDistancesToUiMapper
import com.markettwits.start.data.start.mapper.members.StartMembersToUiMapper
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import com.markettwits.time.TimeMapper
import com.markettwits.time.TimePattern

internal class StartRemoteToUiMapperBase(
    private val timeMapper: TimeMapper,
    private val membersMapper: StartMembersToUiMapper,
    private val commentsMapper: StartCommentsToUiMapper,
    private val albumsMapper: StartAlbumsToUiMapper,
    private val distancesMapper: StartDistancesToUiMapper
) : StartRemoteToUiMapper {
    override fun map(
        startRemote: StartRemote,
        startMember: List<StartMemberItem>,
        startAlbumRemote: StartAlbumRemote,
        commentsRemote: StartCommentsRemote,
        timeRemote: TimeRemote
    ): StartItem =

        StartItem(
            id = startRemote.start_data.id,
            title = startRemote.start_data.name,
            startPlace = startRemote.start_data.coordinates ?: "",
            image = startRemote.start_data.posterLinkFile?.fullPath ?: "",
            startStatus = startRemote.start_data.start_status,
            startTime = timeMapper.mapTime(
                TimePattern.ddMMMMyyyy,
                startRemote.start_data.start_date
            ),
            startData = startRemote.start_data.start_date,
            description = startRemote.start_data.description ?: "",
            paymentDisabled = startRemote.start_data.payment_disabled ?: false,
            distanceInfo = distancesMapper.mapDistanceInfoList(
                startMember = startMember,
                distances = distancesMapper.mapKindOfSportsToDistanceItemList(
                    startId = startRemote.start_data.id,
                    kindOfSport = startRemote.start_data.select_kinds_sport
                ),
                date = timeRemote.date
            ),
            organizers = startRemote.start_data.organizers,
            membersUi = map(startMember, startRemote.start_data.payment_disabled ?: false),
            commentsRemote = commentsMapper.map(commentsRemote),
            conditionFile = if (startRemote.start_data.conditionFile != null) {
                StartItem.ConditionFile.Base(startRemote.start_data.conditionFile?.fullPath ?: "")
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
            startAlbum = albumsMapper.map(startAlbumRemote),
            regLink = startRemote.start_data.reg_link ?: ""
        )

    override fun map(e: Exception): String = e.message.toString()
    override fun map(commentsRemote: StartCommentsRemote): StartItem.Comments =
        commentsMapper.map(commentsRemote)

    override fun map(
        startMember: List<StartMemberItem>,
        paymentDisabled: Boolean
    ): List<StartMembersUi> {
        return membersMapper.map(startMember, paymentDisabled)
    }

}