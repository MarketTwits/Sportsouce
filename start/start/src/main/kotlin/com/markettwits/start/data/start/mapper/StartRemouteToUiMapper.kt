package com.markettwits.start.data.start.mapper

import android.util.Log
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.start.StartRemote
import com.markettwits.cloud.model.start_album.StartAlbumRemote
import com.markettwits.cloud.model.start_comments.response.StartCommentsRemote
import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.cloud.model.time.TimeRemote
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.core_ui.time.TimePattern
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.membres.list.StartMembersUi
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Locale

interface StartRemoteToUiMapper {

    fun map(
        startRemote: StartRemote,
        startMember: List<StartMemberItem>,
        startAlbumRemote: StartAlbumRemote,
        commentsRemote: StartCommentsRemote,
        timeRemote: TimeRemote
    ): StartItem

    fun map(e: Exception): String
    fun map(commentsRemote: StartCommentsRemote): StartItem.Comments
    fun map(startMember: List<StartMemberItem>, paymentDisabled: Boolean): List<StartMembersUi>

    class Base(
        private val mapper: TimeMapper,
        private val membersToUiMapper: StartMembersToUiMapper,
    ) : StartRemoteToUiMapper {

        override fun map(
            startRemote: StartRemote,
            startMember: List<StartMemberItem>,
            startAlbumRemote: StartAlbumRemote,
            commentsRemote: StartCommentsRemote,
            timeRemote: TimeRemote
        ): StartItem {

            return StartItem(
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
                paymentDisabled = startRemote.start_data.payment_disabled ?: false,
                distanceInfo =
                updateDistanceInfoList(
                    startMember,
                    mapDistanceInfoTest2(startRemote.start_data.select_kinds_sport),
                    mapCurrentTime(timeRemote.date)
                ),
                organizers = startRemote.start_data.organizers,
                membersUi = map(startMember, startRemote.start_data.payment_disabled ?: false),
                commentsRemote = mapComments(commentsRemote),
                conditionFile = if (startRemote.start_data.conditionFile != null) {
                    StartItem.ConditionFile.Base(startRemote.start_data.conditionFile!!.fullPath)
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
                startAlbum = sortStartAlbum(mapStartAlbum(startAlbumRemote))
            )
        }

        private fun mapStartAlbum(startAlbumRemote: StartAlbumRemote): List<StartItem.Album> {
            val photos = startAlbumRemote.rows.flatMap { row ->
                row.photos.map { photoRemote ->
                    StartItem.Album.Photo(
                        id = photoRemote.id,
                        photoId = photoRemote.file_id,
                        imageUrl = photoRemote.file.fullPath,
                        tags = photoRemote.tags.associate { it.id to it.name }
                    )
                }
            }
            val albums = startAlbumRemote.rows.map {
                StartItem.Album(
                    id = it.id,
                    startId = it.start_id,
                    photos = photos,
                    name = it.name,
                    isBeforeStart = it.isBeforeStart
                )
            }
            return albums
        }

        private fun sortStartAlbum(items: List<StartItem.Album>): List<StartItem.Album> {
            return items.map { album ->
                val sortedPhotos = album.photos.sortedByDescending { photo ->
                    photo.tags.isNotEmpty()
                }
                album.copy(photos = sortedPhotos)
            }
        }

        override fun map(e: Exception): String = e.message.toString()
        override fun map(commentsRemote: StartCommentsRemote): StartItem.Comments =
            mapComments(commentsRemote)

        private fun updateDistanceInfoList(
            startMember: List<StartMemberItem>,
            distances: List<DistanceItem>,
            date: String
        ): List<DistanceItem> {
            return distances.map { distanceInfo ->
                when (distanceInfo) {
                    is DistanceItem.DistanceInfo -> distanceInfo.copy(
                        distance = mapDistanceInfo(
                            startMember,
                            distanceInfo,
                            date
                        ),
                    )

                    is DistanceItem.DistanceCombo -> distanceInfo.copy(
                        distances = distanceInfo.distances,
                        price = mapDistanceComboPrice(
                            sale = distanceInfo.sale?.toInt() ?: 0,
                            distances = distanceInfo.distances,
                            date = date,
                        ),
                        value = comboTitle(
                            comboTitle = distanceInfo.value,
                            distances = distanceInfo.distances
                        )
                    )
                }
            }
        }

        private fun mapDistanceInfo(
            startMember: List<StartMemberItem>,
            distanceInfo: DistanceItem.DistanceInfo,
            date: String
        ): DistanceItem.Distance {
            val newPriceValue = getPriceForDate(distanceInfo.distance, date)
            return distanceInfo.distance.copy(
                price = newPriceValue.toString(),
                slots = calculateRemainingSlotsBase(distanceInfo, startMember).slots
            )
        }

        private fun comboTitle(
            comboTitle: String,
            distances: List<DistanceItem.DistanceInfo>
        ): String {
            val distanceTitles = distances.map { it.value }
            return "$comboTitle (${distanceTitles.joinToString(" + ")})"
        }

        private fun mapDistanceComboPrice(
            sale: Int,
            distances: List<DistanceItem.DistanceInfo>,
            date: String
        ): Int {
            val totalPrices = distances
                .map { getPriceForDate(it.distance, date) }
            val sum = totalPrices.sum()
            return calculateDiscountedCost(sum, sale)
        }

        private fun calculateDiscountedCost(originalCost: Int, discountPercentage: Int): Int {
            val discountMultiplier = 1.0 - (discountPercentage.toDouble() / 100.0)
            return (originalCost * discountMultiplier).toInt()
        }

        private fun mapCurrentTime(time: String): String {
            val inputDateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            val date = inputDateFormat.parse(time)
            val outputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US)
            return outputDateFormat.format(date)
        }

        private fun getPriceForDate(
            distance: DistanceItem.Distance,
            date: String
        ): Int {
            if (distance.prices.isEmpty()) {
                return distance.price.toInt()
            }
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault())
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

        override fun map(
            startMember: List<StartMemberItem>,
            paymentDisabled: Boolean
        ): List<StartMembersUi> {
            return membersToUiMapper.maps(startMember, paymentDisabled)
        }

        private fun mapComments(commentsRemote: StartCommentsRemote): StartItem.Comments {
            return StartItem.Comments(
                id = commentsRemote.count,
                rows = commentsRemote.rows.map {
                    StartItem.Comments.Row(
                        id = it.id,
                        comment = it.comment,
                        countSub = it.countSub,
                        createdAt = mapper.mapTime(
                            TimePattern.ddMMMMyyyy,
                            it.createdAt
                        ),
                        personId = it.personId,
                        replies = it.replies?.map { reply ->
                            StartItem.Comments.Reply(
                                id = reply.id,
                                comment = reply.comment,
                                createdAt = mapper.mapTime(
                                    TimePattern.ddMMMMyyyy,
                                    reply.createdAt
                                ),
                                user = StartItem.Comments.User(
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
                        user = StartItem.Comments.User(
                            id = it.user.id,
                            name = it.user.name,
                            surname = it.user.surname,
                            photo = it.user.photo?.fullPath ?: ""
                        ),
                    )
                }
            )
        }

        @Deprecated("test method")
        private fun mapDistanceInfoTest2(text: String): List<DistanceItem> {
            val json = Json {
                isLenient = true
                ignoreUnknownKeys = true
            }
            return try {
                json.decodeFromString<List<DistanceItem>>(text)
            } catch (e: Exception) {
                Log.e("mt05", "DistanceItemTest")
                Log.e("mt05", "DistanceItemTest#test $e")
                emptyList()
            }
        }

        private fun calculateRemainingSlotsBase(
            distanceInfo: DistanceItem.DistanceInfo,
            startMembers: List<StartMemberItem>
        ): DistanceItem.Distance {
            val matchingStartMembers = startMembers.filter { it.distance == distanceInfo.value }
                .filter { it.payment != null }
            val remainingSlots =
                distanceInfo.distance.slots.toInt().minus(matchingStartMembers.size)
            return distanceInfo.distance.copy(slots = remainingSlots.toString())
        }
    }

}