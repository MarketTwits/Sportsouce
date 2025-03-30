package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.intent.composable.rememberIntentActionByPlatform

@Composable
internal fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    userName: String,
    userRegistrationsCount : Int,
    userImageUrl: String,
    onClickMembers: () -> Unit,
    onClickClub: () -> Unit,
    onClickStarts: () -> Unit,
    onClickOrders: () -> Unit,
    onClickEditProfile: () -> Unit
) {

    var isFullImage by rememberSaveable { mutableStateOf(false) }
    val intentAction = rememberIntentActionByPlatform()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.outlineVariant)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            ProfileInfoTopBar(
                imageUrl = userImageUrl,
                userName = userName,
                onClickImage = { isFullImage = true },
                onClickEditProfile = onClickEditProfile
            )
            ProfileActionCards(
                onClickOrders = onClickOrders,
                startsCount = userRegistrationsCount,
                onClickStarts = onClickStarts
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        ProfileClubCards(onClick = onClickClub)
        Spacer(modifier = Modifier.height(16.dp))
        Column(Modifier.fillMaxWidth()) {
            ProfileActionButton(
                text = "Участники",
                onClick = onClickMembers
            )
            ProfileActionButton(
                text = "Помощь",
                actionIcon = Icons.AutoMirrored.Filled.Message,
                onClick = {
                    intentAction.openWebPage(SPORTSAUCE_TG_URL)
                })
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

private const val SPORTSAUCE_TG_URL = "https://t.me/sportsoyuznsk"

