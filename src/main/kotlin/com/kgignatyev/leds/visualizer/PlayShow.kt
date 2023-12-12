package com.kgignatyev.leds.visualizer

import java.awt.Color
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.Timer


class LedSimulator(val showFile: File) : JPanel(), ActionListener {

    private var parentFrame: JFrame = JFrame()
    val h = 800
    val w = 1100

    val strings = 6
    val ledsPerString = 50

    val currentLedStates: MutableList<Color> = mutableListOf()
    val ledStates: MutableList<List<Color>> = mutableListOf()
    val frameLength = 20

    val framesTimer = Timer(frameLength, this)
    var currentFrameIndex = 0
    fun setCurrentStates(states: List<Color>) {
        currentLedStates.clear()
        if (states.size != strings * ledsPerString) {
            throw IllegalArgumentException("States size must be equal to section length")
        }
        currentLedStates.addAll(states)
        repaint()
    }


    init {
        currentLedStates.fill(Color(0, 0, 0))
        parentFrame.setSize(w, h)
        parentFrame.setLocation(50, 50)
        parentFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        parentFrame.title = "LED Visualizer"

        // Set the properties of this panel
        this.setLocation(0, 0)
        parentFrame.contentPane.add(this)
        parentFrame.isVisible = true;
        framesTimer.start()
    }

    override fun paint(g: java.awt.Graphics) {
        g.color = Color(30, 30, 30)
        g.fillRect(0, 0, w, h)
        val ledWidth = 12

        (0..ledsPerString - 1).forEach { ledIndex ->
            (0..strings - 1).forEach { stringIndex ->
                val reversed = stringIndex % 2 == 1
                val m = 2
                val k = if (stringIndex <= 2) {
                    -1 * (4 - stringIndex) * ledIndex * m
                } else {
                    stringIndex * ledIndex * m
                }
                val x = (w/2 - ((ledWidth + m)*3)) + stringIndex * ledWidth + k
                val y = h - ledIndex * ledWidth - 4 * ledIndex - ledWidth
                val i = if (reversed) {
                    stringIndex * ledsPerString + (ledsPerString - ledIndex - 1)
                } else {
                    stringIndex * ledsPerString + ledIndex
                }
                if (i < currentLedStates.size) {
                    g.color = currentLedStates[i]
                } else {
                    g.color = Color(0, 0, 0)
                }
                g.fillRect(x, y, ledWidth, ledWidth)
            }
        }
    }

    var fileTimestamp = 0L


    fun readFileIfNecessary() {
        if (showFile.exists()) {
            if (fileTimestamp != showFile.lastModified()) {
                println("Waiting for file $showFile to refresh")
                var lines: List<String> = listOf()
                var lastLineFound = false
                //let's wait for file to have END at last line
                var lastHash = showFile.hashCode()
                while (! lastLineFound) {
                    lines = showFile.readLines()
                    if( lines.isNotEmpty()) {
                        lastLineFound = lines.last().startsWith("END")
                    }
                    Thread.sleep(100)
                }
                println("Reading file $showFile")

                fileTimestamp = showFile.lastModified()
                ledStates.clear()
                lines.forEach { line ->
                    if(! line.startsWith("END") ) {
                        val states = line.split(",").map { color ->
                            val rgb = color.split(" ").map { c -> c.toInt() }
                            Color(rgb[0], rgb[1], rgb[2])
                        }
                        ledStates.add(states)
                    }
                }
                currentFrameIndex = 0
            }
        }
    }

    var counter = 0
    var N = 100
    override fun actionPerformed(e: ActionEvent?) {
        if (counter % N == 0) {
            readFileIfNecessary()
            counter = 0
        }
        counter++
        showNextFrame()
    }


    private fun showNextFrame() {
        if (ledStates.isNotEmpty()) {
            val frame = ledStates[currentFrameIndex]
            setCurrentStates(frame)
            currentFrameIndex++
            if (currentFrameIndex >= ledStates.size) {
                currentFrameIndex = 0
            }
        }
    }
}

fun main() {
    LedSimulator(File("light-show.txt"))

}
