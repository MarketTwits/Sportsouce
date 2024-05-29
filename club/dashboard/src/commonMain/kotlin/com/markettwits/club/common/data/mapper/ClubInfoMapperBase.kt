package com.markettwits.club.common.data.mapper

import com.markettwits.club.cloud.models.club_settings.ClubSettingsRemote
import com.markettwits.club.cloud.models.questions.QuestionRemoteRow
import com.markettwits.club.cloud.models.schedule.ScheduleRemote
import com.markettwits.club.cloud.models.trainer.TrainersRemote
import com.markettwits.club.cloud.models.workout.WorkoutRemote
import com.markettwits.club.common.domain.mapper.ClubInfoMapper
import com.markettwits.club.info.domain.models.Question
import com.markettwits.club.info.domain.models.Schedule
import com.markettwits.club.info.domain.models.Statistic
import com.markettwits.club.info.domain.models.Trainer
import com.markettwits.club.info.domain.models.Training

internal class ClubInfoMapperBase : ClubInfoMapper {
    override fun mapTrainers(trainersRemoteRow: TrainersRemote): List<Trainer> =
        trainersRemoteRow.rows.map {
            Trainer(
                id = it.id,
                name = it.name,
                surname = it.surname,
                description = it.description,
                imageUrl = it.photo.fullPath,
                kindOfSports = it.kindOfSports.map { it.name },
            )
        }

    override fun mapQuestions(questionRemoteRow: QuestionRemoteRow): List<Question> =
        questionRemoteRow.rows.map {
            Question(
                id = it.id,
                answer = it.answer,
                question = it.question
            )
        }


    override fun mapStatistics(settingsRemoteRow: ClubSettingsRemote): List<Statistic> =
        settingsRemoteRow.rows
            .filter { it.key == "statistic" }
            .map {
                Statistic(
                    id = it.id,
                    value = it.name,
                    title = it.description ?: ""
                )
            }


    override fun mapTraining(workoutRemoteRow: WorkoutRemote): List<Training> =
        workoutRemoteRow.rows.map {
            Training(
                id = it.id,
                type = it.type,
                htmlDescription = it.description,
                imageUrl = it.icon.fullPath
            )
        }

    override fun mapSchedule(scheduleRemote: ScheduleRemote): List<Schedule> =
        scheduleRemote.rows.map {
            Schedule(
                id = it.id,
                workoutId = it.workout_id,
                address = it.address,
                description = it.description,
                kindOfSport = it.kindOfSport.name,
                startDate = it.startDate,
                trainerFullName = "${it.trainer.name} ${it.trainer.surname}",
                weekday = it.weekday,
                workoutTitle = it.workout.type
            )
        }
}