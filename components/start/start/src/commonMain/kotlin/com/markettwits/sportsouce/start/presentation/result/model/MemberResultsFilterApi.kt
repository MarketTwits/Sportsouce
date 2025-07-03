package com.markettwits.sportsouce.start.presentation.result.model

class MemberResultsFilterApi {

    fun createInitialFilterState(members: List<MemberResult>): FilterState {
        return FilterState(
            distanceFilters = extractDistancesFromMembers(members).map {
                DistanceFilter(name = it, isSelected = false)
            },
            groupFilters = extractGroupsFromMembers(members).map {
                GroupFilter(name = it, isSelected = false)
            },
            teamFilters = extractTeamsFromMembers(members).map {
                TeamFilter(name = it, isSelected = false)
            }
        )
    }

    fun filterAndSort(
        members: List<MemberResult>,
        filterState: FilterState
    ): List<MemberResult> {
        return members
            .filter { member -> applyFilters(member, filterState) }
            .sortedWith(createComparator(filterState.sortBy, filterState.sortOrder))
    }

    private fun applyFilters(member: MemberResult, filterState: FilterState): Boolean {
        // Search filter
        if (filterState.searchQuery.isNotBlank()) {
            val query = filterState.searchQuery.lowercase()
            val name = member.name.lowercase()
            if (!name.contains(query)) return false
        }

        // Gender filter
        if (filterState.genderFilter.value != null &&
            member.sex != filterState.genderFilter.value) {
            return false
        }

        // Distance filter
        val selectedDistances = filterState.distanceFilters.filter { it.isSelected }.map { it.name }
        if (selectedDistances.isNotEmpty() && member.distance !in selectedDistances) {
            return false
        }

        // Group filter
        val selectedGroups = filterState.groupFilters.filter { it.isSelected }.map { it.name }
        if (selectedGroups.isNotEmpty() && member.group !in selectedGroups) {
            return false
        }

        // Team filter
        val selectedTeams = filterState.teamFilters.filter { it.isSelected }.map { it.name }
        if (selectedTeams.isNotEmpty() && member.team !in selectedTeams) {
            return false
        }

        return true
    }

    private fun createComparator(sortBy: SortBy, sortOrder: SortOrder): Comparator<MemberResult> {
        val comparator = when (sortBy) {
            SortBy.NAME -> compareBy<MemberResult> { it.name }
            SortBy.RESULT -> compareBy<MemberResult> { parseResult(it.result) }
            SortBy.START_NUMBER -> compareBy<MemberResult> { it.startId }
        }

        return if (sortOrder == SortOrder.ASC) comparator else comparator.reversed()
    }

    private fun parseResult(result: String): Long {
        // Parse time format (e.g., "1:23:45" -> milliseconds)
        return try {
            val parts = result.split(":")
            when (parts.size) {
                1 -> parts[0].toLong() * 1000 // seconds
                2 -> parts[0].toLong() * 60000 + parts[1].toLong() * 1000 // mm:ss
                3 -> parts[0].toLong() * 3600000 + parts[1].toLong() * 60000 + parts[2].toLong() * 1000 // hh:mm:ss
                else -> 0L
            }
        } catch (e: Exception) {
            Long.MAX_VALUE // Put invalid results at the end
        }
    }

    fun extractGroupsFromMembers(members: List<MemberResult>): List<String> {
        return members.map { it.group }.distinct().sorted()
    }

    fun extractTeamsFromMembers(members: List<MemberResult>): List<String> {
        return members.map { it.team }.distinct().sorted()
    }

    fun extractDistancesFromMembers(members: List<MemberResult>): List<String> {
        return members.map { it.distance }.distinct().sorted()
    }
}