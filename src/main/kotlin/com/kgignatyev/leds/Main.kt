package com.kgignatyev.leds

import com.kgignatyev.leds.effects.*
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

    FileWriter("light-show.txt").use {
        writer = it
//        runEffects(4.seconds,
//            FillByOne(ledSections[0], RGB(255, 0, 0)),
//            FillByOne(ledSections[1], RGB(0, 255, 0)),
//            FillByOne(ledSections[2], RGB(0, 0, 255)),
//            FillByOne(ledSections[3], RGB(100, 10, 105)),
//            FillByOne(ledSections[4], RGB(100, 255, 0)),
//            FillByOne(ledSections[5], RGB(255, 100, 0)),
//        )
//        allOff()
//        runEffects(2.seconds, FillByOne(ledSections[0], RGB(255, 0, 0)) )
//        allOff()
//        runEffects(2.seconds,FillByOne(ledSections[1], RGB(0, 255, 0)))
        allOff()
        runEffects(2.seconds, FillByOne(ledSections[0], RGB(255, 0, 0)) )
        runEffects(2.seconds, FillByOne(ledSections[1], RGB(255, 0, 0)) )
        runEffects(2.seconds, FillByOne(ledSections[2], RGB(255, 0, 0)) )
        runEffects(2.seconds, FillByOne(ledSections[3], RGB(255, 0, 0)) )
        runEffects(2.seconds, FillByOne(ledSections[4], RGB(255, 0, 0)) )
        runEffects(2.seconds, FillByOne(ledSections[5], RGB(255, 0, 0)) )

        runEffects(2.seconds, FillByOne(ledSections[0], RGB(0, 255, 0)) )
        runEffects(2.seconds, FillByOne(ledSections[1], RGB(0, 255, 0)) )
        runEffects(2.seconds, FillByOne(ledSections[2], RGB(0, 255, 0)) )
        runEffects(2.seconds, FillByOne(ledSections[3], RGB(0, 255, 0)) )
        runEffects(2.seconds, FillByOne(ledSections[4], RGB(0, 255, 0)) )
        runEffects(2.seconds, FillByOne(ledSections[5], RGB(0, 255, 0)) )


        runEffects(2.seconds, FillByOne(ledSections[0], RGB(0, 0, 255)) )
        runEffects(2.seconds, FillByOne(ledSections[1], RGB(0, 0, 255)) )
        runEffects(2.seconds, FillByOne(ledSections[2], RGB(0, 0, 255)) )
        runEffects(2.seconds, FillByOne(ledSections[3], RGB(0, 0, 255)) )
        runEffects(2.seconds, FillByOne(ledSections[4], RGB(0, 0, 255)) )
        runEffects(2.seconds, FillByOne(ledSections[5], RGB(0, 0, 255)) )
        allOff()

        runEffects(
            4.seconds,
            Glow(ledSections[0], constColor(RGB(255, 0, 0)), 2.seconds),
            Glow(ledSections[1], constColor(RGB(0, 255, 0)), 2.seconds),
            Glow(ledSections[2], constColor(RGB(0, 0, 255)), 2.seconds),
            Glow(ledSections[3], constColor(RGB(255, 0, 0)), 2.seconds),
            Glow(ledSections[4], constColor(RGB(0, 255, 0)), 2.seconds),
            Glow(ledSections[5], constColor(RGB(0, 0, 255)), 2.seconds),
        )
        allOff()
        runEffects(16.seconds, GlowAll(ledSections, rainbow(), 4.seconds))
        allOff()
        runEffects(4.seconds, GlowAll(ledSections,constColor( RGB(255, 0, 0)), 2.seconds))
        runEffects(4.seconds, GlowAll(ledSections, constColor(RGB(0, 255, 0)), 2.seconds))
        runEffects(4.seconds, GlowAll(ledSections, constColor(RGB(0, 0, 255)), 2.seconds))

    }
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

fun writeFrame() {
    val line = ledSections.joinToString(separator = ",") { section ->
        section.leds.joinToString(separator = ",") { led ->
            "${led.r} ${led.g} ${led.b}"
        }
    } + "\n"
    writer.write(line)
    writer.flush()
}

