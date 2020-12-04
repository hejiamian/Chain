package com.github.demo

import com.chain.annotation.Chain
import java.util.*

@Chain(service = "demo:DemoService")
class DemoServiceImpl() : DemoService {
    override fun apply() = "reply!"
    override fun createTime() = Date()
    override fun display(text: String) {
        println("display $text")
    }
}