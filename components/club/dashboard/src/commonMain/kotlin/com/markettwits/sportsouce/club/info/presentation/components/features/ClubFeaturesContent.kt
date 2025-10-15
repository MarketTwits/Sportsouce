package com.markettwits.sportsouce.club.info.presentation.components.features

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.club.info.domain.models.ClubFeature

@Composable
internal fun ClubFeaturesContent(
    modifier: Modifier = Modifier,
    features: List<ClubFeature>,
) {
    var fullImageContent by remember {
        mutableStateOf("")
    }

    if (fullImageContent.isNotEmpty()) {
        FullImageScreen(
            image = fullImageContent,
            onDismiss = { fullImageContent = "" }
        )
    }

    Column(modifier = modifier) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 12.dp,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(features) { feature ->
                FeatureItemContent(feature = feature) {
                    fullImageContent = feature.imageUrl ?: ""
                }
            }
        }
    }
}

@Composable
private fun FeatureItemContent(
    modifier: Modifier = Modifier,
    feature: ClubFeature,
    onClick: () -> Unit,
) {
    OnBackgroundCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (!feature.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = feature.imageUrl,
                    contentDescription = feature.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onClick),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🎁",
                        fontSize = 48.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = feature.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = FontNunito.bold()
            )

            if (feature.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                HtmlText(
                    text = feature.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline,
                    fontFamily = FontNunito.regular(),
                    lineHeight = 18.sp
                )
            }
        }
    }
}