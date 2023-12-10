package com.kgignatyev.leds.effects

import com.kgignatyev.leds.RGB


fun constColor(rgb: RGB): (Long, Double) -> RGB {
    fun f(cycle: Long, fractionOfCycle: Double): RGB {
        return rgb
    }
    return ::f
}


fun rainbow(): (Long, Double) -> RGB {
    fun f(cycle: Long, fractionOfCycle: Double): RGB {
        val r = (Math.sin(fractionOfCycle * Math.PI * 2.0) * 127.0 + 128.0).toInt()
        val g = (Math.sin(fractionOfCycle * Math.PI * 2.0 + 2.0 * Math.PI / 3.0) * 127.0 + 128.0).toInt()
        val b = (Math.sin(fractionOfCycle * Math.PI * 2.0 + 4.0 * Math.PI / 3.0) * 127.0 + 128.0).toInt()
        return RGB(r, g, b)
    }
    return ::f
}
