package com.markettwits.start.register.presentation.member.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.textField.DropDownSpinner
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.members.member_common.domain.ProfileMember
import kotlinx.collections.immutable.toImmutableList

@Suppress("NonSkippableComposable")
@Composable
fun MemberSelectMember(
    modifier: Modifier = Modifier,
    members: List<ProfileMember>,
    onMemberSelected: (ProfileMember) -> Unit
) {
    Row(modifier = modifier) {
        DropDownSpinner(
            selectedItem = null,
            onItemSelected = { index, user ->
                if (user != null) onMemberSelected(members[index])
            },
            itemList = members.map { it.surname + " " + it.name }.toImmutableList(),
        ) {
            Text(
                text = "Выберите участника",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }

}