package com.markettwits.sportsouce.profile.members.members_list.presentation.components.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.profile.members.member_common.presentation.MemberItemCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MembersList(
    modifier: Modifier = Modifier,
    items: List<ProfileMember>,
    onClick: (ProfileMember) -> Unit,
    onClickAddMember: () -> Unit,
) {
    LazyColumn(modifier = modifier) {
        item {
            AddMemberButton(
                title = "Добавить участника",
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                onClickAddMember()
            }
        }
        items(items = items, key = { it -> it.id }) {
            MemberItemCard(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .animateItem(fadeInSpec = tween(600)),
                item = it,
                onClick = { member ->
                    onClick(member)
                })
        }
    }
    if (items.isEmpty()) {
        MembersEmptyCard()
    }
}