package com.github.demo

import com.chain.annotation.Chain

@Chain(service = "demo:UserService")
class UserServiceImpl() : UserService {
    override fun work() {
        println("${javaClass.simpleName} work")
    }

    override fun apply(): String? {
        println("${javaClass.simpleName} apply")
        return "apply"
    }

    override fun execute(task: Runnable) {
        println("${javaClass.simpleName} execute")
        task.run()
    }
}