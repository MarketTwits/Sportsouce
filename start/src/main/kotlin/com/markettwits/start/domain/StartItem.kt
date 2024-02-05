package com.markettwits.start.domain

import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.cloud.model.start.Organizer
import com.markettwits.start.presentation.membres.list.StartMembersUi

data class StartItem(
    val id: Int,
    val title: String,
    val startPlace: String,
    val image: String,
    val startStatus: StartStatus,
    val startData: String,
    val startTime: String,
    val description: String,
    val paymentDisabled : Boolean,
    val distanceInfo: List<DistanceItem>,
    val paymentType: String,
    val organizers: List<Organizer>,
    val membersUi: List<StartMembersUi>,
    val conditionFile: ConditionFile,
    val commentsRemote: Comments,
    val result: List<Result>,
    val usefulLinks: List<Result>
) {
    sealed class ConditionFile {
        data class Base(val url: String) : ConditionFile()
        object Empty : ConditionFile()
    }

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