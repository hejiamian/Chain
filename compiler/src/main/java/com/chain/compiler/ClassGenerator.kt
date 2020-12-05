package com.chain.compiler

import com.chain.annotation.Chain
import com.squareup.javapoet.*
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class ClassGenerator(val elements: MutableList<TypeElement>) {
    companion object {
        private const val CLASSNAME = "ChainBinding"
        private val HASHMAP = ClassName.get("java.util", "HashMap")
        private const val CHAINS = "chains"
        private const val VALUE = "value"
    }

    fun create(): TypeSpec {
        val fields = mutableListOf<FieldSpec>()
        val methods = mutableListOf<MethodSpec>()
        fields += Generator.createField(
            HASHMAP, CHAINS, CodeBlock.of(
                "new \$L<\$L, \$L>()",
                HashMap::class.java.simpleName,
                String::class.java.simpleName,
                Any::class.java.simpleName
            )
        )

        val constructorCodeBlocks = mutableListOf<CodeBlock>()
        elements.forEach {
            val service = it.getAnnotation(Chain::class.java).service
            constructorCodeBlocks += CodeBlock.of("this.\$L.put(\"\$L\", new \$L())", CHAINS, service, it.qualifiedName)
        }
        methods += Generator.createConstructor(constructorCodeBlocks)

        val codeBlocks = mutableListOf<CodeBlock>()
        val parameters = mutableListOf<ParameterSpec>()
        parameters += Generator.createParameter(String::class.java, VALUE)
        codeBlocks += CodeBlock.of("return \$L.get(\$L)", CHAINS, VALUE)
        methods += Generator.createMethod("get", Object::class.java, parameters, codeBlocks)

        return TypeSpec.classBuilder(CLASSNAME)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addFields(fields)
            .addMethods(methods)
            .build()
    }
}