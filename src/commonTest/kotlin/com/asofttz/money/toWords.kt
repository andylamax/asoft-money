package com.asofttz.money

import kotlin.js.JsName
import kotlin.test.Test

class ToWordsTest {

    @Test
    @JsName("test1")
    fun `should convert to readable words`() {
        println("*** " + 123_456_789.convert())
        println("*** " + (-75).convert())
    }
}