package data.mapper.remote

import com.markettwits.random_user.AgeParam
import com.markettwits.random_user.Coordinates
import com.markettwits.random_user.Location
import com.markettwits.random_user.Name
import com.markettwits.random_user.Registered
import com.markettwits.random_user.Street
import com.markettwits.random_user.Timezone
import com.markettwits.random_user.api.model.DobRemote
import com.markettwits.random_user.api.model.LocationRemote
import com.markettwits.random_user.api.model.ResultRemote
import data.mapper.time.TimeMapper
import data.mapper.time.TimePattern

abstract class RandomUsersRemoteToDomainMapperAbstract(private val timeMapper: TimeMapper) :
    RandomUsersRemoteToDomainMapper {
    protected fun mapAgeParam(dobRemote: DobRemote): AgeParam =
        AgeParam(
            date = timeMapper.mapTime(TimePattern.Instance, dobRemote.date),
            age = dobRemote.age
        )

    protected fun mapLocation(locationRemote: LocationRemote): Location =
        Location(
            city = locationRemote.city,
            coordinates = Coordinates(
                latitude = locationRemote.coordinates.latitude,
                longitude = locationRemote.coordinates.longitude
            ),
            country = locationRemote.country,
            state = locationRemote.state,
            street = Street(
                name = locationRemote.street.name,
                number = locationRemote.street.number
            ),
        )

    protected fun mapUserName(nameRemote: ResultRemote.NameRemote): Name =
        Name(
            first = nameRemote.first,
            last = nameRemote.last,
            title = nameRemote.title,
            full = "${nameRemote.title} ${nameRemote.last} ${nameRemote.first}"
        )
}