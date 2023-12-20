package com.markettwits.cloud.model.start

import com.markettwits.cloud.model.common.KindOfSport
import com.markettwits.cloud.model.common.Season
import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.sportsourcedemo.all.PosterLinkFile
import kotlinx.serialization.Serializable

@Serializable
data class StartData(
    val afishaLinkFile: Int?,
    val afisha_link: Int?,
    val age_counting: String,
    val age_groups: List<String>?,
    val archive_start_code: Int?,
    val city: City,
    val city_id: Int,
    val close_reg: String?,
    val conditionFile: ConditionFile?,
    val condition_file: Int?,
    val condition_short: String?,
    val coordinates: String,
    val copernico_id: Int?,
    val copernico_preset: Int?,
    val createdAt: String,
    val days: List<Int>?,
    val description: String? = "",
    val discount: List<Discount>,
    val driving_directions: Int?,
    val end_date: String,
    val finish_reg: String?,
    val hashtags: List<String>?,
    val id: Int,
    val isClosed: Boolean,
  //  val isOpen: Boolean,
    val isRescheduled: Boolean,
    val kindOfSports: List<KindOfSport>,
    val name: String,
    val on_main_page: Boolean?,
    val organizers: List<Organizer>,
    val payment_disabled: Boolean?,
    val payment_type: String?,
    val photos_link: String?,
    val posterLinkFile: PosterLinkFile?,
    val poster_link: Int?,
    val rang: String?,
    val reg_link: String?,
    val reg_on_loc: String?,
    val reg_on_site: Boolean?,
    val registration_end_date: String,
    val registration_start_date: String,
    val result_columns_json: String?,
    val result_json: String?,
    val results: List<Result> = emptyList(),
    val results_link: String?,
    val search_service: String?,
    val season: Season,
    val season_id: Int,
    val select_kinds_sport: String,
    val serial_number: Int?,
    val short_name: String,
    val slug: String?,
    val smallImageFile: String?,
    val small_image: String?,
    val social_networks: List<SocialNetwork?>,
    val sponsors: List<String>?,
    val start_date: String,
    val start_reg: String?,
    val start_status: StartStatus,
    val start_time: String?,
    val status: String?,
    val updatedAt: String,
    val useful_links: List<UsefulLinks>?,
    val viewsCount: Int
)