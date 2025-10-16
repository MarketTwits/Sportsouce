package com.markettwits.sportsouce.club.info.presentation.components.comands

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.markettwits.sportsouce.club.info.domain.models.Trainer
import com.markettwits.sportsouce.club.info.presentation.components.common.SubscribeGradientButton

@Composable
internal fun CommandContent(
    modifier: Modifier = Modifier,
    trainers: List<Trainer>,
    onRegisterClick: (Trainer) -> Unit = {},
) {
    var fullImageContent by remember {
        mutableStateOf("")
    }
    LazyVerticalStaggeredGrid(
        modifier = modifier.fillMaxSize(),
        columns = StaggeredGridCells.Adaptive(160.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(trainers) { trainer ->
            TrainerItemContent(
                trainer = trainer,
                onClickImage = {
                    fullImageContent = trainer.imageUrl
                },
                onRegisterClick = { onRegisterClick(trainer) }
            )
        }
        item(
            span = StaggeredGridItemSpan.FullLine,
        ) {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
    if (fullImageContent.isNotEmpty()) {
        FullImageScreen(
            image = fullImageContent,
            onDismiss = { fullImageContent = "" }
        )
    }
}

@Composable
private fun TrainerItemContent(
    modifier: Modifier = Modifier,
    trainer: Trainer,
    onClickImage: () -> Unit,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onClickImage),
                contentScale = ContentScale.Crop,
                model = imageRequestCrossfade(trainer.imageUrl),
                contentDescription = trainer.name,
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
                text = trainer.fullName(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onPrimary
            )

            if (trainer.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                HtmlText(
                    text = trainer.description,
                    fontSize = 14.sp,
                    fontFamily = FontNunito.regular(),
                    color = MaterialTheme.colorScheme.outline,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = trainer.sports(),
                fontSize = 14.sp,
                fontFamily = FontNunito.semiBoldBold(),
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            SubscribeGradientButton(
                onClick = onRegisterClick,
            )
        }
    }
}