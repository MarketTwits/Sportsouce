package com.markettwits.sportsouce.starts.common.domain

enum class StartStatus(val id: Int) {
    PENDING(1),
    ANNOUNCEMENT(2),
    REGISTRATION_OPEN(3),
    WAITING(4),
    IS_PASSING(5),
    ENDED(6),
}
