package com.github.example

import java.util.*

interface ChainService {
    fun apply(): String
    fun createTime(): Date
    fun display(text: String)
}