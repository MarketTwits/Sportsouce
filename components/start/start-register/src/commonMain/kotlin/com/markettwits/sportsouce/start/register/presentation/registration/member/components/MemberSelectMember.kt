package com.markettwits.sportsouce.start.register.presentation.registration.member.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.textField.DropDownSpinner
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember

@Suppress("NonSkippableComposable")
@Composable
fun MemberSelectMember(
    modifier: Modifier = Modifier,
    selectedNameSurname: String,
    members: List<ProfileMember>,
    onMemberSelected: (ProfileMember) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        DropDownSpinner(
            selectedItem = null,
            onItemSelected = { index, user ->
                if (user != null) onMemberSelected(members[index])
            },
            itemList = members.map { "${it.surname} ${it.name}" },
        ) {
            OutlinedTextFieldBase(
                label = "Выберите из существующих участников",
                value = selectedNameSurname,
                isEnabled = false,
                onValueChange = {}
            )
        }
    }
}