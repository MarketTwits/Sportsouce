package com.markettwits.club.common.data.mapper.club_info

import com.markettwits.club.cloud.models.club_settings.ClubSettingsRemoteRow
import com.markettwits.club.cloud.models.questions.QuestionRemoteRow
import com.markettwits.club.cloud.models.schedule.ScheduleRemoteRow
import com.markettwits.club.cloud.models.trainer.TrainersRemoteRow
import com.markettwits.club.cloud.models.workout.WorkoutRemoteRow
import com.markettwits.club.info.domain.models.Question
import com.markettwits.club.info.domain.models.Schedule
import com.markettwits.club.info.domain.models.Statistic
import com.markettwits.club.info.domain.models.Trainer
import com.markettwits.club.info.domain.models.Training

interface ClubInfoMapper {

    fun mapTrainers(trainersRemoteRow: List<TrainersRemoteRow>): List<Trainer>

    fun mapQuestions(questionRemote: List<QuestionRemoteRow>): List<Question>

    fun mapStatistics(settingsRemoteRow: List<ClubSettingsRemoteRow>): List<Statistic>

    fun mapTraining(workoutRemoteRow: List<WorkoutRemoteRow>): List<Training>

    fun mapSchedule(scheduleRemote: List<ScheduleRemoteRow>): List<Schedule>

}