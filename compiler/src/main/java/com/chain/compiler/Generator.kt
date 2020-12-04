package com.chain.compiler

import com.squareup.javapoet.*
import java.lang.reflect.Type
import javax.lang.model.element.Modifier

class Generator {
    companion object {
        fun createField(className: ClassName, name: String, codeBlock: CodeBlock): FieldSpec {
            return FieldSpec.builder(className, name)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer(codeBlock)
                .build()
        }

        fun createMethod(
            name: String, returnType: Type, parameters: Collection<ParameterSpec>,
            codeBlocks: Collection<CodeBlock>
        ): MethodSpec {
            val methodBuilder = MethodSpec.methodBuilder(name)
            methodBuilder.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(returnType)
                .addParameters(parameters)
            codeBlocks.forEach {
                methodBuilder.addStatement(it)
            }
            return methodBuilder.build()
        }

        fun createConstructor(codeBlocks: Collection<CodeBlock>): MethodSpec {
            val methodBuilder = MethodSpec.constructorBuilder()
            methodBuilder.addModifiers(Modifier.PUBLIC)
            codeBlocks.forEach {
                methodBuilder.addStatement(it)
            }
            return methodBuilder.build()
        }

        fun createParameter(type: Type, name: String): ParameterSpec {
            return ParameterSpec.builder(type, name).addModifiers(Modifier.FINAL).build()
        }
    }
}