package com.markettwits.sportsouce.profile.members.members_list.presentation.components.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.profile.members.member_common.presentation.MemberItemCard


@Composable
internal fun MembersList(
    modifier: Modifier = Modifier,
    items: List<ProfileMember>,
    onClick: (ProfileMember) -> Unit,
    onClickAddMember : () -> Unit,
) {
    Scaffold(
        modifier = modifier.padding(10.dp),
        floatingActionButton = {
            AddMemberActionButton(onClick = onClickAddMember)
        }
    ) {
        LazyColumn {
            items(items = items, key = { it.id }) {
                MemberItemCard(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .animateItem(fadeInSpec = tween(600)),
                    item = it,
                    onClick = { member ->
                        onClick(member)
                    })
            }
            item {
                if (items.isEmpty()) {
                    MembersEmptyCard()
                }
            }
        }
    }

}