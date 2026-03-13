package com.markettwits.sportsouce.starts.common.data.mapper

import com.markettwits.core.time.TimeMapper
import com.markettwits.core.time.TimePattern
import com.markettwits.sportsouce.starts.cloud.model.NetworkKindOfSport
import com.markettwits.sportsouce.starts.cloud.model.NetworkOrganizerPhotoRemoteItem
import com.markettwits.sportsouce.starts.cloud.model.NetworkOrganizerRemoteItem
import com.markettwits.sportsouce.starts.cloud.model.NetworkStartRemoteItem
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

internal abstract class StartsCloudToListMapperAbstract(private val timeMapper: TimeMapper) :
    StartsCloudToListMapper {
    override fun mapSingle(items: List<NetworkStartRemoteItem>): List<StartsListItem> {
        val resultLists = items.map {
            StartsListItem(
                id = it.id,
                name = it.name,
                image = it.posterLinkFile?.fullPath ?: "",
                date = timeMapper.mapTime(TimePattern.FullWithEmptySpace, it.start_date),
                statusCode = StartsListItem.StatusCode(
                    it.start_status.code,
                    it.start_status.name
                ),
                place = it.coordinates ?: "",
                distance = it.condition_short ?: "",
                kindOfSports = mapKindOfSports(it.kindOfSports),
                organizers = mapOrganizers(it.organizers),
                slots = mapSlots(it.open_slots_sum, it.total_slots_sum),
                onMainPage = it.on_main_page ?: false,
                views = it.viewsCount,
                description = it.description ?: "",
                slug = it.slug ?: ""
            )
        }
        return resultLists
    }

    private fun mapKindOfSports(kindOfSports: List<NetworkKindOfSport>): List<StartsListItem.KindOfSport> =
        kindOfSports.map {
            StartsListItem.KindOfSport(
                id = it.id,
                name = it.name
            )
        }

    private fun mapOrganizers(
        organizers: List<NetworkOrganizerRemoteItem>?,
    ): List<StartsListItem.Organizer> {
        if (organizers.isNullOrEmpty()) return emptyList()

        return organizers.map { organizer ->
            StartsListItem.Organizer(
                name = organizer.name.orEmpty(),
                phone = organizer.phone.orEmpty(),
                isMain = organizer.is_main ?: false,
                photo = organizer.photo?.toDomainPhotoOrNull()
            )
        }
    }

    private fun mapSlots(
        openSlots: JsonElement?,
        totalSlots: JsonElement?,
    ): StartsListItem.Slots? {
        val open = openSlots.toNormalizedString()
        val total = totalSlots.toNormalizedString()

        if (open == null && total == null) return null

        val openNormalized = open ?: "0"
        val totalNormalized = total ?: ""

        return StartsListItem.Slots(
            openSlots = openNormalized,
            totalSlots = totalNormalized,
            isAvailable = openNormalized.toIntOrNull()?.let { it > 0 } ?: false
        )
    }

    private fun NetworkOrganizerPhotoRemoteItem.toDomainPhotoOrNull(): StartsListItem.OrganizerPhoto? {
        val fullPath = fullPath.orEmpty().trim()
        if (fullPath.isEmpty()) return null

        return StartsListItem.OrganizerPhoto(
            fullPath = fullPath,
            id = id ?: 0,
            name = name.orEmpty(),
            path = path.orEmpty(),
            extension = extension.orEmpty()
        )
    }

    private fun JsonElement?.toNormalizedString(): String? {
        val primitive = this as? JsonPrimitive ?: return null
        if (primitive is JsonNull) return null

        val rawValue = if (primitive.isString) {
            primitive.contentOrNull
        } else {
            primitive.toString()
        }

        return rawValue?.trim()?.takeIf { it.isNotEmpty() && it.lowercase() != "null" }
    }
}
