package com.kgignatyev.leds.effects

import com.kgignatyev.leds.LedStringSection
import java.awt.Color
import kotlin.time.Duration


class SparklyStrings(val ledStrings: List<LedStringSection>, val colorShift:Int) : Effect {

    private lateinit var sections: List<SparklyString>

    override fun renderFrame() {
        sections.forEach { ledString ->
            ledString.renderFrame()
        }
    }

    override fun init(frameCount: Int, frameDuration: Duration) {
        var initialColorPosition = 0
        sections = ledStrings.map { ledString ->
            val ss = SparklyString(ledString, ColorWheel(initialColorPosition, 1))
            initialColorPosition+=colorShift
            ss
        }
    }

}
class SparklyString(val ledString: LedStringSection, val wheel:ColorWheel) : Effect {

    val numSparks = 5
    var frameN = 0
    val nFrames = 3
    override fun renderFrame() {
        if( frameN % nFrames == 0 ) {
            ledString.setStates(MutableList(ledString.length) { Color.BLACK })
            val color = wheel.next()
            val sparkPositions = (0 until numSparks).map { (Math.random() * ledString.length).toInt() }
            sparkPositions.forEach { sparkPosition ->
                ledString.set(sparkPosition, color )
            }
        }
        frameN++
    }

    override fun init(frameCount: Int, frameDuration: Duration) {
        TODO("Not yet implemented")
    }
}
