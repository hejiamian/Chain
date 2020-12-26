package com.github.standard

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.gradle.api.Project

class StandardTransform(private val project: Project) : Transform() {
    var injectCode = ""
    var android = ""
    override fun getName() = "PreDexInjectCode"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental() = false

    override fun transform(transformInvocation: TransformInvocation) {
        super.transform(transformInvocation)
        val inputs = transformInvocation.inputs
        val outputProvider = transformInvocation.outputProvider

        inputs.forEach { input ->

            input.directoryInputs.forEach { directoryInput ->
                val dest = outputProvider.getContentLocation(
                    directoryInput.name, directoryInput.contentTypes,
                    directoryInput.scopes, Format.DIRECTORY
                )

                InjectClass.injectCode(directoryInput.file.absolutePath, project, android, injectCode)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }

            input.jarInputs.forEach { jarInput ->
                val dest = outputProvider.getContentLocation(
                    jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR
                )
                FileUtils.copyFile(jarInput.file, dest)
            }
        }
    }

}