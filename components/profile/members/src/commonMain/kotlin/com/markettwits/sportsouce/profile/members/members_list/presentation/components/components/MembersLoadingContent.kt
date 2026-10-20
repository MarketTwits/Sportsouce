package com.markettwits.sportsouce.profile.members.members_list.presentation.components.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSpacing

@Composable
internal fun MembersLoadingContent(modifier: Modifier = Modifier) {
    SauceLazyColumn(
        modifier = modifier.fillMaxSize().semantics {
            contentDescription = "Загрузка участников"
            progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
        },
        userScrollEnabled = false,
        contentPadding = PaddingValues(SauceSpacing.large),
        verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
    ) {
        item { SauceSkeleton(Modifier.fillMaxWidth().height(48.dp), shape = SauceShapes.button) }
        items(4) {
            OnBackgroundCard {
                Column(Modifier.fillMaxWidth().padding(SauceSpacing.large),
                    verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
                        SauceSkeleton(Modifier.size(40.dp), shape = CircleShape)
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            SauceSkeleton(Modifier.fillMaxWidth(0.8f).height(20.dp))
                            SauceSkeleton(Modifier.fillMaxWidth(0.55f).height(14.dp))
                        }
                    }
                    SauceSkeleton(Modifier.width(100.dp).height(18.dp))
                    SauceSkeleton(Modifier.fillMaxWidth(0.7f).height(16.dp))
                    SauceSkeleton(Modifier.fillMaxWidth(0.6f).height(16.dp))
                }
            }
        }
    }
}
