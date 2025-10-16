package com.markettwits.sportsouce.club.info.presentation.components.trainings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.club.info.domain.models.Training
import com.markettwits.sportsouce.club.info.presentation.components.common.SubscribeGradientButton

@Composable
internal fun TrainingsContent(
    modifier: Modifier = Modifier,
    trainings: List<Training>,
    onRegisterClick: (Training) -> Unit = {},
) {
    var selectedImageUrl by rememberSaveable {
        mutableStateOf("")
    }

    if (selectedImageUrl.isNotEmpty()) {
        FullImageScreen(image = selectedImageUrl, onDismiss = { selectedImageUrl = "" })
    }

    LazyVerticalStaggeredGrid(
        modifier = modifier.fillMaxSize(),
        columns = StaggeredGridCells.Adaptive(160.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(trainings) { training ->
            TrainingItemContent(
                training = training,
                onClick = {
                    selectedImageUrl = training.imageUrl
                },
                onRegisterClick = { onRegisterClick(training) }
            )
        }
        item(
            span = StaggeredGridItemSpan.FullLine,
        ) {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun TrainingItemContent(
    modifier: Modifier = Modifier,
    training: Training,
    onClick: () -> Unit,
    onRegisterClick: () -> Unit,
) {
    OnBackgroundCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            SubcomposeAsyncImage(
                model = imageRequestCrossfade(training.imageUrl),
                contentDescription = training.type,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onClick),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmer()
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = training.type,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = FontNunito.bold()
            )

            if (training.htmlDescription.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                HtmlText(
                    text = training.htmlDescription,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline,
                    fontFamily = FontNunito.regular(),
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            SubscribeGradientButton(onClick = onRegisterClick)
        }
    }
}