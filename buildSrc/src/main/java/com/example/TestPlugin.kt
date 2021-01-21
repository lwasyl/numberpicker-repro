package com.example

import com.android.build.gradle.internal.LoggerWrapper
import com.android.build.gradle.internal.tasks.DeviceProviderInstrumentTestTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class TestPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.gradle.taskGraph.beforeTask {
            if (this is DeviceProviderInstrumentTestTask) {
                LoggerWrapper.Switch.shouldShowInfoLogsAsLifecycle = true
            }
        }

        target.gradle.taskGraph.afterTask {
            if (this is DeviceProviderInstrumentTestTask) {
                LoggerWrapper.Switch.shouldShowInfoLogsAsLifecycle = false
            }
        }
    }
}
