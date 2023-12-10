package com.kgignatyev.leds.effects

import com.kgignatyev.leds.LedStringSection
import java.awt.Color
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class GlowAll(val sections:List<LedStringSection>, val rgb: (Long, Double) -> Color, val duration: Duration):Effect{

    val glowSections = sections.map { section ->
        Glow(section, rgb, duration)
    }

    override fun renderFrame() {
        glowSections.forEach { glow ->
            glow.renderFrame()
        }
    }

    override fun init(frameCount: Int, frameDuration: Duration) {
        glowSections.forEach { glow ->
            glow.init(frameCount, frameDuration)
        }
    }

}


class Glow(val section: LedStringSection, val frgb:(Long,Double)-> Color, val duration: Duration): Effect {
    var frameDuration: Duration = 0.05.seconds

    var currentFrame = 0

    override fun renderFrame() {
        currentFrame++
        val elapsedTime = frameDuration * currentFrame
        val elapsedTimeInMillis = elapsedTime.inWholeMilliseconds
        val durationInMillis = duration.inWholeMilliseconds
        val cycle = elapsedTimeInMillis / durationInMillis
        val inCycleMillis = elapsedTimeInMillis - (cycle * durationInMillis)
        val fractionOfCycle =  inCycleMillis.toDouble()/durationInMillis.toDouble()
        val isEven = cycle % 2 == 0L
        val rgb = frgb(cycle, fractionOfCycle)
        val newRGB = Color(
            convertColor(rgb.red, fractionOfCycle, isEven),
            convertColor(rgb.green, fractionOfCycle, isEven),
            convertColor(rgb.blue, fractionOfCycle, isEven)
        )
        section.setStates( MutableList(section.length) { newRGB } )
    }

    fun convertColor(v:Int, fraction: Double, increase:Boolean ): Int {
        val c = if( increase ) {
            (v * fraction).toInt()
        } else {
            v - (v * fraction).toInt()
        }
        return if (c < 10) {
             10
        }else {
            c
        }
    }

    override fun init(frameCount: Int, frameDuration: Duration) {
        this.frameDuration = frameDuration

    }
}
