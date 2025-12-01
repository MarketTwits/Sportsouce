package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.support.presentation.component.StartSupportComponent
import com.markettwits.sportsouce.start.support.presentation.components.StartSupportScreen

@Composable
internal fun StartSupport(
    modifier: Modifier,
    component: StartSupportComponent,
    sponsors: List<StartItem.Sponsor> = emptyList(),
    onClickSponsorUrl: (String) -> Unit = {},
    eventContent: (EventContent) -> Unit,
) {
    var isShowSupportDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Text(
                text = "Поддержка проекта",
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontNunito.bold(),
                fontSize = 18.sp
            )

            // Sponsors list if available
            if (sponsors.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(sponsors) { sponsor ->
                        SponsorCard(
                            sponsor = sponsor,
                            onClick = { onClickSponsorUrl(sponsor.link) }
                        )
                    }
                }
            }

            // Support button
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                thickness = 1.dp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(Shapes.medium)
                    .noRippleClickable {
                        isShowSupportDialog = true
                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Поддержать проект",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = FontNunito.bold(),
                        fontSize = 16.sp
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    if (isShowSupportDialog) {
        DefaultModalBottomSheet(
            onDismissRequest = { isShowSupportDialog = false },
        ) {
            StartSupportScreen(
                modifier = modifier,
                component = component,
                event = eventContent,
                onClickDismiss = {
                    isShowSupportDialog = false
                }
            )
        }
    }
}

@Composable
private fun SponsorCard(
    modifier: Modifier = Modifier,
    sponsor: StartItem.Sponsor,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .width(140.dp)
            .clip(Shapes.medium)
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(Shapes.medium)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                model = imageRequestCrossfade(model = sponsor.imageUrl),
                contentDescription = sponsor.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .clip(Shapes.medium),
                error = {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        modifier = Modifier.size(40.dp)
                    )
                },
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    )
                },
                success = {
                    SubcomposeAsyncImageContent(modifier = Modifier)
                }
            )
        }

        Text(
            text = sponsor.name,
            fontSize = 13.sp,
            fontFamily = FontNunito.semiBoldBold(),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
