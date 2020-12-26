package com.github.standard

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class StandardPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val android = project.extensions.getByType(AppExtension::class.java)
        val transform = StandardTransform(project)
        android.registerTransform(transform)

        val extension = project.extensions.create("InjectClassCode", StandardExtension::class.java)
        project.afterEvaluate {
            transform.injectCode = extension.injectCode
            transform.android = extension.android
        }
    }
}