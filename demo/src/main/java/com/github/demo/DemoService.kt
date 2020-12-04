package com.github.demo

import java.util.*

interface DemoService {
    fun apply(): String
    fun createTime(): Date
    fun display(text: String)
}