package com.asofttz.money

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import kotlin.math.floor

@Serializable
data class Money(var value: Double = 0.0, var cur: Currency = Currency.TZS) {
    @Serializer(forClass = Money::class)
    companion object : KSerializer<Money> {
        override val descriptor = StringDescriptor.withName("Money")

        override fun deserialize(input: Decoder): Money {
            val splits = input.decodeString().split(" ")
            val cur = Currency.valueOf(splits[0])
            return Money(splits[1].replace(",", "").toDouble(), cur)
        }

        override fun serialize(output: Encoder, obj: Money) {
            output.encodeString(obj.toFullString())
        }
    }

    operator fun times(n: Number) = Money(n.toDouble() * value, cur)

    operator fun plus(m: Money): Money {
        if (m.cur != cur) throw Exception("Can't add between two currencies ${m.cur.name}/${cur.name}")
        return Money(m.value + value, cur)
    }

    operator fun plusAssign(m: Money) {
        if (m.cur != cur) throw Exception("Can't add between two currencies ${m.cur.name}/${cur.name}")
        value += m.value
    }

    fun toFullString(): String = "${cur.name} $this"

    override fun toString(): String {
        var out = ""
        var amount = floor(value * 100) / 100
        val splits = amount.toString().split(".")
        var characteristic = splits[0]
        var mantisa = splits.elementAtOrElse(1) { "0" }
        var counts = 0
        for (i in characteristic.length downTo 0) {
            out = characteristic[i] + out
            if (counts == 3 && i != 0) {
                out = ",$out"
                counts = 0
            }
            counts++
        }
        val mantisaValue = (if (mantisa.toInt() < 10) "0" else "") + mantisa.toInt()
        if (mantisaValue != "00") {
            out += ".$mantisaValue"
        }
        return out
    }
}

operator fun Number.times(m: Money) = m * this
fun Number.toMoney(currency: Currency = Currency.TZS) = Money(this.toDouble(), currency)