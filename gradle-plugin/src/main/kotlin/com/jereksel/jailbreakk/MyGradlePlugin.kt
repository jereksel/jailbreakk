package com.jereksel.jailbreakk

import org.gradle.api.Plugin
import org.gradle.api.Project

class MyGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        /*
         * Users can configure this extension in their build.gradle like this:
         * debugLog {
         *   enabled = false
         *   // ... set other members on the MyGradleExtension class
         * }
         */
        project.extensions.create(
            "jailbreakk",
            MyGradleExtension::class.java
        )
    }
}