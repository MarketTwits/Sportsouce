package data.mapper.remote

import com.markettwits.random_user.RandomUser
import com.markettwits.random_user.api.model.ContactsRemote
import data.mapper.time.TimeMapper

class RandomUsersRemoteToDomainMapperImpl(timeMapper: TimeMapper) : RandomUsersRemoteToDomainMapperAbstract(timeMapper) {
    override suspend fun map(remote: ContactsRemote): List<RandomUser> =
        remote.results.map { user ->
            RandomUser(
                cell = user.cell,
                ageParam = mapAgeParam(user.dob),
                email = user.email,
                gender = user.gender,
                id = user.id.value ?: "",
                location = mapLocation(user.location),
                name = mapUserName(user.name),
                phone = user.phone,
                picture = user.picture.large,
            )
        }
}