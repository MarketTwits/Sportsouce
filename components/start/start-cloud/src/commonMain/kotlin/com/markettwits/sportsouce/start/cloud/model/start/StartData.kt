package com.markettwits.sportsouce.start.cloud.model.start

import com.markettwits.sportsouce.start.cloud.model.start.fields.City
import com.markettwits.sportsouce.start.cloud.model.start.fields.ConditionFile
import com.markettwits.sportsouce.start.cloud.model.start.fields.Discount
import com.markettwits.sportsouce.start.cloud.model.start.fields.KindOfSport
import com.markettwits.sportsouce.start.cloud.model.start.fields.Organizer
import com.markettwits.sportsouce.start.cloud.model.start.fields.PosterLinkFile
import com.markettwits.sportsouce.start.cloud.model.start.fields.Result
import com.markettwits.sportsouce.start.cloud.model.start.fields.Season
import com.markettwits.sportsouce.start.cloud.model.start.fields.SocialNetwork
import com.markettwits.sportsouce.start.cloud.model.start.fields.StartStatus
import com.markettwits.sportsouce.start.cloud.model.start.fields.UsefulLinks
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartData(
    @SerialName("city") val city: City?,
    @SerialName("conditionFile") val conditionFile: ConditionFile?,
    @SerialName("coordinates") val coordinates: String?,
    @SerialName("description") val description: String? = "",
    @SerialName("end_date") val endDate: String,
    @SerialName("finish_reg") val finishReg: String?,
    @SerialName("hashtags") val hashtags: List<String>?,
    @SerialName("id") val id: Int,
    @SerialName("isClosed") val isClosed: Boolean?,
    @SerialName("kindOfSports") val kindOfSports: List<KindOfSport>,
    @SerialName("name") val name: String,
    @SerialName("discount") val discount: List<Discount>,
    @SerialName("organizers") val organizers: List<Organizer>,
    @SerialName("payment_disabled") val paymentDisabled: Boolean?,
    @SerialName("payment_type") val paymentType: String?,
    @SerialName("posterLinkFile") val posterLinkFile: PosterLinkFile?,
    @SerialName("reg_link") val regLink: String?,
    @SerialName("reg_on_site") val regOnSite: Boolean?,
    @SerialName("registration_end_date") val registrationEndDate: String,
    @SerialName("registration_start_date") val registrationStartDate: String,
    @SerialName("results") val results: List<Result> = emptyList(),
    @SerialName("season") val season: Season?,
    @SerialName("select_kinds_sport") val selectKindsSport: String,
    @SerialName("serial_number") val serialNumber: String?,
    @SerialName("short_name") val shortName: String,
    @SerialName("slug") val slug: String?,
    @SerialName("social_networks") val socialNetworks: List<SocialNetwork?>,
    @SerialName("start_date") val startDate: String,
    @SerialName("start_reg") val startReg: String?,
    @SerialName("start_status") val startStatus: StartStatus,
    @SerialName("start_time") val startTime: String?,
    @SerialName("status") val status: String?,
    @SerialName("useful_links") val usefulLinks: List<UsefulLinks>?,
    @SerialName("viewsCount") val viewsCount: Int
)