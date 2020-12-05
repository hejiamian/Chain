package com.chain

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class Chain constructor(private val module: String?, private val service: String?) {
    private val targets = mutableMapOf<String?, Any?>()
    fun <T> create(clazz: Class<T>): T? {
        val serviceAny = targets[module].obtainTarget()
        val classes = arrayOf(clazz)
        return serviceAny?.let {
            Proxy.newProxyInstance(
                serviceAny::class.java.classLoader,
                classes, object : InvocationHandler {
                    override fun invoke(proxy: Any?, method: Method, args: Array<out Any>?): Any? {
                        try {
                            if (args === null || args.isEmpty()) {
                                val otherMethod = serviceAny::class.java.getDeclaredMethod(method.name)
                                return otherMethod.invoke(serviceAny)
                            } else {
                                val types = arrayOfNulls<Class<*>>(args.size)
                                for (i in args.indices) {
                                    types[i] = args[i]::class.java
                                }
                                val otherMethod = serviceAny::class.java.getDeclaredMethod(method.name, *types)
                                return otherMethod.invoke(serviceAny, *args)
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            throw ex
                        }
                    }
                }) as T
        }
    }

    private fun Any?.obtainTarget(): Any? {
        return if (this === null) {
            try {
                val clazz = Class.forName("${module}.ChainBinding")
                val constructor = clazz.getDeclaredConstructor()
                val binding = constructor.newInstance()
                val method = binding.javaClass.getDeclaredMethod("get", String::class.java)
                val serviceInstance = method.invoke(binding, service)
                targets[module] = serviceInstance
                serviceInstance
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
        } else this
    }

    class Builder {
        private var module: String? = null
        private var service: String? = null
        fun module(module: String?) = apply {
            this@Builder.module = module
        }

        fun service(service: String?) = apply {
            this@Builder.service = service
        }

        fun build(): Chain {
            return Chain(module, service)
        }
    }
}