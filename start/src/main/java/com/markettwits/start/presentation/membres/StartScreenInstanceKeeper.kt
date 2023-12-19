package com.markettwits.start.presentation.membres

import android.util.Log
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.start.data.StartDataSource
import com.markettwits.start.presentation.start.StartItemUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartMembersScreenInstanceKeeper(
    private val service: StartDataSource,
    private val startId: Int,
    private val membersUi: List<StartMembersUi>
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(Dispatchers.Main)
    val start: MutableValue<List<StartMembersUi>> = MutableValue(emptyList())

    init {
        start.value = membersUi
    }

    fun filter(value: String) {
        Log.d("mt05", value)
        Log.d("mt05", start.value.toString())
        if (value.isEmpty()) {
            start.value = membersUi
        } else {
            val filteredMembers = membersUi.filter { it.name.contains(value, ignoreCase = true) || it.surname.contains(value, ignoreCase = true) }
            val sortedMembers = filteredMembers.sortedWith(compareBy({ it.name }, { it.surname }))
            start.value = sortedMembers
        }
    }
}