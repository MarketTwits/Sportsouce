package com.markettwits.cloud.model.starts


import com.markettwits.cloud.model.common.KindOfSport
import com.markettwits.cloud.model.common.Season
import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.sportsourcedemo.all.City
import com.markettwits.sportsourcedemo.all.ConditionFile
import com.markettwits.sportsourcedemo.all.PosterLinkFile
import kotlinx.serialization.Serializable

@Serializable
data class Row(
    val afishaLinkFile: String?,
    val afisha_link: String?,
    val archive_start_code: Int?,
    val city: City?,
    val city_id: Int?,
    val close_reg: String?,
    val conditionFile: ConditionFile?,
    val condition_file: Int?,
    val condition_short: String?,
    val coordinates: String?,
    val copernico_id: String?,
    val copernico_preset: String?,
    val createdAt: String,
    val description: String? = "",
    val driving_directions: String?,
    val end_date: String,
    val finish_reg: String?,
    val id: Int,
    val isClosed: Boolean?,
    val isOpen: String,
    val isRescheduled: Boolean?,
    val kindOfSports: List<KindOfSport>,
    val name: String,
    val on_main_page: Boolean?,
    val payment_disabled: String?,
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
    val results: List<Result>,
    val results_link: String?,
    val search_service: String?,
    val season: Season?,
    val season_id: Int?,
    val serial_number: String?,
    val short_name: String?,
    val slug: String?,
    val start_date: String,
    val start_reg: String?,
    val start_status: StartStatus,
    val start_time: String?,
    val status: String?,
    val updatedAt: String,
    val viewsCount: Int
)