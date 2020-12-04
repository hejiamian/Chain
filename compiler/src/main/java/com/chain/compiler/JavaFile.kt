package com.chain.compiler

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import java.io.IOException
import javax.annotation.processing.Filer

class JavaFile() {
    companion object {
        fun create(filer: Filer, packageName: String, type: TypeSpec) {
            var javaFile = JavaFile.builder(
                packageName, type
            ).build()

            try {
                javaFile.writeTo(filer)
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
    }
}