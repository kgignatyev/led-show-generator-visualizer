package com.kgignatyev.leds.effects

import com.kgignatyev.leds.LedStringSection
import com.kgignatyev.leds.RGB
import kotlin.time.Duration


class FillByOne(val section: LedStringSection,val rgb: RGB): Effect {
    private var framesPerLed: Double = 1.0
    var frameCount = 10
    var currentFrame = 0
    override fun renderFrame() {
        val numLeds = ( framesPerLed * currentFrame).toInt()
        0.until(numLeds-1).forEach { ledIndex ->
            section.set(ledIndex, rgb)
        }
        currentFrame++
    }

    override fun init(frameCount: Int, frameDuration: Duration) {
        this.frameCount = frameCount
        framesPerLed = (1.0 *  section.length)/frameCount
    }
}
