package com.markettwits.crashlitics.configuration

import com.markettwits.buildkonfig.isDebugMode
import ru.ok.tracer.CoreTracerConfiguration
import ru.ok.tracer.TracerConfiguration
import ru.ok.tracer.crash.report.CrashFreeConfiguration
import ru.ok.tracer.crash.report.CrashReportConfiguration
import ru.ok.tracer.heap.dumps.HeapDumpConfiguration
import ru.ok.tracer.profiler.sampling.SamplingProfilerConfiguration

abstract class AnalyticsConfigurationAbstract : AnalyticsConfiguration {
    override val defaultTracerConfigurations: MutableList<TracerConfiguration> by lazy {
        val enabled = !isDebugMode
        mutableListOf(
            CoreTracerConfiguration.build {
            },
            CrashReportConfiguration.build {
                setEnabled(enabled)
                setSendAnr(enabled)
                setNativeEnabled(enabled)
            },
            CrashFreeConfiguration.build {
                setEnabled(enabled)
            },
            SamplingProfilerConfiguration.build {
                setEnabled(enabled)
            },
            HeapDumpConfiguration.build {
                setEnabled(enabled)
            },
        )
    }

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
