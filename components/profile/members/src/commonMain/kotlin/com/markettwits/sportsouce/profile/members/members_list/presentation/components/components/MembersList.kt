package com.markettwits.sportsouce.profile.members.members_list.presentation.components.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.profile.members.member_common.domain.MemberIdentityTeam
import com.markettwits.sportsouce.profile.members.members_list.presentation.store.store.MembersListStore
import com.markettwits.sportsouce.profile.members.member_common.presentation.MemberItemCard

@Composable
internal fun MembersList(
    modifier: Modifier = Modifier,
    items: List<ProfileMember>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onClick: (ProfileMember) -> Unit,
    onClickAddMember: () -> Unit,
    team: MemberIdentityTeam? = null,
    teamError: String? = null,
    refreshError: String? = null,
    teamBusy: Boolean = false,
    pendingTrustedUsers: Set<Int> = emptySet(),
    onTeamIntent: (MembersListStore.Intent) -> Unit = {},
) {
    PullToRefreshScreen(modifier = modifier, isRefreshing = isRefreshing, onRefresh = onRefresh) {
        SauceLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(SauceSpacing.large),
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
        ) {
            item(key = "add-member", contentType = "action") {
                AddMemberActionButton(onClick = onClickAddMember)
            }
            refreshError?.let { message -> item(key = "refresh-error") { TeamError(message, onRefresh) } }
            teamError?.let { message -> item(key = "team-error") { TeamError(message, onRefresh) } }
            val linked = team?.myTeam.orEmpty().filter { person -> items.none { it.participantUserId == person.userId } }
            linked.forEach { person -> item(key = "linked-${person.id}") { MemberAccessCard(person, trusted = false, busy = teamBusy,
                canAddTrust = team?.trusted?.none { it.userId == person.userId } == true && person.userId !in pendingTrustedUsers,
                onRelationChanged = { onTeamIntent(MembersListStore.Intent.UpdateRelation(person.userId, it)) },
                onAddTrust = { onTeamIntent(MembersListStore.Intent.TrustPerson(person.userId)) }) } }
            if (items.isEmpty() && linked.isEmpty()) {
                item(key = "empty", contentType = "empty") { MembersEmptyCard() }
            } else {
                items(items, key = { it.id }, contentType = { "member" }) { member ->
                    MemberItemCard(item = member, onClick = onClick,
                        connectionConfirmed = team?.myTeam?.any { it.userId == member.participantUserId } == true)
                }
            }
        }
    }
}
