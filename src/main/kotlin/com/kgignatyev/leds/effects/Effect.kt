package com.kgignatyev.leds.effects

import kotlin.time.Duration


interface Effect {
    fun  renderFrame()
    fun init(frameCount: Int, frameDuration: Duration)
}
