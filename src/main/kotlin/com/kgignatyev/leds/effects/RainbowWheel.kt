package com.kgignatyev.leds.effects

import com.kgignatyev.leds.LedStringSection
import com.kgignatyev.leds.ledSections
import kotlin.time.Duration


class RainbowWheel(ledSections: List<LedStringSection>) : Effect {

    val wheelK = 7
    var wheelPosition = 0
    val k = 20
    var frameN = 0
    override fun renderFrame() {
        if( frameN % k == 0 ) {
            ledSections.forEach { section ->
                val stripColor = wheel(wheelPosition)
                section.setStates(MutableList(section.length) { stripColor })
                wheelPosition+=wheelK
                if (wheelPosition > 255) {
                    wheelPosition = 0
                }
            }
        }
        frameN++
    }

    override fun init(frameCount: Int, frameDuration: Duration) {
        // nothing to do
    }
}
