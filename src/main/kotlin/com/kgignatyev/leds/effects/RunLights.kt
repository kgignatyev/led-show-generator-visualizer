package com.kgignatyev.leds.effects

import com.kgignatyev.leds.LedStringSection
import java.awt.Color
import kotlin.time.Duration


class RunLights( val sections:List<LedStringSection>,vararg val colors: (Long, Double) -> Color ):Effect{

    var currentFrame = 0
    var framesPerShift = 4
    val colorsPerLedFunction = mutableListOf<(Long, Double) -> Color>()
    override fun renderFrame() {
        val ledColors = colorsPerLedFunction.map { color ->
            color(currentFrame.toLong(), 0.0)
        }
        sections.forEach { section ->
            section.setStates(ledColors)
        }
        currentFrame++
        if( currentFrame % framesPerShift == 0 ) {
            val lastColor = colorsPerLedFunction.removeLast()
            colorsPerLedFunction.addFirst(lastColor)
        }
    }

    override fun init(frameCount: Int, frameDuration: Duration) {
        val ledsPerColor = sections[0].length / colors.size

        0.until(sections[0].length).forEach { ledIndex ->
            val colorIndex = ledIndex / ledsPerColor
            val color = colors[colorIndex]
            colorsPerLedFunction.add(color)
        }
    }

}
