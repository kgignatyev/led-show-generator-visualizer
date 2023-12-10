package com.kgignatyev.leds


data class RGB(val r: Int, val g: Int, val b: Int)



class LedStringSection( val length: Int = 50, val reversed: Boolean = false) {
    val leds: MutableList<RGB> = MutableList(length) { RGB(0, 0, 0) }

    fun setStates(states: List<RGB>) {
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

    fun set(ledIndex: Int, rgb: RGB) {
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
        leds.fill(RGB(0, 0, 0))
    }
}
