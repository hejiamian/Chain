package com.github.customplugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AppPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def android = project.extensions.getByType(AppExtension)

        def appTransform = new AppTransform(project)
        android.registerTransform(appTransform)

        def extension = project.extensions.create("InjectClassCode", InjectExtension)
        project.afterEvaluate {
            appTransform.injectCode = extension.injectCode
        }
    }
}