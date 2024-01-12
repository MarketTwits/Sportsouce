package com.markettwits.schedule.schedule.data

import android.util.Log
import com.markettwits.starts.presentation.StartsListItem
import java.time.LocalDate

import java.time.format.DateTimeFormatter

interface StartScheduleMapper {
    fun map(items: List<StartsListItem>) : List<StartsListItem>
}
class StartScheduleMapperBase : StartScheduleMapper {
    override fun map(items: List<StartsListItem>) : List<StartsListItem> {
        val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

        // Sort the list by days of the week
        val sortedList = items.sortedBy { item ->
            val itemDate = LocalDate.parse(item.date, dateFormatter)
            val currentDate = LocalDate.now()

            // Calculate the difference in days between the item date and the current date
            val dayDifference = itemDate.until(currentDate, java.time.temporal.ChronoUnit.DAYS)

            // Ensure the day difference is non-negative
            val adjustedDayDifference = if (dayDifference < 0) dayDifference + 7 else dayDifference

            // Return the adjusted day difference to be used for sorting
            adjustedDayDifference
        }

        // Print the sorted list
        sortedList.forEach { Log.e("mt05", it.name) }
        return sortedList
    }
}