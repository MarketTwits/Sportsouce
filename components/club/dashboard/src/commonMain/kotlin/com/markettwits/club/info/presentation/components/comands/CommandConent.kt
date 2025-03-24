package com.markettwits.club.info.presentation.components.comands

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.markettwits.club.info.domain.models.Trainer
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun CommandContent(
    modifier: Modifier = Modifier,
    trainers: List<Trainer>
) {
    var fullImageContent by remember {
        mutableStateOf("")
    }
    Column(modifier = modifier.padding(10.dp)) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(150.dp)
        ) {
            item {
                Text(
                    textAlign = TextAlign.Start,
                    text = "Наша команда",
                    fontSize = 20.sp,
                    fontFamily = FontNunito.bold(),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            items(trainers) {
                TrainerItemContent(
                    modifier = Modifier.padding(10.dp),
                    trainer = it,
                    onClickImage = {
                        fullImageContent = it
                    }
                )
            }
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
    onClickImage: (String) -> Unit
) {
    OnBackgroundCard(
        modifier = modifier
    ) {
        Column(
            modifier = it
        ) {
            AsyncImage(
                modifier = Modifier
                    .clip(Shapes.medium)
                    .clickable {
                        onClickImage(trainer.imageUrl)
                    },
                model = imageRequestCrossfade(trainer.imageUrl),
                contentDescription = trainer.name
            )
            Text(
                textAlign = TextAlign.Start,
                text = trainer.fullName(),
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onPrimary
            )
            HtmlText(
                textAlign = TextAlign.Start,
                text = trainer.description,
                fontSize = 12.sp,
                fontFamily = FontNunito.semiBoldBold(),
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                textAlign = TextAlign.Start,
                text = trainer.sports(),
                fontSize = 12.sp,
                fontFamily = FontNunito.semiBoldBold(),
                color = MaterialTheme.colorScheme.secondary
            )
        }

    }
}