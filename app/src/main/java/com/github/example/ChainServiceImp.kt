package com.github.example

import com.chain.Chain

class ChainServiceImp {
    fun apply() {
        val chainService = Chain.Builder()
            .module("demo")
            .service("demo:DemoService")
            .build().create(ChainService::class.java)
        chainService?.apply()
    }

    fun display(text: String) {
        val chainService = Chain.Builder()
            .module("demo")
            .service("demo:DemoService")
            .build().create(ChainService::class.java)
        chainService?.display(text)
    }
}