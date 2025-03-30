package com.markettwits.sportsouce.profile.members.member_detail.domain

interface MemberDetailUseCase {
    suspend fun deleteMember(memberId: Int): Result<Unit>
}