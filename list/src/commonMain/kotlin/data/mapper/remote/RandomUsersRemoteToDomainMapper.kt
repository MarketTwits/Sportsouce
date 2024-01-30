package data.mapper.remote

import com.markettwits.random_user.RandomUser
import com.markettwits.random_user.api.model.ContactsRemote

interface RandomUsersRemoteToDomainMapper {
    suspend fun map(remote: ContactsRemote): List<RandomUser>
}