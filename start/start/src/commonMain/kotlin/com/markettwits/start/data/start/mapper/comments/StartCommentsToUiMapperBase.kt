package com.markettwits.start.data.start.mapper.comments

import com.markettwits.cloud.model.start_comments.response.StartCommentsRemote
import com.markettwits.start.domain.StartItem
import com.markettwits.time.TimeMapper
import com.markettwits.time.TimePattern

internal class StartCommentsToUiMapperBase(
    private val timeMapper: TimeMapper
) : StartCommentsToUiMapper {
    override fun map(commentsRemote: StartCommentsRemote): StartItem.Comments = StartItem.Comments(
        id = commentsRemote.count,
        rows = commentsRemote.rows.map {
            StartItem.Comments.Row(
                id = it.id,
                comment = it.comment,
                countSub = it.countSub,
                createdAt = timeMapper.mapTime(
                    TimePattern.FullWithEmptySpace,
                    it.createdAt
                ),
                personId = it.personId,
                replies = it.replies?.map { reply ->
                    StartItem.Comments.Reply(
                        id = reply.id,
                        comment = reply.comment,
                        createdAt = timeMapper.mapTime(
                            TimePattern.FullWithEmptySpace,
                            reply.createdAt
                        ),
                        user = StartItem.Comments.User(
                            id = reply.user.id,
                            name = reply.user.name,
                            surname = reply.user.surname,
                            photo = reply.user.photo?.fullPath ?: ""
                        ),
                    )
                } ?: emptyList(),
                updatedAt = timeMapper.mapTime(
                    TimePattern.FullWithEmptySpace,
                    it.updatedAt
                ),
                user = StartItem.Comments.User(
                    id = it.user.id,
                    name = it.user.name,
                    surname = it.user.surname,
                    photo = it.user.photo?.fullPath ?: ""
                ),
            )
        }
    )
}