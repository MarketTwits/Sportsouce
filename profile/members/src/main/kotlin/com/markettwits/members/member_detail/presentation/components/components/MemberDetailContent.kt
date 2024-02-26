package com.markettwits.members.member_detail.presentation.components.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.members.common.domain.ProfileMember
import com.markettwits.members.common.presentation.MemberItemCard

@Composable
fun MemberDetailContent(modifier: Modifier = Modifier, item: ProfileMember) {
    Column(modifier = modifier.padding(10.dp)) {
        MemberItemCard(
            full = true,
            item = item,
            onClick = {}
        )
        MemberDetailButtons(
            modifier = Modifier.padding(vertical = 10.dp),
            onClickEdit = {},
            onClickDelete = {}
        )
    }
}