package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.core_ui.items.window.calculateWindowSizeClass
import com.markettwits.core_ui.items.window.rememberScreenSizeInfo
import com.markettwits.intent.composable.rememberIntentActionByPlatform
import com.markettwits.sportsouce.profile.authorized.authorized.domain.UserProfile
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    userName: String,
    userPhoneNumber: String,
    userRegistrationsCount: Int,
    userRegistrations: List<StartOrderInfo>,
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
    onSocialNetworkClick: (String) -> Unit,
    onAddSocialNetwork: () -> Unit,
) {

    var isFullImage by rememberSaveable { mutableStateOf(false) }
    val intentAction = rememberIntentActionByPlatform()
    val backgroundColor =
        if (LocalDarkOrLightTheme.current) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.outlineVariant

    // Calculate adaptive width for top bar
    val windowSizeClass = calculateWindowSizeClass()
    val screenWidth = rememberScreenSizeInfo().wDP
    val maxWidth = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> Dp.Unspecified
        WindowWidthSizeClass.Medium -> (screenWidth * 0.8f).coerceAtMost(800.dp)
        WindowWidthSizeClass.Expanded -> (screenWidth * 0.7f).coerceAtMost(1350.dp)
        else -> Dp.Unspecified
    }

    PullToRefreshScreen(
        isRefreshing = false,
        onRefresh = onRefresh
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundColor)
                .windowInsetsPadding(WindowInsets.statusBars)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        modifier = Modifier.widthIn(max = maxWidth)
                    ) {
                        Spacer(Modifier.height(12.dp))
                        ProfileInfoTopBar(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            imageUrl = userImageUrl,
                            userName = userName,
                            userPhoneNumber = userPhoneNumber,
                            socialNetwork = socialNetwork,
                            onClickImage = { isFullImage = true },
                            onClickEditProfile = onClickEditProfile,
                            onSocialNetworkClick = onSocialNetworkClick,
                            onAddSocialNetwork = onAddSocialNetwork
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            AdaptivePane {
                Column {
                    ProfileActionCards(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        startsCount = userRegistrationsCount,
                        registrations = userRegistrations,
                        onClickOrders = onClickOrders,
                        onClickStarts = onClickStarts,
                        onClickFavorites = onClickFavorites,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileClubCards(onClick = onClickClub)
                    Spacer(modifier = Modifier.height(20.dp))
                    ProfileRegistrationsBlock(
                        registrations = userRegistrations,
                        onClickRegistration = onClickRegistration,
                        onClickViewAll = onClickStarts
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileActionCadrs(
                        onClickHelp = {
                            intentAction.openWebPage(SPORTSAUCE_TG_URL)
                        },
                        onClickMembers = onClickMembers,
                        onClickSettings = oClickSettings
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
        AnimatedVisibility(
            visible = isFullImage
        ) {
            FullImageScreen(image = userImageUrl) {
                isFullImage = false
            }
        }
    }
}

private const val SPORTSAUCE_TG_URL = "https://t.me/sportsoyuznsk"

