package ru.alexpanov.core_network.api

import ru.alexpanov.core_network.model.all.StartsCloud

interface SportsouceApi {
    suspend fun fetchStarts() : StartsCloud
    suspend fun fetchStartsWithFilter() : StartsCloud
}