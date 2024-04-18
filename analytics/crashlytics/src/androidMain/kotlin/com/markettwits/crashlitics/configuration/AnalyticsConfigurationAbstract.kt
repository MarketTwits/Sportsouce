package com.markettwits.crashlitics.configuration

import ru.ok.tracer.CoreTracerConfiguration
import ru.ok.tracer.TracerConfiguration
import ru.ok.tracer.crash.report.CrashFreeConfiguration
import ru.ok.tracer.crash.report.CrashReportConfiguration

abstract class AnalyticsConfigurationAbstract : AnalyticsConfiguration {

    override val defaultTracerConfigurations: MutableList<TracerConfiguration> =
        mutableListOf(
            CoreTracerConfiguration.build {},
            CrashReportConfiguration.build {},
            CrashFreeConfiguration.build {},
        )

    override val tracerConfiguration: MutableList<TracerConfiguration>
        get() = defaultTracerConfigurations

    override fun addConfiguration(tracerConfiguration: TracerConfiguration) {
        defaultTracerConfigurations.add(tracerConfiguration)
    }

    override fun addConfigurations(tracerConfigurations: List<TracerConfiguration>) {
        defaultTracerConfigurations.addAll(tracerConfiguration)
    }

    override fun updateConfigurations(tracerConfigurations: List<TracerConfiguration>) {
        defaultTracerConfigurations.clear()
        addConfigurations(tracerConfigurations)
    }

    override fun getConfigurations(): List<TracerConfiguration> = tracerConfiguration
}