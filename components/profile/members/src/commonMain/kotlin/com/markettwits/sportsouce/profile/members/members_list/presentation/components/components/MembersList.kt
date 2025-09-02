package com.markettwits.sportsouce.profile.members.members_list.presentation.components.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.profile.members.member_common.presentation.MemberItemCard
import kotlinx.coroutines.delay


@Composable
internal fun MembersList(
    modifier: Modifier = Modifier,
    items: List<ProfileMember>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onClick: (ProfileMember) -> Unit,
    onClickAddMember: () -> Unit,
) {
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        showContent = true
    }
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            AddMemberActionButton(onClick = onClickAddMember)
        },
        containerColor = androidx.compose.ui.graphics.Color.Transparent
    ) { paddingValues ->
        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(animationSpec = tween(400))
        ) {
            PullToRefreshScreen(
                isRefreshing = isRefreshing,
                onRefresh = onRefresh
            ) { innerModifier ->
                if (items.isEmpty()) {
                    MembersEmptyCard(
                        modifier = innerModifier
                            .padding(paddingValues)
                            .padding(16.dp)
                            .padding(bottom = paddingValues.calculateBottomPadding())
                    )
                } else {
                    LazyColumn(
                        modifier = innerModifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .padding(bottom = paddingValues.calculateBottomPadding()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(
                            items = items,
                            key = { _, item -> item.id }
                        ) { index, item ->
                            var isItemVisible by remember { mutableStateOf(false) }

                            LaunchedEffect(item.id) {
                                delay(index * 50L) // Staggered animation
                                isItemVisible = true
                            }

                            AnimatedVisibility(
                                visible = isItemVisible,
                                enter = slideInVertically(
                                    animationSpec = tween(400),
                                    initialOffsetY = { it / 2 }
                                ) + fadeIn(animationSpec = tween(400))
                            ) {
                                MemberItemCard(
                                    modifier = Modifier.animateItem(
                                        fadeInSpec = tween(600),
                                        fadeOutSpec = tween(400),
                                        placementSpec = tween(600)
                                    ),
                                    item = item,
                                    onClick = { member ->
                                        onClick(member)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
