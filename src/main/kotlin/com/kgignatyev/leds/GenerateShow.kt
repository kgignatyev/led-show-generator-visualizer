package com.kgignatyev.leds

import com.kgignatyev.leds.effects.*
import com.kgignatyev.leds.effects.Rainbow.Companion.rainbowStrips
import java.awt.Color
import java.io.FileWriter
import java.io.Writer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

val frameDuration = 20.milliseconds
val ledSections = listOf(
    LedStringSection(50),
    LedStringSection(50, true),
    LedStringSection(50),
    LedStringSection(50, true),
    LedStringSection(50),
    LedStringSection(50, true),
)



private lateinit var writer: Writer

fun main() {

    val fileName = "light-show.txt"
    FileWriter(fileName).use {
        writer = it
        allOff()
        runEffects(10.seconds,*rainbowStrips(ledSections, 10 ) )
        allOff()
        runEffects(10.seconds, RainbowWheel(ledSections) )
        allOff()
        runEffects(10.seconds, RunLights(ledSections,
             constColor( Color(30,144,255)),
             constColor(Color(173,255,47)),
             constColor(Color(165,42,42)),
             constColor(Color(255,255,0)),
             constColor(Color(250,128,114))) )
        allOff()
        runEffects(2.seconds, FillByOne(ledSections[0], Color.ORANGE) )
        runEffects(2.seconds, FillByOne(ledSections[1], Color.ORANGE) )
        runEffects(2.seconds, FillByOne(ledSections[2], Color.ORANGE) )
        runEffects(2.seconds, FillByOne(ledSections[3], Color.ORANGE) )
        runEffects(2.seconds, FillByOne(ledSections[4], Color.ORANGE) )
        runEffects(2.seconds, FillByOne(ledSections[5], Color.ORANGE) )

        runEffects(2.seconds, FillByOne(ledSections[0], Color.green) )
        runEffects(2.seconds, FillByOne(ledSections[1], Color.green) )
        runEffects(2.seconds, FillByOne(ledSections[2], Color.green) )
        runEffects(2.seconds, FillByOne(ledSections[3], Color.green) )
        runEffects(2.seconds, FillByOne(ledSections[4], Color.green) )
        runEffects(2.seconds, FillByOne(ledSections[5], Color.green) )


        runEffects(2.seconds, FillByOne(ledSections[0], Color.blue) )
        runEffects(2.seconds, FillByOne(ledSections[1], Color.blue) )
        runEffects(2.seconds, FillByOne(ledSections[2], Color.blue) )
        runEffects(2.seconds, FillByOne(ledSections[3], Color.blue) )
        runEffects(2.seconds, FillByOne(ledSections[4], Color.blue) )
        runEffects(2.seconds, FillByOne(ledSections[5], Color.blue) )
        allOff()

        runEffects(
            4.seconds,
            Glow(ledSections[0], constColor(Color.red), 2.seconds),
            Glow(ledSections[1], constColor(Color.green), 2.seconds),
            Glow(ledSections[2], constColor(Color.blue), 2.seconds),
            Glow(ledSections[3], constColor(Color.ORANGE), 2.seconds),
            Glow(ledSections[4], constColor(Color.CYAN), 2.seconds),
            Glow(ledSections[5], constColor(Color.YELLOW), 2.seconds),
        )
        allOff()
        runEffects(16.seconds, GlowAll(ledSections, rainbow(), 4.seconds))
        allOff()
        runEffects(4.seconds, GlowAll(ledSections, constColor( Color.red), 2.seconds))
        runEffects(4.seconds, GlowAll(ledSections, constColor(Color.green), 2.seconds))
        runEffects(4.seconds, GlowAll(ledSections, constColor(Color.blue), 2.seconds))
        end()
    }

    println("Show is generated in $fileName")
}

fun allOff() {
    ledSections.forEach { section ->
        section.allOff()
    }
}


fun runEffects( duration: Duration, vararg effects: Effect) {
    val frames = duration / frameDuration
    val frameCount = frames.toInt()
    effects.forEach { it.init(frameCount,frameDuration) }
    for (i in 0 until frameCount) {
        effects.forEach { it.renderFrame() }
        writeFrame()
    }
}

fun end() {
    writer.write("END\n") // end of show, need this to ensure we got full file when
    //we are reading it by PI
    writer.flush()
}
fun writeFrame() {
    val line = ledSections.joinToString(separator = ",") { section ->
        section.leds.joinToString(separator = ",") { led ->
            "${led.red} ${led.green} ${led.blue}"
        }
    } + "\n"
    writer.write(line)
}

