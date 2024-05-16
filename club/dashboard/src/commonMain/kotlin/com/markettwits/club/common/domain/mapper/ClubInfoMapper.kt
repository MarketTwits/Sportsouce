package com.markettwits.club.common.domain.mapper

import com.markettwits.club.cloud.models.club_settings.ClubSettingsRemote
import com.markettwits.club.cloud.models.questions.QuestionRemoteRow
import com.markettwits.club.cloud.models.schedule.ScheduleRemote
import com.markettwits.club.cloud.models.trainer.TrainersRemote
import com.markettwits.club.cloud.models.workout.WorkoutRemote
import com.markettwits.club.info.domain.models.Question
import com.markettwits.club.info.domain.models.Schedule
import com.markettwits.club.info.domain.models.Statistic
import com.markettwits.club.info.domain.models.Trainer
import com.markettwits.club.info.domain.models.Training

interface ClubInfoMapper {
    fun mapTrainers(trainersRemoteRow: TrainersRemote): List<Trainer>
    fun mapQuestions(questionRemoteRow: QuestionRemoteRow): List<Question>
    fun mapStatistics(settingsRemoteRow: ClubSettingsRemote): List<Statistic>
    fun mapTraining(workoutRemoteRow: WorkoutRemote): List<Training>
    fun mapSchedule(scheduleRemote: ScheduleRemote): List<Schedule>
}