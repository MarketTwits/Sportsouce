package com.markettwits.start.data

import com.markettwits.cloud.model.start.StartRemote
import com.markettwits.cloud.model.start_comments.StartCommentsRemote
import com.markettwits.cloud.model.start_member.StartMember
import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.start.core.TimeMapper
import com.markettwits.start.core.TimePattern
import com.markettwits.start.data.model.DistanceInfo
import com.markettwits.start.presentation.membres.StartMembersUi
import com.markettwits.start.presentation.start.StartItemUi
import kotlinx.serialization.json.Json

interface StartRemoteToUiMapper {
    fun map(
        startRemote: StartRemote,
        startMember: List<StartMemberItem>,
        commentsRemote: StartCommentsRemote
    ): StartItemUi

    fun map(startMember: List<StartMemberItem>): List<StartMembersUi>
    class Base(private val mapper: TimeMapper) : StartRemoteToUiMapper {
        override fun map(
            startRemote: StartRemote,
            startMember: List<StartMemberItem>,
            commentsRemote: StartCommentsRemote
        ): StartItemUi {
            return StartItemUi.StartItemUiSuccess(
                id = startRemote.start_data.id,
                title = startRemote.start_data.name,
                startPlace = startRemote.start_data.coordinates,
                image = startRemote.start_data.posterLinkFile?.fullPath ?: "",
                startStatus = startRemote.start_data.start_status,
                startTime = mapper.mapTime(
                    TimePattern.ddMMMMyyyy,
                    startRemote.start_data.start_date
                ),
                startData = startRemote.start_data.start_date,
                description = startRemote.start_data.description ?: "",
                distanceInfo = mapDistanceInfo(startRemote.start_data.select_kinds_sport),
                organizers = startRemote.start_data.organizers,
                membersUi = map(startMember),
                commentsRemote = mapComments(commentsRemote),
                conditionFile = if (startRemote.start_data.conditionFile != null) {
                    StartItemUi.StartItemUiSuccess.ConditionFile.Base(startRemote.start_data.conditionFile!!.fullPath)
                } else {
                    StartItemUi.StartItemUiSuccess.ConditionFile.Empty
                },
                result = startRemote.start_data.results.filter {
                    it.file != null
                }.map {
                    StartItemUi.StartItemUiSuccess.Result(
                        id = it.id,
                        name = it.name,
                        url = it.file?.fullPath ?: ""
                    )
                },
                usefulLinks = startRemote.start_data.useful_links?.map {
                    StartItemUi.StartItemUiSuccess.Result(
                        id = it.id,
                        name = it.text,
                        url = it.url,
                    )
                } ?: emptyList()
            )
        }

        override fun map(startMember: List<StartMemberItem>): List<StartMembersUi> {
            val list = mutableListOf<StartMembersUi>()
            startMember.forEach {
                if (it.payment != null) {
                    list.add(
                        StartMembersUi(
                            id = it.id,
                            name = it.name,
                            surname = it.surname,
                            distance = it.distance,
                            team = it.team,
                            group = it.mapStartMember(it.group).name,
                            city = it.city ?: ""
                        )
                    )
                }
            }
            return list
        }

        private fun mapComments(commentsRemote: StartCommentsRemote): StartItemUi.StartItemUiSuccess.Comments {
            return StartItemUi.StartItemUiSuccess.Comments(
                id = commentsRemote.count,
                rows = commentsRemote.rows.map {
                    StartItemUi.StartItemUiSuccess.Comments.Row(
                        id = it.id,
                        comment = it.comment,
                        countSub = it.countSub,
                        createdAt = mapper.mapTime(
                            TimePattern.ddMMMMyyyy,
                            it.createdAt
                        ),
                        personId = it.personId,
                        replies = it.replies.map { reply ->
                            StartItemUi.StartItemUiSuccess.Comments.Reply(
                                id = reply.id,
                                comment = reply.comment,
                                createdAt = mapper.mapTime(
                                    TimePattern.ddMMMMyyyy,
                                    reply.createdAt
                                ),
                                user = StartItemUi.StartItemUiSuccess.Comments.User(
                                    id = reply.user.id,
                                    name = reply.user.name,
                                    surname = reply.user.surname,
                                    photo = reply.user.photo?.fullPath ?: ""
                                ),
                            )
                        },
                        startId = it.startId,
                        updatedAt = mapper.mapTime(
                            TimePattern.ddMMMMyyyy,
                            it.updatedAt
                        ),
                        user = StartItemUi.StartItemUiSuccess.Comments.User(
                            id = it.user.id,
                            name = it.user.name,
                            surname = it.user.surname,
                            photo = it.user.photo?.fullPath ?: ""
                        ),
                    )
                }
            )
        }

        private fun mapDistanceInfo(text: String): List<DistanceInfo> {
            val json = Json {
                ignoreUnknownKeys = true
            }
            return try {
                json.decodeFromString<List<DistanceInfo>>(text)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

}