package com.kgignatyev.leds

import java.awt.Color



class LedStringSection( val length: Int = 50, val reversed: Boolean = false) {
    val leds: MutableList<Color> = MutableList(length) { Color.black}

    fun setStates(states: List<Color>) {
        if (states.size != length) {
            throw IllegalArgumentException("States size must be equal to section length")
        }
        leds.clear()
        if( reversed ) {
            leds.addAll(states.reversed())
        }else {
            leds.addAll(states)
        }
    }

    fun set(ledIndex: Int, rgb: Color) {
        if( ledIndex >= length ) {
            throw IllegalArgumentException("Led index must be less than section length")
        }
        if( reversed ) {
            leds[length - ledIndex - 1] = rgb
        } else {
            leds[ledIndex] = rgb
        }
    }

    fun allOff() {
        leds.fill(Color(0, 0, 0))
    }
}
