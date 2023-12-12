package com.kgignatyev.leds.effects

import java.awt.Color


fun constColor(rgb: Color): (Long, Double) -> Color {
    fun f(cycle: Long, fractionOfCycle: Double): Color {
        return rgb
    }
    return ::f
}


fun rainbow(): (Long, Double) -> Color {
    fun f(cycle: Long, fractionOfCycle: Double): Color {
        val r = (Math.sin(fractionOfCycle * Math.PI * 2.0) * 127.0 + 128.0).toInt()
        val g = (Math.sin(fractionOfCycle * Math.PI * 2.0 + 2.0 * Math.PI / 3.0) * 127.0 + 128.0).toInt()
        val b = (Math.sin(fractionOfCycle * Math.PI * 2.0 + 4.0 * Math.PI / 3.0) * 127.0 + 128.0).toInt()
        return Color(r, g, b)
    }
    return ::f
}

fun wheel(pos: Int): Color {
    return when {
        pos < 85 -> Color(pos * 3, 255 - pos * 3, 0)
        pos < 170 -> {
            val adjustedPos = pos - 85
            Color(255 - adjustedPos * 3, 0, adjustedPos * 3)
        }
        else -> {
            val adjustedPos = pos - 170
            Color(0, adjustedPos * 3, 255 - adjustedPos * 3)
        }
    }
}
