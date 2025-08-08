package com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model

enum class SignUpStage(val index: Int) {
    FIRST(1), SECOND(2), THIRD(3);

    companion object {
        fun fromIndex(index: Int) = when (index) {
            FIRST.index -> FIRST
            SECOND.index -> SECOND
            THIRD.index -> THIRD
            else -> FIRST
        }
    }
}