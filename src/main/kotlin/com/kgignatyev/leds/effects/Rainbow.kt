package com.kgignatyev.leds.effects

import com.kgignatyev.leds.LedStringSection
import kotlin.time.Duration


class Rainbow(val ledSection: LedStringSection, shift: Int) : Effect {
    var frameN = 0
    val k = 20
    val wheelStep = 255 / ledSection.length
    val colorWheel = ColorWheel(shift, wheelStep)
    override fun renderFrame() {
        if (frameN % k == 0) {
            (0 until ledSection.length).forEach { ledIndex ->
                val ledColor = colorWheel.next()
                ledSection.set(ledIndex, ledColor)
                colorWheel.position += wheelStep
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
