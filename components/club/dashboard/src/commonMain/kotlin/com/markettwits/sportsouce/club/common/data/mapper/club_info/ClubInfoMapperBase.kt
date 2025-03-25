package com.markettwits.sportsouce.club.common.data.mapper.club_info

import com.markettwits.sportsouce.club.info.domain.models.Question
import com.markettwits.sportsouce.club.info.domain.models.Schedule
import com.markettwits.sportsouce.club.info.domain.models.Statistic
import com.markettwits.sportsouce.club.info.domain.models.Trainer
import com.markettwits.sportsouce.club.info.domain.models.Training
import com.markettwits.sportsouce.club.cloud.models.club_settings.ClubSettingsRemoteRow
import com.markettwits.sportsouce.club.cloud.models.questions.QuestionRemoteRow
import com.markettwits.sportsouce.club.cloud.models.schedule.ScheduleRemoteRow
import com.markettwits.sportsouce.club.cloud.models.trainer.TrainersRemoteRow
import com.markettwits.sportsouce.club.cloud.models.workout.WorkoutRemoteRow

internal class ClubInfoMapperBase : ClubInfoMapper {

    override fun mapTrainers(trainersRemoteRow: List<TrainersRemoteRow>): List<Trainer> =
        trainersRemoteRow.map {
            Trainer(
                id = it.id,
                name = it.name,
                surname = it.surname,
                description = it.description,
                imageUrl = it.photo.fullPath,
                kindOfSports = it.kindOfSports.map { it.name },
            )
        }

    override fun mapQuestions(questionRemote: List<QuestionRemoteRow>): List<Question> =
        questionRemote.map {
            Question(
                id = it.id,
                answer = it.answer,
                question = it.question
            )
        }


    override fun mapStatistics(settingsRemoteRow: List<ClubSettingsRemoteRow>): List<Statistic> =
        settingsRemoteRow
            .filter { it.key == "statistic" }
            .map {
                Statistic(
                    id = it.id,
                    value = it.name,
                    title = it.description ?: ""
                )
            }


    override fun mapTraining(workoutRemoteRow: List<WorkoutRemoteRow>): List<Training> =
        workoutRemoteRow.map {
            Training(
                id = it.id,
                type = it.type,
                htmlDescription = it.description,
                imageUrl = it.icon?.fullPath ?: ""
            )
        }

    override fun mapSchedule(scheduleRemote: List<ScheduleRemoteRow>): List<Schedule> =
        scheduleRemote.map {
            Schedule(
                id = it.id,
                workoutId = it.workoutId ?: 0,
                address = it.address,
                description = it.description,
                kindOfSport = it.kindOfSport.name,
                startDate = it.startDate,
                trainerFullName = if (it.trainers.isNotEmpty()) {
                    it.trainers.joinToString(separator = "\n") { trainer -> "${trainer.name} ${trainer.surname}" }
                } else "",
                weekday = it.weekday,
                workoutTitle = it.workout?.type ?: ""
            )
        }
}