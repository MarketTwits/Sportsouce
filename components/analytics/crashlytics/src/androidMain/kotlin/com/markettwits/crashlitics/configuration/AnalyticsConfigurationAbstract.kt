package com.markettwits.crashlitics.configuration

import com.markettwits.analitics.crashlytics.BuildConfig
import ru.ok.tracer.CoreTracerConfiguration
import ru.ok.tracer.TracerConfiguration
import ru.ok.tracer.crash.report.CrashFreeConfiguration
import ru.ok.tracer.crash.report.CrashReportConfiguration
import ru.ok.tracer.disk.usage.DiskUsageConfiguration
import ru.ok.tracer.heap.dumps.HeapDumpConfiguration
import ru.ok.tracer.profiler.sampling.SamplingProfilerConfiguration

abstract class AnalyticsConfigurationAbstract : AnalyticsConfiguration {

    private val enabled: Boolean = !BuildConfig.DEBUG

    override val defaultTracerConfigurations: MutableList<TracerConfiguration> =
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
            DiskUsageConfiguration.build {
                setEnabled(enabled)
                setInterestingSize(3L * 1024 * 1024 * 1024) // 3GB. Default 10GB
                setProbability(100) // ( 1 / 100 ) * 100% = 1%
            },
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