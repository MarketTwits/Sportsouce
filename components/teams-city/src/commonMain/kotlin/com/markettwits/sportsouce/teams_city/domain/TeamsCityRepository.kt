package com.markettwits.sportsouce.teams_city.domain

import kotlinx.coroutines.flow.Flow

interface TeamsCityRepository {

    suspend fun cityFlow(): Flow<List<City>>

    suspend fun teamsFlow(): Flow<List<Team>>

    suspend fun city(withStarts : Boolean = false): Result<List<City>>

    suspend fun teams(): Result<List<Team>>

}