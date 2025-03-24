package com.markettwits.start.data.start.mapper.comments

import com.markettwits.start.domain.StartItem
import com.markettwits.start_cloud.model.comments.response.Comment

internal interface StartCommentsToUiMapper {
    fun map(commentsRemote: List<Comment>): StartItem.Comments
}