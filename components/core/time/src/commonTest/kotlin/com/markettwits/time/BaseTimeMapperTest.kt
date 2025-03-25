package com.markettwits.time

import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.core.time.TimeMapper
import com.markettwits.core.time.TimePattern
import kotlin.test.Test
import kotlin.test.assertEquals


class BaseTimeMapperTest {

    private val mapper : TimeMapper = BaseTimeMapper()

    @Test
    fun map_cloud_to_full() {
        val time = mapper.mapTime(TimePattern.Full, UTC_TIME_VALUE)
        assertEquals("14.05.2005 00:00", time)
    }

    @Test
    fun map_cloud_to_full_with_dots() {
        val time = mapper.mapTime(TimePattern.FullWithDots, UTC_TIME_VALUE)
        assertEquals("14.05.2005", time)
    }

    @Test
    fun map_cloud_to_full_with_empty_space() {
        val time = mapper.mapTime(TimePattern.FullWithEmptySpace, UTC_TIME_VALUE)
        assertEquals("14 мая 2005", time)
    }

    @Test
    fun map_time_to_cloud(){
        val t1 = mapper.mapTimeToCloud(TimePattern.FullWithEmptySpace, "14 мая 2005")
        assertEquals(UTC_TIME_VALUE, t1)
        val t2 = mapper.mapTimeToCloud(TimePattern.FullWithDots, "14.05.2005")
        assertEquals(UTC_TIME_VALUE, t2)
        val t3 = mapper.mapTimeToCloud(TimePattern.Full,"14.05.2005 00:00")
        assertEquals(UTC_TIME_VALUE, t3)
    }


    private companion object{
        const val UTC_TIME_VALUE = "2005-05-14T00:00:00Z"
    }

}