@file:JvmName("StartScreenKt")

package com.markettwits.start.presentation.start.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.start.presentation.common.LoadingScreen
import com.markettwits.start.presentation.start.MockStartScreen
import com.markettwits.start.presentation.start.StartItemUi
import com.markettwits.start.presentation.start.StartScreen
import com.markettwits.start.presentation.start.component.BackFloatingActionButton
import com.markettwits.start.presentation.start.component.CustomScreen
import com.markettwits.start.presentation.start.component.StartDescription
import com.markettwits.start.presentation.start.component.StartDistances
import com.markettwits.start.presentation.start.component.StartMembersPanel
import com.markettwits.start.presentation.start.component.StartStatus
import com.markettwits.start.presentation.start.component.StartTitle

@Composable
fun StartScreen(component: StartScreen) {
    val startData by component.start.subscribeAsState()

    when(startData){
        is StartItemUi.StartItemUiSuccess -> {
            val data = (startData as StartItemUi.StartItemUiSuccess)
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    CustomScreen(imageUrl = data.image)
                    val modifier = Modifier.padding(5.dp)
                    Column(modifier = modifier) {
                        StartTitle(
                            modifier = modifier,
                            title = data.title,
                            place = data.startPlace
                        )
                        StartStatus(
                            modifier = modifier,
                            status = data.startStatus, date = data.startTime
                        )
                        StartDescription(modifier = modifier, description = data.description)
                        StartDistances(modifier = modifier, distance = data.distanceInfo)
                        StartMembersPanel(modifier = modifier, membersCount = data.membersUi.size){
                            component.goMembers(data.membersUi)
                        }
                    }
                }
                BackFloatingActionButton{
                    component.goBack()
                }
            }
        }
        is StartItemUi.Loading -> {
            LoadingScreen()
        }
    }


    }
//    Box(modifier = Modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier
//                .verticalScroll(rememberScrollState())
//        ) {
//            CustomScreen()
//            val modifier = Modifier.padding(5.dp)
//            Column(modifier = modifier) {
//                StartTitle(
//                    modifier = modifier,
//                    title = "Новогодний забег Тропа Деда М.",
//                    place = "Дельфиния центр океанографии и морской биологии (\u200BЖуковского, 100/4)"
//                )
//                StartStatus(
//                    modifier = modifier,
//                    status = com.markettwits.cloud.model.common.StartStatus(
//                        3,
//                        "Регистрация открыта"
//                    ), date = "17 декабря 2023 года"
//                )
//                StartDescription(modifier = modifier, description = testDescription)
//                HorizontalDivider()
//                StartDistances(modifier = modifier,distance = testSelectKindsSport)
//            }
//        }
//        SmallFloatingActionButton(
//            modifier = Modifier.padding(10.dp),
//            containerColor = Color.White,
//            contentColor = SportSouceColor.SportSouceBlue,
//            onClick = {
//                component.goBack()
//            },
//        ) {
//            Icon(
//                modifier = Modifier.size(20.dp),
//                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                contentDescription = "Floating action button."
//            )
//        }
//    }



@Preview(showBackground = true)
@Composable
private fun StartScreenPreview() {
    StartScreen(MockStartScreen())
}

private val testSelectKindsSport = "[{\"value\":\"Детский забег\",\"prompt\":false,\"groups\":[{\"name\":\"ДМ1\",\"sex\":\"male\",\"ageFrom\":\"1\",\"ageTo\":\"7\"},{\"name\":\"ДМ2\",\"sex\":\"male\",\"ageFrom\":\"8\",\"ageTo\":\"10\"},{\"name\":\"ДМ3\",\"sex\":\"male\",\"ageFrom\":\"11\",\"ageTo\":\"13\"},{\"name\":\"ДЖ1\",\"sex\":\"female\",\"ageFrom\":\"1\",\"ageTo\":\"7\"},{\"name\":\"ДЖ2\",\"sex\":\"female\",\"ageFrom\":\"8\",\"ageTo\":\"10\"},{\"name\":\"ДЖ3\",\"sex\":\"female\",\"ageFrom\":\"11\",\"ageTo\":\"13\"}],\"format\":\"Лично\",\"distanceStages\":[],\"distance\":{\"description\":\"\",\"price\":\"500\",\"prices\":[{\"c_to\":\"2023-12-24T05:00:00.000Z\",\"c_from\":\"2023-11-12T05:00:00.000Z\",\"value\":500}],\"slots\":\"70\",\"infinitySlots\":false,\"track_link\":{},\"track_code\":\"\"}},{\"value\":\"5 км\",\"prompt\":false,\"groups\":[{\"name\":\"ЮМ1\",\"sex\":\"male\",\"ageFrom\":\"14\",\"ageTo\":\"17\"},{\"name\":\"М1\",\"sex\":\"male\",\"ageFrom\":\"18\",\"ageTo\":\"34\"},{\"name\":\"М2\",\"sex\":\"male\",\"ageFrom\":\"35\",\"ageTo\":\"49\"},{\"name\":\"М3\",\"sex\":\"male\",\"ageFrom\":\"50\",\"ageTo\":\"59\"},{\"name\":\"М4\",\"sex\":\"male\",\"ageFrom\":\"60\",\"ageTo\":\"100\"},{\"name\":\"ЮЖ1\",\"sex\":\"female\",\"ageFrom\":\"14\",\"ageTo\":\"17\"},{\"name\":\"Ж1\",\"sex\":\"female\",\"ageFrom\":\"18\",\"ageTo\":\"34\"},{\"name\":\"Ж2\",\"sex\":\"female\",\"ageFrom\":\"35\",\"ageTo\":\"44\"},{\"name\":\"Ж3\",\"sex\":\"female\",\"ageFrom\":\"45\",\"ageTo\":\"100\"}],\"format\":\"Лично\",\"distanceStages\":[],\"distance\":{\"description\":\"\",\"price\":\"1200\",\"prices\":[{\"c_to\":\"2023-12-10T16:59:00.000Z\",\"c_from\":\"2023-11-12T05:00:00.000Z\",\"value\":800},{\"c_to\":\"2023-12-24T05:00:00.000Z\",\"c_from\":\"2023-12-10T17:00:00.000Z\",\"value\":1200}],\"slots\":\"200\",\"infinitySlots\":false,\"track_link\":{},\"track_code\":\"\"}}]"

