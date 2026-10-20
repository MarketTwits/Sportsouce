package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core.errors.api.throwable.mapToString
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import com.markettwits.core_ui.items.components.list.SauceActionRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.intent.composable.rememberIntentActionByPlatform
import com.markettwits.sportsouce.profile.authorized.authorized.domain.UserProfile
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@Composable
internal fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    userName: String,
    userPhoneNumber: String,
    userRegistrationsCount: Int,
    userRegistrations: List<StartOrderInfo>,
    recentStarts: List<StartsListItem>,
    userImageUrl: String,
    socialNetwork: UserProfile.SocialNetwork,
    onRefresh: () -> Unit,
    onClickMembers: () -> Unit,
    oClickSettings: () -> Unit,
    onClickClub: () -> Unit,
    onClickStarts: () -> Unit,
    onClickOrders: () -> Unit,
    onClickFavorites: () -> Unit,
    onClickEditProfile: () -> Unit,
    onClickRegistration: (StartOrderInfo) -> Unit,
    onClickRecentStart: (StartsListItem) -> Unit,
    onSocialNetworkClick: (String) -> Unit,
    onAddSocialNetwork: () -> Unit,
    refreshError: SauceError? = null,
) {
    var isFullImage by rememberSaveable { mutableStateOf(false) }
    val intentAction = rememberIntentActionByPlatform()
    PullToRefreshScreen(isRefreshing = isRefreshing, onRefresh = onRefresh) {
        Column(modifier.fillMaxSize().background(SauceTheme.colors.background).verticalScroll(rememberScrollState())) {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
            AdaptivePane(fillHeight = false) {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    ProfileInfoTopBar(imageUrl = userImageUrl, userName = userName, userPhoneNumber = userPhoneNumber,
                        socialNetwork = socialNetwork, onClickImage = { isFullImage = true }, onClickEditProfile = onClickEditProfile,
                        onSocialNetworkClick = onSocialNetworkClick, onAddSocialNetwork = onAddSocialNetwork)
                    if (refreshError != null) OnBackgroundCard {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Не удалось обновить профиль", style = MaterialTheme.typography.titleSmall,
                                color = SauceTheme.colors.textPrimary)
                            Text(refreshError.mapToString(), style = MaterialTheme.typography.bodyMedium,
                                color = SauceTheme.colors.textSecondary)
                            SauceButton("Повторить попытку", onRefresh, variant = SauceButtonVariant.Outlined)
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        ProfileRegistrationsBlock(registrations = userRegistrations,
                            registrationsCount = userRegistrationsCount,
                            onClickRegistration = onClickRegistration, onClickViewAll = onClickStarts)
                        ProfileActionCards(onClickFavorites = onClickFavorites, onClickMembers = onClickMembers)
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        ProfileSectionHeader("Покупки и оплаты")
                        OnBackgroundCard {
                            SauceActionRow("Мои заказы", onClickOrders, modifier = Modifier.padding(8.dp),
                                icon = Icons.Default.Inventory2)
                        }
                    }
                    ProfileRecentStartsBlock(starts = recentStarts, onClickStart = onClickRecentStart)
                    ProfileActionCadrs(onClickClub = onClickClub, onClickSettings = oClickSettings,
                        onClickHelp = { intentAction.openWebPage(SPORTSAUCE_TG_URL) })
                    ProfileExtraInfoContent()
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
    if (isFullImage) FullImageScreen(image = userImageUrl) { isFullImage = false }
}

private const val SPORTSAUCE_TG_URL = "https://t.me/sportsoyuznsk"
