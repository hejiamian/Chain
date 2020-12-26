package com.github.customplugin

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.gradle.api.Project

import java.awt.MultipleGradientPaint

class InjectClass {
    private ClassPool pool = ClassPool.getDefault()

    void inject(String path, Project project, String injectCode) {
        println("filePath = " + path)

        pool.appendClassPath(path)

        pool.appendClassPath(project.android.bootClasspath[0].toString())

        File dir = new File(path)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->
                if (file.name == "MainActivity.class") {
                    CtClass ctClass = pool.getCtClass("com.github.example.MainActivity")

                    if (ctClass.isFrozen()) ctClass.defrost()

                    CtMethod ctMethod = ctClass.getDeclaredMethod("onCreate")
                    println("ctMethod = " + ctMethod)

                    ctMethod.insertBefore(injectCode)
                    ctClass.writeFile(path)
                    ctClass.detach()
                }
            }
        }
    }
}