package com.markettwits.registrations.registrations.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.markettwits.registrations.R
import com.markettwits.registrations.registrations.presentation.RegistrationsStore


@Composable
fun RegistrationsStart(starts: List<RegistrationsStore.StartsStateInfo>, onClick: (Int) -> Unit) {
    // Ленивый список с вертикальной прокруткой
    LazyColumn {
        // Добавляем каждый элемент в список
        items(starts) { event ->
            RegistrationsCard(event){
                onClick(it)
            }
          //  EventCard(event)
        }
    }
}