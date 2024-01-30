package detail.domain

class LaunchFeatureMock : LaunchFeature {
    override fun sendEmail(email: String) = Unit

    override fun openMap(coordinates: String) = Unit

    override fun openGeoOnMap(latitude: String) = Unit

    override fun openPhone(phone: String) = Unit
}