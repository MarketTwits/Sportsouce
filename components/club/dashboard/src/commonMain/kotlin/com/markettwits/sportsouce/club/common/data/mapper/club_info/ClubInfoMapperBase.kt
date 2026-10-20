package com.markettwits.sportsouce.club.common.data.mapper.club_info

import com.markettwits.sportsouce.club.cloud.models.club_settings.ClubSettingsRemoteRow
import com.markettwits.sportsouce.club.cloud.models.questions.QuestionRemoteRow
import com.markettwits.sportsouce.club.cloud.models.schedule.ScheduleRemoteRow
import com.markettwits.sportsouce.club.cloud.models.trainer.TrainersRemoteRow
import com.markettwits.sportsouce.club.cloud.models.workout.WorkoutRemoteRow
import com.markettwits.sportsouce.club.info.domain.models.*

internal class ClubInfoMapperBase : ClubInfoMapper {

    override fun mapTrainers(trainersRemoteRow: List<TrainersRemoteRow>): List<Trainer> =
        trainersRemoteRow.sortedBy { it.order }.map {
            Trainer(
                id = it.id,
                name = it.name,
                surname = it.surname,
                description = it.description,
                imageUrl = it.photo?.url.orEmpty(),
                kindOfSports = it.kindOfSports.map { it.name },
                isAcceptingRequests = it.isAcceptingRequests,
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
                    value = it.name.orEmpty(),
                    title = it.description ?: ""
                )
            }


    override fun mapTraining(workoutRemoteRow: List<WorkoutRemoteRow>): List<Training> =
        workoutRemoteRow.filterNot { it.isHidden }.sortedBy { it.order }.map {
            Training(
                id = it.id,
                type = it.type,
                htmlDescription = it.description,
                imageUrl = it.icon?.url.orEmpty()
            )
        }

    override fun mapSchedule(scheduleRemote: List<ScheduleRemoteRow>): List<Schedule> =
        scheduleRemote.sortedBy { it.order }.map {
            Schedule(
                id = it.id,
                workoutId = it.workoutId ?: it.workout?.id ?: 0,
                address = it.address,
                description = it.description,
                kindOfSport = it.kindOfSport.name,
                startDate = it.startDate.replace('.', ':'),
                trainerFullName = if (it.trainers.isNotEmpty()) {
                    it.trainers.joinToString(separator = "\n") { trainer -> "${trainer.name} ${trainer.surname}" }
                } else "",
                weekday = it.weekday ?: "",
                workoutTitle = it.workout?.type ?: "",
                note = it.note.orEmpty(),
                isTempDisabled = it.isTempDisabled,
            )
        }

    override fun mapMainImage(settingsRemoteRow: List<ClubSettingsRemoteRow>): ClubMainImage? =
        settingsRemoteRow
            .filter { it.key == "main_image" }
            .firstOrNull()
            ?.takeIf { it.image != null }
            ?.let {
                ClubMainImage(
                    id = it.id,
                    name = it.name.orEmpty(),
                    imageUrl = it.image!!.url
                )
            }

    override fun mapFeatures(settingsRemoteRow: List<ClubSettingsRemoteRow>): List<ClubFeature> =
        settingsRemoteRow
            .filter { it.key == "features" }
            .map {
                ClubFeature(
                    id = it.id,
                    name = it.name.orEmpty(),
                    description = it.description ?: "",
                    imageUrl = it.image?.url
                )
            }
}
