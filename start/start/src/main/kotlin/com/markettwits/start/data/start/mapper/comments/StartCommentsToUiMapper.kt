package com.markettwits.start.data.start.mapper.comments

import com.markettwits.cloud.model.start_comments.response.StartCommentsRemote
import com.markettwits.start.domain.StartItem

internal interface StartCommentsToUiMapper {
    fun map(commentsRemote: StartCommentsRemote): StartItem.Comments
}