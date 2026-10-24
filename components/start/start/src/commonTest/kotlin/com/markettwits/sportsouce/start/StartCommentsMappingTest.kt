package com.markettwits.sportsouce.start

import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.sportsouce.start.cloud.model.comments.response.Comment
import com.markettwits.sportsouce.start.cloud.model.comments.response.Reply
import com.markettwits.sportsouce.start.cloud.model.comments.response.UserComment
import com.markettwits.sportsouce.start.data.start.mapper.comments.StartCommentsToUiMapperBase
import com.markettwits.sportsouce.start.presentation.start.component.toCommentData
import kotlin.test.*

class StartCommentsMappingTest {
    @Test fun serverFlagsAndRatingsSurviveMappingAndReplyNavigation() {
        val date = "2026-10-24T04:48:03.000Z"
        val user = UserComment(id = 1, name = "Имя", surname = "Фамилия")
        val remote = Comment(id = 172, comment = "Вопрос", countSub = 1, personId = "1", startId = 318,
            createdAt = date, updatedAt = date, user = user,
            isQuestion = true, isAnswered = false, isReview = true, averageScore = "4.5",
            replies = listOf(Reply(comment = "Ответ", createdAt = date, id = 84, user = user, isAdminAnswer = true)))
        val row = StartCommentsToUiMapperBase(BaseTimeMapper()).map(listOf(remote)).rows.single()
        assertTrue(row.isQuestion)
        assertFalse(row.isAnswered, "Only the server's parent status determines the answered filter")
        assertTrue(row.isReview)
        assertEquals(4.5, row.averageScore)
        val navigationData = row.toCommentData()
        assertTrue(navigationData.isQuestion)
        assertFalse(navigationData.isAnswered)
        assertTrue(navigationData.isReview)
        assertEquals(4.5, navigationData.averageScore)
        assertEquals(84, navigationData.replies.single().id)
        assertTrue(navigationData.replies.single().isAdminAnswer)
    }
}
