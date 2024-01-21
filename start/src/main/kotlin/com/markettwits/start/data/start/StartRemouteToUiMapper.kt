package com.markettwits.start.data.start

import android.util.Log
import com.markettwits.cloud.ext_model.Distance
import com.markettwits.cloud.ext_model.DistanceInfo
import com.markettwits.cloud.model.start.StartRemote
import com.markettwits.cloud.model.start_comments.response.StartCommentsRemote
import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.cloud.model.time.TimeRemote
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.core_ui.time.TimePattern
import com.markettwits.start.data.start.test.SelectKindsSportItem
import com.markettwits.start.presentation.membres.list.StartMembersUi
import com.markettwits.start.presentation.start.StartItemUi
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

interface StartRemoteToUiMapper {
    fun map(
        startRemote: StartRemote,
        startMember: List<StartMemberItem>,
        commentsRemote: StartCommentsRemote
    ): StartItemUi

    fun map(
        startRemote: StartRemote,
        startMember: List<StartMemberItem>,
        commentsRemote: StartCommentsRemote,
        timeRemote: TimeRemote
    ): StartItemUi
    fun map(e : Exception) : StartItemUi

    fun map(startMember: List<StartMemberItem>): List<StartMembersUi>
    class Base(
        private val mapper: TimeMapper,
        private val membersToUiMapper: StartMembersToUiMapper,
    ) : StartRemoteToUiMapper {
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
                //  distanceInfo = mapDistanceInfo(startRemote.start_data.select_kinds_sport),
                distanceInfo = calculateRemainingSlots(
                    mapDistanceInfo(startRemote.start_data.select_kinds_sport),
                    startMember
                ),
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

        override fun map(
            startRemote: StartRemote,
            startMember: List<StartMemberItem>,
            commentsRemote: StartCommentsRemote,
            timeRemote: TimeRemote
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
                //  distanceInfo = mapDistanceInfo(startRemote.start_data.select_kinds_sport),
                distanceInfo =
                updateDistanceInfoList(
                    calculateRemainingSlots(
                        mapDistanceInfo(startRemote.start_data.select_kinds_sport),
                        startMember
                    ),
                    mapCurrentTime(timeRemote.dateTime)
                ),
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

        override fun map(e: Exception): StartItemUi = StartItemUi.Error(e.message.toString())

        private fun updateDistanceInfoList(
            distances: List<DistanceInfo>,
            date: String
        ): List<DistanceInfo> {
            return distances.map { distanceInfo ->
                val newPriceValue = getPriceForDate(distanceInfo.distance, date)
                val updatedDistance = distanceInfo.distance.copy(price = newPriceValue.toString())
                val updatedDistanceInfo = distanceInfo.copy(distance = updatedDistance)
                updatedDistanceInfo
            }
        }

        fun mapCurrentTime(time: String): String {
            val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS")
            val outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")

            // Parse the input date string

            //FIXME fix 6 or 7 elements in time
          //
            val inputDate = LocalDateTime.parse(time, inputFormat)
           // val inputDate = LocalDateTime.parse("2023-12-25T22:46:48.5554295", inputFormat)

            // Format the output date string
            val outputDate = inputDate.atOffset(ZoneOffset.UTC).format(outputFormat)
            return outputDate

        }

        fun getPriceForDate(distance: Distance, date: String): Int {

            if (distance.prices.isEmpty()) {
                return distance.price.toInt()
            }
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val targetDate = dateFormat.parse(date) ?: return 0 // handle date parsing failure

            for (price in distance.prices) {
                val from = dateFormat.parse(price.c_from) ?: continue // skip on parsing failure
                val to = dateFormat.parse(price.c_to) ?: continue // skip on parsing failure

                if (targetDate in from..to) {
                    return price.value
                }
            }

            return distance.price.toInt() // or whatever default value you want to return when no interval matches
        }

        override fun map(startMember: List<StartMemberItem>): List<StartMembersUi> {
            return membersToUiMapper.maps(startMember)
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
                        replies = it.replies?.map { reply ->
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
                        } ?: emptyList(),
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
            val data = mapDistanceInfoTest(text)
            Log.e("mt05", "size " + data.size.toString())
            val json = Json {
                ignoreUnknownKeys = true
            }
            return try {
                json.decodeFromString<List<DistanceInfo>>(text)
            } catch (e: Exception) {
                //Log.e("mt05", "StartRemoteToUiMapper#mapDistanceInfo $e")
                emptyList()
            }
        }
        @Deprecated("test method")
        private fun mapDistanceInfoTest(text: String): List<SelectKindsSportItem> {
            val json = Json {
                ignoreUnknownKeys = true
            }
            return try {
                json.decodeFromString<List<SelectKindsSportItem>>(text)
            } catch (e: Exception) {
                Log.e("mt05", "StartRemoteToUiMapper#test $e")
                emptyList()
            }
        }

        fun calculateRemainingSlots(
            distanceInfos: List<DistanceInfo>,
            startMembers: List<StartMemberItem>
        ): List<DistanceInfo> {
            return distanceInfos.map { distanceInfo ->
                val matchingStartMembers = startMembers.filter { it.distance == distanceInfo.value }
                    .filter { it.payment != null }
                val remainingSlots = distanceInfo.distance.slots.toInt() - matchingStartMembers.size
                distanceInfo.copy(distance = distanceInfo.distance.copy(slots = remainingSlots.toString()))
            }
        }

    }

}