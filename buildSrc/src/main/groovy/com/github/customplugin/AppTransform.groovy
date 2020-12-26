package com.github.customplugin

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.gradle.api.Project

class AppTransform extends Transform {
    private Project project
    String injectCode

    AppTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return "PreDexInjectCode"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        transformInvocation.inputs.each { TransformInput input ->
            input.jarInputs.each { JarInput jarInput ->
                def jarName = jarInput.name
                def dest = transformInvocation.outputProvider.getContentLocation(
                        jarName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(jarInput.file, dest)
            }
            input.directoryInputs.each { DirectoryInput directoryInput ->
                def dest = transformInvocation.outputProvider.getContentLocation(
                        directoryInput.name, directoryInput.contentTypes, directoryInput.scopes,
                        Format.DIRECTORY)
                def injectClass = new InjectClass()
                injectClass.inject(directoryInput.file.parent, project, injectCode)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
        }
    }
}