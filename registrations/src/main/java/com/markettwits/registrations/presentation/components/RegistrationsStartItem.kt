package com.markettwits.registrations.presentation.components

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
import com.markettwits.registrations.presentation.RegistrationsStore

@Composable
fun RegistrationsStartItem() {

}
@Composable
fun EventCard(start: RegistrationsStore.StartsStateInfo) {
    // Карточка с закругленными углами и белым фоном
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        // Горизонтальный стек для размещения изображения и текста
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Изображение с ресурса
            AsyncImage(
                model = start.image,
                contentDescription = null,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
            )
            // Вертикальный стек для размещения текста
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // Название события
                Text(
                    text = start.startTitle,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                // Вид спорта
                Text(
                    text = "Вид спорта: ${start.member}",
                    fontSize = 14.sp
                )
                // Дистанция
                Text(
                    text = "Дистанция: ${start.distance}",
                    fontSize = 14.sp
                )
                // Команда
                Text(
                    text = "Команда: ${start.team}",
                    fontSize = 14.sp
                )
                // Возрастная группа
                Text(
                    text = "Возрастная группа: ${start.ageGroup}",
                    fontSize = 14.sp
                )
                // Дата старта
                Text(
                    text = "Дата старта: ${start.dateStart}",
                    fontSize = 14.sp
                )
                // Цена
                Text(
                    text = "Цена: ${start.cost}",
                    fontSize = 14.sp
                )
            }
            // Кнопка для подачи заявки
            Button(
                onClick = { /* TODO: обработать нажатие */ },
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(text = "Старт заявки")
            }
        }
    }
}

// Компонент для отображения списка элементов
@Composable
fun RegistrationsStart(starts: List<RegistrationsStore.StartsStateInfo>,onClick: (Int) -> Unit) {
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