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

interface ClubInfoMapper {

    fun mapTrainers(trainersRemoteRow: List<TrainersRemoteRow>): List<Trainer>

    fun mapQuestions(questionRemote: List<QuestionRemoteRow>): List<Question>

    fun mapStatistics(settingsRemoteRow: List<ClubSettingsRemoteRow>): List<Statistic>

    fun mapTraining(workoutRemoteRow: List<WorkoutRemoteRow>): List<Training>

    fun mapSchedule(scheduleRemote: List<ScheduleRemoteRow>): List<Schedule>

}