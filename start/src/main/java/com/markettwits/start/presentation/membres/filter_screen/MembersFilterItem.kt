package com.markettwits.start.presentation.membres.filter_screen

import androidx.compose.runtime.Immutable

@Immutable
sealed class MembersFilterItem(open val title: String){
    data class Selected(override val title : String) : MembersFilterItem(title)
    data class Base(override val title : String) : MembersFilterItem(title)
}
@Immutable
data class MembersFilterGroup(
    val title: String,
    val items : List<MembersFilterItem>
)