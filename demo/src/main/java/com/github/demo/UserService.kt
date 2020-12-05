package com.github.demo

interface UserService {
    fun work()
    fun apply(): String?
    fun execute(task: Runnable)
}