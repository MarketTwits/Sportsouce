package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.start.domain.StartItem

@Composable
internal fun StartAlbums(
    modifier: Modifier = Modifier,
    albums: List<StartItem.Album>,
    onCLickFullAlbum: () -> Unit,
) {
    if (albums.isNotEmpty()) {
        StartContentBasePanel(
            modifier = modifier,
            label = albums[0].name,
            icon = Icons.Default.PhotoLibrary
        ) {
            StartAlbumsContent(
                album = albums[0].photos.take(6).reversed(),
                hasMorePhotos = albums[0].photos.size > 6,
                onCLickFullAlbum = { onCLickFullAlbum() }
            )
        }
    }
}

@Composable
private fun StartAlbumsContent(
    modifier: Modifier = Modifier,
    album: List<StartItem.Album.Photo>,
    hasMorePhotos: Boolean,
    onCLickFullAlbum: () -> Unit,
) {
    var fullImage by rememberSaveable {
        mutableStateOf(false)
    }
    var selectedImageIndex by rememberSaveable {
        mutableIntStateOf(-1)
    }
    LazyRow(
        modifier = modifier,
    ) {
        itemsIndexed(
            items = album
        ) { index, item ->
            StartAlbumItemContent(
                modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null, placementSpec = tween(600)),
                image = item.imageUrl,
                hashtag = item.tags.values.firstOrNull() ?: "",
                onClickImage = {
                    selectedImageIndex = index
                    fullImage = true
                }
            )
        }
        if (hasMorePhotos) {
            item {
                StartAlbumItemContentEmpty {
                    onCLickFullAlbum()
                }
            }
        }
    }
    if (fullImage) {
        FullImageScreen(selectedImageIndex = selectedImageIndex, image = album.map { it.imageUrl }) {
            fullImage = !fullImage
            selectedImageIndex = -1
        }
    }
}

@Composable
private fun StartAlbumItemContent(
    modifier: Modifier = Modifier,
    image: String,
    hashtag: String,
    onClickImage: () -> Unit,
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }
    val color =
        if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.tertiary
    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, color.copy(0.4f)),
        startY = sizeImage.height.toFloat() / 4f,
        endY = sizeImage.height.toFloat()
    )
    Box(
        modifier = modifier
            .padding(8.dp)
            .size(width = 180.dp, height = 220.dp)
            .clip(Shapes.large)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                shape = Shapes.large
            )
            .clickable { onClickImage() }
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            SubcomposeAsyncImage(
                model = imageRequestCrossfade(image),
                filterQuality = FilterQuality.Medium,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.2f))
                    .onGloballyPositioned {
                        sizeImage = it.size
                    }
                    .fillMaxSize(),
                error = {
                    Box(modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer))
                },
                loading = {
                    Box(modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer))
                },
                success = {
                    SubcomposeAsyncImageContent(modifier = Modifier.align(Alignment.BottomCenter))
                }
            )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(gradient)
        )
        if (hashtag.isNotEmpty()) {
            HashtagLabel(
                modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.TopStart),
                hashtag = if (hashtag == "preview") "Превью" else hashtag
            )
        }
    }
}

@Composable
private fun StartAlbumItemContentEmpty(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }
    val color =
        if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.tertiary
    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, color.copy(0.5f)),
        startY = sizeImage.height.toFloat() / 10,
        endY = sizeImage.height.toFloat()
    )
    Box(
        modifier = modifier
            .padding(8.dp)
            .size(width = 180.dp, height = 220.dp)
            .clip(Shapes.large)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                shape = Shapes.large
            )
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .onGloballyPositioned {
                    sizeImage = it.size
                }
                .matchParentSize()
                .background(gradient)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Ещё фото",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 3,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
private fun HashtagLabel(modifier: Modifier, hashtag: String) {
    Box(
        modifier = modifier
            .clip(Shapes.medium)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
                    )
                )
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = hashtag,
            fontSize = 11.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}