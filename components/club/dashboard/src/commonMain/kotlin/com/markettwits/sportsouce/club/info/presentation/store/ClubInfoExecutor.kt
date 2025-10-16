package com.markettwits.sportsouce.club.info.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStore.*
import com.markettwits.sportsouce.club.registration.domain.RegistrationType

internal class ClubInfoExecutor : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)

            is Intent.OnTrainerRegister -> {
                publish(
                    Label.OpenRegistration(
                        RegistrationType.Trainer(
                            intent.trainer.id,
                            intent.trainer.fullName()
                        )
                    )
                )
            }

            is Intent.OnTrainingRegister -> {
                publish(
                    Label.OpenRegistration(
                        RegistrationType.Workout(
                            intent.training.id,
                            intent.training.type
                        )
                    )
                )
            }

            is Intent.OnClickSubscribe -> {
                publish(Label.OpenSchedule)
            }
        }
    }
}
