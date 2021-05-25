package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.canvas.Canvas
import java.util.LinkedList

/**
 * @author Benedikt Wüller
 */
class FillTool(canvas: Canvas) : LayerTool(canvas) {

    override val primaryAction: (Layer.() -> Unit) = {
        val pixels = mutableListOf<Pair<Int, Int>>()
        val queue = LinkedList<Pair<Int, Int>>()
        queue.push(cursorX to cursorY)

        val replaceColor = this.getColor(cursorX, cursorY)
        do {
            val coords = queue.poll()
            val (x, y) = coords

            val color = this.getColor(coords.first, coords.second)
            if (color != replaceColor) continue

            pixels.add(coords)
            listOf((x - 1) to y, (x + 1) to y, x to (y - 1), x to (y + 1))
                    .filter { it !in pixels }
                    .forEach { queue.add(it) }
        } while (queue.isNotEmpty())

        pixels.forEach { (x, y) -> this.setColor(x, y, canvas.currentColor) }
    }

}
