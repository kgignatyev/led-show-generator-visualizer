package com.kgignatyev.leds.effects

import com.kgignatyev.leds.LedStringSection
import kotlin.time.Duration


class Rainbow(val ledSection: LedStringSection, shift: Int) : Effect {
    var frameN = 0
    val k = 20
    var wheelPosition = shift
    val wheelStep = 255 / ledSection.length
    override fun renderFrame() {
        if (frameN % k == 0) {
            (0 until ledSection.length).forEach { ledIndex ->
                var ledColorPosition = ledIndex * wheelStep + wheelPosition
                if( ledColorPosition > 255 ) {
                    ledColorPosition = 0
                }
                val ledColor = wheel(ledColorPosition)
                if (wheelPosition > 255) {
                    wheelPosition = 0
                }
                ledSection.set(ledIndex, ledColor)
                wheelPosition++
            }
        }
        frameN++
    }

    override fun init(frameCount: Int, frameDuration: Duration) {

    }

    companion object {
        fun rainbowStrips(ledSections: List<LedStringSection>, shift: Int): Array<Effect> {
            return ledSections.mapIndexed { index, section ->
                Rainbow(section, shift * index)
            }.toTypedArray()
        }
    }
}