private val testDescription =
    "<p class=\"ql-align-justify\">Точный регламент старта и схема трассы будут опубликованы не позднее, чем за час до старта.</p><p><strong>24.12.2023</strong></p><ul><li class=\"ql-align-justify\"><span style=\"color: black;\">09:00 – 10:30 Выдача стартовых номеров и чипов участникам.</span></li><li>09:30 Старт участников на дистанции 1 000 м.</li><li>09:50 Старт участников на дистанции 500 м.</li><li>10:00 Старт участников на дистанции 200 м.</li><li>10:20 Награждение победителей и призеров Детского забега.</li><li class=\"ql-align-justify\"><span style=\"color: black;\">11:00 Старт участников на дистанцию 5&nbsp;000 м.</span></li></ul><p class=\"ql-align-justify\">Награждение победителей и призеров через 20 минут, после финиша последнего участника.</p><p class=\"ql-align-justify\"><strong>Дистанция:</strong></p><ul><li class=\"ql-align-justify\">5 000 м – участники 2009 г.р. и старше;</li><li class=\"ql-align-justify\">Детский забег (примерно 1&nbsp;000 м) – ДМ3 и ДЖ3</li><li class=\"ql-align-justify\">Детский забег (примерно 500 м) – ДМ2 и ДЖ2</li><li class=\"ql-align-justify\">Детский забег (примерно 200 м) – ДМ1 и ДЖ1</li></ul><p class=\"ql-align-justify\">Финальное расстояние детской дистанция будет зависеть от состояния трассы в день забега.</p><p class=\"ql-align-justify\"><strong>Взрослые группы:</strong></p><p>Мужские группы</p><ul><li>ЮМ1 - 2009-2006 (14-17 лет)</li><li>М1 - 2005-1989 (18-34 лет)</li><li>М2 - 1988-1974 (35-49 лет)</li><li>М3 - 1973-1964 (50-59 лет)</li><li>М4 - 1963 и старше (60 лет и старше)</li></ul><p>Женские группы</p><ul><li>ЮЖ1 - 2009-2006 (14-17 лет)</li><li>Ж1 - 2005-1989 (18-34 лет)</li><li>Ж2 - 1988-1979 (35-44 лет)</li><li>Ж3 - 1978 и старше (45 лет и старше)</li></ul><p>Детские группы</p><ul><li>ДМ1 - 2016 г.р. и младше\t(7 лет и младше)</li><li>ДМ2 - 2015 – 2013\t(8 – 10 лет)</li><li>ДМ3 - 2012 – 2010\t(11 – 13 лет)</li><li>ДЖ1 - 2016 г.р. и младше (7 лет и младше)</li><li>ДЖ2 - 2015 – 2013 (8 – 10 лет)</li><li>ДЖ3 - 2012 – 2010 (11 – 13 лет)</li></ul><p class=\"ql-align-justify\"><strong>Взнос:</strong></p><p>5 000 м</p><p>с 13.11.2023 по 10.12.2023 - 800 рублей</p><p>с 11.12.2023 по 24.12.2023 - 1 200 рублей</p><p>Детский забег (200 м, 500 м, 1 000 м)</p><p class=\"ql-align-justify\">с 13.11.2023 по 24.12.2023 - 500 рублей</p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-center\"><br></p>"