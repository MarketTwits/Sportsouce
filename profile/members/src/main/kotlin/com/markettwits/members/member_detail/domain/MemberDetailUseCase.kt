package com.markettwits.members.member_detail.domain

interface MemberDetailUseCase {
    suspend fun deleteMember(memberId: Int): Result<Unit>
}