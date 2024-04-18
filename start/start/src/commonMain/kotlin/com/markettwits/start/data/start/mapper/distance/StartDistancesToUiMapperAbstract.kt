package com.markettwits.start.data.start.mapper.distance

import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.start_member.StartMemberItem
import java.text.SimpleDateFormat
import java.util.Locale

internal abstract class StartDistancesToUiMapperAbstract : StartDistancesToUiMapper {
    protected fun mapDistanceInfo(
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

    protected fun mapComboTitle(
        comboTitle: String,
        distances: List<DistanceItem.DistanceInfo>
    ): String {
        val distanceTitles = distances.map { it.value }
        return "$comboTitle (${distanceTitles.joinToString(" + ")})"
    }

    protected fun mapPriceForDistanceCombo(
        sale: Int,
        distances: List<DistanceItem.DistanceInfo>,
        date: String
    ): Int {
        val totalPrices = distances.sumOf { getPriceForDate(it.distance, date) }
        return calculateDiscountedCost(totalPrices, sale)
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

    private fun calculateDiscountedCost(originalCost: Int, discountPercentage: Int): Int {
        val discountMultiplier = 1.0 - (discountPercentage.toDouble() / 100.0)
        return (originalCost * discountMultiplier).toInt()
    }

    protected fun mapCurrentTime(time: String): String {
        val inputDateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val date = inputDateFormat.parse(time)
        val outputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US)
        return outputDateFormat.format(date)
    }

}