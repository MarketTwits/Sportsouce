package com.markettwits.sportsouce.club.dashboard.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.isNetworkConnectionError
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core.log.LogTagProvider
import com.markettwits.core.log.errorLog
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.club.common.domain.ClubRepository
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import com.markettwits.sportsouce.club.info.domain.models.ClubInfo

internal class ClubDashboardExecutor(
    private val clubRepository: ClubRepository,
    private val exceptionTracker: ExceptionTracker,
) : CoroutineExecutor<Intent, Unit, State, Message, Label>(), LogTagProvider {

    override val tag: String = "ClubDashboardExecutor"

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnClickBack -> publish(Label.GoBack)

            is Intent.RetryRequest -> launchClubInfo()

            is Intent.OnClickRegistration -> publish(Label.OnClickRegistration(intent.type))


            is Intent.OnClickSubscriptions -> {
                publish(Label.OnClickSubscriptions)
            }

            is Intent.OnClickSchedule -> {
                publish(Label.OnClickSchedule)
            }

            is Intent.OpenClubInfoDetail -> {
                publish(Label.OpenClubInfoDetail(intent.selectedTab, intent.bottomSheetData))
            }
        }
    }

    override fun executeAction(action: Unit) {
        launchClubInfo()
    }

    private fun launchClubInfo() {
        if (state().isLoading) return
        dispatch(Message.Loading)
        scope.launch {
            clubRepository.clubInfo()
                .onSuccess { clubInfoList ->
                    dispatch(Message.UpdateBottomSheetData(extractBottomSheetData(clubInfoList)))
            }.onFailure {
                if (it is CancellationException) throw it
                dispatch(Message.Failed(if (it.isNetworkConnectionError()) SauceError.Connection(it) else SauceError.General(it)))
                errorLog { "can't load List<ClubInfo : ${it.message}" }
                if (!it.isNetworkConnectionError())
                    exceptionTracker.reportException(
                        exception = it,
                        key = "ClubDashboardExecutor#launchClubInfo"
                    )
            }
        }
    }

    private fun extractBottomSheetData(clubInfoList: List<ClubInfo>): BottomSheetData {
        val trainers =
            clubInfoList.filterIsInstance<ClubInfo.Commands>()
                .flatMap { it.trainers }

        val trainings =
            clubInfoList.filterIsInstance<ClubInfo.Trainings>()
                .flatMap { it.training }

        val statistics =
            clubInfoList.filterIsInstance<ClubInfo.Statistics>()
                .flatMap { it.statistics }

        val questions =
            clubInfoList.filterIsInstance<ClubInfo.Questions>()
                .flatMap { it.questions }

        val features =
            clubInfoList.filterIsInstance<ClubInfo.Features>()
                .flatMap { it.features }

        return BottomSheetData(
            trainers = trainers,
            trainings = trainings,
            statistics = statistics,
            questions = questions,
            features = features,
            mainImage = clubInfoList.filterIsInstance<ClubInfo.MainImage>().firstOrNull()?.url,
            steps = clubInfoList.filterIsInstance<ClubInfo.Process>().flatMap { it.steps },
        )
    }
}
