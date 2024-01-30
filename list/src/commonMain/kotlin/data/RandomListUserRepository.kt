package data

import com.markettwits.random_user.RandomUser


interface RandomListUserRepository {
    suspend fun randomUser(forced : Boolean) : Result<List<RandomUser>>
    suspend fun launch(): List<RandomUser>
}