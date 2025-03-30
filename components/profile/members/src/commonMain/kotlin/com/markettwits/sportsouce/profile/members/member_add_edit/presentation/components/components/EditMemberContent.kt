package com.markettwits.sportsouce.profile.members.member_add_edit.presentation.components.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.teams_city.domain.Team

@Composable
fun EditMemberContent(
    modifier: Modifier = Modifier,
    onMemberChange: (ProfileMember) -> Unit,
    member: ProfileMember,
    teams: List<Team>,
) {
    Column(modifier = modifier) {
        EditMemberTextFieldsContent(
            modifier = Modifier.padding(10.dp),
            onMemberChange = onMemberChange,
            member = member,
            teams = teams
        )
    }
}