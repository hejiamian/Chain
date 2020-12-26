package com.github.standard

import javassist.ClassPool
import org.gradle.api.Project
import java.io.File

class InjectClass {
    companion object {
        private val pool: ClassPool by lazy {
            ClassPool.getDefault()
        }

        fun injectCode(path: String, project: Project, android: String, injectCode: String) {
            println("filePath = $path")
            pool.appendClassPath(path)
            pool.appendClassPath(android)
//            pool.appendClassPath(project.android.bootClasspath[0].toString())

            val dir = File(path)
            traversal(dir, path, injectCode)
        }

        private fun traversal(file: File, path: String, injectCode: String) {
            if (file.isFile) {
                if (file.name == "MainActivity.class") {
                    println("file.name = ${file.name}")
                    val ctClass = pool.getCtClass("com.github.example.MainActivity")
                    println("ctClass = $ctClass")
                    if (ctClass.isFrozen) ctClass.defrost()

                    val ctMethod = ctClass.getDeclaredMethod("onCreate")
                    println("ctMethod = $ctMethod")

                    ctMethod.insertBefore(injectCode)
                    println("file.absolutePath = ${file.absolutePath}")
                    ctClass.writeFile(path)
                    ctClass.detach()
                }
            } else {
                val files = file.listFiles()
                files.forEach {
                    traversal(it, path, injectCode)
                }
            }
        }
    }


}