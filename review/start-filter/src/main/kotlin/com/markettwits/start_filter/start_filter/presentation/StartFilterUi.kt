package com.markettwits.start_filter.start_filter.presentation

import kotlinx.serialization.Serializable

@Serializable
data class StartFilterUi(
    val items: List<FilterGroupItemUi>,
) {
    @Serializable
    data class FilterGroupItemUi(
        val label : String,
        val type: FilterStartType,
        val selected: List<String> = emptyList(),
        val list: List<String>
    ){
        fun checkSelected(item : String) = selected.contains(item)
    }
    @Serializable
    sealed interface FilterStartType {
        @Serializable
        object Calendar : FilterStartType
        @Serializable
        object DropDown : FilterStartType
        @Serializable
        object Dialog : FilterStartType
    }

}