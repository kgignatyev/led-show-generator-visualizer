package com.kgignatyev.leds.visualizer

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.geom.Line2D
import java.awt.image.BufferedImage
import java.io.File
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.Timer
import kotlin.math.sin


class PlayTopOfHouseShow(val showFile: File) : JPanel(), ActionListener {

    private var parentFrame: JFrame = JFrame()
    val h = 400

    val sections = 24
    val ledPerDropString = 10
    val ledRadiusPx = 4
    val houseLengthMm = 13000
    val distanceBetweenLedsMm = 80
    val scale = 0.1
    val margin = 20
    val w = (houseLengthMm * scale).toInt() + margin * 2
    val yOffset = 20

    val ledsPerSection:Int = houseLengthMm/ sections / distanceBetweenLedsMm

    val ledMatrix = Array(sections*ledsPerSection+1 ) { Array(ledPerDropString+1) { -1 } }
    val matrixToHouseScale =  (w - margin * 2)/ ledMatrix.size

    val currentLedStates: MutableList<Color> = mutableListOf()
    val frameLength = 20

    val framesTimer = Timer(frameLength, this)
    var currentFrameIndex = 0

    init {
        val numLeds = sections * ledsPerSection + ledPerDropString * (sections + 1)
        val mX = ledMatrix.size-1
        val mY = ledMatrix[0].size-1
        var x = 0
        var ledPosition = 0
        var y = mY
        (0 ..< sections).forEach { sectionIndex ->
             y = mY
            (0 ..< ledPerDropString).forEach { ledIndex ->
                ledMatrix[x][y] = ledPosition
                y--
                ledPosition++
            }
            (0 ..< ledsPerSection).forEach { ledIndex ->
                ledMatrix[x][y] = ledPosition
                x++
                ledPosition++
            }
        }
        (0 .. ledPerDropString).forEach { ledIndex ->
            ledMatrix[x][y] = ledPosition
            y++
            ledPosition++
        }
        (0..ledPosition).forEach { ledIndex ->
            currentLedStates.add(Color(90, 90, 90))
        }
        parentFrame.setSize(w, h)
        parentFrame.setLocation(50, 50)
        parentFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        parentFrame.title = "Top of house Visualizer"

        // Set the properties of this panel
        this.setLocation(0, 0)
        parentFrame.contentPane.add(this)
        parentFrame.isVisible = true;
        framesTimer.start()
        println("Num LEDs needed: " + currentLedStates.size)
    }

    override fun actionPerformed(e: ActionEvent) {
        currentFrameIndex++
        repaint()
    }

    private fun drawWaves(g: Graphics, imgW:Int, imgH:Int, waveW:Int) {
        val g2d = g as Graphics2D
        val amplitude = 50.0
        val frequency = 0.005 // Lower frequency to fit wave on screen
        val WIDTH = imgW/2
        val xOffset = 300

        for (x in 0 until imgW + xOffset) {
            val y1 = 0.0
            val y2 = imgW.toDouble()
            var colorOffset = currentFrameIndex + x
            // Set color to cycle through hues for a rainbow effect
            g2d.color = Color.getHSBColor(colorOffset.toFloat() / WIDTH, 1.0f, 1.0f)
            g2d.stroke = BasicStroke(2f)

            // Draw the line between each pair of points
            g2d.draw(Line2D.Double((x-xOffset).toDouble(), y1, (x+xOffset).toDouble(), y2))
        }
    }

    override fun paint(g: Graphics) {
        g.color = Color(0, 0, 0)
        g.fillRect(0, 0, w, h)
        val bImg = BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)
        val g1 = bImg.createGraphics()
        drawWaves(g1, w, 200, w/2)
        g1.dispose()
        g.drawImage(bImg, 0, 200, this)
        val ledWidth = ledRadiusPx * 2
        val g2d = g as Graphics2D

        parentFrame.getGraphics().color = Color(0, 0, 0)

        (0 until ledMatrix.size).forEach { x ->
            (0 until ledMatrix[x].size).forEach { y ->
                val ledIndex = ledMatrix[x][y]
                if (ledIndex != -1) {
                    val xPx = x * matrixToHouseScale + margin
                    val yPx = y * matrixToHouseScale + yOffset

                    val c = Color(bImg.getRGB(xPx,yPx) )
//                    val ledColor = currentLedStates[ledIndex]
                    val ledColor = c

                    g.color = ledColor
                    g.fillOval(xPx, yPx, ledWidth, ledWidth)
                }
            }
        }


    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val showFile = File("top_of_house_show.json")
            PlayTopOfHouseShow(showFile)
        }
    }
}
