package com.markettwits.start.domain

import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import com.markettwits.start_cloud.model.start.fields.Distance
import com.markettwits.start_cloud.model.start.fields.DistinctDistance

data class StartItem(
    val id: Int,
    val title: String,
    val startPlace: String,
    val image: String,
    val startStatus: StartStatus,
    val startData: String,
    val startAlbum: List<Album>,
    val startTime: String,
    val description: String,
    val paymentDisabled: Boolean,
    val regLink: String,
    val distanceInfoNew : List<DistinctDistance>,
    val distanceMapNew : List<Distance>,
    val paymentType: String,
    val organizers: List<com.markettwits.start_cloud.model.start.fields.Organizer>,
    val membersUi: List<StartMembersUi>,
    val conditionFile: ConditionFile,
    val commentsRemote: Comments,
    val result: List<Result>,
    val usefulLinks: List<Result>,
    val startTimes: StartTimes,
) {
    sealed class ConditionFile {
        data class Base(val url: String) : ConditionFile()
        data object Empty : ConditionFile()
    }

    data class StartStatus(
        val code : Int,
        val name : String
    )

    data class Album(
        val id: Int,
        val startId: Int,
        val photos: List<Photo>,
        val name: String,
    ) {
        data class Photo(
            val id: Int,
            val photoId: Int,
            val imageUrl: String,
            val tags: Map<Int, String>,
        )
    }

    data class StartTimes(
        val beginningRegistry: String,
        val endRegistry: String,
        val beginningStart: String,
        val endStart: String,
    )

    data class Result(
        val id: Int,
        val name: String,
        val url: String
    )

    data class Comments(
        val id: Int,
        val rows: List<Row>
    ) {
        data class Row(
            val comment: String,
            val countSub: Int,
            val createdAt: String,
            val id: Int,
            val personId: String,
            val replies: List<Reply>,
            val updatedAt: String,
            val user: User
        )

        data class User(
            val id: Int,
            val name: String,
            val photo: String?,
            val surname: String
        )

        data class Reply(
            val comment: String,
            val createdAt: String,
            val id: Int,
            val user: User
        )
    }
}