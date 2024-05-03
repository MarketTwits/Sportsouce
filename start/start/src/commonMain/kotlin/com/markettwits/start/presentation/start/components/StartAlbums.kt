package com.markettwits.start.presentation.start.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.markettwits.core_ui.items.base_screen.FullImageScreen
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.common.StartContentBasePanel

@Composable
fun StartAlbums(
    modifier: Modifier = Modifier,
    albums: List<StartItem.Album>,
    onCLickFullAlbum: () -> Unit
) {
    if (albums.isNotEmpty()) {
        StartContentBasePanel(modifier = modifier, label = albums[0].name) {
            StartAlbumsContent(album = albums[0], onCLickFullAlbum = {
                onCLickFullAlbum()
            })
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun StartAlbumsContent(
    modifier: Modifier = Modifier,
    album: StartItem.Album,
    onCLickFullAlbum: () -> Unit
) {
    var fullImage by rememberSaveable {
        mutableStateOf(false)
    }
    var selectedImage by rememberSaveable {
        mutableStateOf("")
    }
    LazyRow(
        modifier = modifier,
    ) {
        items(album.photos.take(6), key = { it.id }) {
            StartAlbumItemContent(
                modifier = modifier.animateItemPlacement(animationSpec = tween(600)),
                image = it.imageUrl,
                hashtag = it.tags.values.firstOrNull() ?: "",
                onClickImage = {
                    selectedImage = it.imageUrl
                    fullImage = true
                }
            )
        }
        if (album.photos.size > 6) {
            item {
                StartAlbumItemContentEmpty {
                    onCLickFullAlbum()
                }
            }
        }
    }
    if (fullImage) {
        FullImageScreen(image = selectedImage) {
            fullImage = !fullImage
            selectedImage = ""
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
            .padding(10.dp)
            .size(width = 150.dp, height = 180.dp)
            .clip(Shapes.medium)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), Shapes.medium)
            .clickable {
                onClickImage()
            }
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
    onClick: () -> Unit
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
            .padding(10.dp)
            .size(width = 150.dp, height = 180.dp)
            .clip(Shapes.medium)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), Shapes.medium)
            .clickable {
                onClick()
            }
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
            .size(width = 90.dp, height = 40.dp)
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .clip(Shapes.medium)
                .align(Alignment.Center)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondary),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = hashtag,
                fontSize = 12.sp,
                overflow = TextOverflow.Ellipsis,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}