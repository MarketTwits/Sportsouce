package com.markettwits.club.info.domain

import com.markettwits.club.info.domain.models.ClubInfo
import com.markettwits.club.info.domain.models.Trainer

interface ClubInfoRepository {
    suspend fun trainers(): Result<List<Trainer>>
    suspend fun clubInfo(): Result<List<ClubInfo>>
}