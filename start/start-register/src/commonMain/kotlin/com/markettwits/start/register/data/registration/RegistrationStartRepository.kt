package com.markettwits.start.register.data.registration


import com.markettwits.start.register.domain.StartPromo

interface RegistrationStartRepository {

    suspend fun promo(value: String, startId: Int,distancesId : List<Int>): Result<StartPromo>

}