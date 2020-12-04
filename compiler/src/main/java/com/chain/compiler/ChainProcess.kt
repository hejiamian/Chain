package com.chain.compiler

import com.chain.annotation.Chain
import com.google.auto.service.AutoService
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic.Kind.NOTE

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.chain.annotation.Chain")
@AutoService(Processor::class)
class ChainProcess : AbstractProcessor() {

    private val messager: Messager by lazy {
        processingEnv.messager
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment
    ): Boolean {
        val chains = mutableListOf<TypeElement>()
        val chainElements = roundEnv.getElementsAnnotatedWith(Chain::class.java)
        if (chainElements.isEmpty()) {
            messager.printMessage(NOTE, "Annotation Chain is never used!")
            return false
        }
        chainElements.forEach {
            if (it is TypeElement) {
                chains.add(it)
            } else {
                messager.printMessage(NOTE, "enclosingElement is not Type of TypeElement")
            }
        }

        val packageName = processingEnv.options["AROUTER_MODULE_NAME"]
        val clazz = ClassGenerator(chains).create()

        JavaFile.create(processingEnv.filer, packageName!!, clazz)
        return true
    }
}