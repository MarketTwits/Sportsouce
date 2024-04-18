package com.markettwits.crashlitics.configuration

import ru.ok.tracer.HasTracerConfiguration
import ru.ok.tracer.TracerConfiguration

interface AnalyticsConfiguration : HasTracerConfiguration {
    val defaultTracerConfigurations: List<TracerConfiguration>
    fun addConfiguration(tracerConfiguration: TracerConfiguration)
    fun addConfigurations(tracerConfigurations: List<TracerConfiguration>)
    fun updateConfigurations(tracerConfigurations: List<TracerConfiguration>)
    fun getConfigurations(): List<TracerConfiguration>
}