package detail.domain

interface LaunchFeature {
    fun sendEmail(email : String)
    fun openMap(coordinates : String)
    fun openGeoOnMap(latitude : String)
    fun openPhone(phone : String)
}