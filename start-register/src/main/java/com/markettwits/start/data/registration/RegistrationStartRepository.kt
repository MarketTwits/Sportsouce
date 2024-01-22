package com.markettwits.start.data.registration



import com.markettwits.cloud.ext_model.DistanceInfo
import com.markettwits.start.domain.StartPromo
import com.markettwits.start.domain.StartRegistryResult
import com.markettwits.start.domain.StartStatement

interface RegistrationStartRepository {
    suspend fun preload(price: String, paymentDisabled : Boolean): Result<StartStatement>
    suspend fun promocode(value : String, startId: Int) : Result<StartPromo>
    suspend fun save(
        statement: StartStatement,
        distanceInfo: DistanceInfo,
        startId: Int
    ): Result<StartRegistryResult>
    suspend fun pay(
        statement: StartStatement,
        distanceInfo: DistanceInfo,
        startId: Int
    ): Result<StartRegistryResult>

}