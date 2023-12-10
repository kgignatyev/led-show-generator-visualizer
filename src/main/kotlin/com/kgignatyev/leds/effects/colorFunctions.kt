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
