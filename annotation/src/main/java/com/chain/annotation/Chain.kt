package com.chain.annotation

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Chain(val service: String)