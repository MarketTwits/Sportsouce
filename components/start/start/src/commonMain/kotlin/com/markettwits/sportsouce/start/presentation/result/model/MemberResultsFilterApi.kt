package com.markettwits.sportsouce.start.presentation.result.model

class MemberResultsFilterApi {

    fun createInitialFilterState(members: List<MemberResult>): FilterState {
        val distanceFilters = extractDistancesFromMembers(members).mapIndexed { index, filter ->
            DistanceFilter(name = filter, isSelected = index == 0)
        }

        val selectedDistance = distanceFilters.firstOrNull { it.isSelected }?.name

        val groupFilters = members
            .filter { it.distance == selectedDistance }
            .map { it.group }
            .distinct()
            .map {
                GroupFilter(name = it, isSelected = false)
            }

        val teamFilters = extractTeamsFromMembers(members).map {
            TeamFilter(name = it, isSelected = false)
        }

        val genderFilters = extractGendersFromMembers(members).map {
            GenderFilter(name = it, isSelected = false)
        }

        return FilterState(
            distanceFilters = distanceFilters,
            groupFilters = groupFilters,
            teamFilters = teamFilters,
            genderFilters = genderFilters
        )
    }

    fun filterAndSort(
        members: List<MemberResult>,
        filterState: FilterState,
    ): List<MemberResult> {
        return members
            .filter { member -> applyFilters(member, filterState) }
            .sortedWith(createComparator(filterState.sortBy, filterState.sortOrder))
    }

    private fun applyFilters(member: MemberResult, filterState: FilterState): Boolean {
        // Поиск
        if (filterState.searchQuery.isNotBlank()) {
            val query = filterState.searchQuery.lowercase()
            val name = member.name.lowercase()
            if (!name.contains(query)) return false
        }

        // Дистанция
        val selectedDistances = filterState.distanceFilters.filter { it.isSelected }.map { it.name }
        if (selectedDistances.isNotEmpty() && member.distance !in selectedDistances) {
            return false
        }

        // Группа (только если дистанция совпадает)
        val selectedDistance = selectedDistances.firstOrNull()
        if (selectedDistance != null && member.distance == selectedDistance) {
            val selectedGroups = filterState.groupFilters.filter { it.isSelected }.map { it.name }
            if (selectedGroups.isNotEmpty() && member.group !in selectedGroups) {
                return false
            }
        }

        // Команда
        val selectedTeams = filterState.teamFilters.filter { it.isSelected }.map { it.name }
        if (selectedTeams.isNotEmpty() && member.team !in selectedTeams) {
            return false
        }

        // Пол
        val selectedGenders = filterState.genderFilters.filter { it.isSelected }.map { it.name }
        return !(selectedGenders.isNotEmpty() && member.sex !in selectedGenders)
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

    fun extractGendersFromMembers(members: List<MemberResult>): List<String> {
        return members.map { it.sex }.distinct().sorted()
    }
}