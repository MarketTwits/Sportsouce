package com.markettwits.sportsouce.start.search.filter

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.markettwits.sportsouce.start.search.filter.data.mapper.StartFilterDomainToRemoteMapper
import com.markettwits.sportsouce.start.search.filter.domain.StartFilter
import com.markettwits.sportsouce.start.search.filter.domain.StartFilterRepository
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterUi
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterUi.Key
import com.markettwits.sportsouce.start.search.filter.presentation.store.StartFilerStoreFactory
import com.markettwits.sportsouce.start.search.filter.presentation.store.StartFilterStore.Intent
import com.markettwits.sportsouce.start.search.filter.presentation.store.StartFilterStore.Label
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class StartSearchFiltersTest {
    private val from = LocalDate(2026, 10, 1)
    private val to = LocalDate(2026, 10, 31)

    @Test
    fun reorderedFiltersAndOpenEndedPeriodReachTheSearchRequest() {
        val draft = StartFilterUi(
            items = listOf(
                option(Key.Organizer, "Спорт Союз"),
                option(Key.City, "Новосибирск"),
                option(Key.Status, "Прошедшие"),
                option(Key.Sport, "бег"),
                option(Key.Season, "Осень 2026", legacy = true),
            ),
            fromDate = from,
        )
        val mapper = StartFilterDomainToRemoteMapper.Base()
        val request = mapper.map(draft, "Трейл", StartFilter.Sorted.LastBefore)
        assertEquals("Спорт Союз", request["organizerName"])
        assertEquals("Новосибирск", request["city"])
        assertEquals("0,6", request["status"])
        assertEquals("бег", request["kindOfSports"])
        assertEquals("Осень 2026", request["name"])
        assertEquals("Трейл", request["filterText"])
        assertEquals("start_date DESC", request["sorting"])
        assertEquals("2026-10-01", request["fromDate"])
        assertFalse("toDate" in request)

        assertEquals("2026-10-31", mapper.map(draft.copy(toDate = to), "")["toDate"])
        val cleared = draft.cleared()
        assertTrue(cleared.filterIsEmpty())
        assertEquals(setOf("group", "maxResultCount", "sorting"), mapper.map(cleared, "").keys)
    }

    @Test
    fun invalidPeriodDoesNotApplyAndCorrectedPeriodKeepsOrganizerAndSort() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        try {
            val repository = object : StartFilterRepository {
                override suspend fun filter() = flowOf(
                    StartFilter(emptyList(), emptyList(), emptyList(), emptyList(), StartFilter.Sorted.Popular),
                )
            }
            val initial = StartFilterUi(listOf(option(Key.Organizer, "Спорт Союз"), option(Key.Sort, "Сначала поздние")))
            val store = StartFilerStoreFactory(DefaultStoreFactory(), repository).create(initial)
            try {
                val labels = mutableListOf<Label>()
                backgroundScope.launch(dispatcher) { store.labels.collect { labels += it } }
                store.accept(Intent.SetFromDate(to))
                store.accept(Intent.SetToDate(from))
                assertTrue(store.state.filter.hasInvalidPeriod)
                store.accept(Intent.Apply)
                assertTrue(labels.isEmpty())

                store.accept(Intent.SetFromDate(from))
                store.accept(Intent.SetToDate(to))
                store.accept(Intent.Apply)
                val applied = assertIs<Label.Apply>(labels.single())
                assertEquals(StartFilter.Sorted.LastBefore, applied.sorted)
                assertEquals("Спорт Союз", applied.item.item(Key.Organizer)?.selected?.single())
                assertEquals(from, applied.item.fromDate)
                assertEquals(to, applied.item.toDate)

                store.accept(Intent.Reset)
                assertTrue(store.state.filter.filterIsEmpty())
            } finally {
                store.dispose()
            }
        } finally {
            Dispatchers.resetMain()
        }
    }

    private fun option(key: Key, value: String, legacy: Boolean = false) = StartFilterUi.FilterGroupItemUi(
        label = key.label,
        type = StartFilterUi.FilterStartType.DropDown,
        selected = listOf(value),
        list = listOf(value),
        key = key.takeUnless { legacy },
    )
}
