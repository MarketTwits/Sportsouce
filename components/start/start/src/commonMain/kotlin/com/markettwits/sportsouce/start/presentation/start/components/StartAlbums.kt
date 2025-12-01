package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StartAlbums(
    modifier: Modifier = Modifier,
    albums: List<StartItem.Album>,
    onCLickFullAlbum: (StartItem.Album) -> Unit,
) {
    var showAllAlbums by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (albums.isNotEmpty()) {
        StartContentBasePanel(
            modifier = modifier,
            label = albums[0].name,
            icon = Icons.Default.PhotoLibrary
        ) {
            StartAlbumsContent(
                album = albums[0].photos.take(6).reversed(),
                hasMorePhotos = albums[0].photos.size > 6,
                onCLickFullAlbum = { onCLickFullAlbum(albums[0]) },
            )

            if (albums.size > 1) {
                ShowAllAlbumsButton(
                    albumsCount = albums.size - 1,
                    onClick = { showAllAlbums = true }
                )
            }
        }
    }

    if (showAllAlbums) {
        ModalBottomSheet(
            onDismissRequest = { showAllAlbums = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
            tonalElevation = 0.dp,
        ) {
            AllAlbumsContent(
                albums = albums,
                onCLickFullAlbum = onCLickFullAlbum,
                onDismiss = { showAllAlbums = false }
            )
        }
    }
}

@Composable
private fun ShowAllAlbumsButton(
    modifier: Modifier = Modifier,
    albumsCount: Int,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp)
            .clip(Shapes.medium)
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                shape = Shapes.medium
            )
            .clickable { onClick() }
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f),
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                    )
                )
            )
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.PhotoLibrary,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Показать все альбомы ($albumsCount)",
                fontSize = 15.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun AllAlbumsContent(
    modifier: Modifier = Modifier,
    albums: List<StartItem.Album>,
    onCLickFullAlbum: (StartItem.Album) -> Unit,
    onDismiss: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Все альбомы",
                fontSize = 22.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onBackground
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Закрыть",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(albums.filter { album -> album.photos.isNotEmpty() }) { album ->
                AlbumGridItem(
                    album = album,
                    onClick = {
                        onCLickFullAlbum(album)
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun AlbumGridItem(
    modifier: Modifier = Modifier,
    album: StartItem.Album,
    onClick: () -> Unit,
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }
    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, MaterialTheme.colorScheme.tertiary.copy(0.8f)),
        startY = sizeImage.height.toFloat() / 3f,
        endY = sizeImage.height.toFloat()
    )

    Box(
        modifier = modifier
            .aspectRatio(0.85f)
            .clip(Shapes.large)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                shape = Shapes.large
            )
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (album.photos.isNotEmpty()) {
                SubcomposeAsyncImage(
                    model = imageRequestCrossfade(album.photos.first().imageUrl),
                    filterQuality = FilterQuality.Medium,
                    contentDescription = album.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.2f))
                        .onGloballyPositioned { sizeImage = it.size }
                        .fillMaxSize(),
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoLibrary,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    },
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                        )
                    },
                    success = {
                        SubcomposeAsyncImageContent(modifier = Modifier.align(Alignment.BottomCenter))
                    }
                )
            }
        }

        if (album.photos.isNotEmpty())
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(gradient)
            )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = album.name,
                fontSize = 15.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onTertiary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Photo,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.9f),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${album.photos.size} фото",
                    fontSize = 13.sp,
                    fontFamily = FontNunito.regular(),
                    color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.9f)
                )
            }
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
    var fullImage by rememberSaveable { mutableStateOf(false) }
    var selectedImageIndex by rememberSaveable { mutableIntStateOf(-1) }

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        itemsIndexed(items = album) { index, item ->
            StartAlbumItemContent(
                modifier = Modifier.animateItem(
                    fadeInSpec = null,
                    fadeOutSpec = null,
                    placementSpec = tween(600)
                ),
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
        FullImageScreen(
            selectedImageIndex = selectedImageIndex,
            image = album.map { it.imageUrl }
        ) {
            fullImage = false
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
    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, MaterialTheme.colorScheme.tertiary.copy(0.4f)),
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
        Box(modifier = Modifier.fillMaxSize()) {
            SubcomposeAsyncImage(
                model = imageRequestCrossfade(image),
                filterQuality = FilterQuality.Medium,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.2f))
                    .onGloballyPositioned { sizeImage = it.size }
                    .fillMaxSize(),
                error = {
                    Box(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer))
                },
                loading = {
                    Box(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer))
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
    val color = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.onTertiary
    } else {
        MaterialTheme.colorScheme.tertiary
    }
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
                .onGloballyPositioned { sizeImage = it.size }
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
private fun HashtagLabel(
    modifier: Modifier,
    hashtag: String,
) {
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