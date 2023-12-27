package com.markettwits.start.presentation.membres.filter_screen

import kotlinx.serialization.Serializable

//@Serializable
//sealed class MembersFilterItem(open val title: String){
//    data class Selected(override val title : String) : MembersFilterItem(title)
//    data class Base(override val title : String) : MembersFilterItem(title)
//}
@Serializable
sealed interface MembersFilterItem{
    val title : String
    @Serializable
    data class Selected(override val title : String) : MembersFilterItem
    @Serializable
    data class Base(override val title : String) : MembersFilterItem
}
@Serializable
data class MembersFilterGroup(
    val title: String,
    val items : List<MembersFilterItem>
)
