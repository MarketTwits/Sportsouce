package com.markettwits.sportsouce.start.data.start.mapper.comments

import com.markettwits.sportsouce.start.cloud.model.comments.response.Comment
import com.markettwits.sportsouce.start.domain.StartItem

internal interface StartCommentsToUiMapper {
    fun map(commentsRemote: List<Comment>): StartItem.Comments
}