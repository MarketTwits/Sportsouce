package com.markettwits.start.presentation.membres.filter

import kotlinx.serialization.Serializable


@Serializable
sealed interface MembersFilterItem {
    val title: String

    @Serializable
    data class Selected(override val title: String) : MembersFilterItem

    @Serializable
    data class Base(override val title: String) : MembersFilterItem
}

@Serializable
data class MembersFilterGroup(
    val title: String,
    val items: List<MembersFilterItem>
)
