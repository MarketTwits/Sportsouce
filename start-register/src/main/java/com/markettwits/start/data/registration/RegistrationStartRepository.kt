package com.markettwits.start.data.registration



import com.markettwits.cloud.ext_model.DistanceInfo
import com.markettwits.start.domain.StartRegistryResult
import com.markettwits.start.domain.StartStatement

interface RegistrationStartRepository {
    suspend fun preload(price: String): Result<StartStatement>
    suspend fun save(
        statement: StartStatement,
        distanceInfo: DistanceInfo,
        startId: Int
    ): StartRegistryResult
    suspend fun pay(
        statement: StartStatement,
        distanceInfo: DistanceInfo,
        startId: Int
    ): StartRegistryResult

}